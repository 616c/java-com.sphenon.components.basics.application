package com.sphenon.basics.application.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.session.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.application.*;
import com.sphenon.basics.application.tplinst.*;

import java.lang.reflect.*;

public class ApplicationSessionClass implements ApplicationSession {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationSessionClass"); };

    // -------------------------------------------------------------------------------------------------
    // construction & destruction

    // construction

    protected ApplicationSessionData asd;
    protected String id;
    static protected long next_id = 0;

    protected ApplicationSessionClass(CallContext context, ApplicationInstance application_instance, ApplicationSessionComponentData... datas) {
        this.application_instance = application_instance;
        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating session of application '%(applicationid)'", "applicationid", this.getApplication(context).getId(context)); }

        synchronized(ApplicationSessionClass.class) {
            this.id = "#" + next_id++;
        }

        ClassLoader current = null;
        try {
            current = this.application_instance.getApplication(context).setApplicationClassLoader(context);

            for (ApplicationSessionComponentData data : datas) {
                this.getApplicationSessionComponentDatas(context).set(context, data);
            }
            
            this.sph_session = new Session(context);
            this.sph_session.notifyClientSessionBegin(context);
            
            this.asd = new ApplicationSessionData(context);
            this.asd.setApplicationSession(context, this);
            this.sph_session.setSessionData(context, asd);
            
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising components for application session '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }
            
            for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                ApplicationSessionComponentData ascd = ac.notifyApplicationSessionBegin (this.getModifyableApplicationSessionRootContext(), this);
                if (ascd != null) {
                    this.getApplicationSessionComponentDatas(context).set(context, ascd);
                }
            }
            
        } finally {
            this.application_instance.getApplication(context).resetApplicationClassLoader(context, current);
        }
    }

    static public ApplicationSessionClass create(CallContext context, ApplicationInstance application_instance, ApplicationSessionComponentData... datas) {
        return new ApplicationSessionClass(context, application_instance, datas);
    }

    // destruction

    protected boolean is_destroyed = false;

    public boolean getIsDestroyed(CallContext context) {
        return this.is_destroyed;
    }

    public void destroy (CallContext context) {
        if ( ! this.is_destroyed) {
            synchronized (this) {
                if ( ! this.is_destroyed) {
                    this.is_destroyed = true;

                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising components for application session '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }

                    ClassLoader current = null;
                    try {
                        current = this.application_instance.getApplication(context).setApplicationClassLoader(context);

                        for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                            ac.notifyApplicationSessionEnd (this.getModifyableApplicationSessionRootContext(), this);
                        }
                        
                        this.sph_session.notifyClientSessionEnd(context);
                        
                        this.asd.setApplicationSession(context, null);

                    } finally {
                        this.application_instance.getApplication(context).resetApplicationClassLoader(context, current);
                    }
                }
            }
        }
    }

    public void finalize () throws Throwable {
        this.destroy(this.getApplicationSessionRootContext());
    }

    // -------------------------------------------------------------------------------------------------
    // attributes

    protected ApplicationInstance application_instance;

    public ApplicationInstance getApplicationInstance (CallContext context) {
        return this.application_instance;
    }

    public Application getApplication (CallContext context) {
        return this.getApplicationInstance(context).getApplication(context);
    }

    protected OSet_ApplicationSessionComponentData_Type_ application_session_component_datas;

    public OSet_ApplicationSessionComponentData_Type_ getApplicationSessionComponentDatas(CallContext context) {
        if (this.application_session_component_datas == null) {
            this.application_session_component_datas = new OSetImpl_ApplicationSessionComponentData_Type_(context);
        }
        return this.application_session_component_datas;
    }

    public <ApplicationSessionComponentDataType extends ApplicationSessionComponentData> ApplicationSessionComponentDataType getApplicationSessionComponentData(CallContext context, Class<ApplicationSessionComponentDataType> target_class) {
        if (this.application_session_component_datas == null) { return null; }
        ApplicationSessionComponentData data = this.application_session_component_datas.tryGetSole(context, TypeManager.get(context, target_class));
        return (ApplicationSessionComponentDataType) data;
    }

    protected Session sph_session;

    public Session getSession(CallContext context) {
        return this.sph_session;
    }

    // -------------------------------------------------------------------------------------------------
    // session factory

    public ApplicationMessageExchange createApplicationMessageExchange(CallContext context, ApplicationMessageExchangeComponentData... datas) {
        return ApplicationMessageExchangeClass.create(context, this, datas);
    }

    // -------------------------------------------------------------------------------------------------
    // root context

    volatile protected Context application_session_root_context;

    public CallContext getApplicationSessionRootContext() {
        return this.getModifyableApplicationSessionRootContext();
    }
    
    public Context getModifyableApplicationSessionRootContext() {
        if (this.application_session_root_context == null) {
            synchronized(this)  {
                if (this.application_session_root_context == null) {
                    Context context = this.application_session_root_context = Context.create(this.application_instance.getApplicationInstanceRootContext());
                    CustomaryContext cc = CustomaryContext.create(context);
                    
                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating application session root context for '%(id)'", "id", this.getApplication(context).getId(context)); }
                    
                    SessionContext sc = SessionContext.create(context);
                    sc.setSession(context, sph_session);
                    
                    ApplicationContext ac = ApplicationContext.create(context);
                    ac.setApplicationSession(context, this);
                }
            } 
        } 
        return this.application_session_root_context;
    }

    // -------------------------------------------------------------------------------------------------
    // session id

    public String getId (CallContext context) {
        return this.id;
    }
}

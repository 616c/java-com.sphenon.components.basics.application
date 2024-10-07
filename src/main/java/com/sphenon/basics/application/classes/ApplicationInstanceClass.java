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
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.application.*;
import com.sphenon.basics.application.tplinst.*;

import java.lang.reflect.*;

public class ApplicationInstanceClass implements ApplicationInstance {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationInstanceClass"); };

    // -------------------------------------------------------------------------------------------------
    // construction & destruction

    // construction

    protected ApplicationInstanceClass(CallContext context, Application application, ApplicationInstanceComponentData... datas) {
        this.application = application;
        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating instance of application '%(applicationid)'", "applicationid", this.application.getId(context)); }

        ClassLoader current = null;
        try {
            current = this.application.setApplicationClassLoader(context);

            for (ApplicationInstanceComponentData data : datas) {
                this.getApplicationInstanceComponentDatas(context).set(context, data);
            }
            
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising components for application instance '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }
            
            for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                ApplicationInstanceComponentData aicd = ac.notifyApplicationInstanceBegin (this.getModifyableApplicationInstanceRootContext(), this);
                if (aicd != null) {
                    this.getApplicationInstanceComponentDatas(context).set(context, aicd);
                }
            }

        } finally {
            this.application.resetApplicationClassLoader(context, current);
        }
    }

    static public ApplicationInstanceClass create(CallContext context, Application application, ApplicationInstanceComponentData... datas) {
        return new ApplicationInstanceClass(context, application, datas);
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
                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising components for application instance '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }

                    ClassLoader current = null;
                    try {
                        current = this.application.setApplicationClassLoader(context);

                        for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                            ac.notifyApplicationInstanceEnd (this.getModifyableApplicationInstanceRootContext(), this);
                        }

                    } finally {
                        this.application.resetApplicationClassLoader(context, current);
                    }
                }
            }
        }
    }

    public void finalize () throws Throwable {
        this.destroy(this.getApplicationInstanceRootContext());
    }

    // -------------------------------------------------------------------------------------------------
    // attributes

    protected Application application;

    public Application getApplication (CallContext context) {
        return this.application;
    }

    protected OSet_ApplicationInstanceComponentData_Type_ application_instance_component_datas;

    public OSet_ApplicationInstanceComponentData_Type_ getApplicationInstanceComponentDatas(CallContext context) {
        if (this.application_instance_component_datas == null) {
            this.application_instance_component_datas = new OSetImpl_ApplicationInstanceComponentData_Type_(context);
        }
        return this.application_instance_component_datas;
    }

    public <ApplicationInstanceComponentDataType extends ApplicationInstanceComponentData> ApplicationInstanceComponentDataType getApplicationInstanceComponentData(CallContext context, Class<ApplicationInstanceComponentDataType> target_class) {
        if (this.application_instance_component_datas == null) { return null; }
        ApplicationInstanceComponentData data = this.application_instance_component_datas.tryGetSole(context, TypeManager.get(context, target_class));
        return (ApplicationInstanceComponentDataType) data;
    }

    protected ApplicationSessionRegistry application_session_registry;

    public ApplicationSessionRegistry getApplicationSessionRegistry(CallContext context) {
        if (this.application_session_registry == null) {
            this.application_session_registry = new ApplicationSessionRegistry(context);
        }
        return this.application_session_registry;
    }

    // -------------------------------------------------------------------------------------------------
    // session factory

    public ApplicationSession createApplicationSession(CallContext context, ApplicationSessionComponentData... datas) {
        return ApplicationSessionClass.create(context, this, datas);
    }

    // -------------------------------------------------------------------------------------------------
    // root context

    volatile protected Context application_instance_root_context;

    public CallContext getApplicationInstanceRootContext() {
        return this.getModifyableApplicationInstanceRootContext();
    }

    public Context getModifyableApplicationInstanceRootContext() {
      if (this.application_instance_root_context == null) {
        synchronized (this) {
          if (this.application_instance_root_context == null) {
              Context context = this.application_instance_root_context = Context.create(this.application.getApplicationRootContext());
              CustomaryContext cc = CustomaryContext.create(context);
  
              if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating application instance root context for '%(id)'", "id", this.getApplication(context).getId(context)); }
  
              ApplicationContext ac = ApplicationContext.create(context);
              ac.setApplicationInstance(context, this);
          }
        }
      }
      return this.application_instance_root_context;
    }
}

package com.sphenon.basics.application.classes;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

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

public class ApplicationMessageExchangeClass implements ApplicationMessageExchange {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationMessageExchangeClass"); };

    // -------------------------------------------------------------------------------------------------
    // construction & destruction

    // construction

    protected ApplicationMessageExchangeClass(CallContext context, ApplicationSession application_session, ApplicationMessageExchangeComponentData... datas) {
        this.application_session = application_session;
        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating message exchange with application '%(applicationid)'", "applicationid", this.getApplication(context).getId(context)); }

        ClassLoader current = null;
        try {
            current = this.application_session.getApplicationInstance(context).getApplication(context).setApplicationClassLoader(context);

            for (ApplicationMessageExchangeComponentData data : datas) {
                this.getApplicationMessageExchangeComponentDatas(context).set(context, data);
            }

            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising components for application message exchange '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }

            Context modifyable_application_message_exchange_root_context  = this.getModifyableApplicationMessageExchangeRootContext();
            CallContext previous_context = RootContext.setFallbackCallContext(modifyable_application_message_exchange_root_context);
            try {
                for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                    ApplicationMessageExchangeComponentData amecd = ac.notifyApplicationMessageExchangeBegin (this.getModifyableApplicationMessageExchangeRootContext(), this);
                    if (amecd != null) {
                        this.getApplicationMessageExchangeComponentDatas(context).set(context, amecd);
                    }
                }
            } finally {
                RootContext.setFallbackCallContext(previous_context);
            }

        } finally {
            this.application_session.getApplicationInstance(context).getApplication(context).resetApplicationClassLoader(context, current);
        }
    }

    static public ApplicationMessageExchangeClass create(CallContext context, ApplicationSession application_session, ApplicationMessageExchangeComponentData... datas) {
        return new ApplicationMessageExchangeClass(context, application_session, datas);
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
                    Context modifyable_application_message_exchange_root_context  = this.getModifyableApplicationMessageExchangeRootContext();
                    CallContext previous_context = RootContext.setFallbackCallContext(modifyable_application_message_exchange_root_context);
                    ClassLoader current = null;
                    try {
                        current = this.application_session.getApplicationInstance(context).getApplication(context).setApplicationClassLoader(context);
                        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising components for application message exchange '%(applicationid)'...", "applicationid", this.getApplication(context).getId(context)); }
                        for (ApplicationComponent ac : this.getApplication(context).getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                            ac.notifyApplicationMessageExchangeEnd (this.getModifyableApplicationMessageExchangeRootContext(), this);
                        }
                    } finally {
                        RootContext.setFallbackCallContext(previous_context);
                        this.application_session.getApplicationInstance(context).getApplication(context).resetApplicationClassLoader(context, current);
                    }
                }
            }
        }
    }

    public void finalize () throws Throwable {
        this.destroy(this.getApplicationMessageExchangeRootContext());
    }

    // -------------------------------------------------------------------------------------------------
    // attributes

    protected ApplicationSession application_session;

    public ApplicationSession getApplicationSession (CallContext context) {
        return this.application_session;
    }

    public ApplicationInstance getApplicationInstance (CallContext context) {
        return this.getApplicationSession(context).getApplicationInstance(context);
    }

    public Application getApplication (CallContext context) {
        return this.getApplicationSession(context).getApplicationInstance(context).getApplication(context);
    }

    protected OSet_ApplicationMessageExchangeComponentData_Type_ application_message_exchange_component_datas;

    public OSet_ApplicationMessageExchangeComponentData_Type_ getApplicationMessageExchangeComponentDatas(CallContext context) {
        if (this.application_message_exchange_component_datas == null) {
            this.application_message_exchange_component_datas = new OSetImpl_ApplicationMessageExchangeComponentData_Type_(context);
        }
        return this.application_message_exchange_component_datas;
    }

    public <ApplicationMessageExchangeComponentDataType extends ApplicationMessageExchangeComponentData> ApplicationMessageExchangeComponentDataType getApplicationMessageExchangeComponentData(CallContext context, Class<ApplicationMessageExchangeComponentDataType> target_class) {
        if (this.application_message_exchange_component_datas == null) { return null; }
        ApplicationMessageExchangeComponentData data = this.application_message_exchange_component_datas.tryGetSole(context, TypeManager.get(context, target_class));
        return (ApplicationMessageExchangeComponentDataType) data;
    }

    // -------------------------------------------------------------------------------------------------
    // root context

    protected Context application_message_exchange_root_context;

    public CallContext getApplicationMessageExchangeRootContext() {
        return this.getModifyableApplicationMessageExchangeRootContext();
    }

    public Context getModifyableApplicationMessageExchangeRootContext() {
        if (this.application_message_exchange_root_context == null) {
            Context context = this.application_message_exchange_root_context = Context.create(this.application_session.getApplicationSessionRootContext());
            CustomaryContext cc = CustomaryContext.create(context);

            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating application message exchange root context for '%(id)'", "id", this.getApplication(context).getId(context)); }

            ApplicationContext ac = ApplicationContext.create(context);
            ac.setApplicationMessageExchange(context, this);
        }
        return this.application_message_exchange_root_context;
    }
}

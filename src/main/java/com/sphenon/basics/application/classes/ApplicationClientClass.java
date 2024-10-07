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
import com.sphenon.basics.security.*;
import com.sphenon.basics.session.*;
import com.sphenon.basics.function.*;

import com.sphenon.basics.application.*;

import java.util.Map;
import java.util.HashMap;

public class ApplicationClientClass implements ApplicationClient {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationClientClass"); };

    protected String  application_id;
    protected String  client_id;
    protected String  client_name;

    public String getApplicationId() {
        return this.application_id;
    }

    public String getClientId() {
        return this.client_id;
    }

    public String getClientName() {
        return this.client_name;
    }

    static protected Map<String,Long> created_instances;
    static public Long getCreatedInstances(CallContext context, String application_id) {
        Long ci = (created_instances == null ? null : created_instances.get(application_id));
        return ci == null ? 0L : ci;
    }
    static protected Map<String,Long> destroyed_instances;
    static public Long getDestroyedInstances(CallContext context, String application_id) {
        Long di = (destroyed_instances == null ? null : destroyed_instances.get(application_id));
        return di == null ? 0L : di;
    }

    protected ApplicationClientManager manager;

    public ApplicationClientClass(CallContext root_context, String application_id, String client_id, String client_name, boolean do_initialise) {
        this(root_context, application_id, client_id, client_name, null, do_initialise);
    }

    public ApplicationClientClass(CallContext root_context, String application_id, String client_id, String client_name, ApplicationClientManager manager, boolean do_initialise) {
        this.root_context    = root_context;
        this.application_id  = application_id;
        this.client_id       = client_id;
        this.client_name     = client_name;
        this.manager         = manager;
        this.actors          = new ThreadLocal<Integer>();
        this.actors.set(0);

        this.initialised    = false;

        if (created_instances == null) { created_instances = new HashMap<String,Long>(); }
        Long noci = created_instances.get(this.application_id);
        created_instances.put(this.application_id, (noci == null ? 0L : noci) + 1);

        if (do_initialise) {
            this.initialise();
        }
    }

    protected boolean                    initialised;

    protected CallContext                root_context;
    protected Application                app;
    protected ApplicationInstance        appi;
    protected ApplicationSession         apps;
    protected ApplicationMessageExchange appm;
    protected Authority                  authority;
    protected Context                    context;
    protected ApplicationContext         ac;
    protected ThreadLocal<Integer>       actors;
    protected ClassLoader                current_loader;

    protected ApplicationComponentData[]                getApplicationComponentDatas(CallContext context)                { return new ApplicationComponentData[0]; }
    protected ApplicationInstanceComponentData[]        getApplicationInstanceComponentDatas(CallContext context)        { return new ApplicationInstanceComponentData[0]; }
    protected ApplicationSessionComponentData[]         getApplicationSessionComponentDatas(CallContext context)         { return new ApplicationSessionComponentData[0]; }
    protected ApplicationMessageExchangeComponentData[] getApplicationMessageExchangeComponentDatas(CallContext context) { return new ApplicationMessageExchangeComponentData[0]; }

    public CallContext getContext() {
        if (this.context == null) {
            this.app       = ApplicationManager.getApplication(this.root_context, this.application_id, this.getApplicationComponentDatas(this.root_context));
            this.appi      = app.getApplicationInstance(this.root_context, "singleton", this.getApplicationInstanceComponentDatas(this.root_context));
            this.apps      = appi.createApplicationSession(this.root_context, this.getApplicationSessionComponentDatas(this.root_context));
            this.context   = Context.create(apps.getApplicationSessionRootContext());
            this.ac        = ApplicationContext.create(context);
            this.ac.setApplicationClient(context, this, this.getClass());
            this.authority = SecuritySessionData.get(context).getAuthority(context);

            // [Issue:SessionData - ApplicationClientClass.java,SecuritySessionData.java,SessionData.java]
            // maybe this could be moved to something like SecurityApplicationComponent
            // maybe simply SecuritySessionData/SessionData refactored to SecurityApplicationComponentData
            if (this.authority != null) {
                SecurityContext sc = SecurityContext.create(context);
                sc.setAuthority(context, this.authority);
            }
        }
        return this.context;
    }

    public Application getApplication(CallContext context) {
        this.getContext(); // for preparation
        return this.app;
    }

    public ApplicationInstance getApplicationInstance(CallContext context) {
        this.getContext(); // for preparation
        return this.appi;
    }

    public ApplicationSession getApplicationSession(CallContext context) {
        this.getContext(); // for preparation
        return this.apps;
    }

    public ApplicationMessageExchange getApplicationMessageExchange(CallContext context) {
        this.getContext(); // for preparation
        return this.appm;
    }

    public Authority getAuthority(CallContext context) {
        this.getContext(); // for preparation
        return this.authority;
    }

    protected int getActors() {
        Integer a = this.actors.get();
        return a == null ? 0 : a;
    }

    public void beginInteraction() {
        if (this.getActors() == 0) {
            this.current_loader = this.app.setApplicationClassLoader(context);
        }
        this.actors.set(this.getActors() + 1);
    }

    public void endInteraction() {
        this.actors.set(this.getActors() - 1);
        if (this.getActors() == 0) {
            this.app.resetApplicationClassLoader(context, this.current_loader);
        }
    }

    public synchronized void initialise() {
        if (this.initialised == false) {
            this.initialised = true;
            CallContext context = this.getContext();
            try {
                this.beginInteraction();
                this.initialiseClient(context);
            } finally {
                this.endInteraction();
            }
        }
    }

    protected ApplicationClientTimeoutAdjuster timeout_adjuster;

    protected void initialiseClient(CallContext context) {
        if (this.manager != null) {
            Session.get(context).getEventDispatcherActorChangeEvent(context).addListener(context, timeout_adjuster = new ApplicationClientTimeoutAdjuster(context, this, this.manager));
        }
    }

    public synchronized void deinitialise() {
        if (this.initialised == true) {
            this.initialised = false;
            CallContext context = this.getContext();
            try {
                this.beginInteraction();
                for (Object service_instance : this.service_instances.values()) {
                    if (service_instance instanceof ManagedResource) {
                        ((ManagedResource) service_instance).release(context);
                    }
                }
                this.service_instances = null;
                this.deinitialiseClient(context);
            } finally {
                this.endInteraction();
            }
            if (this.apps != null) { apps.destroy(context); }
            this.apps = null;

            if (destroyed_instances == null) { destroyed_instances = new HashMap<String,Long>(); }
            Long nodi = destroyed_instances.get(this.application_id);
            destroyed_instances.put(this.application_id, (nodi == null ? 0L : nodi) + 1);
        }
    }

    protected void deinitialiseClient(CallContext context) {
        if (this.manager != null && this.timeout_adjuster != null) {
            Session.get(context).getEventDispatcherActorChangeEvent(context).removeListener(context, timeout_adjuster);
        }
    }

    protected Map<Class, Object> service_instances;

    public<T> T getServiceInstance(CallContext call_context, Class<T> service_class, Creator<T> creator) {
        T service_instance = null;

        if (this.service_instances == null) {
            this.service_instances = new HashMap<Class, Object>();
        } else {
            service_instance = (T) this.service_instances.get(service_class);
        }

        if (service_instance == null) {
            try {
                this.initialise();
                CallContext context = this.getContext();
                this.beginInteraction();

                service_instance = creator.create(context);
                this.service_instances.put(service_class, service_instance);
            } finally {
                this.endInteraction();
            }
        }

        return service_instance;
    }
}

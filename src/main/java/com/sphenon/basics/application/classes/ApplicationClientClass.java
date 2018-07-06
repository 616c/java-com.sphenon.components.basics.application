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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.security.*;

import com.sphenon.basics.application.*;

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

    public ApplicationClientClass(CallContext root_context, String application_id, String client_id, String client_name, boolean do_initialise) {
        this.root_context    = root_context;
        this.application_id  = application_id;
        this.client_id       = client_id;
        this.client_name     = client_name;
        this.actors          = 0;

        this.initialised    = false;

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
    protected int                        actors;
    protected ClassLoader                current_loader;

    public CallContext getContext() {
        if (context == null) {
            this.app       = ApplicationManager.getApplication(this.root_context, this.application_id);
            this.appi      = app.getApplicationInstance(this.root_context, "singleton");
            this.apps      = appi.createApplicationSession(this.root_context);
            this.context   = Context.create(apps.getApplicationSessionRootContext());
            this.authority = SecuritySessionData.get(context).getAuthority(context);
        }
        return context;
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

    public void beginInteraction() {
        if (this.actors == 0) {
            this.current_loader = this.app.setApplicationClassLoader(context);
        }
        this.actors++;
    }

    public void endInteraction() {
        this.actors--;
        if (this.actors == 0) {
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

    protected void initialiseClient(CallContext context) {
    }

    public synchronized void deinitialise() {
        if (this.initialised == true) {
            this.initialised = false;
            CallContext context = this.getContext();
            try {
                this.beginInteraction();
                this.deinitialiseClient(context);
            } finally {
                this.endInteraction();
            }
            if (this.apps != null) { apps.destroy(context); }
            this.apps = null;
        }
    }

    protected void deinitialiseClient(CallContext context) {
    }
}

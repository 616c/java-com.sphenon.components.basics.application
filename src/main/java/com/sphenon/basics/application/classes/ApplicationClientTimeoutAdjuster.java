package com.sphenon.basics.application.classes;

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.actor.*;
import com.sphenon.basics.actor.tplinst.*;

import com.sphenon.basics.application.*;

public class ApplicationClientTimeoutAdjuster implements EventListener_ActorChangeEvent_ {

    static final public Class _class = ApplicationClientTimeoutAdjuster.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    static protected Configuration config;
    static { config = Configuration.create(RootContext.getInitialisationContext(), _class); };

    public ApplicationClientTimeoutAdjuster(CallContext context) {
        this.client  = null;
        this.manager = null;
    }

    protected ApplicationClient        client;
    protected ApplicationClientManager manager;

    public ApplicationClientTimeoutAdjuster(CallContext context, ApplicationClient client, ApplicationClientManager manager) {
        this.client  = client;
        this.manager = manager;
    }

    public void notify(CallContext context) {
        this.notify(context, null);
    }

    public void notify(CallContext context, ActorChangeEvent event) {
        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Received actor change event"); }

        long timeout = -1;

        if (    event != null
             && event.getNewActor(context) != null
             && event.getNewActor(context) instanceof ActorWithProperties
           ) {
            ActorWithProperties awp = (ActorWithProperties) event.getNewActor(context);
            timeout = awp.getProperty(context, "SessionTimeout", -1);
        }

        if (timeout > 0) {
            this.manager.setMaximumLifeTimeMilliSeconds(context, timeout * 1000L, this.client.getClientId());
        }
    }
}

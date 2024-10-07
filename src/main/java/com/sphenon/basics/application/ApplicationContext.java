package com.sphenon.basics.application;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.configuration.*;

import java.util.Map;
import java.util.HashMap;

public class ApplicationContext extends SpecificContext {

    static public ApplicationContext getOrCreate(Context context) {
        ApplicationContext application_context = (ApplicationContext) context.getSpecificContext(ApplicationContext.class);
        if (application_context == null) {
            application_context = new ApplicationContext(context);
            context.setSpecificContext(ApplicationContext.class, application_context);
        }
        return application_context;
    }

    static public ApplicationContext get(Context context) {
        ApplicationContext application_context = (ApplicationContext) context.getSpecificContext(ApplicationContext.class);
        return application_context;
    }

    static public ApplicationContext create(Context context) {
        ApplicationContext application_context = new ApplicationContext(context);
        context.setSpecificContext(ApplicationContext.class, application_context);
        return application_context;
    }

    protected ApplicationContext (Context context) {
        super(context);
    }

    protected Application application;

    public void setApplication(CallContext context, Application application) {
        this.application = application;
    }

    public Application getApplication(CallContext cc) {
        ApplicationContext application_context;
        return (this.application != null ?
                     this.application
                  : (application_context = (ApplicationContext) this.getCallContext(ApplicationContext.class)) != null ?
                       application_context.getApplication(cc)
                     : null
               );
    }

    protected ApplicationInstance application_instance;

    public void setApplicationInstance(CallContext context, ApplicationInstance application_instance) {
        this.application_instance = application_instance;
    }

    public ApplicationInstance getApplicationInstance(CallContext cc) {
        ApplicationContext application_context;
        return (this.application_instance != null ?
                     this.application_instance
                  : (application_context = (ApplicationContext) this.getCallContext(ApplicationContext.class)) != null ?
                       application_context.getApplicationInstance(cc)
                     : null
               );
    }

    protected ApplicationSession application_session;

    public void setApplicationSession(CallContext context, ApplicationSession application_session) {
        this.application_session = application_session;
    }

    public ApplicationSession getApplicationSession(CallContext cc) {
        ApplicationContext application_context;
        return (this.application_session != null ?
                     this.application_session
                  : (application_context = (ApplicationContext) this.getCallContext(ApplicationContext.class)) != null ?
                       application_context.getApplicationSession(cc)
                     : null
               );
    }

    protected ApplicationMessageExchange application_message_exchange;

    public void setApplicationMessageExchange(CallContext context, ApplicationMessageExchange application_message_exchange) {
        this.application_message_exchange = application_message_exchange;
    }

    public ApplicationMessageExchange getApplicationMessageExchange(CallContext cc) {
        ApplicationContext application_context;
        return (this.application_message_exchange != null ?
                     this.application_message_exchange
                  : (application_context = (ApplicationContext) this.getCallContext(ApplicationContext.class)) != null ?
                       application_context.getApplicationMessageExchange(cc)
                     : null
               );
    }

    protected Map<Class,ApplicationClient> application_clients_by_class;

    public void setApplicationClient(CallContext context, ApplicationClient application_client, Class application_client_class) {
        if (this.application_clients_by_class == null) {
            this.application_clients_by_class = new HashMap<Class,ApplicationClient>();
        }
        this.application_clients_by_class.put(application_client_class, application_client);
    }

    public<T extends ApplicationClient> T getApplicationClient(CallContext cc, Class<T> application_client_class) {
        ApplicationContext application_context;
        T result = null;
        return ( (    this.application_clients_by_class != null
                   && (result = (T) this.application_clients_by_class.get(application_client_class)) != null
                 ) ? result
                   : (application_context = (ApplicationContext) this.getCallContext(ApplicationContext.class)) != null ?
                        application_context.getApplicationClient(cc, application_client_class)
                      : null
               );
    }

    public String getApplicationId(CallContext cc) {
        Application application = getApplication(cc);
        return (application != null ? application.getId(cc) : "");
    }

    public Configuration getApplicationConfiguration (CallContext context) {
        Application application = getApplication(context);
        return (application != null ? application.getApplicationConfiguration(context) : null);
    }

    static public ClassLoader setApplicationClassLoader(CallContext context) {
        ApplicationContext ac = ApplicationContext.get((Context) context);
        if (ac == null) { return null; }
        Application a = ac.getApplication(context);
        if (a == null) { return null; }
        return a.setApplicationClassLoader(context);
    }

    static public void resetApplicationClassLoader(CallContext context, ClassLoader current) {
        ApplicationContext ac = ApplicationContext.get((Context) context);
        if (ac == null) { return; }
        Application a = ac.getApplication(context);
        if (a == null) { return; }
        a.resetApplicationClassLoader(context, current);
    }

    static public void handleException(CallContext context, Throwable throwable) {
        ApplicationContext ac = ApplicationContext.get((Context) context);
        if (ac == null) {
            System.err.println("***** WARNING! NO APPLICATION FOUND TO REPORT EXCEPTION! *****");
            throwable.printStackTrace();
            return;
        }
        Application a = ac.getApplication(context);
        if (a == null) {
            System.err.println("***** WARNING! NO APPLICATION FOUND TO REPORT EXCEPTION! *****");
            throwable.printStackTrace();
            return;
        }
        a.handleException(context, throwable);
    }
}

package com.sphenon.basics.application;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;

/**
   Represents a component of an application and serves mainly as an interface
   and hook to perform application management operations (initialization and
   deinitialization). Each such component has to be registered via the
   colon-seperated property entry

   com.sphenon.basics.application.APPLICATION.<id>.ApplicationComponents=...

   The ApplicationComponent instances are loaded (i.e. instantiated) during
   initialization of the corresponding {@link Application} instance,
   i.e. once per process. They are used repeatedly for initialising and
   deinitialising of respective applications, instances, sessions and message
   exchanges.

   @note There are several contexts involved in the invocation of the
   following functions. Each instance in the object hierachy {@link Application},
   {@link ApplicationInstance},{@link ApplicationSession}, and
   {@link ApplicationMessageExchange} has an associated root context. These
   root contexts are connected via a parent-child relationship in the same
   order. The lowest context (session or message-exchange, depending on the
   type of frontend) then is passed into all application code.

   The notify methods here typically attach or modify specific contexts
   within the several root contexts passed as their first parameter.
   Therefore, normally no invocation of Context.create() is used in these
   implementations.
*/
public interface ApplicationComponent {

    /**
       Unique identifier for this application component.
    */
    public String getId(CallContext context);

    /**
       Called when a new {@link Application} is created to initialise this
       {@link ApplicationComponent} with respect to the given application.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application The currently created application
       @returns Optionally an instance of Application as well as ApplicationComponent
                specific data whose lifecycle is bound to the application. The instance 
                will be available to all invoked code via the {@link ApplicationContext}.
    */
    public ApplicationComponentData notifyApplicationBegin (Context modifyable_application_root_context, Application application);

    /**
       Called when an {@link Application} is destroyed to deinitialise this
       {@link ApplicationComponent} with respect to the given application.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application The currently destroyed application
    */
    public void notifyApplicationEnd (Context modifyable_application_root_context, Application application);

    /**
       Called when a new {@link ApplicationInstance} is created to initialise this
       {@link ApplicationComponent} with respect to the given application instance.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_instance The currently created application instance
       @returns Optionally an instance of ApplicationInstance as well as ApplicationComponent
                specific data whose lifecycle is bound to the application instance. The instance 
                will be available to all invoked code via the {@link ApplicationContext}.
    */
    public ApplicationInstanceComponentData notifyApplicationInstanceBegin (Context modifyable_application_instance_root_context, ApplicationInstance application_instance);

    /**
       Called when an {@link ApplicationInstance} is destroyed to deinitialise this
       {@link ApplicationComponent} with respect to the given application instance.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_instance The currently destroyed application instance
    */
    public void notifyApplicationInstanceEnd (Context modifyable_application_instance_root_context, ApplicationInstance application_instance);

    /**
       Called when a new {@link ApplicationSession} is created to initialise this
       {@link ApplicationComponent} with respect to the given application session.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_session The currently created application session
       @returns Optionally an instance of ApplicationSession as well as ApplicationComponent
                specific data whose lifecycle is bound to the application session. The instance 
                will be available to all invoked code via the {@link ApplicationContext}.
    */
    public ApplicationSessionComponentData notifyApplicationSessionBegin (Context modifyable_application_session_root_context, ApplicationSession application_session);

    /**
       Called when an {@link ApplicationSession} is destroyed to deinitialise this
       {@link ApplicationComponent} with respect to the given application session.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_session The currently destroyed application session
    */
    public void notifyApplicationSessionEnd (Context modifyable_application_session_root_context, ApplicationSession application_session);

    /**
      Called before a new {@link ApplicationMessageExchange} is created, at begin of the
      trigger/setting phase
      @note Please be sure to read about application contexts in the
      introduction to this interface.
     * @param application_session The current application session
     * @param request_type Type of frontend - determines the type of request_data
     * @param request_data Data - PageContext for request type "http"
    */
    public void notifyApplicationMessageExchangeTriggerBegin (Context modifyable_application_session_root_context, ApplicationSession application_session, String request_type, Object request_data);

    /**
       Called when a new {@link ApplicationMessageExchange} is created to initialise this
       {@link ApplicationComponent} with respect to the given application message exchange.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_message_exchange The currently created application message exchange
       @returns Optionally an instance of ApplicationMessageExchange as well as ApplicationComponent
                specific data whose lifecycle is bound to the application message exchange. The instance 
                will be available to all invoked code via the {@link ApplicationContext}.
    */
    public ApplicationMessageExchangeComponentData notifyApplicationMessageExchangeBegin (Context modifyable_application_message_exchange_root_context, ApplicationMessageExchange application_message_exchange);
    /**
       Called when an {@link ApplicationMessageExchange} is destroyed to deinitialise this
       {@link ApplicationComponent} with respect to the given application message exchange.
       
       @note Please be sure to read about application contexts in the
       introduction to this interface.

       @param application_message_exchange The currently destroyed application message exchange
    */

    public void notifyApplicationMessageExchangeEnd (Context modifyable_application_message_exchange_root_context, ApplicationMessageExchange application_message_exchange);
    /**
      Called when something really weird happend after the first TriggerBegin notification and Messageprocessing aborted.
      
      @note Please be sure to read about application contexts in the
      introduction to this interface.
     * @param application_session The current application session
     * @param request_type Type of frontend - determines the type of request_data
     * @param request_data Data - PageContext for request type "http"
    */
    public void notifyApplicationMessageExchangeAbort(Context modifyable_application_session_root_context, ApplicationSession application_session);

    /**
       Will be dispatched from {@link Application#notifyException} to all
       components.

       @param throwable the exception
       @returns true, if the exception was handled and no further dispatching
                to other components shall be performed
    */
    public boolean handleException(CallContext context, Throwable throwable);
}

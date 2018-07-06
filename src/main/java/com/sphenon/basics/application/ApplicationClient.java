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
import com.sphenon.basics.security.*;

/**
   An instance of this class represents a client of an Application
   and maintains internally the application setup, interaction
   protocol and instances of Application, ApplicationInstance,
   ApplicationSession, ApplicationMessageExchange and Context.

   Use cases for this class include User Interface Sessions
   and Web Services, like REST and SOAP.

   Typically, in these scenarious this class is subclassed,
   and specific use case dependent additional interaction
   instances added and managed.
*/

public interface ApplicationClient {
    /**
       Get the application identifier of the associated application
    */
    public String getApplicationId();

    /**
       Get client identifier for use as token in remote interactions
    */
    public String getClientId();

    /**
       Get a descriptive name of the purpose of this application client
    */
    public String getClientName();

    /**
       Get the current, most specific {@link Context}.
    */
    public CallContext getContext();

    /**
       Get the associated {@link Application}.
    */
    public Application getApplication(CallContext context);

    /**
       Get the associated {@link ApplicationInstance}.
    */
    public ApplicationInstance getApplicationInstance(CallContext context);

    /**
       Get the associated {@link ApplicationSession}.
    */
    public ApplicationSession getApplicationSession(CallContext context);

    /**
       Get the associated {@link ApplicationMessageExchange}.
    */
    public ApplicationMessageExchange getApplicationMessageExchange(CallContext context);
    // see note in 'ApplicationMessageExchange' concerning renaming to 'ApplicationInteraction'

    /**
       Get the associated {@link Authority}.
    */
    public Authority getAuthority(CallContext context);

    /**
       Initialised the client. Typically, this is called implicitly
       from within the constructor.
    */
    public void initialise();

    /**
       Deinitialises the client. To ensure safe cleanup, this shall
       be called after interacting with the client.
    */
    public void deinitialise();

    /**
       Begin interacting with objects of this application.
       This prepares the class loader if the application uses it's own.
    */
    public void beginInteraction();

    /**
       Stop interacting with objects of this application.
       This resets the class loader to it's previous state.
    */
    public void endInteraction();
}

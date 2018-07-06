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

import com.sphenon.basics.application.tplinst.*;

/**
   An instance of this class represents a session between some actor and an
   application instance. 

   @note Depending on the type of the user interface or application frontend,
   {@link ApplicationMessageExchange} instance may or may not be
   created. Typically, a client/server frontend (e.g. HTTP, SOAP) will create
   them, while a typical desktop GUI won't.

   @note This class might be renamed to "ApplicationInteraction", which makes
   more sense in scenarious where no messages are exchanged, but nevertheless
   interaction takes place. This emphasises what actually happens during the
   interaction (during the MessageExchange in former terms). Given this
   renaming, and based on the assumption of a very lightweight implementation,
   it could and should be used generally then, to guarantee e.g. classloader
   preparation automatically.
*/
public interface ApplicationMessageExchange {
    /**
       Get the associated {@link ApplicationSession}.
    */
    public ApplicationSession getApplicationSession(CallContext context);

    /**
       A collection of component specific instances of {@link ApplicationMessageExchangeComponentData}.

       @returns An OSet of ApplicationMessageExchangeComponentDatas, retrievable by their specific Type.
     */
    public OSet_ApplicationMessageExchangeComponentData_Type_ getApplicationMessageExchangeComponentDatas(CallContext call_context);

    /**
       Retrieve a specific {@link ApplicationMessageExchangeComponentData} from the collection.

       @returns An instance of ApplicationMessageExchangeComponentDataType, if found, or null
     */
    public <ApplicationMessageExchangeComponentDataType extends ApplicationMessageExchangeComponentData> ApplicationMessageExchangeComponentDataType getApplicationMessageExchangeComponentData(CallContext call_context, Class<ApplicationMessageExchangeComponentDataType> target_class);

    /**
      Returns and optionally creates a derived context based upon the
      corresponding application session root context and prepares it with
      application message exchange specific settings. The application session
      root context is obtained from the {@link ApplicationSession} instance.
     */
    public CallContext getApplicationMessageExchangeRootContext();

    /**
       Performs any cleanup operations and resource deallocation necessary to
       destroy this instance and marks it as destroyed. The instance must not
       be used afterwards.
    */
    public void destroy (CallContext context);

    /**
       Tells whether this instance is still valid and usable or already has
       been destroyed by invoking the destroy method.
    */
    public boolean getIsDestroyed(CallContext context);
}


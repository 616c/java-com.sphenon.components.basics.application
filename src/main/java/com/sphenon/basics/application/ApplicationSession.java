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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.application.tplinst.*;

/**
   An instance of this class represents a session between some actor and an
   application instance. 
*/
public interface ApplicationSession {
    /**
       Get the associated {@link ApplicationInstance}.
    */
    public ApplicationInstance getApplicationInstance(CallContext context);

    /**
       A collection of component specific instances of {@link ApplicationSessionComponentData}.

       @returns An OSet of ApplicationSessionComponentDatas, retrievable by their specific Type.
     */
    public OSet_ApplicationSessionComponentData_Type_ getApplicationSessionComponentDatas(CallContext call_context);

    /**
       Retrieve a specific {@link ApplicationSessionComponentData} from the collection.

       @returns An instance of ApplicationSessionComponentDataType, if found, or null
     */
    public <ApplicationSessionComponentDataType extends ApplicationSessionComponentData> ApplicationSessionComponentDataType getApplicationSessionComponentData(CallContext call_context, Class<ApplicationSessionComponentDataType> target_class);

    /**
       Create a new message exchange with this application session.

       @param datas Optionally a set of datas which will be associated with the
                    newly created {@link ApplicationMessageExchange} (i.e. stored in the
                    above mentioned OSet).
     */
    public ApplicationMessageExchange createApplicationMessageExchange(CallContext call_context, ApplicationMessageExchangeComponentData... datas);

    /**
      Returns and optionally creates a derived context based upon the
      corresponding application instance root context and prepares it with
      application session specific settings. The application instance root
      context is obtained from the {@link ApplicationInstance} instance.
     */
    public CallContext getApplicationSessionRootContext();
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

    /**
       Get the session's id, if there is an associated one.
    */
    public String getId(CallContext context);
}


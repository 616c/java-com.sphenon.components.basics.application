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
   An instance of this class represents an instance of a running application.
   It is typically associated with a single session at one time, but this is
   not a requirement.

   The difference between an {@link ApplicationInstance} and an {@link ApplicationSession}
   is e.g. in groupware scenarios, where several sessions may take place
   with the same instance, maybe at different points in time.

   The difference between an {@link ApplicationInstance} and an {@link Application}
   is that e.g. on a server there is only one instance of the latter, created
   at startup, while each user creates it's own instance of the former.
*/
public interface ApplicationInstance {
    /**
       Get the associated {@link Application}.
    */
    public Application getApplication(CallContext context);

    /**
       A collection of component specific instances of {@link ApplicationInstanceComponentData}.

       @returns An OSet of ApplicationInstanceComponentDatas, retrievable by their specific Type.
     */
    public OSet_ApplicationInstanceComponentData_Type_ getApplicationInstanceComponentDatas(CallContext call_context);

    /**
       Retrieve a specific {@link ApplicationInstanceComponentData} from the collection.

       @returns An instance of ApplicationInstanceComponentDataType, if found, or null
     */
    public <ApplicationInstanceComponentDataType extends ApplicationInstanceComponentData> ApplicationInstanceComponentDataType getApplicationInstanceComponentData(CallContext call_context, Class<ApplicationInstanceComponentDataType> target_class);

    /**
       Create a new session with this application instance.

       @param datas Optionally a set of datas which will be associated with the
                    newly created {@link ApplicationSession} (i.e. stored in the
                    above mentioned OSet).
     */
    public ApplicationSession createApplicationSession(CallContext call_context, ApplicationSessionComponentData... datas);

    /**
      Returns and optionally creates a derived context based upon the
      corresponding application root context and prepares it with application
      instance specific settings. The application root context is obtained
      from the {@link Application} instance.
     */
    public CallContext getApplicationInstanceRootContext();
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
       Returns a registry for this instance which can be used to store and
       retrieve sessions by id. This serves as a convenience feature for
       frontends, it is not mandatory to use the registry.
    */
    public ApplicationSessionRegistry getApplicationSessionRegistry(CallContext context);
}

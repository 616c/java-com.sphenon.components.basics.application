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
import com.sphenon.basics.configuration.*;

import com.sphenon.basics.application.tplinst.*;

/**
   An instance of this class represents a specific Application implementation,
   in contrast to an {@link ApplicationInstance}. It serves as an access point
   for several types of application related configuration items.
*/
public interface Application {

    /**
       Unique identifier for this application.
       Recommended use is a Java package path like "com.sphenon.myapp".
    */
    public String getId(CallContext context);

    /**
       Returns a preconfigured {@link Configuration} instance suitable to
       obtain configuration properties for this application.
     */
    public Configuration getApplicationConfiguration (CallContext context);

    /**
       A list of all loaded ApplicationComponents associated with this
       application.
     */
    public Vector_ApplicationComponent_long_ getApplicationComponents(CallContext context);

    /**
       A collection of component specific instances of {@link ApplicationComponentData}.

       @returns An OSet of ApplicationComponentDatas, retrievable by their specific Type.
       @todo:al von BW: Es wäre ganz nett wenn dieser spezielle OSet auch die Basisklassen berücksichtigen würde
                        Im Moment sind es (leider) nur die Interfaces.
     */
    public OSet_ApplicationComponentData_Type_ getApplicationComponentDatas(CallContext context);

    /**
       Retrieve a specific {@link ApplicationComponentData} from the collection.

       @returns An instance of ApplicationComponentDataType, if found, or null
     */
    public <ApplicationComponentDataType extends ApplicationComponentData> ApplicationComponentDataType getApplicationComponentData(CallContext context, Class<ApplicationComponentDataType> target_class);

    /**
       A new instance of this application.

       @param datas Optionally a set of datas which will be associated with the
                    newly created {@link ApplicationInstance} (i.e. stored in the
                    above mentioned OSet).
     */
    public ApplicationInstance createApplicationInstance(CallContext context, ApplicationInstanceComponentData... datas);

    /**
       Retrieves and optionally creates a new instance of this application
       which is or will be associated with the given key. If not destroyed,
       this method always returns the same {@link ApplicationInstance} for
       the same key. If the {@link ApplicationInstance} is destroyed, the call
       to this method for the same key will return a new instance.

       @param key An instance which is used as a hash key to store and
                  retrieve the {@link ApplicationInstance}.
       @param datas Will be passed to the respective createApplicationInstance call.
     */
    public ApplicationInstance getApplicationInstance(CallContext context, Object key, ApplicationInstanceComponentData... datas);

    /**
      Returns and optionally creates a derived context based upon the
      initialisation context and prepares it with application specific
      settings. The InitialisationContext is obtained from the {@link
      RootContext} class.
     */
    public CallContext getApplicationRootContext();

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
       Attaches an (optionally defined) application specific class loader to
       the current thread and returns the previous defined loader.
    */
    public ClassLoader setApplicationClassLoader(CallContext context);

    /**
       Reattaches optionally the previously defined loader to the current thread.
    */
    public void resetApplicationClassLoader(CallContext context, ClassLoader current);

    /**
       Use to report (actually unexpected) exceptions that happen during
       processing of some request and which cannot or shall not be reported
       directly to the user interface, either because there is no good way to
       do so in some circumstances (like preparing a choice box) or the user
       cannot handle the information in this situation.

       The application will dispatch this exception to its components until
       one component returns true and thereby signals that it did handle the
       exception.
    */
    public void handleException(CallContext context, Throwable throwable);
}


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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.system.*;

import java.lang.reflect.*;

public class ApplicationPackageInitialiser {

    static protected boolean initialised = false;

    static {
        initialise(RootContext.getRootContext());
    }

    static public synchronized void initialise (CallContext context) {
        
        if (initialised == false) {
            initialised = true;

            Configuration.loadDefaultProperties(context, com.sphenon.basics.application.ApplicationPackageInitialiser.class);

            String load_applications = getConfiguration(context).get(context, "LoadApplications", (String) null);
            if (load_applications != null) {
                for (String app : load_applications.split(";")) {
                    String[] conf = app.split("#");
                    String application_id = conf[0];
                    String software_unit  = conf.length > 1 ? conf[1] : null;

                    if (application_id != null && application_id.isEmpty() == false) {
                        if (software_unit != null && software_unit.isEmpty() == false) {
                            ReflectionUtilities ru = new ReflectionUtilities(context);
                            System.err.println("Registering class loader: " + application_id + ", " + software_unit);
                            Constructor constructor = ru.tryGetConstructor(context, "com.sphenon.software.production.classes.ApplicationClassLoaderFactory_SoftwareUnit", CallContext.class, String.class, String.class, ApplicationClassLoaderFactory.class, String.class, String.class);
                            if (constructor != null) {
                                ApplicationClassLoaderFactory aclf = (ApplicationClassLoaderFactory) ru.tryNewInstance(context, constructor, context, application_id, software_unit, null /* aclf_interaction */, null, "^(sphenon-1.0|javascript|xml|eclipselink)$");
                                ApplicationClassLoaderFactoryRegistry.registerFactory(context, aclf);
                            } else {
                                System.err.println("Registering class loader: failed");
                            }
                        }

                        Application application = ApplicationManager.getApplication(context, application_id);
                    }
                }
            }
        }
    }

    static protected Configuration config;
    static public Configuration getConfiguration (CallContext context) {
        if (config == null) {
            config = Configuration.create(RootContext.getInitialisationContext(), "com.sphenon.basics.application");
        }
        return config;
    }
}

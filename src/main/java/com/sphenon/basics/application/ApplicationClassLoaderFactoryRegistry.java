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

import java.util.Vector;

public class ApplicationClassLoaderFactoryRegistry {

    static protected Vector<ApplicationClassLoaderFactory> factories;

    static synchronized public void registerFactory(CallContext context, ApplicationClassLoaderFactory application_class_loader_factory) {
        if (factories == null) {
            factories = new Vector<ApplicationClassLoaderFactory>();
        } else {
            for (ApplicationClassLoaderFactory factory : factories) {
                if (factory.equals(application_class_loader_factory)) {
                    return;
                }
            }
        }
        factories.add(application_class_loader_factory);
    }

    static synchronized public void deregisterFactory(CallContext context, ApplicationClassLoaderFactory application_class_loader_factory) {
        if (factories != null) {
            for (int i=0; i<factories.size(); i++) {
                if (factories.get(i) == application_class_loader_factory) {
                    factories.remove(i);
                    return;
                }
            }
        }
    }

    static synchronized public ApplicationClassLoaderFactory tryGetClassLoaderFactory (CallContext context, String application_id) {
        if (factories != null) {
            for (ApplicationClassLoaderFactory factory : factories) {
                if (factory.matches(context, application_id)) {
                    return factory;
                }
            }
        }
        return null;
    }
}

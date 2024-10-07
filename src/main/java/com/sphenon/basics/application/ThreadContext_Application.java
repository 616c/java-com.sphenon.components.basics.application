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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.system.*;

import java.lang.reflect.*;
import java.net.*;

public class ThreadContext_Application extends Class_ThreadContext {

    protected String application_id;
    protected URL[] class_path;
    protected String parent_class_filter;
    protected String[] configuration_arguments;
    protected boolean initialise_configuration;
    protected boolean use_current_class_loader_as_parent;

    /**
      Do not call directly, do use static createThreadContext methods
      instead. Constructor is public only because we need to access it via
      reflection.
    */
    public ThreadContext_Application(String application_id, URL[] class_path, String parent_class_filter, ClassLoader class_loader, String[] configuration_arguments, boolean initialise_configuration, boolean use_current_class_loader_as_parent) {
        super(null, class_loader);
        this.application_id = application_id;
        this.class_path = class_path;
        this.parent_class_filter = parent_class_filter;
        this.configuration_arguments = configuration_arguments;
        this.initialise_configuration = initialise_configuration;
        this.use_current_class_loader_as_parent = use_current_class_loader_as_parent;
    }

    /**
      Do not call directly, do use static createThreadContext methods
      instead. Constructor is public only because we need to access it via
      reflection.
    */
    public ThreadContext_Application(CallContext context, ClassLoader class_loader) {
        super(context, class_loader);
        this.initialise_configuration = false;
    }

    static public Object[] createThreadContext(CallContext context, ClassLoader class_loader) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader((ClassLoader) class_loader);

            // eigentlich müßte man hier noch CallContext via ClassLoader laden,
            // aber dann müßte man auch das erste Argument von der Methode hier
            // auf Object setzen, und naja, was ist der Zweck der Methode?
            // eher ja wohl ein ClassLoader der was zusätzliches beinhaltet und
            // nicht isoliert ist, und dann ist es eh egal
            Constructor constructor = ReflectionUtilities.getConstructor(context, "com.sphenon.basics.application.ThreadContext_Application", CallContext.class, ClassLoader.class);
            Object tc = ReflectionUtilities.newInstance(context, constructor, context, class_loader);
            return new Object[] { tc, class_loader };

        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    static public Object[] createThreadContext(CallContext context, String application_id, URL[] class_path, String[] configuration_arguments, boolean initialise_configuration, boolean use_current_class_loader_as_parent, String parent_class_filter) {
        ClassLoader class_loader;
        try {
            class_loader = new URLClassLoaderWithId(class_path, use_current_class_loader_as_parent ? Thread.currentThread().getContextClassLoader() : null, "<thread context " + application_id + ">", parent_class_filter);
        } catch (java.util.regex.PatternSyntaxException pse) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, pse, "Parent class loader filter '%(filter)' is invalid", "filter", parent_class_filter);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader((ClassLoader) class_loader);

            Constructor constructor = ReflectionUtilities.getConstructor(context, "com.sphenon.basics.application.ThreadContext_Application", String.class, URL[].class, String.class, ClassLoader.class, String[].class, boolean.class, boolean.class);
            Object tc = ReflectionUtilities.newInstance(context, constructor, application_id, class_path, parent_class_filter, class_loader, configuration_arguments, initialise_configuration, use_current_class_loader_as_parent);
            return new Object[] { tc, class_loader };

        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    public CallContext getContext () {
        if (this.context == null) {
            CallContext root_context = com.sphenon.basics.context.classes.RootContext.getRootContext();

            if (this.initialise_configuration) {
                if (this.configuration_arguments != null) {
                    Configuration.checkCommandLineArgs(this.configuration_arguments);
                }

                Configuration.initialise(root_context);
            }

            if (this.application_id != null) {
                
                Application         app  = ApplicationManager.getApplication(root_context, this.application_id);
                ApplicationInstance appi = app.getApplicationInstance(root_context, "singleton");
                ApplicationSession  apps = appi.createApplicationSession(root_context);
                CallContext         sc   = apps.getApplicationSessionRootContext();


                /*
                  TODO MULTITHREADED TEST SCRIPTS
                
                  1. hier keinen Session Context erzeugen, sondern auf Instance Level bleiben
                  2. in den TestScripts selbst am Anfang eine Session öffnen
                     via ApplicationContext
                         getApplicationInstance
                         createApplicationSession
                         getApplicationSessionRootContext

                  3a. parallele Test, also eine Testklasse, die eine Reihe von Subtests
                      bekommt, die eben parallel ausgeführt werden

                  3b. TestScript-Sync-Points
                      ein Mechanismus, der am VUIScript aufgerufen wird, und im
                      aktiven Script ein "reached(current_point)" einbaut
                      und in allen anderen gerade recordeten scripts ein "waitfor(current_point)"
                      und den point anschließend hochzählt

                  ODER einfacher

                  3c  beim script recording den context session spezifisch mit rausschreiben
                      und auch mehrere vui_roots anlegen 

                */

                this.context = sc;
            } else {
                this.context = root_context;
            }
        }
        return this.context;
    }
}

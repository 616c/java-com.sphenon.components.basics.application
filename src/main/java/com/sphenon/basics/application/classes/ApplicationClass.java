package com.sphenon.basics.application.classes;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.classes.*;

import com.sphenon.basics.application.*;
import com.sphenon.basics.application.tplinst.*;
import com.sphenon.basics.application.returncodes.*;

import java.io.File;
import java.lang.reflect.*;
import java.util.concurrent.*;
import java.util.*;

public class ApplicationClass implements Application {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationClass"); };

    // -------------------------------------------------------------------------------------------------
    // construction & destruction

    // construction

    public ClassLoader setApplicationClassLoader(CallContext context) {
        if (this.class_loader != null) {        
            ClassLoader current = Thread.currentThread().getContextClassLoader();
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Setting current thread classloader to '%(new)', previous value was '%(old)'", "new", this.class_loader, "old", current); }
            Thread.currentThread().setContextClassLoader(this.class_loader);
            return current;
        }
        return null;
    }

    public void resetApplicationClassLoader(CallContext context, ClassLoader current) {
        if (current != null) {        
            Thread.currentThread().setContextClassLoader(current);
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Resetting current thread classloader to '%(old)'", "old", current); }
        }
    }

    protected ApplicationClass(CallContext context, String id, ApplicationComponentData... datas) {
        this.id = id;
        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating Application '%(applicationid)'", "applicationid", this.id); }

        ApplicationClassLoaderFactory cl_factory = ApplicationClassLoaderFactoryRegistry.tryGetClassLoaderFactory(context, id);
        this.class_loader       = (cl_factory == null ? null : cl_factory.createClassLoader(context));
        this.partial_class_path = (cl_factory == null ? null : cl_factory.tryGetPartialClassPath(context));
        
        ClassLoader current = null;
        try {
            current = this.setApplicationClassLoader(context);

            this.optionallyInitialisePackage(context);

            this.setupComponents(context);

            this.type_context_name = this.getApplicationConfiguration(context).get(context, "TypeContext", (String) null);
            this.string_pool_name = this.getApplicationConfiguration(context).get(context, "StringPool", (String) null);

            if (    this.got_components == false
                 && (    this.type_context_name == null
                      || this.type_context_name.length() == 0)
                 && (    this.string_pool_name == null
                      || this.string_pool_name.length() == 0)
               ) {
                CustomaryContext.create((Context)context).throwConfigurationError(context, "There is no application defined named '%(id)'", "id", this.id);
                throw (ExceptionConfigurationError) null; // compiler insists
            }

            for (ApplicationComponentData data : datas) {
                this.getApplicationComponentDatas(context).set(context, data);
            }

            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising components for application '%(applicationid)'...", "applicationid", this.getId(context)); }

            for (ApplicationComponent ac : this.getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                ApplicationComponentData acd = ac.notifyApplicationBegin (this.getModifyableApplicationRootContext(), this);
                if (acd != null) {
                    this.getApplicationComponentDatas(context).set(context, acd);
                }
            }
        } finally {
            this.resetApplicationClassLoader(context, current);
        }
    }

    public void ensure(CallContext context, ApplicationComponentData... datas) {
        if (datas != null) {
            TO_ADD: for (ApplicationComponentData data : datas) {
                for (Iterator_ApplicationComponentData_ iterator = this.getApplicationComponentDatas(context).getNavigator(context);
                     iterator.canGetCurrent(context);
                     iterator.next(context)) {
                    if (data == iterator.tryGetCurrent(context)) {
                        continue TO_ADD;
                    }
                }
                this.getApplicationComponentDatas(context).set(context, data);
            }
        }
    }

    static protected ConcurrentHashMap<String,ApplicationClass> applications = new ConcurrentHashMap<String, ApplicationClass>(); 

    static public ApplicationClass getOrCreate(CallContext context, String id, ApplicationComponentData... datas) {
        synchronized (ApplicationClass.class) {
            ApplicationClass application = applications.get(id);
            if (application == null) {
                application = new ApplicationClass(context, id, datas);
                applications.put(id, application);
            } else {
                ((ApplicationClass) application).ensure(context, datas);
            }
            return application;
        }
    }

    static public void clearApplicationCache(CallContext context) {
        synchronized (ApplicationClass.class) {
            if (applications != null) {
                for (String aid : applications.keySet()) {
                    Application a = applications.get(aid);
                    a.destroy(context);
                }
            }
            applications = new ConcurrentHashMap<String,ApplicationClass>();
        }
    }

    protected ClassLoader class_loader;
    protected String partial_class_path;

    // destruction

    protected boolean is_destroyed = false;

    public boolean getIsDestroyed(CallContext context) {
        return this.is_destroyed;
    }

    public void destroy (CallContext context) {
        if ( ! this.is_destroyed) {
            synchronized (this) {
                if ( ! this.is_destroyed) {
                    this.is_destroyed = true;

                    ClassLoader current = null;
                    try {
                        current = this.setApplicationClassLoader(context);

                        this.optionallyDeInitialisePackage(context);

                        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising components for application '%(applicationid)'...", "applicationid", this.getId(context)); }
                        for (ApplicationComponent ac : this.getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
                            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deinitialising component '%(componentid)'...", "componentid", ac.getId(context)); }
                            ac.notifyApplicationEnd (this.getModifyableApplicationRootContext(), this);
                        }
                        
                        TypeManager.cleanupTypeContext(context, this.type_context_name);
                        TypeManager.cleanupTypesByClassLoader(context, this.class_loader );

                    } finally {
                        this.resetApplicationClassLoader(context, current);
                    }
                    if (this.class_loader != null && this.class_loader instanceof URLClassLoaderWithId) {
                        ((URLClassLoaderWithId) this.class_loader).close(context);
                    }
                }
            }
        }
    }

    public void finalize () throws Throwable {
        this.destroy(this.getApplicationRootContext());
    }

    // -------------------------------------------------------------------------------------------------
    // attributes

    protected String type_context_name;
    protected String string_pool_name;

    protected String id;

    public String getId (CallContext context) {
        return this.id;
    }

    protected Configuration application_configuration;

    public Configuration getApplicationConfiguration (CallContext context) {
        if (this.application_configuration == null) {
            this.application_configuration = Configuration.create(RootContext.getInitialisationContext(), "com.sphenon.basics.application.APPLICATION." + this.id);
        }
        return this.application_configuration;
    }

    protected OSet_ApplicationComponentData_Type_ application_component_datas;

    public OSet_ApplicationComponentData_Type_ getApplicationComponentDatas(CallContext context) {
        if (this.application_component_datas == null) {
            this.application_component_datas = new OSetImpl_ApplicationComponentData_Type_(context);
        }
        return this.application_component_datas;
    }

    public <ApplicationComponentDataType extends ApplicationComponentData> ApplicationComponentDataType getApplicationComponentData(CallContext context, Class<ApplicationComponentDataType> target_class) {
        if (this.application_component_datas == null) { return null; }
        ApplicationComponentData data = this.application_component_datas.tryGetSole(context, TypeManager.get(context, target_class));
        return (ApplicationComponentDataType) data;
    }

    // -------------------------------------------------------------------------------------------------
    // instance factory

    public ApplicationInstance createApplicationInstance(CallContext context, ApplicationInstanceComponentData... datas) {
        return ApplicationInstanceClass.create(context, this, datas);
    }

    protected volatile ConcurrentHashMap<Object, ApplicationInstance> application_instances;

    public ApplicationInstance getApplicationInstance(CallContext context, Object key, ApplicationInstanceComponentData... datas) {
        
        if (this.application_instances == null) {
            synchronized(this) {
              if (this.application_instances == null) {
                this.application_instances = new ConcurrentHashMap<Object, ApplicationInstance>();
              }
            }
        }
        ApplicationInstance application_instance = this.application_instances.get(key);
        if (application_instance == null || application_instance.getIsDestroyed(context)) {
          synchronized(this) {
            application_instance = this.application_instances.get(key);
            if (application_instance == null || application_instance.getIsDestroyed(context)) {
              application_instance = createApplicationInstance(context, datas);
              this.application_instances.put(key, application_instance);
            }
          }
        }
        return application_instance;
    }

    // -------------------------------------------------------------------------------------------------
    // root context

    volatile protected Context application_root_context;

    public CallContext getApplicationRootContext() {
        return this.getModifyableApplicationRootContext();
    }

    public Context getModifyableApplicationRootContext() {
        if (this.application_root_context == null) {
            synchronized(this) {
                if (this.application_root_context == null) {
                    Context context = this.application_root_context = Context.create(RootContext.getInitialisationContext());
                    CustomaryContext cc = CustomaryContext.create(context);
                    
                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating application root context for '%(id)'", "id", this.id); }
                    
                    if (this.type_context_name != null) {
                        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...with Type Context '%(typecontext)'", "typecontext", this.type_context_name); }
                        TypeContext tc = TypeContext.create(context);
                        tc.setSearchPathContext(context, this.type_context_name);
                    }

                    if (this.string_pool_name != null) {
                        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...with String Pool '%(string_pool)'", "string_pool", this.string_pool_name); }
                        StringContext sc = StringContext.create(context);
                        sc.setStringPool(context, StringPoolConfiguration.create(context, this.string_pool_name));
                    }

                    ApplicationContext ac = ApplicationContext.create(context);
                    ac.setApplication(context, this);

                    if (this.partial_class_path != null) {
                        ConfigurationContext configuration_context = ConfigurationContext.create(context);
                        String cp = configuration_context.getPropertyEntry(context, "com.sphenon.basics.javacode.classes.JavaCodeManagerImpl.ClassPath");
                        configuration_context.instantiateLocalProperties(context);
                        cp = this.partial_class_path + (cp != null && cp.length() != 0 ? (File.pathSeparator + cp) : "");
                        configuration_context.setPropertyEntry(context, "com.sphenon.basics.javacode.classes.JavaCodeManagerImpl.ClassPath", cp);
                    }

                }
            }
        }
        return this.application_root_context;
    }

    // -------------------------------------------------------------------------------------------------
    // initialise & deinitialise

    protected void optionallyInitialisePackage(CallContext context) {
        ReflectionUtilities ru = new ReflectionUtilities(context);
        Method m = ru.tryGetMethod(context, this.id + ".PackageManager", "getSingleton", CallContext.class);
        if (m != null) {
            ru.invoke(context, m, null, context);
        }
    }

    protected void optionallyDeInitialisePackage(CallContext context) {
        ReflectionUtilities ru = new ReflectionUtilities(context);
        Method m = ru.tryGetMethod(context, this.id + ".PackageManager", "destroySingleton", CallContext.class);
        if (m != null) {
            ru.invoke(context, m, null, context);
        }
    }

    // -------------------------------------------------------------------------------------------------
    // application component management

    protected Vector_ApplicationComponent_long_ application_components;
    protected boolean got_components;

    public Vector_ApplicationComponent_long_ getApplicationComponents(CallContext context) {
        return this.application_components;
    }

    protected void setupComponents(CallContext context) {
        this.application_components = new Factory_Vector_ApplicationComponent_long_(context).construct(context);
        String components_property = "APPLICATION." + this.id + ".ApplicationComponents";
        Configuration config = ApplicationPackageInitialiser.getConfiguration(context);
        String components = config.get(context, components_property, (String) null);
        this.got_components = false;
        int index=1;
        while (components != null) {
            this.got_components = true;
            if (components.length() != 0) {
                for (String c : components.split(":")) {
                    if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Creating Application Component '%(component)'", "component", c); }
                    instantiateComponent(context, c);
                }
            }
            components = config.get(context, components_property + "." + index++, (String) null);
        }
    }

    protected void instantiateComponent(CallContext context, String application_component_name) {
        CustomaryContext cc = CustomaryContext.create((Context)context);

        if (application_component_name == null || application_component_name.length() == 0) { return; }

        Class appcompclass = null;
        try {
            appcompclass = com.sphenon.basics.cache.ClassCache.getClassForName(context, application_component_name);
        } catch (ClassNotFoundException cnfe1) {
            cc.throwPreConditionViolation(context, "ApplicationComponent class '%(appcompclass)' does not exist", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        }
        Constructor cons = null;
        try {
            cons = appcompclass.getConstructor(com.sphenon.basics.context.CallContext.class);
        } catch (NoSuchMethodException nsme) {
            cc.throwPreConditionViolation(context, nsme, "ApplicationComponent class '%(appcompclass)' does not provide an adequate constructor '(CallContext)'", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        }

        try {
            this.application_components.append(context, (ApplicationComponent) cons.newInstance(context));
        } catch (ClassCastException cce) {
            cc.throwPreConditionViolation(context, cce, "Cannot instantiate ApplicationComponent '%(appcompclass)', class is not derived from ApplicationComponent", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        } catch (InstantiationException e) {
            cc.throwPreConditionViolation(context, e, "Cannot instantiate ApplicationComponent '%(appcompclass)', class is abstract", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        } catch (IllegalAccessException e) {
            cc.throwPreConditionViolation(context, e, "Cannot instantiate ApplicationComponent '%(appcompclass)', constructor is inaccessible", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        } catch (IllegalArgumentException e) {
            cc.throwPreConditionViolation(context, e, "Cannot instantiate ApplicationComponent '%(appcompclass)', signature mismatch or unwrapping or method invocation", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof java.lang.RuntimeException) {
                java.lang.RuntimeException re = null;
                try {
                    re = (java.lang.RuntimeException) e.getTargetException();
                } catch (ClassCastException ee) {
                    cc.throwImpossibleState(context, ee, "Java 'instanceof' operator reports castability to 'java.lang.RuntimeException', but cast failed");
                }
                throw re;
            }
            if (e.getTargetException() instanceof java.lang.Error) {
                try {
                    throw (java.lang.Error) e.getTargetException();
                } catch (ClassCastException ee) {
                    cc.throwImpossibleState(context, "Java 'instanceof' operator reports castability to 'java.lang.Error', but cast failed");
                }
            }
            cc.throwPreConditionViolation(context, e.getTargetException(), "Cannot instantiate ApplicationComponent '%(appcompclass)', constructor throwed an exception", "appcompclass", application_component_name);
            throw (ExceptionPreConditionViolation) null;
        }
    }

    public void handleException(CallContext context, Throwable throwable) {
        for (ApplicationComponent ac : this.getApplicationComponents(context).getIterable_ApplicationComponent_(context)) {
            if (ac.handleException(context, throwable) == true) {
                return;
            }
        }
        System.err.println("*** CAUTION - NO APPLICATION COMPONENT HANDLED THIS EXCEPTION ***");
        throwable.printStackTrace();
    }
}

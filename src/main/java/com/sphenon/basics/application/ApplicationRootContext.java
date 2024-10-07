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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.configuration.*;

public class ApplicationRootContext {

    public ApplicationRootContext(String application_id, String instance_id) {
        this.root_context = RootContext.getRootContext();
        this.application = ApplicationManager.getApplication(this.root_context, application_id);
        this.application_instance = this.application.getApplicationInstance(this.root_context, instance_id);
        this.application_session  = this.application_instance.createApplicationSession(this.root_context);
        this.context = Context.create(this.application_session.getApplicationSessionRootContext());
    }

    protected Application application;

    public Application getApplication (CallContext context) {
        return this.application;
    }

    protected ApplicationInstance application_instance;

    public ApplicationInstance getApplicationInstance (CallContext context) {
        return this.application_instance;
    }

    protected ApplicationSession application_session;

    public ApplicationSession getApplicationSession (CallContext context) {
        return this.application_session;
    }

    protected Context root_context;

    public Context getRootContext (CallContext context) {
        return this.root_context;
    }

    protected Context context;

    public Context getContext (CallContext context) {
        return this.context;
    }

    protected ClassLoader current_class_loader;

    public void setClassLoader() {
        this.current_class_loader = this.application.setApplicationClassLoader(context);
    }

    public void resetClassLoader() {
        this.application.resetApplicationClassLoader(context, this.current_class_loader);
    }
}

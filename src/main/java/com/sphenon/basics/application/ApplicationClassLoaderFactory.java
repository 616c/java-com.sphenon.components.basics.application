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
import com.sphenon.basics.configuration.*;

import com.sphenon.basics.application.tplinst.*;

public interface ApplicationClassLoaderFactory {
    /**
       True, if this factory is responsible for creating class loaders for
       instances of the application given by id.
    */
    public boolean matches(CallContext context, String application_id);

    /**
       Create or retrieve a ClassLoader for instances of this application.
    */
    public ClassLoader createClassLoader(CallContext context);

    /**
       If necessary as well as possible, provide an additional part of a
       classpath, for purposes of ondemand runtime compilation.
    */
    public String tryGetPartialClassPath(CallContext context);
}


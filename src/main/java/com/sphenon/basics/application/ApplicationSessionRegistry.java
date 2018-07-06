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

import com.sphenon.basics.application.classes.*;
import com.sphenon.basics.application.returncodes.*;

import java.util.Map;
import java.util.HashMap;

public class ApplicationSessionRegistry {
    protected Map<String,ApplicationSession> registry;

    public ApplicationSessionRegistry(CallContext context) {
    }

    // might throw: ALFInvalidSession (outdated)
    // but then, it should be removed, shouldn't it?
    public synchronized ApplicationSession get(CallContext context, String session_id) {
        if (registry == null) {
            return null;
        }
        return registry.get(session_id);
    }

    public synchronized void add(CallContext context, ApplicationSession application_session) {
        if (registry == null) {
            registry = new HashMap<String,ApplicationSession>();
        }
        registry.put(application_session.getId(context), application_session);
    }

    public synchronized void remove(CallContext context, ApplicationSession application_session) {
        if (registry != null) {
            registry.remove(application_session.getId(context));
        }
    }
}

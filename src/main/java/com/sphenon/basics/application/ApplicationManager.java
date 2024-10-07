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

import com.sphenon.basics.application.classes.*;
import com.sphenon.basics.application.returncodes.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public final class ApplicationManager {
    static final public Class _class = ApplicationManager.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    static public Application getApplication (CallContext context, String application_id, ApplicationComponentData... datas) {
        return ApplicationClass.getOrCreate(context, optionallyResolveApplicationAlias(context, application_id), datas);
    }

    static protected Map<String,Stack<String>> application_aliases;

    static public String optionallyResolveApplicationAlias(CallContext context, String application_id) {
        if (application_aliases == null) {
            return application_id;
        }
        Stack<String> stack = application_aliases.get(application_id);
        if (stack == null) {
            return application_id;
        }
        if (stack.isEmpty()) {
            return application_id;
        }
        return stack.peek();
    }

    static public void registerApplicationAlias(CallContext context, String alias, String application_id) {
        if (application_aliases == null) {
            application_aliases = new HashMap<String,Stack<String>>();
        }
        Stack<String> stack = application_aliases.get(alias);
        if (stack == null) {
            stack = new Stack<String>();
            application_aliases.put(alias, stack);
        }
        stack.push(application_id);
    }

    static public void unregisterApplicationAlias(CallContext context, String alias, String application_id) {
        if (application_aliases == null) {
            if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "While trying to unregister application alias '%(alias)', no registry was found", "alias", alias); }
            return;
        }
        Stack<String> stack = application_aliases.get(alias);
        if (stack == null) {
            if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "While trying to unregister application alias '%(alias)', no registry stack was found", "alias", alias); }
            return;
        }
        if (stack.isEmpty()) {
            if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "While trying to unregister application alias '%(alias)', registry stack is empty", "alias", alias); }
            return;
        }
        if (application_id.equals(stack.peek()) == false) {
            if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "While trying to unregister application alias '%(alias)', no registry stack entry '%(entry)' does not equal id '%(id)' to be removed", "alias", alias, "entry", stack.peek(), "id", application_id); }
            return;
        }
        stack.pop();
    }
}

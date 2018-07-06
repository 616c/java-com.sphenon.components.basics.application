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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.many.*;

import com.sphenon.basics.application.*;

public class ApplicationClientManager<ClientType extends ApplicationClient> {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.application.classes.ApplicationClientManager"); };

    static final public boolean ID_UPPERCASE    = true;
    static final public boolean ID_DASHES       = false;
    static final public int     ID_RANDOM_BYTES = 16;

    public ApplicationClientManager(CallContext root_context, String application_id, String client_name) {
        this.application_id = application_id;
        this.client_name = client_name;
        this.client_map = new ExpiringMap<String,ClientType>(15 * 60 * 1000);
    }

    protected String application_id;
    protected String client_name;
    protected ExpiringMap<String,ClientType> client_map;

    static public enum IdType { Unknown, New, PossiblyValid, Valid, Invalid };

    public IdType checkId(CallContext root_context, String client_id) {
        if (client_id == null || client_id.matches("^(|create|new)$") || SystemUtilities.checkUUID(root_context, client_id, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES, true)) {
            return IdType.New;
        } else if (SystemUtilities.checkUUID(root_context, client_id, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES)) {
            return IdType.PossiblyValid;
        } else {
            return IdType.Invalid;
        }
    }

    public ClientType get(CallContext root_context, String client_id, IdType id_type) {
        if (client_id != null && id_type == IdType.Unknown) {
            id_type = checkId(root_context, client_id);
        }
        if (id_type == IdType.PossiblyValid || id_type == IdType.Valid) {
            return client_map.get(client_id);
        } else {
            return null;
        }
    }

    public ClientType create(CallContext root_context, String client_id, IdType id_type) {
        if (client_id != null && id_type == IdType.Unknown) {
            id_type = checkId(root_context, client_id);
        }
        if (id_type == IdType.New) {
            ClientType client = null;
            client_id = com.sphenon.basics.system.SystemUtilities.getUUID(root_context, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES);
            client = this.createApplicationClient(root_context, this.application_id, client_id, this.client_name);
            client_map.putAndReturnValue(client_id, client);
            return client;
        } else {
            return null;
        }
    }

    public void terminate(CallContext root_context, String client_id, IdType id_type) {
        if (client_id != null && id_type == IdType.Unknown) {
            id_type = checkId(root_context, client_id);
        }
        if (id_type == IdType.PossiblyValid || id_type == IdType.Valid) {
            this.terminate(root_context, get(root_context, client_id, id_type));
        }
    }

    public void terminate(CallContext root_context, ClientType client) {
        if (client != null) {
            client.deinitialise();
            client_map.remove(client.getClientId());
        }
    }

    protected ClientType createApplicationClient(CallContext root_context, String application_id, String client_id, String client_name) {
        return (ClientType) (new ApplicationClientClass(root_context, application_id, client_id, client_name, true));
    }
}

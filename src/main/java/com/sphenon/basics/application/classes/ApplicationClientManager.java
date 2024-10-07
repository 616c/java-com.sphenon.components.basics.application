package com.sphenon.basics.application.classes;

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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.many.*;

import com.sphenon.basics.application.*;

import java.util.Map;
import java.util.HashMap;

public class ApplicationClientManager<ClientType extends ApplicationClient> {
    static final public Class _class = ApplicationClientManager.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    static protected Configuration config;
    static { config = Configuration.create(RootContext.getInitialisationContext(), _class); };

    static final public boolean ID_UPPERCASE    = true;
    static final public boolean ID_DASHES       = false;
    static final public int     ID_RANDOM_BYTES = 16;

    protected ClientFactory client_factory;

    public ApplicationClientManager(CallContext root_context, String application_id, String client_name) {
        this(root_context, application_id, client_name, null);
    }

    public ApplicationClientManager(CallContext root_context, String application_id, String client_name, ClientFactory client_factory) {
        this.application_id = application_id;
        this.client_name = client_name;
        this.client_map = new ExpiringMap<String, ClientType>(this.getMaximumLifeTimeMilliSeconds(root_context));
        this.client_factory = client_factory;
    }

    protected Long maximum_life_time_milli_seconds;

    public long getMaximumLifeTimeMilliSeconds(CallContext context) {
        if (this.maximum_life_time_milli_seconds == null) {
            this.maximum_life_time_milli_seconds = config.get(context, "ClientTimeoutMilliSeconds", 15 * 60 * 1000L);
        }
        return this.maximum_life_time_milli_seconds;
    }

    public void setMaximumLifeTimeMilliSeconds(CallContext context, long mltms) {
        this.maximum_life_time_milli_seconds = mltms;
        this.client_map.setDefaultMaxLifeTimeMillis(mltms);
    }

    public void setMaximumLifeTimeMilliSeconds(CallContext context, long mltms, String client_id) {
        if (this.client_map != null) {
            this.client_map.setMaxLifeTimeMillis(mltms, client_id);
        }
    }

    static public interface ClientFactory<ClientType extends ApplicationClient> {
        public ClientType createApplicationClient(CallContext root_context, String application_id, String client_id, String client_name, ApplicationClientManager manager, Object... options);
    }

    static protected Map<String, ApplicationClientManager> managers_by_realm;

    static public<ClientType extends ApplicationClient> ApplicationClientManager<ClientType> get(CallContext root_context, String application_id, String client_name) {
        return getOrCreate(root_context, application_id, client_name, null, false);
    }

    static public<ClientType extends ApplicationClient> ApplicationClientManager<ClientType> getOrCreate(CallContext root_context, String application_id, String client_name, ClientFactory client_factory) {
        return getOrCreate(root_context, application_id, client_name, client_factory, true);
    }

    static protected<ClientType extends ApplicationClient> ApplicationClientManager<ClientType> getOrCreate(CallContext root_context, String application_id, String client_name, ClientFactory client_factory, boolean optionally_create) {
        ApplicationClientManager manager = null;

        String client_realm = application_id + "." + client_name;

        if (managers_by_realm == null) {
            managers_by_realm = new HashMap<String, ApplicationClientManager>();
        } else {
            manager = managers_by_realm.get(client_realm);
        }

        if (manager == null && optionally_create) {
            manager = new ApplicationClientManager(root_context, application_id, client_name, client_factory);
            managers_by_realm.put(client_realm, manager);
        }

        return manager;
    }

    protected String application_id;
    protected String client_name;
    protected ExpiringMap<String, ClientType> client_map;

    static public enum IdType { Unknown, New, PossiblyValid, Valid, Invalid };

    static public IdType checkId(CallContext root_context, String client_id) {
        if (client_id == null || client_id.matches("^(|create|new)$") || SystemUtilities.checkUUIDHex(root_context, client_id, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES, true)) {
            return IdType.New;
        } else if (SystemUtilities.checkUUIDHex(root_context, client_id, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES)) {
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

    public ClientType create(CallContext root_context, String client_id, IdType id_type, Object... options) {
        if (client_id != null && id_type == IdType.Unknown) {
            id_type = checkId(root_context, client_id);
        }
        if (id_type == IdType.New || id_type == IdType.PossiblyValid) {
            ClientType client = null;
            client_id = com.sphenon.basics.system.SystemUtilities.getUUIDHex(root_context, ID_UPPERCASE, ID_DASHES, ID_RANDOM_BYTES);
            client = this.createApplicationClient(root_context, this.application_id, client_id, this.client_name, options);
            client_map.putAndReturnValue(client_id, client);
            return client;
        } else {
            return null;
        }
    }

    public ClientType getOrCreate(CallContext root_context, String client_id, IdType id_type, Object... options) {
        if (id_type == IdType.Unknown) {
            id_type = checkId(root_context, client_id);
        }
        if (id_type == IdType.PossiblyValid || id_type == IdType.Valid) {
            ClientType client = this.get(root_context, client_id, id_type);
            if (client != null || id_type == IdType.Valid) {
                return client;
            }
        }
        if (id_type == IdType.New || id_type == IdType.PossiblyValid) {
            return this.create(root_context, client_id, id_type, options);
        }
        return null;
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

    protected ClientType createApplicationClient(CallContext root_context, String application_id, String client_id, String client_name, Object... options) {
        return (this.client_factory != null ?
                  (ClientType) this.client_factory.createApplicationClient(root_context, application_id, client_id, client_name, this, options)
                : (ClientType) (new ApplicationClientClass(root_context, application_id, client_id, client_name, this, true)));
    }
}

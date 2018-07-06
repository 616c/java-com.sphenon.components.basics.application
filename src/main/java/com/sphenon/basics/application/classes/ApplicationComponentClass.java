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
import com.sphenon.basics.application.*;

public abstract class ApplicationComponentClass implements ApplicationComponent {
  
    public ApplicationComponentData notifyApplicationBegin (Context modifyable_application_root_context, Application application) {
        return null;
    }

    public void notifyApplicationEnd (Context modifyable_application_root_context, Application application) {
    }

    public ApplicationInstanceComponentData notifyApplicationInstanceBegin (Context modifyable_application_instance_root_context, ApplicationInstance application_instance) {
        return null;
    }

    public void notifyApplicationInstanceEnd (Context modifyable_application_instance_root_context, ApplicationInstance application_instance) {
    }

    public ApplicationSessionComponentData notifyApplicationSessionBegin (Context modifyable_application_session_root_context, ApplicationSession application_session) {
        return null;
    }

    public void notifyApplicationSessionEnd (Context modifyable_application_session_root_context, ApplicationSession application_session) {
    }

    public void notifyApplicationMessageExchangeTriggerBegin (Context modifyable_application_session_root_context, ApplicationSession application_session, String request_type, Object request_data) {
    }

    public ApplicationMessageExchangeComponentData notifyApplicationMessageExchangeBegin (Context modifyable_application_message_exchange_root_context, ApplicationMessageExchange application_message_exchange) {
        return null;
    }

    public void notifyApplicationMessageExchangeEnd (Context modifyable_application_message_exchange_root_context, ApplicationMessageExchange application_message_exchange) {
    }

    public void notifyApplicationMessageExchangeAbort (Context modifyable_application_session_root_context, ApplicationSession application_session) {
    }

    public boolean handleException(CallContext context, Throwable throwable) {
        return false;
    }
}

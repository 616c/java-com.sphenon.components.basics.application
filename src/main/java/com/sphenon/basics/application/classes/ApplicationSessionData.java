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
import com.sphenon.basics.session.*;

import com.sphenon.basics.application.*;
import com.sphenon.basics.application.tplinst.*;
import com.sphenon.basics.application.returncodes.*;

public class ApplicationSessionData implements SessionData {

    public ApplicationSessionData(CallContext context) {
    }

    public static ApplicationSessionData getFromSession(CallContext context, Session session) {
        return (ApplicationSessionData) session.getSessionData(context, TypeManager.get(context, ApplicationSessionData.class));
    }

    public static ApplicationSessionData get(CallContext context) {
        SessionContext sc = SessionContext.get((Context) context);
        Session session = sc.getSession(context);
        ApplicationSessionData asd = getFromSession(context, session);
    
        if (asd == null) {
            asd = new ApplicationSessionData(context);
            session.setSessionData(context, asd);
        }

        return asd;
    }

    public void notifyClientSessionBegin (CallContext context, int client_sessions) {
    }

    public void notifyClientSessionEnd (CallContext context, int client_sessions) {
    }

    protected ApplicationSession application_session;

    public ApplicationSession getApplicationSession (CallContext context) {
        return this.application_session;
    }

    public void setApplicationSession (CallContext context, ApplicationSession application_session) {
        this.application_session = application_session;
    }
}

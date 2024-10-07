// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/WriteSet.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface WriteSet_ApplicationMessageExchangeComponentData_
{
    // adds item, may already exist
    public void     set     (CallContext context, ApplicationMessageExchangeComponentData item);

    // adds item, must not already exist
    public void     add     (CallContext context, ApplicationMessageExchangeComponentData item) throws AlreadyExists;

    // replace item, must already exist
    public void     replace (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;
    // does this method make any sense?
    // should'nt it be? : 
    // public void     replace (CallContext context, ApplicationMessageExchangeComponentData item, ApplicationMessageExchangeComponentData item) throws DoesNotExist;

    // removes item, need not exist
    public void     unset   (CallContext context, ApplicationMessageExchangeComponentData item);

    // removes item, must exist
    public void     remove  (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;
}


// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Set.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface Set_ApplicationMessageExchangeComponentData_
  extends ReadSet_ApplicationMessageExchangeComponentData_,
          WriteSet_ApplicationMessageExchangeComponentData_,
          Navigatable_Iterator_ApplicationMessageExchangeComponentData__,
          OfKnownSize
{
    public boolean contains (CallContext context, ApplicationMessageExchangeComponentData item);

    public void     set     (CallContext context, ApplicationMessageExchangeComponentData item);
    public void     add     (CallContext context, ApplicationMessageExchangeComponentData item) throws AlreadyExists;
    public void     replace (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;
    public void     unset   (CallContext context, ApplicationMessageExchangeComponentData item);
    public void     remove  (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;

    public Iterator_ApplicationMessageExchangeComponentData_ getNavigator (CallContext context);

    public long     getSize (CallContext context);
}


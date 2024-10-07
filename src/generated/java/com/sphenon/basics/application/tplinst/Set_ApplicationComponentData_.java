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

public interface Set_ApplicationComponentData_
  extends ReadSet_ApplicationComponentData_,
          WriteSet_ApplicationComponentData_,
          Navigatable_Iterator_ApplicationComponentData__,
          OfKnownSize
{
    public boolean contains (CallContext context, ApplicationComponentData item);

    public void     set     (CallContext context, ApplicationComponentData item);
    public void     add     (CallContext context, ApplicationComponentData item) throws AlreadyExists;
    public void     replace (CallContext context, ApplicationComponentData item) throws DoesNotExist;
    public void     unset   (CallContext context, ApplicationComponentData item);
    public void     remove  (CallContext context, ApplicationComponentData item) throws DoesNotExist;

    public Iterator_ApplicationComponentData_ getNavigator (CallContext context);

    public long     getSize (CallContext context);
}


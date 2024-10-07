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

public interface Set_ApplicationSessionComponentData_
  extends ReadSet_ApplicationSessionComponentData_,
          WriteSet_ApplicationSessionComponentData_,
          Navigatable_Iterator_ApplicationSessionComponentData__,
          OfKnownSize
{
    public boolean contains (CallContext context, ApplicationSessionComponentData item);

    public void     set     (CallContext context, ApplicationSessionComponentData item);
    public void     add     (CallContext context, ApplicationSessionComponentData item) throws AlreadyExists;
    public void     replace (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist;
    public void     unset   (CallContext context, ApplicationSessionComponentData item);
    public void     remove  (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist;

    public Iterator_ApplicationSessionComponentData_ getNavigator (CallContext context);

    public long     getSize (CallContext context);
}


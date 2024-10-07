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

public interface Set_ApplicationInstanceComponentData_
  extends ReadSet_ApplicationInstanceComponentData_,
          WriteSet_ApplicationInstanceComponentData_,
          Navigatable_Iterator_ApplicationInstanceComponentData__,
          OfKnownSize
{
    public boolean contains (CallContext context, ApplicationInstanceComponentData item);

    public void     set     (CallContext context, ApplicationInstanceComponentData item);
    public void     add     (CallContext context, ApplicationInstanceComponentData item) throws AlreadyExists;
    public void     replace (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist;
    public void     unset   (CallContext context, ApplicationInstanceComponentData item);
    public void     remove  (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist;

    public Iterator_ApplicationInstanceComponentData_ getNavigator (CallContext context);

    public long     getSize (CallContext context);
}


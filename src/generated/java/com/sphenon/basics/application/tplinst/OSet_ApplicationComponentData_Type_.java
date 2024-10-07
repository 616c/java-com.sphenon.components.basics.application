// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface OSet_ApplicationComponentData_Type_
  extends ReadMap_Set_ApplicationComponentData__Type_,
          WriteSet_ApplicationComponentData_,
          Navigatable_Iterator_ApplicationComponentData__,
          OfKnownSize
{
    public Set_ApplicationComponentData_ get        (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationComponentData_ tryGet     (CallContext context, Type index);
    public boolean       canGet     (CallContext context, Type index);

    public Set_ApplicationComponentData_ getMany    (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationComponentData_ tryGetMany (CallContext context, Type index);
    public boolean       canGetMany (CallContext context, Type index);

    public ApplicationComponentData      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne;
    public ApplicationComponentData      tryGetSole (CallContext context, Type index);
    public boolean       canGetSole (CallContext context, Type index);

    public void          set        (CallContext context, ApplicationComponentData item);
    public void          add        (CallContext context, ApplicationComponentData item) throws AlreadyExists;
    public void          replace    (CallContext context, ApplicationComponentData item) throws DoesNotExist;
    public void          unset      (CallContext context, ApplicationComponentData item);
    public void          remove     (CallContext context, ApplicationComponentData item) throws DoesNotExist;

    public Iterator_ApplicationComponentData_ getNavigator (CallContext context);

    public long          getSize (CallContext context);
}


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

public interface OSet_ApplicationSessionComponentData_Type_
  extends ReadMap_Set_ApplicationSessionComponentData__Type_,
          WriteSet_ApplicationSessionComponentData_,
          Navigatable_Iterator_ApplicationSessionComponentData__,
          OfKnownSize
{
    public Set_ApplicationSessionComponentData_ get        (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationSessionComponentData_ tryGet     (CallContext context, Type index);
    public boolean       canGet     (CallContext context, Type index);

    public Set_ApplicationSessionComponentData_ getMany    (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationSessionComponentData_ tryGetMany (CallContext context, Type index);
    public boolean       canGetMany (CallContext context, Type index);

    public ApplicationSessionComponentData      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne;
    public ApplicationSessionComponentData      tryGetSole (CallContext context, Type index);
    public boolean       canGetSole (CallContext context, Type index);

    public void          set        (CallContext context, ApplicationSessionComponentData item);
    public void          add        (CallContext context, ApplicationSessionComponentData item) throws AlreadyExists;
    public void          replace    (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist;
    public void          unset      (CallContext context, ApplicationSessionComponentData item);
    public void          remove     (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist;

    public Iterator_ApplicationSessionComponentData_ getNavigator (CallContext context);

    public long          getSize (CallContext context);
}


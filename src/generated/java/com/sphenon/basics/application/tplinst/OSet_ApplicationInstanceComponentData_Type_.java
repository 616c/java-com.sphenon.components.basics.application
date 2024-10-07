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

public interface OSet_ApplicationInstanceComponentData_Type_
  extends ReadMap_Set_ApplicationInstanceComponentData__Type_,
          WriteSet_ApplicationInstanceComponentData_,
          Navigatable_Iterator_ApplicationInstanceComponentData__,
          OfKnownSize
{
    public Set_ApplicationInstanceComponentData_ get        (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationInstanceComponentData_ tryGet     (CallContext context, Type index);
    public boolean       canGet     (CallContext context, Type index);

    public Set_ApplicationInstanceComponentData_ getMany    (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationInstanceComponentData_ tryGetMany (CallContext context, Type index);
    public boolean       canGetMany (CallContext context, Type index);

    public ApplicationInstanceComponentData      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne;
    public ApplicationInstanceComponentData      tryGetSole (CallContext context, Type index);
    public boolean       canGetSole (CallContext context, Type index);

    public void          set        (CallContext context, ApplicationInstanceComponentData item);
    public void          add        (CallContext context, ApplicationInstanceComponentData item) throws AlreadyExists;
    public void          replace    (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist;
    public void          unset      (CallContext context, ApplicationInstanceComponentData item);
    public void          remove     (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist;

    public Iterator_ApplicationInstanceComponentData_ getNavigator (CallContext context);

    public long          getSize (CallContext context);
}


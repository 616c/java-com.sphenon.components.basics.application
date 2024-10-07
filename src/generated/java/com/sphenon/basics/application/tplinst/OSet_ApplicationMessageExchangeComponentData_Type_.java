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

public interface OSet_ApplicationMessageExchangeComponentData_Type_
  extends ReadMap_Set_ApplicationMessageExchangeComponentData__Type_,
          WriteSet_ApplicationMessageExchangeComponentData_,
          Navigatable_Iterator_ApplicationMessageExchangeComponentData__,
          OfKnownSize
{
    public Set_ApplicationMessageExchangeComponentData_ get        (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationMessageExchangeComponentData_ tryGet     (CallContext context, Type index);
    public boolean       canGet     (CallContext context, Type index);

    public Set_ApplicationMessageExchangeComponentData_ getMany    (CallContext context, Type index) throws DoesNotExist;
    public Set_ApplicationMessageExchangeComponentData_ tryGetMany (CallContext context, Type index);
    public boolean       canGetMany (CallContext context, Type index);

    public ApplicationMessageExchangeComponentData      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne;
    public ApplicationMessageExchangeComponentData      tryGetSole (CallContext context, Type index);
    public boolean       canGetSole (CallContext context, Type index);

    public void          set        (CallContext context, ApplicationMessageExchangeComponentData item);
    public void          add        (CallContext context, ApplicationMessageExchangeComponentData item) throws AlreadyExists;
    public void          replace    (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;
    public void          unset      (CallContext context, ApplicationMessageExchangeComponentData item);
    public void          remove     (CallContext context, ApplicationMessageExchangeComponentData item) throws DoesNotExist;

    public Iterator_ApplicationMessageExchangeComponentData_ getNavigator (CallContext context);

    public long          getSize (CallContext context);
}


// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet.javatpl

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


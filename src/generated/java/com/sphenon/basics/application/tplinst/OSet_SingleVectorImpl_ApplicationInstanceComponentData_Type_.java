// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet_SingleVectorImpl.javatpl

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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Vector;
import java.util.ListIterator;

public class OSet_SingleVectorImpl_ApplicationInstanceComponentData_Type_
  implements OSet_ApplicationInstanceComponentData_Type_ {
    protected java.util.Vector vector;
    protected Type itemtype;


    public OSet_SingleVectorImpl_ApplicationInstanceComponentData_Type_ (CallContext context) {
        this.vector = new java.util.Vector ();
        this.itemtype = TypeManager.get(context, ApplicationInstanceComponentData.class);
    }

    public OSet_SingleVectorImpl_ApplicationInstanceComponentData_Type_ (CallContext context, java.util.Vector vector) {
        this.vector = vector;
        this.itemtype = TypeManager.get(context, ApplicationInstanceComponentData.class);
    }


    public Set_ApplicationInstanceComponentData_ get     (CallContext context, Type index) throws DoesNotExist {
        Set_ApplicationInstanceComponentData_ result = tryGet(context, index);
        if (result.getSize(context) == 0) DoesNotExist.createAndThrow(context);
        return result;
    }

    public Set_ApplicationInstanceComponentData_ getMany    (CallContext context, Type index) throws DoesNotExist {
        return this.get(context, index);
    }

    public ApplicationInstanceComponentData      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne {
        ApplicationInstanceComponentData result = null;
        for (Object o: vector) {
            if (TypeManager.get(context, o.getClass()).isA(context, index)) {
                if (result != null) MoreThanOne.createAndThrow(context);
                result = (ApplicationInstanceComponentData) o;
            }
        }
        if (result == null) DoesNotExist.createAndThrow(context);
        return result;
    }

    public Set_ApplicationInstanceComponentData_ tryGet  (CallContext context, Type index) {
        Set_ApplicationInstanceComponentData_ result = new SetImpl_ApplicationInstanceComponentData_(context);
        for (Object o: vector) {
            if (TypeManager.get(context, o.getClass()).isA(context, index)) {
                result.set(context, (ApplicationInstanceComponentData) o);
            }
        }
        return result;
    }

    public Set_ApplicationInstanceComponentData_ tryGetMany (CallContext context, Type index) {
        return this.tryGet(context, index);
    }

    public ApplicationInstanceComponentData      tryGetSole (CallContext context, Type index) {
        ApplicationInstanceComponentData result = null;
        for (Object o: vector) {
            if (TypeManager.get(context, o.getClass()).isA(context, index)) {
                if (result != null) return null;
                result = (ApplicationInstanceComponentData) o;
            }
        }
        return result;
    }

    public boolean  canGet  (CallContext context, Type index) {
        for (Object o: vector) {
            if (TypeManager.get(context, o.getClass()).isA(context, index)) {
                return true;
            }
        }
        return false;
    }

    public boolean       canGetMany (CallContext context, Type index) {
        return this.canGet(context, index);
   
    }

    public boolean       canGetSole (CallContext context, Type index) {
        ApplicationInstanceComponentData result = null;
        for (Object o: vector) {
            if (TypeManager.get(context, o.getClass()).isA(context, index)) {
                if (result != null) return false;
                result = (ApplicationInstanceComponentData) o;
            }
        }
        return result == null ? false : true;
    }

    public void     set     (CallContext context, ApplicationInstanceComponentData item) {
        vector.add(item);
    }

    public void     add     (CallContext context, ApplicationInstanceComponentData item) throws AlreadyExists {
        vector.add(item);
    }

    public void     replace (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist {
    }

    public void     unset   (CallContext context, ApplicationInstanceComponentData item) {
        ListIterator li = vector.listIterator();
        while (li.hasNext()) {
            if (li.next() == item) {
                li.remove();
                return;
            }
        }
    }

    public void     remove  (CallContext context, ApplicationInstanceComponentData item) throws DoesNotExist {
        ListIterator li = vector.listIterator();
        while (li.hasNext()) {
            if (li.next() == item) {
                li.remove();
                return;
            }
        }
        DoesNotExist.createAndThrow(context);
    }

    public Iterator_ApplicationInstanceComponentData_ getNavigator (CallContext context) {
        return new OSet_SingleVector_IteratorImpl_ApplicationInstanceComponentData_Type_(context, this.vector);
    }

    public long     getSize (CallContext context) {
        return this.vector.size();
    }
}

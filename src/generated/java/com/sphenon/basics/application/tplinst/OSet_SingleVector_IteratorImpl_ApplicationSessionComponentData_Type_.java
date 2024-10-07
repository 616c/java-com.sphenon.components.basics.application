// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet_SingleVector_IteratorImpl.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class OSet_SingleVector_IteratorImpl_ApplicationSessionComponentData_Type_
    implements Iterator_ApplicationSessionComponentData_,
               Cloneable
{
    private java.util.Vector vector;
    private java.util.ListIterator list_iterator;
    private ApplicationSessionComponentData current_item;

    public OSet_SingleVector_IteratorImpl_ApplicationSessionComponentData_Type_ (CallContext context, java.util.Vector vector) {
        this.vector = vector;
        this.list_iterator = vector.listIterator();
        this.next(context);
    }

    public void     next          (CallContext context) {
        if (this.list_iterator.hasNext()) {
            this.current_item = (ApplicationSessionComponentData) this.list_iterator.next();
        } else {
            this.current_item = null;
        }
    }

    public ApplicationSessionComponentData getCurrent    (CallContext context) throws DoesNotExist {
        if (this.current_item == null) {
            DoesNotExist.createAndThrow(context);
        }
        return this.current_item;
    }

    public ApplicationSessionComponentData tryGetCurrent (CallContext context) {
        return this.current_item;
    }

    public boolean  canGetCurrent (CallContext context) {
        return this.current_item == null ? false : true;
    }

    public OSet_SingleVector_IteratorImpl_ApplicationSessionComponentData_Type_ clone(CallContext context) {
        try {
            return (OSet_SingleVector_IteratorImpl_ApplicationSessionComponentData_Type_) super.clone();
        } catch (CloneNotSupportedException cnse) { return null; }
    }
}

// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Set_LinkedListTreeIteratorImpl.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class Set_LinkedListTreeIteratorImpl_ApplicationMessageExchangeComponentData_
    implements Iterator_ApplicationMessageExchangeComponentData_
{
    private java.util.LinkedList linked_list;
    private java.util.ListIterator list_iterator;
    Set_LinkedListTreeIteratorImpl_ApplicationMessageExchangeComponentData_ sub_iterator;
    private ApplicationMessageExchangeComponentData current;

    public Set_LinkedListTreeIteratorImpl_ApplicationMessageExchangeComponentData_ (CallContext context, java.util.LinkedList linked_list) {
        this.linked_list = linked_list;
        this.list_iterator = linked_list.listIterator(0);

        this.next(context);
    }

    public void     next          (CallContext context)
    {
        if (sub_iterator != null) {
            sub_iterator.next(context);
            if (sub_iterator.canGetCurrent(context)) {
                this.current = sub_iterator.tryGetCurrent(context);
                return;
            }
            sub_iterator = null;
        }
        if (this.list_iterator.hasNext()) {
            Object o = this.list_iterator.next();
            if (o == null || o instanceof ApplicationMessageExchangeComponentData) {
                this.current = (ApplicationMessageExchangeComponentData) o;
            } else {
                Set_LinkedListTreeImpl_ApplicationMessageExchangeComponentData_ sllti = ((Set_LinkedListTreeImpl_ApplicationMessageExchangeComponentData_)o);
                sub_iterator = (Set_LinkedListTreeIteratorImpl_ApplicationMessageExchangeComponentData_) (sllti.getNavigator(context));
                if (sub_iterator.canGetCurrent(context)) {
                    this.current = sub_iterator.tryGetCurrent(context);
                } else {
                    sub_iterator = null;
                    next(context);
                }
            }
        } else {
            this.current = null;
        }
    }

    public ApplicationMessageExchangeComponentData getCurrent    (CallContext context) throws DoesNotExist
    {
        if (this.current == null) DoesNotExist.createAndThrow(context);
        return this.current;
    }

    public ApplicationMessageExchangeComponentData tryGetCurrent (CallContext context)
    {
        return this.current;
    }

    public boolean  canGetCurrent (CallContext context)
    {
        return (this.current != null) ? true : false;
    }

    public SetIteratorImpl_ApplicationMessageExchangeComponentData_ clone(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot clone, Set_LinkedListTreeIteratorImpl is not cloneable");
        throw (ExceptionLimitation) null; // compiler insists
    }
}


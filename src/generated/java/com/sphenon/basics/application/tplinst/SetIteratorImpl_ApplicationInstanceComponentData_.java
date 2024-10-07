// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/SetIteratorImpl.javatpl
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

public class SetIteratorImpl_ApplicationInstanceComponentData_
    implements Iterator_ApplicationInstanceComponentData_
{
    private java.util.Hashtable map;
    private java.util.Set entry_set;
    private java.util.Iterator iterator;
    private ApplicationInstanceComponentData current;

    public SetIteratorImpl_ApplicationInstanceComponentData_ (CallContext context, java.util.Hashtable map) {
        this.map = map;
        this.entry_set = map.entrySet();
        this.iterator = entry_set.iterator();
        if (this.iterator.hasNext()) {
            java.util.Map.Entry me = (java.util.Map.Entry) this.iterator.next();
            this.current = (ApplicationInstanceComponentData) me.getValue();
        } else {
            this.current = null;
        }
    }

    public void     next          (CallContext context)
    {
        if (this.iterator.hasNext()) {
            java.util.Map.Entry me = (java.util.Map.Entry) this.iterator.next();
            this.current = (ApplicationInstanceComponentData) me.getValue();
        } else {
            this.current = null;
        }
    }

    public ApplicationInstanceComponentData getCurrent    (CallContext context) throws DoesNotExist
    {
        if (this.current == null) DoesNotExist.createAndThrow(context);
        return this.current;
    }

    public ApplicationInstanceComponentData tryGetCurrent (CallContext context)
    {
        return this.current;
    }

    public boolean  canGetCurrent (CallContext context)
    {
        return (this.current != null) ? true : false;
    }

    public SetIteratorImpl_ApplicationInstanceComponentData_ clone(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot clone, map entry set iterator is not cloneable");
        throw (ExceptionLimitation) null; // compiler insists
    }
}


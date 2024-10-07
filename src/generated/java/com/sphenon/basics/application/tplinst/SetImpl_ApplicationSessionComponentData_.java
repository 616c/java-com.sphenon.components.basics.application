// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/SetImpl.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class SetImpl_ApplicationSessionComponentData_
  implements Set_ApplicationSessionComponentData_
{
    private java.util.Hashtable map;

    public SetImpl_ApplicationSessionComponentData_ (CallContext context)
    {
    }

    protected void initialise(CallContext context) {
        if (map == null) {
            map = new java.util.Hashtable(4);
        }
    }

    public SetImpl_ApplicationSessionComponentData_ (CallContext context, java.util.Hashtable map)
    {
        this.map = map;
    }

    public boolean  contains (CallContext context, ApplicationSessionComponentData item)
    {
        return (map == null ? false : map.containsKey(item));
    }

    public void     set     (CallContext context, ApplicationSessionComponentData item)
    {
        initialise(context);
        map.put(item, item);
    }

    public void     add     (CallContext context, ApplicationSessionComponentData item) throws AlreadyExists
    {
        initialise(context);
        if (map.containsKey(item)) AlreadyExists.createAndThrow (context);
        map.put(item, item);
    }

    public void     replace (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.put(item, item);
    }

    public void     unset   (CallContext context, ApplicationSessionComponentData item)
    {
        initialise(context);
        map.remove(item);
    }

    public void     remove  (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.remove(item);
    }

    public Iterator_ApplicationSessionComponentData_ getNavigator (CallContext context)
    {
        initialise(context);
        return new SetIteratorImpl_ApplicationSessionComponentData_ (context, map);
    }

    public long     getSize (CallContext context)
    {
        return (map == null ? 0L : map.size());
    }
}


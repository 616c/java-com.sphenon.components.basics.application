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

public class SetImpl_ApplicationComponentData_
  implements Set_ApplicationComponentData_
{
    private java.util.Hashtable map;

    public SetImpl_ApplicationComponentData_ (CallContext context)
    {
    }

    protected void initialise(CallContext context) {
        if (map == null) {
            map = new java.util.Hashtable(4);
        }
    }

    public SetImpl_ApplicationComponentData_ (CallContext context, java.util.Hashtable map)
    {
        this.map = map;
    }

    public boolean  contains (CallContext context, ApplicationComponentData item)
    {
        return (map == null ? false : map.containsKey(item));
    }

    public void     set     (CallContext context, ApplicationComponentData item)
    {
        initialise(context);
        map.put(item, item);
    }

    public void     add     (CallContext context, ApplicationComponentData item) throws AlreadyExists
    {
        initialise(context);
        if (map.containsKey(item)) AlreadyExists.createAndThrow (context);
        map.put(item, item);
    }

    public void     replace (CallContext context, ApplicationComponentData item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.put(item, item);
    }

    public void     unset   (CallContext context, ApplicationComponentData item)
    {
        initialise(context);
        map.remove(item);
    }

    public void     remove  (CallContext context, ApplicationComponentData item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.remove(item);
    }

    public Iterator_ApplicationComponentData_ getNavigator (CallContext context)
    {
        initialise(context);
        return new SetIteratorImpl_ApplicationComponentData_ (context, map);
    }

    public long     getSize (CallContext context)
    {
        return (map == null ? 0L : map.size());
    }
}


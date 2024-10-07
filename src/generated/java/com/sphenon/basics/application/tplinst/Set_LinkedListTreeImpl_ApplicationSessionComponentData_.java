// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Set_LinkedListTreeImpl.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class Set_LinkedListTreeImpl_ApplicationSessionComponentData_
  implements Set_ApplicationSessionComponentData_
{
    private java.util.LinkedList linked_list;

    public Set_LinkedListTreeImpl_ApplicationSessionComponentData_ (CallContext context)
    {
        linked_list = new java.util.LinkedList ();
    }

    public Set_LinkedListTreeImpl_ApplicationSessionComponentData_ (CallContext context, java.util.LinkedList linked_list)
    {
        this.linked_list = linked_list;
    }

    public boolean  contains (CallContext context, ApplicationSessionComponentData item)
    {
        return linked_list.contains(item);
    }

    public void     set     (CallContext context, ApplicationSessionComponentData item)
    {
        linked_list.addLast(item);
    }

    public void     set     (CallContext context, Set_LinkedListTreeImpl_ApplicationSessionComponentData_ item_set)
    {
        linked_list.addLast(item_set);
    }

    public void     add     (CallContext context, ApplicationSessionComponentData item) throws AlreadyExists
    {
        if (linked_list.contains(item)) AlreadyExists.createAndThrow (context);
        linked_list.addLast(item);
    }

    public void     add     (CallContext context, Set_LinkedListTreeImpl_ApplicationSessionComponentData_ item_set) throws AlreadyExists
    {
        if (linked_list.contains(item_set)) AlreadyExists.createAndThrow (context);
        linked_list.addLast(item_set);
    }

    public void     replace (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist
    {
        if (!linked_list.contains(item)) DoesNotExist.createAndThrow (context);
        // linked_list.put(item, item);
    }

    public void     unset   (CallContext context, ApplicationSessionComponentData item)
    {
        linked_list.remove(item);
    }

    public void     unset   (CallContext context, Set_LinkedListTreeImpl_ApplicationSessionComponentData_ item_set)
    {
        linked_list.remove(item_set);
    }

    public void     remove  (CallContext context, ApplicationSessionComponentData item) throws DoesNotExist
    {
        if (!linked_list.contains(item)) DoesNotExist.createAndThrow (context);
        linked_list.remove(item);
    }

    public Iterator_ApplicationSessionComponentData_ getNavigator (CallContext context)
    {
        return new Set_LinkedListTreeIteratorImpl_ApplicationSessionComponentData_ (context, linked_list);
    }

    public long     getSize (CallContext context)
    {
        long size = 0;
        for (Object o : linked_list) {
            size += (o instanceof Set_ApplicationSessionComponentData_ ? ((Set_ApplicationSessionComponentData_) o).getSize(context) : 1);
        }
        return size;
    }
}


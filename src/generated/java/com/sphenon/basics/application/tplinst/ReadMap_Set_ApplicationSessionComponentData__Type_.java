// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/ReadMap.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface ReadMap_Set_ApplicationSessionComponentData__Type_
{
    // retrieves item at index; item must exist
    public Set_ApplicationSessionComponentData_ get     (CallContext context, Type index) throws DoesNotExist;

    // retrieves item at index; returns null if item does not exist
    public Set_ApplicationSessionComponentData_ tryGet  (CallContext context, Type index);

    // returns true if item at index exists, otherwise false
    public boolean  canGet  (CallContext context, Type index);
}


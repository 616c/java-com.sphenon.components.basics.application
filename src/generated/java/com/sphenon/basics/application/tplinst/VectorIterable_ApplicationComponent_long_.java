// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/VectorIterable.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public class VectorIterable_ApplicationComponent_long_ implements Iterable<ApplicationComponent>
{
    protected java.util.Iterator<ApplicationComponent> iterator;

    public VectorIterable_ApplicationComponent_long_ (CallContext context, Vector_ApplicationComponent_long_ vector) {
        this.iterator = (vector == null ? (new java.util.Vector<ApplicationComponent>()).iterator() : vector.getIterator_ApplicationComponent_(context));
    }

    public java.util.Iterator<ApplicationComponent> iterator () {
        return this.iterator;
    }
}


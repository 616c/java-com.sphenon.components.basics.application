// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Factory_Vector.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.tplinst.*;

public class Factory_Vector_ApplicationComponent_long_
{
    private String[] names;
    private ApplicationComponent[] values;

    static public Vector_ApplicationComponent_long_ construct (CallContext context) {
        return (new Factory_Vector_ApplicationComponent_long_(context)).create(context);
    }

    public Factory_Vector_ApplicationComponent_long_ (CallContext context) {
    }

    public Factory_Vector_ApplicationComponent_long_ () {
    }

    protected Vector_ApplicationComponent_long_ instance;

    public Vector_ApplicationComponent_long_ precreate (CallContext context) {
        return this.instance = VectorImpl_ApplicationComponent_long_.create(context);
    }

    public Vector_ApplicationComponent_long_ create (CallContext context) {
        Vector_ApplicationComponent_long_ vector = (this.instance != null ? this.instance : VectorImpl_ApplicationComponent_long_.create(context));
        this.instance = null;
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                vector.set(context, i, values[i]); // naja, eigentlich "add" statt "set"
            }
        }
        return vector;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, ApplicationComponent[] values) {
        if (names.length != values.length) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, ManyStringPool.get(context, "0.5.0" /* Number of names differs from number of values */));
        }
        this.names = names;
        this.values = values;
    }
}

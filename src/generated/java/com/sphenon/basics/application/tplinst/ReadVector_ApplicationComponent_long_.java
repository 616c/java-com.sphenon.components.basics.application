// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/ReadVector.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface ReadVector_ApplicationComponent_long_
{
    public ApplicationComponent                                    get             (CallContext context, long index) throws DoesNotExist;
    public ApplicationComponent                                    tryGet          (CallContext context, long index);
    public boolean                                     canGet          (CallContext context, long index);

    public ReferenceToMember_ApplicationComponent_long_ReadOnlyVector_ApplicationComponent_long__  getReference    (CallContext context, long index) throws DoesNotExist;
    public ReferenceToMember_ApplicationComponent_long_ReadOnlyVector_ApplicationComponent_long__  tryGetReference (CallContext context, long index);
}


// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/ReferenceToMember.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.reference.*;
import com.sphenon.basics.many.*;

public interface ReferenceToMember_ApplicationComponent_long_ReadOnlyVector_ApplicationComponent_long__
  extends Reference_ApplicationComponent_
    , ReferenceToMember<ApplicationComponent,ReadOnlyVector<ApplicationComponent>>
{
    public ReadOnlyVector_ApplicationComponent_long_ getContainer(CallContext context);
    public long     getIndex    (CallContext context);
}

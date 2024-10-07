// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/WriteVector.javatpl
// please do not modify this file directly
package com.sphenon.basics.application.tplinst;

import com.sphenon.basics.application.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface WriteVector_ApplicationComponent_long_
{
    public ApplicationComponent set          (CallContext context, long index, ApplicationComponent item);
    public void     add          (CallContext context, long index, ApplicationComponent item) throws AlreadyExists;
    public void     prepend      (CallContext context, ApplicationComponent item);
    public void     append       (CallContext context, ApplicationComponent item);
    public void     insertBefore (CallContext context, long index, ApplicationComponent item) throws DoesNotExist;
    public void     insertBehind (CallContext context, long index, ApplicationComponent item) throws DoesNotExist;
    public ApplicationComponent replace      (CallContext context, long index, ApplicationComponent item) throws DoesNotExist;
    public ApplicationComponent unset        (CallContext context, long index);
    public ApplicationComponent remove       (CallContext context, long index) throws DoesNotExist;
}


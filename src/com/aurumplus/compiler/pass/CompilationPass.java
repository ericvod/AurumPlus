package com.aurumplus.compiler.pass;

import com.aurumplus.compiler.components.IOComponent;

public abstract class CompilationPass<I extends IOComponent<I>, O extends IOComponent<O>> {

    public abstract Class<I> getInputType();

    public abstract Class<O> getOutputType();

    public abstract String getDebugName();

    public abstract O pass(I input);
}

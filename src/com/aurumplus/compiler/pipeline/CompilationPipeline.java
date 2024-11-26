package com.aurumplus.compiler.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.aurumplus.compiler.components.IOComponent;
import com.aurumplus.compiler.exception.UnexpectInputType;
import com.aurumplus.compiler.pass.CompilationPass;

@SuppressWarnings("all")
public class CompilationPipeline {
    private List<CompilationPass<? extends IOComponent, ? extends IOComponent>> passes;

    public CompilationPipeline() {
        passes = new ArrayList<>();
    }

    public CompilationPipeline insertStage(CompilationPass<? extends IOComponent, ? extends IOComponent> pass) {
        passes.add(pass);
        return this;
    }


    public void execute(IOComponent input) {
        IOComponent currentInput = input;

        for (CompilationPass<? extends IOComponent, ? extends IOComponent> pass : passes) {
            checkInputType(pass, currentInput);
            System.out.println("Executing " + pass.getDebugName());
            currentInput = runPass(pass, currentInput);
        }
    }

    private <I extends IOComponent<I>, O extends IOComponent<O>> IOComponent runPass(CompilationPass<I, O> pass,
            IOComponent input) {
        if (!pass.getInputType().isInstance(input)) {
            throw new IllegalArgumentException(
                    "Input type mismatch. Expected: " + pass.getInputType() + ", but got: " + input.getClass());
        }
        return pass.pass((I) input);
    }

    private void checkInputType(CompilationPass<? extends IOComponent, ? extends IOComponent> pass, IOComponent input) {
        if (!pass.getInputType().equals(input.getClass())) {
            throw new UnexpectInputType();
        }
    }
}

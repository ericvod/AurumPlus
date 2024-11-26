package com.aurumplus;

import com.aurumplus.compiler.lexer.Lexer;
import com.aurumplus.compiler.parse.Parser;
import com.aurumplus.compiler.pipeline.CompilationPipeline;
import com.aurumplus.utils.AurumPlusFile;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:/Users/Eric Dourado/Documents/Projects/AurumPlus/AurumPlus/TestFile/example.amp";

        CompilationPipeline pipeline = new CompilationPipeline();
        AurumPlusFile file = new AurumPlusFile(filePath);
        pipeline.insertStage(new Lexer())
                .insertStage(new Parser());

        pipeline.execute(file);
    }
}

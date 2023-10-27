package com.mpcs.config.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.mpcs.scratchpad.config.annotations.AConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ConfigAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, 
      RoundEnvironment roundEnv) {
        System.out.println(annotations.toString());
        try {
            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile("test_gen.class");
            PrintWriter out = new PrintWriter(builderFile.openWriter());
            out.write("AAAA");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}

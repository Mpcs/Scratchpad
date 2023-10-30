package com.mpcs.config.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import javax.lang.model.element.*;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes("com.mpcs.config.annotations.Config")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ConfigAnnotationProcessor extends AbstractProcessor {

    String fileContents1 = """
            package com.mpcs.config;
            
            public class ConfigVars { 
            public static final ConfigElement[] elements = {""";

    String fileContents2 = """
            };
            }
            """;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            return false;
        }
        String arrayContent = "";

        for (TypeElement ann : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(ann)) {
                System.out.println(element.getEnclosingElement().toString() + " |:| " + element.toString());
                element.getModifiers().contains(Modifier.FINAL);
                arrayContent += "new ConfigElement(\"" + element.getEnclosingElement().toString() + "\", \"" + element + "\"),";
            }
        }
        try {
            FileObject file = processingEnv.getFiler().createSourceFile("com.mpcs.config.ConfigVars");
            Writer writer = file.openWriter();
            writer.write(fileContents1 + arrayContent + fileContents2);
            writer.close();
            int[] a = {1, 3};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}

package com.mpcs.scratchpad.config.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import java.io.Writer;
import java.util.Set;
import javax.lang.model.element.*;
import javax.tools.FileObject;

@SupportedAnnotationTypes("com.mpcs.config.annotations.Config")
@SupportedSourceVersion(SourceVersion.RELEASE_20)
public class ConfigAnnotationProcessor extends AbstractProcessor {

    String fileContents1 = """
            package com.mpcs.config;
            
            import java.util.Map;
            
            public class ConfigVars {
            public static final Map<String, ConfigElement> elements = Map.of(""";

    String fileContents2 = """
            );
            }
            """;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        StringBuilder arrayContent = new StringBuilder();

        for (TypeElement ann : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(ann)) {
                //System.out.println(element.getEnclosingElement().toString() + " |:| " + element);
                element.getModifiers().contains(Modifier.FINAL);
                arrayContent.append("\"")
                        .append(element)
                        .append("\", new ConfigElement(\"")
                        .append(element.getEnclosingElement())
                        .append("\", \"")
                        .append(element)
                        .append("\"),");
            }
        }
        arrayContent.deleteCharAt(arrayContent.length()-1);
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

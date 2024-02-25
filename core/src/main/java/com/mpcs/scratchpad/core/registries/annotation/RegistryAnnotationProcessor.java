package com.mpcs.scratchpad.core.registries.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

@SupportedAnnotationTypes("com.mpcs.scratchpad.core.registries.annotation.RegistryAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_21)

public class RegistryAnnotationProcessor extends AbstractProcessor {

    public static final String FILE_EXTENSION = ".registry";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement masterAnnotation : annotations) {
            Set<? extends Element> subAnnotations = roundEnv.getElementsAnnotatedWith(masterAnnotation);
            parseSubAnnotations(roundEnv, (Set<TypeElement>) subAnnotations);

        }
        return true;
    }

    private void parseSubAnnotations(RoundEnvironment roundEnvironment, Set<TypeElement> annotations) {
        Elements elements = processingEnv.getElementUtils();

        for (TypeElement annotation : annotations) {
            Set<String> annotatedClassNames = new HashSet<>();

            for (Element element : roundEnvironment.getElementsAnnotatedWith(annotation)) {
                if (element instanceof TypeElement typeElement) {
                    annotatedClassNames.add(elements.getBinaryName(typeElement).toString());
                }
            }
            writeToResourceFile(annotatedClassNames, annotation.getSimpleName().toString());
        }
    }

    private void writeToResourceFile(Set<String> classNames, String registryName) {
        try {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", registryName + FILE_EXTENSION);
            try (Writer resourceWriter = fileObject.openWriter()) {
                for (String className : classNames) {
                    resourceWriter.write(className);
                    resourceWriter.write("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

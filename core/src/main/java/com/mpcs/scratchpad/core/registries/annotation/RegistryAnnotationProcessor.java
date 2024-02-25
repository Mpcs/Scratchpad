package com.mpcs.scratchpad.core.registries.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes("com.mpcs.scratchpad.core.registries.annotation.Registry")
@SupportedSourceVersion(SourceVersion.RELEASE_21)

public class RegistryAnnotationProcessor extends AbstractProcessor {

    public static final String FILE_EXTENSION = ".registry";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement masterAnnotation : annotations) {
            Set<? extends Element> registeredElements = roundEnv.getElementsAnnotatedWith(masterAnnotation);
            Map<String, Set<String>> typeToElementsMap = splitElementsByType((Set<TypeElement>) registeredElements, masterAnnotation);

            for (Map.Entry<String, Set<String>> entry : typeToElementsMap.entrySet()) {
                writeToResourceFile(entry.getKey(), entry.getValue());
            }
        }
        return true;
    }

    private Map<String, Set<String>> splitElementsByType(Set<TypeElement> registryElements, TypeElement annotation) {
        Elements elements = processingEnv.getElementUtils();
        Types types = processingEnv.getTypeUtils();

        Map<String, Set<String>> map = new HashMap<>();
        for (TypeElement element : registryElements) {
            String registryName = types.asElement(getTypeValue(element)).getSimpleName().toString();
            String className = elements.getBinaryName(element).toString();
            if (!map.containsKey(registryName)) {
                map.put(registryName, new HashSet<>());
            }
            map.get(registryName).add(className);
        }

        return map;

    }

    private static AnnotationMirror getAnnotationMirror(TypeElement typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for(AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if(m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ) {
            if(entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    public TypeMirror getTypeValue(TypeElement typeElement) {
        AnnotationMirror am = getAnnotationMirror(typeElement, Registry.class);
        if(am == null) {
            return null;
        }
        AnnotationValue av = getAnnotationValue(am, "value");
        if(av == null) {
            return null;
        } else {
            return (TypeMirror)av.getValue();
        }
    }
    private void writeToResourceFile(String registryName, Set<String> classNames) {
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

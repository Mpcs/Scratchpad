package com.mpcs.scratchpad.core.scene.nodes.annotation;

import com.mpcs.scratchpad.core.registries.annotation.RegistryAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RegistryAnnotation
public @interface RegisterNode {
}

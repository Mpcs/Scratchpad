package com.mpcs.scratchpad.core.registries.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Registry {
    Class<?> value();
}

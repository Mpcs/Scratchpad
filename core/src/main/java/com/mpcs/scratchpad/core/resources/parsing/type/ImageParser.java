package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.resources.Image;
import com.mpcs.scratchpad.core.resources.ResourceManager;

import java.io.IOException;

@Registry(TypeParser.class)
public class ImageParser implements TypeParser<Image>{
    @Override
    public Image parse(String string) throws TypeParseException {
        try {
            return Context.get(ResourceManager.class).getResourceImage(string);
        } catch (IOException e) {
            throw new TypeParseException("Image file not found: " + string);
        }
    }

    @Override
    public boolean matchesType(Class<?> clazz) {
        return clazz.isAssignableFrom(Image.class);
    }
}

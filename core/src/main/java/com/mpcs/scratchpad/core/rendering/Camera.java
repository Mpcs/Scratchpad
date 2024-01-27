package com.mpcs.scratchpad.core.rendering;

import org.joml.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f(0.0f, 0.0f, 3.0f);
    public Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
}

package com.mpcs.math;

public record Vector3(float x, float y, float z) {

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3 add(Vector3 value) {
        return new Vector3(this.x() + value.x(), this.y() + value.y(), this.z() + value.z());
    }

    public Vector3 subtract(Vector3 value) {
        return new Vector3(this.x() - value.x(), this.y() - value.y(), this.z() - value.z());
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }
}

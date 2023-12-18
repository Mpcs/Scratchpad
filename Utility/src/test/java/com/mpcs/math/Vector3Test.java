package com.mpcs.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector3Test {

    @Test
    void defaultConstructorShouldCreateZeroVector() {
        Vector3 vector3 = new Vector3();
        assertEquals(0, vector3.x());
        assertEquals(0, vector3.y());
        assertEquals(0, vector3.z());
    }

    @Test
    void vectorAdding() {
        Vector3 firstVec = new Vector3(5, 5, 5);
        Vector3 secondVec = new Vector3(-5 , 3, 4);
        Vector3 resultVector = firstVec.add(secondVec);
        assertEquals(0, resultVector.x());
        assertEquals(8, resultVector.y());
        assertEquals(9, resultVector.z());
    }

    @Test
    void vectorSubtraction() {
        Vector3 firstVec = new Vector3(5, 5, 5);
        Vector3 secondVec = new Vector3(-5 , 3, 4);
        Vector3 resultVector = firstVec.subtract(secondVec);
        assertEquals(10, resultVector.x());
        assertEquals(2, resultVector.y());
        assertEquals(1, resultVector.z());
    }

    @Test
    void vectorToArray() {
         Vector3 vector = new Vector3(5, 8, -3);
         float[] array = vector.toArray();
         assertArrayEquals(new float[]{5, 8, -3}, array);
    }

}
package com.mpcs.scratchpad.core.resources.obj;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ObjFace {

    private final List<Vector3i> facePoints = new ArrayList<>(); // x - position, y - texture, z - normal
    private final int fileLineNumber;
    public ObjFace(int lineNumber) {
        this.fileLineNumber = lineNumber;
    }

    public void putPoint(int positionIndex, int textureIndex, int normalIndex) {
        facePoints.add(new Vector3i(positionIndex, textureIndex, normalIndex));
    }

    //TODO: Make dataformat influence the function
    public List<Integer> getTriangulatedIndicesFormatted(VertexDataFormat dataFormat) {
        List<Integer> list = triangulate(facePoints);

        if (list.size()/6 != facePoints.size()-2) {
            //Logger.warn("Wrong amount of triangles for face " + fileLineNumber + " Could be a triangulation error?");
        }

        return list;
    }

    private List<Integer> triangulate(List<Vector3i> points) {
        List<Integer> list = new ArrayList<>();
        List<Vector3i> facePointsCopy = new ArrayList<>(List.copyOf(points));

        for(int i = 0; i < facePointsCopy.size()-2; i++) {  // take 3 points from index i inclusive. Save a triangle from them, and remove the center point
            list.add(facePointsCopy.get(i).x);              // from the point list, since it has connected to all its neighbours. Then move one index to the right.
            list.add(facePointsCopy.get(i).y);
            list.add(facePointsCopy.get(i+1).x);
            list.add(facePointsCopy.get(i+1).y);
            list.add(facePointsCopy.get(i+2).x);
            list.add(facePointsCopy.get(i+2).y);

            facePointsCopy.remove(i+1);
        }
        if (facePointsCopy.size() >= 3) {                   // if we have atleast 3 points left, triangulate again recursively,
            list.addAll(triangulate(facePointsCopy));       // if we have less than 3, triangulation is finished
        }

        return list;
    }
}

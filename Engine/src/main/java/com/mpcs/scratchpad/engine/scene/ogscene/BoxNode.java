package com.mpcs.scratchpad.engine.scene.ogscene;

import com.mpcs.scratchpad.engine.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.engine.rendering.Mesh3D;

public class BoxNode extends Model3DNode {


	public BoxNode(Mesh3D mesh) {
        super(mesh);
    }

	@Override
	public void update(float deltaTime) {
		this.rotation += 1;
	}
}
package com.mpcs.scratchpad.core.scene.ogscene;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.Image;
import com.mpcs.scratchpad.core.scene.nodes.Model3DNode;

@Deprecated
public class BoxNode extends Model3DNode {

	public BoxNode(Mesh3D mesh, Image texture) {
        super(mesh, texture);
    }

	@Override
	public void update(float deltaTime) {
		InputManager inputManager = Context.get().getInputManager();
		if(inputManager.isKeyPressed(KeyEvent.VK_RIGHT))
			this.rotation += 1;
		if(inputManager.isKeyPressed(KeyEvent.VK_LEFT))
			this.rotation -= 1;
		if(inputManager.isKeyPressed(KeyEvent.VK_P))
			this.scale.add(0.1f, 0.1f, 0.1f);
		if(inputManager.isKeyPressed(KeyEvent.VK_L))
			this.scale.sub(0.1f, 0.1f, 0.1f);
	}
}
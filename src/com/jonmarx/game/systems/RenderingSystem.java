package com.jonmarx.game.systems;

import java.util.List;

import com.jonmarx.core.Renderer;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;

public class RenderingSystem extends ECSSystem {
	
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};

	public RenderingSystem() {
		super("renderingSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		// none
	}
	
	@Override
	protected void updateO() {
		List<ECSEntity> entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		for(ECSEntity entity : e) {
			if(!entity.containsComponent("modelComponent")) {
				entities.remove(entity);
			}
		}
		Renderer.renderFromList(entities);
	}
	
	@Override
	protected void init() {
		
	}
}

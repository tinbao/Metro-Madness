package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class CargoTrain extends Train {
	
	// size of the train
	Size size;
	public final static String TYPE = "Cargo";

	public CargoTrain(Line trainLine, Station start, boolean forward, 
			String name, Size size) {
		super(trainLine, start, forward, name);
		this.size = size;
	}
	
	@Override
	public void embark(Passenger p, String stationType) throws Exception {
		if (stationType.equals("Cargo")) {
			if (this.passengers.size() + 1 <= size.capacity && 
					size.luggage + p.getCargo().getWeight() > size.luggage) {
				passengers.add(p);
			} else {
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
	}
	
	@Override
	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = isForward() ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size()/size.circleSpec;
			renderer.setColor(col.cpy().lerp(size.color, percentage));
			
			// Draws cargo trains as squares instead of circles to differentiate
			renderer.rect(getPos().x-TRAIN_WIDTH*(2+percentage)/2, 
					getPos().y-TRAIN_WIDTH*(2+percentage)/2, 
					TRAIN_WIDTH*(2+percentage), TRAIN_WIDTH*(2+percentage));
		}
	}

	@Override
	public String getType(){
		return TYPE;
	}
}

package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerTrain extends Train {
	
	// size of the train
	Size size;
	public final static String TYPE = "Passenger";

	public PassengerTrain(Line trainLine, Station start, boolean forward, String name, Size size) {
		super(trainLine, start, forward, name);
		this.size = size;
	}
	
	@Override
	public void embark(Passenger p, String stationType) throws Exception {
		if (stationType.equals("Active")) {
			if (p.getCargo().getWeight() == 0 && this.passengers.size() + 1 <= size.capacity) {
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
			renderer.circle(getPos().x, getPos().y, TRAIN_WIDTH*(1+percentage));
		}
	}

}

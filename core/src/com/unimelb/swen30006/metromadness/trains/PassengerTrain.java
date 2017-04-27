package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.stations.Station.StationType;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerTrain extends Train {
	
	public final static TrainType TYPE = TrainType.PASSENGER;
	
	public PassengerTrain(Line trainLine, Station start, boolean forward, String name, TrainSize size) {
		super(trainLine, start, forward, name, TYPE, size);
	}
	
	@Override
	public void embark(Passenger p, StationType stationType) throws Exception {
		if (stationType == StationType.ACTIVE) { 
			if(p.cargo.weight == 0 
					&& this.passengers.size() + 1 <= size.capacity) {
				passengers.add(p);
			}
			else { return; }
		}
		else { throw new Exception(); }
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

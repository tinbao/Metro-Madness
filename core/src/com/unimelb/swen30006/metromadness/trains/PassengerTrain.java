package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerTrain extends Train {
	
	// size of the train
	Size size;
	
	public enum Size {
		BIG (80, Color.LIGHT_GRAY, 20f),
		SMALL (10, Color.MAROON, 10f);
		
		private final int capacity;	// how many passengers it can hold
		private final Color color;	// color rendered
		private final float circleSpec;	// dividing constant to determine color interpolation and size
		
		Size(int capacity, Color color, float circleSpec) {
			this.capacity = capacity;
			this.color = color;
			this.circleSpec = circleSpec;
		}
	}

	public PassengerTrain(Line trainLine, Station start, boolean forward, String name, Size size) {
		super(trainLine, start, forward, name);
		this.size = size;
	}
	
	@Override
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > size.capacity){
			throw new Exception();
		}
		this.passengers.add(p);
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

package com.unimelb.swen30006.metromadness.trains;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.stations.Station.StationType;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class CargoTrain extends Train {
	
	public final static TrainType TYPE = TrainType.CARGO;
	public int totalWeight; // weight of all the cargo on the train

	public CargoTrain(Line trainLine, Station start, boolean forward, String name, TrainSize size) {
		super(trainLine, start, forward, name, TYPE, size);
		totalWeight = 0;
	}
	
	@Override
	public void embark(Passenger p, StationType stationType) throws Exception {
		if (stationType == StationType.CARGO) {
			if (this.passengers.size() + 1 <= size.capacity 
					&& p.cargo.weight + totalWeight <= size.luggage) {
				passengers.add(p);
				totalWeight += p.cargo.weight;
			} else { return; }
		} else { throw new Exception(); }
	}
	
	@Override
	public ArrayList<Passenger> disembark(){
		ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
		Iterator<Passenger> iterator = this.passengers.iterator();
		while(iterator.hasNext()){
			Passenger p = iterator.next();
			if(this.station.shouldLeave(p)){
				logger.info("Passenger "+p.id+" is disembarking at "+this.station.name);
				disembarking.add(p);
				totalWeight -= p.cargo.weight;
				iterator.remove();
			}
		}
		return disembarking;
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
}

package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.stations.Station.StationType;

public class Passenger {
	
	public final static int NO_CARGO = 0;

	final public int id;
	public Station beginning;
	public Station destination;
	public float travelTime;
	public boolean reachedDestination;
	public Cargo cargo;
	
	public Passenger(int id, Random random, Station start, Station end){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.cargo = generateCargo(random);
	}
	
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	public Cargo getCargo(){
		return cargo;
	}
	public Cargo generateCargo(Random random){
		// Assumption that passengers at Active stations have no cargo
		if(this.beginning.type == StationType.ACTIVE){
			return new Cargo(NO_CARGO);
		}
		return new Cargo(random.nextInt(51));
	}
	
	public class Cargo{
		public int weight;
		
		public Cargo(int weight){
			this.setWeight(weight);
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}

	
	
}

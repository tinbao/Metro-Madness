package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {
	
	// Random number generator
	static final private Random random = new Random(30006);
	
	// Passenger id generator
	static private int idGen = 1;
	
	
	// The station that passengers are getting on
	public Station s;
	// The line they are travelling on
	public ArrayList<Line> lines;
	
	// The max volume
	public float maxVolume;
	
	public PassengerGenerator(Station s, ArrayList<Line> lines, float max){
		this.s = s;
		this.lines = lines;
		this.maxVolume = max;
	}
	
	public Passenger[] generatePassengers(){
		int count = random.nextInt(4)+1;
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}
	
	public Passenger generatePassenger(Random random){
		// Pick a random station from the line
		Line l = this.lines.get(random.nextInt(this.lines.size()));
		int currentStation = l.stations.indexOf(this.s);
		boolean forward = random.nextBoolean();
		
		// If we are the end of the line then set our direction forward or backward
		if(currentStation == 0){
			forward = true;
		} else if (currentStation == l.stations.size()-1){
			forward = false;
		}
		
		// Find the station
		int index = 0;
		boolean found = true;
		
		String stationType = l.stations.get(currentStation).type;
		
		if (forward){
			index = random.nextInt(l.stations.size()-1-currentStation) + currentStation + 1;
		} else {
			index = currentStation - 1 - random.nextInt(currentStation);
		}
		
		Station s = l.stations.get(index);
		
		return this.s.generatePassenger(idGen++, random, s);
	}
	
}
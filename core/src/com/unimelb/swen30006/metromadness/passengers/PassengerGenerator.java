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
		
		// Find the station
		int index = 0;
		
		String stationType = l.stations.get(currentStation).type;
		String stationName = l.stations.get(currentStation).name;
		
		ArrayList<Station> cargoStations = new ArrayList<Station>();
		ArrayList<Station> activeStations = new ArrayList<Station>();
		
		for(Station station : l.stations){
			if(station.type.equals("Cargo") && !station.name.equals(stationName)){
				cargoStations.add(station);
			} else if (station.type.equals("Active") && !station.name.equals(stationName)) {
				activeStations.add(station);
			}
		}
		
		Station s = null;
		if(stationType.equals("Cargo")){
			index = random.nextInt(cargoStations.size());
			s = cargoStations.get(index);
		} else if (stationType.equals("Active")) {
			index = random.nextInt(activeStations.size());
			s = activeStations.get(index);
		}
		
		return this.s.generatePassenger(idGen++, random, s);
	}
	
}
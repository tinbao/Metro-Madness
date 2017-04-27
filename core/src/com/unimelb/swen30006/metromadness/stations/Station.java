package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Station {
	
	public enum StationType {
		ACTIVE, CARGO, OTHER
	}
	
	public static final int PLATFORMS=2;
	
	// Logger
	private static Logger logger = LogManager.getLogger();
	
	public String name;
	public StationType type;
	public Point2D.Float position;
	
	protected PassengerGenerator g;
	protected PassengerRouter router;
	protected float maxVolume;
	
	protected static final float RADIUS=6;
	protected static final int NUM_CIRCLE_STATMENTS=100;
	protected static final int MAX_LINES=3;
	protected static final float DEPARTURE_TIME = 2;
	
	protected ArrayList<Line> lines;
	protected ArrayList<Train> trains;
	protected ArrayList<Passenger> waiting;
	
	public Station(float x, float y, PassengerRouter router, String name, StationType type, float maxPax){
		this.name = name;
		this.type = type;
		this.router = router;
		this.position = new Point2D.Float(x,y);
		this.lines = new ArrayList<Line>();
		this.trains = new ArrayList<Train>();
		this.waiting = new ArrayList<Passenger>();
		this.g = new PassengerGenerator(this, this.lines, maxPax);
		this.maxVolume = maxPax;
	}
	
	public void registerLine(Line l){
		this.lines.add(l);
	}
	
	public void render(ShapeRenderer renderer){
		float radius = RADIUS;
		for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
			Line l = this.lines.get(i);
			renderer.setColor(l.lineColour);
			renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
			radius -= 1;
		}
		
		// Calculate the percentage
		float t = this.trains.size()/(float)PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);	
	}
	
	public void enter(Train t) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			// Add the train
			this.trains.add(t);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while(pIter.hasNext()){
				Passenger p = pIter.next();
				try {
					logger.info("Passenger "+p.id+" carrying "+p.cargo.weight +" kg cargo embarking at "
							+this.name+" heading to "+p.destination.name);
					t.embark(p, this.type);
					pIter.remove();
				} catch (Exception e){
					// Do nothing, already waiting
					break;
				}
			}
			
			//Do not add new passengers if there are too many already
			if (this.waiting.size() > maxVolume){
				return;
			}
			// Add the new passenger
			Passenger[] ps = this.g.generatePassengers();
			for(Passenger p: ps){
				try {
					logger.info("Passenger "+p.id+" carrying "+p.cargo.weight +" kg embarking at "
							+this.name+" heading to "+p.destination.name);
					t.embark(p, this.type);
				} catch(Exception e){
					this.waiting.add(p);
				}
			}
		}
	}
	
	
	public void depart(Train t) throws Exception {
		if(this.trains.contains(t)){
			this.trains.remove(t);
		} else {
			throw new Exception();
		}
	}
	
	public boolean canEnter(Line l) throws Exception {
		return trains.size() < PLATFORMS;
	}

	// Returns departure time in seconds
	public float getDepartureTime() {
		return DEPARTURE_TIME;
	}

	public boolean shouldLeave(Passenger p) {
		return this.router.shouldLeave(this, p);
	}

	@Override
	public String toString() {
		return "Station [position=" + position + ", name=" + name + ", trains=" + trains.size()
				+ ", router=" + router + "]";
	}

	public Passenger generatePassenger(int id, Random random, Station s) {
		return new Passenger(id, random, this, s);
	}
}
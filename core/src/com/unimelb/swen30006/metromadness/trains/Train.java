package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

public class Train {
	
	// Logger
	private static Logger logger = LogManager.getLogger();

	// Constants
	public static final int MAX_TRIPS=4;
	public static final Color FORWARD_COLOUR = Color.ORANGE;
	public static final Color BACKWARD_COLOUR = Color.VIOLET;
	public static final float TRAIN_WIDTH=4;
	public static final float TRAIN_LENGTH = 6;
	public static final float TRAIN_SPEED=50f;
	
	// The train's name
	public String name;

	// The line that this is traveling on
	private Line trainLine;

	// Passenger Information
	public ArrayList<Passenger> passengers;
	private float departureTimer;

	// Station and track and position information
	private Station station;
	private Track track;

	// The x and y coordinates of the train
	private Point2D.Float pos;

	// Direction and direction
	private boolean forward;
	private TrainState state;

	// State variables
	public int numTrips;
	private boolean disembarked;
	
	private TrainState previousState = null;

	
	public Train(Line trainLine, Station start, boolean forward, String name){
		this.trainLine = trainLine;
		this.station = start;
		this.state = TrainState.FROM_DEPOT;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
		this.name = name;
	}

	public void update(float delta){
		// Update all passengers
		for(Passenger p: this.passengers){
			p.update(delta);
		}
		
		// Update the state
		try {
			setState(state.event(this, delta));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			move(delta);
		}

	}

	public void move(float delta){
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x,this.pos.y,this.station.position.x,this.station.position.y);
		float newX = this.pos.x + (float)( Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y + (float)( Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}

	public void embark(Passenger p) throws Exception {
		throw new Exception();
	}


	public ArrayList<Passenger> disembark(){
		ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
		Iterator<Passenger> iterator = this.passengers.iterator();
		while(iterator.hasNext()){
			Passenger p = iterator.next();
			if(this.station.shouldLeave(p)){
				logger.info("Passenger "+p.id+" is disembarking at "+this.station.name);
				disembarking.add(p);
				iterator.remove();
			}
		}
		return disembarking;
	}

	@Override
	public String toString() {
		return "Train [line=" + this.trainLine.name +", departureTimer=" + departureTimer + ", pos=" + pos + ", forward=" + forward + ", state=" + state
				+ ", numTrips=" + numTrips + ", disembarked=" + disembarked + "]";
	}

	public boolean inStation(){
		return (this.state == TrainState.IN_STATION || this.state == TrainState.READY_DEPART);
	}
	
	public float angleAlongLine(float x1, float y1, float x2, float y2){	
		return (float) Math.atan2((y2-y1),(x2-x1));
	}

	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			renderer.setColor(col);
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH);
		}
	}
	
	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}
	
	public Line getTrainLine() {
		return trainLine;
	}
	
	public void setTrainLine(Line trainLine) {
		this.trainLine = trainLine;
	}
	
	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}
	
	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}
	
	public boolean isDisembarked() {
		return disembarked;
	}

	public void setDisembarked(boolean disembarked) {
		this.disembarked = disembarked;
	}
	
	public Point2D.Float getPos() {
		return pos;
	}

	public void setPos(Point2D.Float pos) {
		this.pos = pos;
	}
	
	public float getDepartureTimer() {
		return departureTimer;
	}

	public void setDepartureTimer(float departureTimer) {
		this.departureTimer = departureTimer;
	}

	public TrainState getPreviousState() {
		return previousState;
	}

	public void setPreviousState(TrainState previousState) {
		this.previousState = previousState;
	}
	
	public TrainState getState() {
		return state;
	}

	public void setState(TrainState state) {
		this.state = state;
	}
}

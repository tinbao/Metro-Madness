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
import com.unimelb.swen30006.metromadness.stations.Station.StationType;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

public class Train {
	
	public enum TrainType {
		PASSENGER, CARGO
	}
	
	public Train(Line trainLine, Station start, boolean forward, String name, TrainType type, TrainSize size) {
		this.trainLine = trainLine;
		this.station = start;
		this.state = TrainState.FROM_DEPOT;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
		this.name = name;
		this.size = size;
	}
	
	// type and size
	public TrainType type;
	public TrainSize size;
	
	// Logger
	protected static Logger logger = LogManager.getLogger();

	// Constants
	public static final int MAX_TRIPS=4;
	public static final Color FORWARD_COLOUR = Color.ORANGE;
	public static final Color BACKWARD_COLOUR = Color.VIOLET;
	public static final float TRAIN_WIDTH=4;
	public static final float TRAIN_LENGTH = 6;
	public static final float TRAIN_SPEED=50f;
	
	
	public String name;
	Line trainLine;
	
	protected ArrayList<Passenger> passengers;
	protected float departureTimer;
	protected Station station;
	protected Track track;
	protected boolean forward;
	protected TrainState state;
	protected int numTrips;
	protected boolean disembarked;
	protected TrainState previousState = null;
	
	// The x and y coordinates of the train
	protected Point2D.Float pos;
	
	
	public void update(float delta){
		// Update all passengers
		for(Passenger p: this.passengers){
			p.update(delta);
		}

		// Update the trainState
		try {
			setState(state.event(this, delta));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			move(delta);
		}

	}
	
	public void embark(Passenger p, StationType type) throws Exception {
		throw new Exception();
	}

	public void move(float delta){
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x,this.pos.y,this.station.position.x,this.station.position.y);
		float newX = this.pos.x + (float)( Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y + (float)( Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}


	public ArrayList<Passenger> disembark() {
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
		return "Train [line=" + this.trainLine.name +", departureTimer=" + departureTimer +
				", pos=" + pos + ", forward=" + forward + ", state=" + state
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

	public void setTrack(Track track) {
		this.track = track;
	}

	public void setTrainLine(Line trainLine) {
		this.trainLine = trainLine;
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
	
	public void setDepartureTimer(float departureTimer) {
		this.departureTimer = departureTimer;
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

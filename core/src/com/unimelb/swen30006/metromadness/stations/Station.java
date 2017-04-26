package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Station {
	
	public static final int PLATFORMS=2;
	
	public Point2D.Float position;
	public static final float RADIUS=6;
	public static final int NUM_CIRCLE_STATMENTS=100;
	public static final int MAX_LINES=3;
	public String name;
	public String type;
	public ArrayList<Line> lines;
	public ArrayList<Train> trains;
	public static final float DEPARTURE_TIME = 2;
	public PassengerRouter router;

	public Station(float x, float y, PassengerRouter router, String name, String type){
		this.name = name;
		this.type = type;
		this.router = router;
		this.position = new Point2D.Float(x,y);
		this.lines = new ArrayList<Line>();
		this.trains = new ArrayList<Train>();
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
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
		BitmapFont font = new BitmapFont();
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
	    font.setColor(Color.BLACK);
	    font.draw(batch, this.name, (float)position.getX(), (float)position.getY()+10);
	    font.draw(batch, this.type, (float)position.getX(), (float)position.getY()+5);
	    batch.end();
	}
	
	public void enter(Train t) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			this.trains.add(t);
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
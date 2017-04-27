package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D.Float;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public class DualTrack extends Track {
	
	private static final float TRACK_COLOR = 245f/255f;
	private static final float TRACK_COLOR_ALPHA = 0.5f;
	private static final float RECT_FACTOR = 3;

	public boolean forwardOccupied;
	public boolean backwardOccupied;
	
	public DualTrack(Float start, Float end, Color col) {
		super(start, end, col);
		this.forwardOccupied = false;
		this.backwardOccupied = false;
	}
	
	public void render(ShapeRenderer renderer){
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
		renderer.setColor(new Color(TRACK_COLOR,TRACK_COLOR,TRACK_COLOR,TRACK_COLOR_ALPHA).
				lerp(this.trackColour, TRACK_COLOR_ALPHA));
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH/RECT_FACTOR);
		renderer.setColor(this.trackColour);
	}
	
	@Override
	public void enter(Train t){
		if(t.isForward()){
			this.forwardOccupied = true;
		} else {
			this.backwardOccupied = true;
		}
	}

	@Override
	public boolean canEnter(boolean forward) {
		if(forward){
			return !this.forwardOccupied;
		} else {
			return !this.backwardOccupied;
		}
	}

	@Override
	public void leave(Train t) {
		if(t.isForward()){
			this.forwardOccupied = false;
		} else {
			this.backwardOccupied = false;
		}
	}

}

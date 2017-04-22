package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

//The state that a train can be in 
enum TrainState {
	IN_STATION /*{
		public TrainState trainMove(Train t) throws Exception{
			boolean endOfLine = t.getTrainLine().endOfLine(t.getStation());
			if(endOfLine){
				t.setForward(false);
			}
			t.setTrack(t.getTrainLine().nextTrack(t.getStation(), t.isForward()));
			return READY_DEPART;
		
		}
	}*/, READY_DEPART, ON_ROUTE, WAITING_ENTRY, 
		
	FROM_DEPOT {
			
		public TrainState entering(Train t) throws Exception{
			// We have our station initialized we just need to retrieve the next track, enter the
			// current station officially and mark as in station
			if(t.getStation().canEnter(t.getTrainLine())){
				t.getStation().enter(t);
				t.setPos((Point2D.Float) t.getStation().position.clone());
				t.setDisembarked(false);
			}
			return FROM_DEPOT;
		}
	};

	
	public TrainState entering(Train t) throws Exception{
		if(t.getStation().canEnter(t.getTrainLine())){
			t.getStation().enter(t);
			t.setPos((Point2D.Float) t.getStation().position.clone());
			t.setDisembarked(false);
		}
		return FROM_DEPOT;
	}
	/*
	public TrainState trainMove(Train t) throws Exception{
		boolean endOfLine = t.getTrainLine().endOfLine(t.getStation());
		if(endOfLine){
			t.setForward(false);
		}
		t.setTrack(t.getTrainLine().nextTrack(t.getStation(), t.isForward()));
		return READY_DEPART;
	}
	*/
}
	
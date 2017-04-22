package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

//The state that a train can be in 
enum TrainState {
	
	IN_STATION {
		
		public TrainState entering(Train t) throws Exception{
			return null;
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
		
	}, 
	
	READY_DEPART {
		public TrainState entering(Train t) throws Exception {
			return null;
		}
		
	}, 
	
	ON_ROUTE {
		public TrainState entering(Train t) throws Exception {
			// Checkout if we have reached the new station
			if(t.getPos().distance(t.getStation().position) < 10 ){
				return WAITING_ENTRY;
			} else {
				return ON_ROUTE;
			}
		}
		
	}, 
	
	WAITING_ENTRY {
		
		public TrainState entering(Train t) throws Exception {
			if(t.getStation().canEnter(t.getTrainLine())){
				t.getTrack().leave(t);
				t.setPos((Point2D.Float) t.getStation().position.clone());
				t.getStation().enter(t);
				t.setDisembarked(false);
				return IN_STATION;
			}
			return WAITING_ENTRY;
		}
		
	}, 
	
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

	abstract TrainState entering(Train t) throws Exception;
	
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
	
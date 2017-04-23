package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//The state that a train can be in 
enum TrainState {
	
	/* The state of train that is currently docked at a station */
	IN_STATION {
		
		public TrainState entering(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t.getPreviousState(), t.getState(), t)){
				logger.info(t.name+" is in "+ t.getStation().name+" Station.");
			}
			
			// When in station we want to disembark passengers 
			// and wait 10 seconds for incoming passengers
			if(!t.isDisembarked()){
				t.disembark();
				t.setDepartureTimer(t.getStation().getDepartureTime());
				t.setDisembarked(true);
			} else {
				
				// Count down if departure timer. 
				if(t.getDepartureTimer() > 0){
					t.setDepartureTimer(t.getDepartureTimer() - delta);
						
				} else {
					// We are ready to depart, find the next track and wait until we can enter 
					boolean endOfLine = t.getTrainLine().endOfLine(t.getStation());
					if(endOfLine){
						t.setForward(!t.isForward());
					}
					t.setTrack(t.getTrainLine().nextTrack(t.getStation(), t.isForward()));
					return READY_DEPART;
				}
			}
			return IN_STATION;
		}	
	}, 
	
	/* The state of train that is ready to depart the station */
	READY_DEPART {
		public TrainState entering(Train t, float delta) throws Exception{
			
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t.getPreviousState(), t.getState(), t)){
				logger.info(t.name+ " is ready to depart for "+t.getStation().name+" Station!");
			}
			
			if(t.getTrack().canEnter(t.isForward())){
				// Find the next
				t.getTrainLine().nextStation(t.getStation(), t.isForward());
				// Depart our current station
				t.getStation().depart(t);
				t.setStation(t.getTrainLine().nextStation(t.getStation(), t.isForward()));
				t.getTrack().enter(t);
				return ON_ROUTE;
			}
			return READY_DEPART;
		}
		
	}, 
	
	/* The state of train that is in the process of going to a station */
	ON_ROUTE {
		public TrainState entering(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t.getPreviousState(), t.getState(), t)){
				logger.info(t.name+ " enroute to "+t.getStation().name+" Station!");
			}
			// Checkout if we have reached the new station
			if(t.getPos().distance(t.getStation().position) < 10 ){
				return WAITING_ENTRY;
			} else {
				return ON_ROUTE;
			}
		}
		
	}, 
	
	/* The state of train that is just arrived at a station */
	WAITING_ENTRY {
		
		public TrainState entering(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t.getPreviousState(), t.getState(), t)){
				logger.info(t.name+ " is awaiting entry "+t.getStation().name+" Station..!");
			}
			
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
	
	/* The state of train that is departed from the depot */
	FROM_DEPOT {
			
		public TrainState entering(Train t, float delta) throws Exception{
			
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t.getPreviousState(), t.getState(), t)){
				logger.info(t.name+ " is travelling from the depot: "+t.getStation().name+" Station...");
			}
			
			// We have our station initialized we just need to retrieve the next track, enter the
			// current station officially and mark as in station
			if(t.getStation().canEnter(t.getTrainLine())){
				t.getStation().enter(t);
				t.setPos((Point2D.Float) t.getStation().position.clone());
				t.setDisembarked(false);
			}
			return IN_STATION;
		}
		
	};

	/**
	 * Determines the next event of the train switching the state as the train
	 * moves around the network, entering stations, leaving and moving.
	 * @param t The train that is changing state
	 */
	abstract TrainState entering(Train t, float delta) throws Exception;
	
	/**
	 * Checks if the state is changed from last state for the logger
	 * @param prev The previous state of the Train
	 * @param curr The current state of the Train
	 * @return true or false whether this is correct or not
	 */
	static boolean hasChanged(TrainState prev, TrainState curr, Train t) {
		if(prev == null || prev != curr){
			t.setPreviousState(curr);
			return true;
		}
		return false;
	}

}
	
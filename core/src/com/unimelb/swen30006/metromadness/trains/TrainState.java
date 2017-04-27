package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//The state that a train can be in 
enum TrainState {
	
	/* The state of train that is currently docked at a station */
	IN_STATION {
		
		public TrainState event(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t)){
				logger.info(t.name+" is in "+ t.station.name+" Station.");
			}
			
			// When in station we want to disembark passengers 
			// and wait 10 seconds for incoming passengers
			if(!t.isDisembarked()){
				t.disembark();
				t.setDepartureTimer(t.station.getDepartureTime());
				t.setDisembarked(true);
			} else {
				// Checks if the station is the correct type of train
				if(t.station.type.equals(CargoTrain.TYPE) && t.departureTimer > 0){
					t.setDepartureTimer(0);
				
				// Count down if departure timer. 
				} else if(t.departureTimer > 0){
					t.setDepartureTimer(t.departureTimer - delta);
						
				} else {
					// We are ready to depart, find the next track and wait until we can enter 
					boolean endOfLine = t.trainLine.endOfLine(t.station);
					if(endOfLine){
						t.setForward(!t.isForward());
					}
					t.setTrack(t.trainLine.nextTrack(t.station, t.isForward()));
					return READY_DEPART;
				}
			}
			return IN_STATION;
		}	
	}, 
	
	/* The state of train that is ready to depart the station */
	READY_DEPART {
		public TrainState event(Train t, float delta) throws Exception{
			
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t)){
				logger.info(t.name+ " is ready to depart for "+t.station.name+" Station!");
			}
			
			if(t.track.canEnter(t.isForward())){
				// Find the next
				t.trainLine.nextStation(t.station, t.isForward());
				// Depart our current station
				t.station.depart(t);
				t.setStation(t.trainLine.nextStation(t.station, t.isForward()));
				t.track.enter(t);
				return ON_ROUTE;
			}
			return READY_DEPART;
		}
		
	}, 
	
	/* The state of train that is in the process of going to a station */
	ON_ROUTE {
		public TrainState event(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t)){
				logger.info(t.name+ " enroute to "+t.station.name+" Station!");
			}
			// Checkout if we have reached the new station
			if(t.getPos().distance(t.station.position) < 10 ){
				return WAITING_ENTRY;
			}
			return ON_ROUTE;
		}
		
	}, 
	
	/* The state of train that is just arrived at a station */
	WAITING_ENTRY {
		
		public TrainState event(Train t, float delta) throws Exception {
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t)){
				logger.info(t.name+ " is awaiting entry "+t.station.name+" Station..!");
			}
			
			if(t.station.canEnter(t.trainLine)){
				if(!t.station.type.equals(CargoTrain.TYPE)) {
					t.setDisembarked(false);
				} else if (!t.station.type.equals("Active")) {
					t.setDisembarked(false);
				} else {
					t.setDisembarked(true);
				}
				t.track.leave(t);
				t.setPos((Point2D.Float) t.station.position.clone());
				t.station.enter(t);
				return IN_STATION;
			} else {
				return WAITING_ENTRY;
			}
		}
		
	}, 
	
	/* The state of train that is departed from the depot */
	FROM_DEPOT {
			
		public TrainState event(Train t, float delta) throws Exception{
			
			Logger logger = LogManager.getLogger();
			
			if(hasChanged(t)){
				logger.info(t.name+ " is travelling from the depot: "+t.station.name+" Station...");
			}
			
			// We have our station initialized we just need to retrieve the next track, enter the
			// current station officially and mark as in station
			if(t.station.canEnter(t.trainLine)){
				t.station.enter(t);
				t.setPos((Point2D.Float) t.station.position.clone());
				t.setDisembarked(false);
				return IN_STATION;
			}
			return FROM_DEPOT;
		}
		
	};

	/**
	 * Determines the next event of the train switching the state as the train
	 * moves around the network, entering stations, leaving and moving.
	 * @param t The train that is changing state
	 */
	abstract TrainState event(Train t, float delta) throws Exception;
	
	/**
	 * Checks if the state is changed from last state for the logger
	 * @param prev The previous state of the Train
	 * @param curr The current state of the Train
	 * @return true or false whether this is correct or not
	 */
	static boolean hasChanged(Train t) {
		TrainState prev = t.previousState;
		TrainState curr = t.state;
		
		if(prev == null || prev != curr){
			t.setPreviousState(curr);
			return true;
		}
		return false;
	}

}
	
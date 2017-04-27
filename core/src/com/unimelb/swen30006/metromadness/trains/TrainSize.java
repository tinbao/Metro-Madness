package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;

/**
 * The size of the train 
 * New sizes can be easily added to the enumerated list
 *
 */
public enum TrainSize {
	// 		(capacity, luggage, color, circleSpec)
	BIG_P 	(80, 0	, Color.LIGHT_GRAY	, 20f),
	SMALL_P	(10, 0	, Color.MAROON		, 10f),
	BIG_C 	(80, 200, Color.LIGHT_GRAY	, 20f),
	SMALL_C	(10, 100, Color.MAROON		, 10f);
	
	/** How many passengers it can hold */
	public final int capacity;	
	/** max luggage capacity (set to 0 when not a cargo train) */
	public final int luggage;
	/** color rendered */
	public final Color color;
	/** dividing constant to determine color interpolation and size */
	public final float circleSpec;	
	
	TrainSize(int capacity, int luggage, Color color, float circleSpec) {
		this.capacity = capacity;
		this.luggage = luggage;
		this.color = color;
		this.circleSpec = circleSpec;
	}
}

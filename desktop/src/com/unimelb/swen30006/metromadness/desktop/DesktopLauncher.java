package com.unimelb.swen30006.metromadness.desktop;

import java.io.FileNotFoundException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unimelb.swen30006.metromadness.MetroMadness;

public class DesktopLauncher {
	public static void main (String[] arg) throws FileNotFoundException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MetroMadness(), config);
	}
}

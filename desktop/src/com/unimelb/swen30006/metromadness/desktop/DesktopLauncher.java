package com.unimelb.swen30006.metromadness.desktop;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unimelb.swen30006.metromadness.MetroMadness;

public class DesktopLauncher {
	public static void main (String[] arg) throws FileNotFoundException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MetroMadness(), config);
		/*
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		*/
	}
}

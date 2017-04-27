package com.unimelb.swen30006.metromadness;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class MetroMadness extends ApplicationAdapter {

	// The width of the world in unitless dimensions
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 1200;
    
    // Font parameters
    private static final int SMALL_FONT = 12;
    private static final int BIG_FONT = 40;
    private static final float HEADER_SCALE = 0.5f;
    private static final int HEADER_HORIZONTAL = 10;
    
    // Camera parameters
    private static final float CAM_MAX = 2f;
    private static final float CAM_ZOOM = 0.1f;
    private static final float CAM_TRANSLATE = 3f;

    // Viewport state
    private int VIEWPORT_WIDTH=200;
	private float viewport_width;

	// Data for simluation, rendering and camera.
	private Simulation sim;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	
	// Font
	private BitmapFont smaller;
	private BitmapFont header;

	@Override
	public void resize(int width, int height) {
        camera.viewportWidth = viewport_width;
        camera.viewportHeight = viewport_width * (float)height/width;
        camera.update();
	}

	@Override
	public void create () {
		// Create the simulation
		sim = new Simulation("filename");		
		
		// Setup our 2D Camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport_width = VIEWPORT_WIDTH;
		camera = new OrthographicCamera(viewport_width, viewport_width * (h / w));
		camera.position.set(camera.viewportWidth / CAM_MAX, camera.viewportHeight / CAM_MAX, 0);
		camera.update();
		
		// Create our shape renderer
		shapeRenderer = new ShapeRenderer();
		
		// Create our font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Book.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = SMALL_FONT;
		smaller = generator.generateFont(parameter);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		FreeTypeFontGenerator headlineGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Bold.ttf"));
		FreeTypeFontParameter headlineParam = new FreeTypeFontParameter();
		headlineParam.size = BIG_FONT;
		header = headlineGen.generateFont(headlineParam);
		headlineGen.dispose(); // don't forget to dispose to avoid memory leaks!
		
		// Setup fonts
		 smaller.setColor(Color.GRAY);
		 header.setColor(Color.BLACK);

	}

	@Override
	public void render () {
		// Clear the graphics to white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Handle user input
		handleInput();
		
		// Update the simulation and camera
		camera.update();
		sim.update();
		
		// Render the simulation
		 shapeRenderer.setProjectionMatrix(camera.combined);
		 
		 // Render all filled shapes.
		 shapeRenderer.begin(ShapeType.Filled);
		 sim.render(shapeRenderer);
		 shapeRenderer.end();
		 
		 // Begin preparations to render text
		 SpriteBatch batch = new SpriteBatch();
		 batch.begin();

		 // Render Header
		 header.getData().setScale(HEADER_SCALE);
		 header.draw(batch, "metro madness.", HEADER_HORIZONTAL, Gdx.graphics.getHeight()-HEADER_HORIZONTAL);
		 batch.end();

	}
	
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += CAM_ZOOM;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
        	camera.zoom -= CAM_ZOOM;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
        	camera.translate(-CAM_TRANSLATE, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	camera.translate(CAM_TRANSLATE, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	camera.translate(0, -CAM_TRANSLATE, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	camera.translate(0, CAM_TRANSLATE, 0);
        }

        
        camera.zoom = MathUtils.clamp(camera.zoom, CAM_ZOOM, WORLD_WIDTH/camera.viewportWidth);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / CAM_MAX, WORLD_WIDTH - effectiveViewportWidth / CAM_MAX);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / CAM_MAX, WORLD_HEIGHT - effectiveViewportHeight / CAM_MAX);
    }

}

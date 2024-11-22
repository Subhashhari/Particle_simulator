package Pack;

import Pack.Emitter.*;
import Pack.FieldPoint.*;
import Pack.Particle.*;
import Pack.ParticleSystem;
import java.util.Vector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.List;

public class SimulationUI extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private ParticleSystem particleSystem; // Backend system
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        particleSystem = new ParticleSystem(); // Initialize backend system
        Pane root = new Pane();

        // Canvas to draw particles
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        // UI Buttons
        HBox controls = createControls();
        controls.setLayoutY(HEIGHT - 40);
        root.getChildren().add(controls);

        // Animation loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Particle Simulation");
        stage.show();
    }

    private HBox createControls() {
        HBox controls = new HBox(10);
        Button addEmitterButton = new Button("Add Emitter");
        Button addFieldButton = new Button("Add Field");

        addEmitterButton.setOnAction(e -> {
            // Add an emitter at a random position
            particleSystem.addEmitter(randomPosition(), 1.0f, 0.7853f, 0.0f, 1f);
        });

        addFieldButton.setOnAction(e -> {
            // Add a field point at a random position
            particleSystem.addFieldPoint(randomPosition(), 10.0f, "A");
        });

        controls.getChildren().addAll(addEmitterButton, addFieldButton);
        return controls;
    }

    private Vector<Float> randomPosition() {
        Vector<Float> position = new Vector<>();
        position.add((float) (Math.random() * WIDTH));  // X-coordinate
        position.add((float) (Math.random() * HEIGHT)); // Y-coordinate
        return position;
    }
    

    private void update() {
        // Call backend to update particle positions
        particleSystem.setForces();
        particleSystem.updateAll();
        particleSystem.removeParticlesOutOfScreen(WIDTH, HEIGHT); // Remove particles that are out of screen
    }

    private void toggleGravity()
    {
        // Toggle gravity on/off
        boolean gravityEnabled = particleSystem.isGravityEnabled();
        particleSystem.setGravityEnabled(!gravityEnabled);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw particles
        List<Particle> particles = particleSystem.getParticles();
        gc.setFill(Color.BLUE);
        for (Particle particle : particles) {
            gc.fillOval(particle.getPosition().get(0), particle.getPosition().get(1), 2, 2); // Render particle as a small circle
        }

        // Draw emitters
        gc.setFill(Color.RED);
        for (Emitter emitter : particleSystem.getEmitters()) {
            Vector<Float> position = emitter.getPosition();
            gc.fillOval(position.get(0), position.get(1), 10, 10); // Render emitter as a larger circle
        }

        // Draw field points
        gc.setFill(Color.GREEN);
        for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
            Vector<Float> position = fieldPoint.getPosition();
            gc.fillOval(position.get(0), position.get(1), 10, 10); // Render field point as a larger circle
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
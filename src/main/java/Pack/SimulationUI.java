package java.Pack;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.Pack.Emitter.Emitter;
import java.Pack.FieldPoint.FieldPoint;
import java.Pack.Particle.Particle;

import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

public class ParticleSimulatorUI extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private ParticleSystem particleSystem;
    private boolean isEmitting = false;
    private Emitter currentEmitter = null;
    private ArrayList<FieldPoint> fieldPoints = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Particle Simulator");

        // Initialize Particle System
        particleSystem = new ParticleSystem();

        // Set up canvas
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        // UI controls
        Button addEmitterButton = new Button("Add Emitter");
        Button addFieldPointButton = new Button("Add Field Point");
        Button clearButton = new Button("Clear");
        Button pauseButton = new Button("Pause");
        Button resumeButton = new Button("Resume");

        Slider speedSlider = new Slider(0, 10, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setBlockIncrement(1);

        Slider spreadSlider = new Slider(0, 90, 15);
        spreadSlider.setShowTickLabels(true);
        spreadSlider.setShowTickMarks(true);
        spreadSlider.setBlockIncrement(5);

        Slider emissionRateSlider = new Slider(1, 50, 10);
        emissionRateSlider.setShowTickLabels(true);
        emissionRateSlider.setShowTickMarks(true);
        emissionRateSlider.setBlockIncrement(1);

        Slider fieldStrengthSlider = new Slider(-10, 10, 1);
        fieldStrengthSlider.setShowTickLabels(true);
        fieldStrengthSlider.setShowTickMarks(true);
        fieldStrengthSlider.setBlockIncrement(1);

        // Event handlers for buttons
        addEmitterButton.setOnAction(e -> {
            Vector<Float> emitterPos = new Vector<>(List.of(400.0f, 300.0f));
            currentEmitter = new Emitter(emitterPos, (float) emissionRateSlider.getValue(), (float) spreadSlider.getValue(), 0.0f);
            gc.setFill(Color.RED);
            gc.fillOval(emitterPos.get(0) - 5, emitterPos.get(1) - 5, 10, 10);
        });

        addFieldPointButton.setOnAction(e -> {
            Vector<Float> fieldPos = new Vector<>(List.of(400.0f, 300.0f));
            FieldPoint fieldPoint = new FieldPoint(fieldPos, (float) fieldStrengthSlider.getValue(), "repulsive");
            fieldPoints.add(fieldPoint);
            gc.setFill(Color.BLUE);
            gc.fillOval(fieldPos.get(0) - 5, fieldPos.get(1) - 5, 10, 10);
            particleSystem.addFieldPoint(fieldPos, (float) fieldStrengthSlider.getValue(), "repulsive");
        });

        clearButton.setOnAction(e -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            particleSystem.getParticles().clear();
            fieldPoints.clear();
            isEmitting = false;
        });

        pauseButton.setOnAction(e -> isEmitting = false);
        resumeButton.setOnAction(e -> isEmitting = true);

        // UI layout
        HBox controls = new HBox(10, addEmitterButton, addFieldPointButton, clearButton, pauseButton, resumeButton);
        VBox sliders = new VBox(10, speedSlider, spreadSlider, emissionRateSlider, fieldStrengthSlider);
        VBox root = new VBox(10, canvas, controls, sliders);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Animation Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isEmitting && currentEmitter != null) {
                    emitParticles(currentEmitter, (float) speedSlider.getValue());
                }
                updateParticles();
                drawParticles();
            }
        }.start();
    }

    private void emitParticles(Emitter emitter, float speed) {
        int emissionRate = (int) emitter.getEmissionRate();
        for (int i = 0; i < emissionRate; i++) {
            Vector<Float> position = emitter.getPosition();
            Vector<Float> velocity = new Vector<>(List.of(speed, speed)); // Set initial velocity
            particleSystem.addParticles(position, new float[][]{{speed, speed}});
        }
    }

    private void updateParticles() {
        // Update forces and positions
        particleSystem.setForces(); // JNI call to compute forces
        particleSystem.updateParticlesPosition(); // JNI call to update positions
    }

    private void drawParticles() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw emitters and field points
        if (currentEmitter != null) {
            Vector<Float> emitterPos = currentEmitter.getPosition();
            gc.setFill(Color.RED);
            gc.fillOval(emitterPos.get(0) - 5, emitterPos.get(1) - 5, 10, 10);
        }
        gc.setFill(Color.BLUE);
        for (FieldPoint fieldPoint : fieldPoints) {
            Vector<Float> fieldPos = fieldPoint.getPosition();
            gc.fillOval(fieldPos.get(0) - 5, fieldPos.get(1) - 5, 10, 10);
        }

        // Draw particles
        gc.setFill(Color.YELLOW);
        for (Particle particle : particleSystem.getParticles()) {
            Vector<Float> pos = particle.getPosition();
            gc.fillOval(pos.get(0) - 1, pos.get(1) - 1, 3, 3);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

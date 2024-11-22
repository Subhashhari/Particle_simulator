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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class SimulationUI extends Application {
    private static final int WIDTH = 1200; // Total screen width
    private static final int HEIGHT = 800; // Total screen height
    private static final int CONTROL_BOX_WIDTH = 300; // Width of the control box

    private ParticleSystem particleSystem; // Backend system
    private Canvas canvas;

    // Sliders for controlling parameters
    private Slider velocitySlider;
    private Slider spreadSlider;
    private Slider angleSlider;

    private Emitter selectedEmitter; // Currently selected emitter
    private FieldPoint selectedFieldPoint; // Currently selected field point

    private boolean draggingEmitter = false; // Is an emitter being dragged?
    private boolean draggingField = false;  // Is a field being dragged?
    private boolean isPaused = false;
    private boolean isStepMode = false;
    private AnimationTimer timer;

    @Override
    public void start(Stage stage) {
        particleSystem = new ParticleSystem(); // Initialize backend system

        // Root layout: Canvas + Control Box
        BorderPane root = new BorderPane();

        // Canvas for particles
        canvas = new Canvas(WIDTH - CONTROL_BOX_WIDTH, HEIGHT); // Leave space for controls
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        root.setCenter(canvas);

        // Controls on the right
        VBox controlBox = createControls();
        controlBox.setPrefWidth(CONTROL_BOX_WIDTH);
        root.setRight(controlBox);

        // Animation loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused || isStepMode) {
                    update();
                    render();
                    isStepMode = false; // Reset step mode after one frame
                }
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Interactive Particle Simulation");
        stage.show();
    }

    private VBox createControls() {
        VBox controls = new VBox(10);
        controls.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");

        // Buttons
        Button addEmitterButton = new Button("Add Emitter");
        Button addFieldButton = new Button("Add Field");
        Button resetButton = new Button("Reset");
        Button toggleTrailsButton = new Button("Toggle Trails");
        Button pauseButton = new Button("Pause/Resume");
        Button stepButton = new Button("Step");

        // Sliders
        velocitySlider = createSlider(1, 50, 10, "Particle Velocity");
        spreadSlider = createSlider(1, 90, 30, "Emitter Spread Angle");
        angleSlider = createSlider(0, 360, 0, "Emission Angle");

        addEmitterButton.setOnAction(e -> {
            // Add an emitter at a random position
            // System.out.println(angleSlider.getValue());
            // System.out.println(velocitySlider.getValue());
            // System.out.println(spreadSlider.getValue());
            float angle = (float) angleSlider.getValue();
            float spread = (float) spreadSlider.getValue();
            float speed = (float)  velocitySlider.getValue();
            particleSystem.addEmitter(randomPosition(), speed,spread,angle,1.0f);
            //System.out.println(angle);
        });

        addFieldButton.setOnAction(e -> {
            // Add a field point at a random position
            particleSystem.addFieldPoint(randomPosition(), 10.0f, "A");
        });

        resetButton.setOnAction(e -> {
            // Clear all particles, emitters, and fields
            particleSystem = new ParticleSystem();
            selectedEmitter = null;
            selectedFieldPoint = null;
        });

        toggleTrailsButton.setOnAction(e -> {
            // Toggle trails for particles
            for (Particle particle : particleSystem.getParticles()) {
                particle.setTrail(!particle.hasTrail());
            }
        });

        pauseButton.setOnAction(e -> {
            isPaused = !isPaused;
        });

        stepButton.setOnAction(e -> {
            if (isPaused) {
                isStepMode = true;
            }
        });

        controls.getChildren().addAll(
            new Label("Controls:"),
            addEmitterButton, addFieldButton, resetButton, 
            toggleTrailsButton, pauseButton, stepButton,
            labeledSlider("Velocity:", velocitySlider),
            labeledSlider("Spread Angle:", spreadSlider),
            labeledSlider("Emission Angle:", angleSlider)
        );
        return controls;
    }

    private HBox labeledSlider(String labelText, Slider slider) {
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        HBox hbox = new HBox(10, label, slider);
        return hbox;
    }

    private Slider createSlider(double min, double max, double value, String tooltip) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(1);
        slider.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        return slider;
    }

    private Vector<Float> randomPosition() {
        Vector<Float> position = new Vector<>();
        position.add((float) (Math.random() * (WIDTH - CONTROL_BOX_WIDTH)));  // X-coordinate
        position.add((float) (Math.random() * HEIGHT)); // Y-coordinate
        return position;
    }

    private void handleMousePressed(MouseEvent e) {
        float x = (float) e.getX();
        float y = (float) e.getY();

        if (x > WIDTH - CONTROL_BOX_WIDTH) return; // Ignore clicks in the control box

        // Check for emitters near the click
        for (Emitter emitter : particleSystem.getEmitters()) {
            Vector<Float> position = emitter.getPosition();
            if (isNear(x, y, position)) {
                selectedEmitter = emitter;
                draggingEmitter = true;
                return;
            }
        }

        // Check for field points near the click
        for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
            Vector<Float> position = fieldPoint.getPosition();
            if (isNear(x, y, position)) {
                selectedFieldPoint = fieldPoint;
                draggingField = true;
                return;
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        float x = (float) e.getX();
        float y = (float) e.getY();

        if (draggingEmitter && selectedEmitter != null) {
            selectedEmitter.getPosition().set(0, x);
            selectedEmitter.getPosition().set(1, y);
        }

        if (draggingField && selectedFieldPoint != null) {
            selectedFieldPoint.getPosition().set(0, x);
            selectedFieldPoint.getPosition().set(1, y);
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        draggingEmitter = false;
        draggingField = false;
        selectedEmitter = null;
        selectedFieldPoint = null;
    }

    private boolean isNear(float x, float y, Vector<Float> position) {
        float dx = x - position.get(0);
        float dy = y - position.get(1);
        return (dx * dx + dy * dy) <= 100; // Within 10px radius
    }

    private void update() {
        // Update particle positions in the backend
        particleSystem.setForces();
        particleSystem.updateAll();

        // Apply repelling force for field points
        // for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
        //     Vector<Float> fieldPosition = fieldPoint.getPosition();
        //     for (Particle particle : particleSystem.getParticles()) {
        //         Vector<Float> particlePosition = particle.getPosition();
        //         Vector<Float> particleForce = particle.getForce();

        //         float dx = particlePosition.get(0) - fieldPosition.get(0);
        //         float dy = particlePosition.get(1) - fieldPosition.get(1);
        //         float distanceSquared = dx * dx + dy * dy;
        //         float distance = (float) Math.sqrt(distanceSquared);

        //         if (distance > 1) { // Avoid division by zero
        //             float repellingForce = 500.0f / distanceSquared; // Repel based on inverse square law
        //             particleForce.set(0, particleForce.get(0) + (repellingForce * dx / distance));
        //             particleForce.set(1, particleForce.get(1) + (repellingForce * dy / distance));
        //         }
        //     }
        // }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK); // Black background
        gc.fillRect(0, 0, WIDTH - CONTROL_BOX_WIDTH, HEIGHT);

        // Draw particles
        List<Particle> particles = particleSystem.getParticles();
        gc.setFill(Color.BLUE);
        for (Particle particle : particles) {
            gc.fillOval(particle.getPosition().get(0), particle.getPosition().get(1), 5, 5);
        }

        // Draw emitters
        gc.setFill(Color.RED);
        for (Emitter emitter : particleSystem.getEmitters()) {
            Vector<Float> position = emitter.getPosition();
            gc.fillOval(position.get(0), position.get(1), 10, 10);
        }

        // Draw field points
        gc.setFill(Color.GREEN);
        for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
            Vector<Float> position = fieldPoint.getPosition();
            gc.fillOval(position.get(0), position.get(1), 10, 10);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
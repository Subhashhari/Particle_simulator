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
                //pause for 10 ms
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        Button toggleGravityButton = new Button("Toggle Gravity");
        Button pauseButton = new Button("Pause/Resume");
        Button stepButton = new Button("Step");

        // Sliders
        velocitySlider = createSlider(1, 10, 3, "Particle Velocity");
        spreadSlider = createSlider(0, 2*3.16, 1, "Emitter Spread Angle");
        angleSlider = createSlider(0, 2*3.16, 0, "Emission Angle");

        addEmitterButton.setOnAction(e -> {
            // Add an emitter at a random position
            // System.out.println(angleSlider.getValue());
            // System.out.println(velocitySlider.getValue());
            // System.out.println(spreadSlider.getValue());
            float angle = (float) angleSlider.getValue();
            float spread = (float) spreadSlider.getValue();
            float speed = (float)  velocitySlider.getValue();
            particleSystem.addOscillatingEmitter(randomPosition(), speed,spread,angle,1.0f, 100f, 0.06f);
            //particleSystem.addOscillatingEmitter(randomPosition(), speed,spread,angle,1.0f,0.1f,10.0f);
            //System.out.println(angle);
        });

        addFieldButton.setOnAction(e -> {
            // Add a field point at a random position
            particleSystem.addFieldPoint(randomPosition(), 10.0f, "B");
        });

        resetButton.setOnAction(e -> {
            // Clear all particles, emitters, and fields
            //particleSystem = new ParticleSystem();
            particleSystem.getParticles().clear();
            // selectedEmitter = null;
            // selectedFieldPoint = null;
        });

        toggleGravityButton.setOnAction(e -> {
            // Toggle trails for particles
            // for (Particle particle : particleSystem.getParticles()) {
            //     particle.setTrail(!particle.hasTrail());
            // }
            particleSystem.setGravityEnabled(particleSystem.isGravityEnabled()^1);
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
            toggleGravityButton, pauseButton, stepButton,
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
        position.add((float) ((WIDTH - CONTROL_BOX_WIDTH))/2);  // X-coordinate
        position.add((float) (HEIGHT)/2); // Y-coordinate
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
        particleSystem.removeParticlesOutOfScreen(WIDTH, HEIGHT);
        particleSystem.setForces();
        particleSystem.updateAll();
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK); // Black background
        gc.fillRect(0, 0, WIDTH - CONTROL_BOX_WIDTH, HEIGHT);

        // Draw particles
        List<Particle> particles = particleSystem.getParticles();
        gc.setFill(Color.BLUE);
        for (Particle particle : particles) {
            gc.fillOval(particle.getPosition().get(0), particle.getPosition().get(1), 2, 2);
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

// //package Pack;

// import Pack.Emitter.*;
// import Pack.FieldPoint.*;
// import Pack.ParticleSystem;
// import javafx.animation.AnimationTimer;
// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.canvas.GraphicsContext;
// import javafx.scene.control.Label;
// import javafx.scene.control.Slider;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.stage.Stage;
// import java.util.*;

// public class SimulationUI extends Application {

//     private static final int WIDTH = 800;
//     private static final int HEIGHT = 600;
//     private static final int CONTROL_BOX_WIDTH = 200;

//     private ParticleSystem particleSystem;
//     private Canvas canvas;
//     private VBox controlBox;
//     private Label emitterLabel;
//     private VBox emitterControls;

//     private Emitter selectedEmitter = null;
//     private boolean draggingEmitter = false;

//     private FieldPoint selectedFieldPoint = null;
//     private boolean draggingField = false;

//     private AnimationTimer timer;
//     private boolean isPaused = false;
//     private boolean isStepMode = false;

//     public static void main(String[] args) {
//         launch(args);
//     }

//     @Override
//     public void start(Stage stage) {
//         initializeUI(stage);
//         initializeAnimation();
//     }

//     private void initializeUI(Stage stage) {
//         particleSystem = new ParticleSystem(); // Backend system initialization

//         // Root layout
//         BorderPane root = new BorderPane();

//         // Canvas for rendering particles
//         canvas = createCanvas();
//         root.setCenter(canvas);

//         // Control box for emitters and field points
//         controlBox = createControlBox();
//         root.setRight(controlBox);

//         Scene scene = new Scene(root, WIDTH, HEIGHT);
//         stage.setScene(scene);
//         stage.setTitle("Interactive Particle Simulation");
//         stage.show();
//     }

//     private Canvas createCanvas() {
//         Canvas canvas = new Canvas(WIDTH - CONTROL_BOX_WIDTH, HEIGHT);
//         canvas.setOnMousePressed(this::handleMousePressed);
//         canvas.setOnMouseDragged(this::handleMouseDragged);
//         canvas.setOnMouseReleased(this::handleMouseReleased);
//         return canvas;
//     }

//     private VBox createControlBox() {
//         VBox box = new VBox(10);
//         box.setPrefWidth(CONTROL_BOX_WIDTH);
//         box.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");
    
//         emitterLabel = new Label("Select an emitter or field point");
//         emitterLabel.setTextFill(Color.WHITE);
    
//         // Create emitterControls VBox
//         emitterControls = new VBox(10);
//         emitterControls.setStyle("-fx-padding: 10;");
    
//         // Add buttons for actions
//         Button addEmitterButton = new Button("Add Emitter");
//         Button addFieldButton = new Button("Add Field");
//         Button resetButton = new Button("Reset");
//         Button toggleTrailsButton = new Button("Toggle Trails");
//         Button pauseButton = new Button("Pause/Resume");
//         Button stepButton = new Button("Step");
    
//         // Sliders for control
//         velocitySlider = createSlider(1, 10, 0.5, "Particle Velocity");
//         spreadSlider = createSlider(1, 2 * Math.PI, 0.05, "Emitter Spread Angle");
//         angleSlider = createSlider(0, 2 * Math.PI, 0.05, "Emission Angle");
    
//         // Define button actions
//         addEmitterButton.setOnAction(e -> {
//             float angle = (float) angleSlider.getValue();
//             float spread = (float) spreadSlider.getValue();
//             float speed = (float) velocitySlider.getValue();
//             particleSystem.addEmitter(randomPosition(), speed, spread, angle, 1.0f);
//         });
    
//         addFieldButton.setOnAction(e -> {
//             particleSystem.addFieldPoint(randomPosition(), 10.0f, "A");
//         });
    
//         resetButton.setOnAction(e -> {
//             particleSystem = new ParticleSystem();
//             selectedEmitter = null;
//             selectedFieldPoint = null;
//         });
    
//         toggleTrailsButton.setOnAction(e -> {
//             for (Particle particle : particleSystem.getParticles()) {
//                 particle.setTrail(!particle.hasTrail());
//             }
//         });
    
//         pauseButton.setOnAction(e -> {
//             isPaused = !isPaused;
//         });
    
//         stepButton.setOnAction(e -> {
//             if (isPaused) {
//                 isStepMode = true;
//             }
//         });
    
//         // Add all controls to emitterControls
//         emitterControls.getChildren().addAll(
//             addEmitterButton, addFieldButton, resetButton, 
//             toggleTrailsButton, pauseButton, stepButton,
//             labeledSlider("Velocity:", velocitySlider),
//             labeledSlider("Spread Angle:", spreadSlider),
//             labeledSlider("Emission Angle:", angleSlider)
//         );
    
//         // Add emitterLabel and emitterControls to the main box
//         box.getChildren().addAll(emitterLabel, emitterControls);
//         return box;
//     }
    
//     // Utility methods
//     private HBox labeledSlider(String labelText, Slider slider) {
//         Label label = new Label(labelText);
//         label.setTextFill(Color.WHITE);
//         return new HBox(10, label, slider);
//     }
    
//     private Slider createSlider(double min, double max, double value, String tooltip) {
//         Slider slider = new Slider(min, max, value);
//         slider.setShowTickMarks(true);
//         slider.setShowTickLabels(true);
//         slider.setBlockIncrement(1);
//         slider.setTooltip(new javafx.scene.control.Tooltip(tooltip));
//         return slider;
//     }
    
//     private Vector<Float> randomPosition() {
//         Vector<Float> position = new Vector<>();
//         position.add((float) (Math.random() * (WIDTH - CONTROL_BOX_WIDTH))); // X-coordinate
//         position.add((float) (Math.random() * HEIGHT)); // Y-coordinate
//         return position;
//     }
    

//     private void initializeAnimation() {
//         particleSystem.addEmitter(new Vector<>(List.of(10.0f, 100.0f)), 1.0f, (float) Math.PI / 6, 0.0f,1.0f);
//         timer = new AnimationTimer() {
//             @Override
//             public void handle(long now) {
//                 if (!isPaused || isStepMode) {
//                     update();
//                     render();
//                     isStepMode = false; // Reset step mode
//                 }
//             }
//         };
//         timer.start();
//     }

//     private void handleMousePressed(javafx.scene.input.MouseEvent e) {
//         float x = (float) e.getX();
//         float y = (float) e.getY();

//         if (isInControlBox(x)) return;

//         if (selectEmitter(x, y) || selectFieldPoint(x, y)) return;

//         clearSelection();
//     }

//     private boolean selectEmitter(float x, float y) {
//         for (Emitter emitter : particleSystem.getEmitters()) {
//             if (isNear(x, y, emitter.getPosition())) {
//                 selectedEmitter = emitter;
//                 draggingEmitter = true;
//                 updateControlBoxForEmitter();
//                 return true;
//             }
//         }
//         return false;
//     }

//     private boolean selectFieldPoint(float x, float y) {
//         for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
//             if (isNear(x, y, fieldPoint.getPosition())) {
//                 selectedFieldPoint = fieldPoint;
//                 draggingField = true;
//                 emitterLabel.setText("Field point selected (controls not available)");
//                 emitterControls.getChildren().clear();
//                 return true;
//             }
//         }
//         return false;
//     }

//     private void clearSelection() {
//         selectedEmitter = null;
//         selectedFieldPoint = null;
//         emitterLabel.setText("Select an emitter or field point");
//         emitterControls.getChildren().clear();
//     }

//     private void handleMouseDragged(javafx.scene.input.MouseEvent e) {
//         float x = (float) e.getX();
//         float y = (float) e.getY();

//         if (draggingEmitter && selectedEmitter != null) {
//             moveElement(selectedEmitter.getPosition(), x, y);
//         }

//         if (draggingField && selectedFieldPoint != null) {
//             moveElement(selectedFieldPoint.getPosition(), x, y);
//         }
//     }

//     private void moveElement(Vector<Float> position, float x, float y) {
//         position.set(0, x);
//         position.set(1, y);
//     }

//     private void handleMouseReleased(javafx.scene.input.MouseEvent e) {
//         draggingEmitter = false;
//         draggingField = false;
//     }

//     private void updateControlBoxForEmitter() {
//         if (selectedEmitter == null) return;

//         emitterLabel.setText("Emitter Selected");
//         emitterControls.getChildren().clear();

//         // Create and bind sliders for emitter properties
//         Slider velocitySlider = createSlider(1, 10, selectedEmitter.getSpeed(), "Particle Velocity");
//         Slider spreadSlider = createSlider(0, (float) (2 * Math.PI), selectedEmitter.getAngle(), "Emitter Spread Angle");
//         Slider angleSlider = createSlider(0, (float) (2 * Math.PI), selectedEmitter.getAngle(), "Emission Angle");

//         velocitySlider.valueProperty().addListener((obs, oldVal, newVal) -> selectedEmitter.setSpeed(newVal.floatValue()));
//         spreadSlider.valueProperty().addListener((obs, oldVal, newVal) -> selectedEmitter.setAngle(newVal.floatValue()));
//         angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> selectedEmitter.setAngle(newVal.floatValue()));

//         emitterControls.getChildren().addAll(
//             labeledSlider("Velocity:", velocitySlider),
//             labeledSlider("Spread Angle:", spreadSlider),
//             labeledSlider("Emission Angle:", angleSlider)
//         );

//         if (!controlBox.getChildren().contains(emitterControls)) {
//             controlBox.getChildren().add(emitterControls);
//         }
//     }

//     private void update() {
//         // Update particle positions in the backend
//         particleSystem.setForces();
//         particleSystem.updateAll();
//     }

//     private void render() {
//         GraphicsContext gc = canvas.getGraphicsContext2D();
//         gc.setFill(Color.BLACK); // Black background
//         gc.fillRect(0, 0, WIDTH - CONTROL_BOX_WIDTH, HEIGHT);

//         // Draw particles
//         List<Particle> particles = particleSystem.getParticles();
//         gc.setFill(Color.BLUE);
//         for (Particle particle : particles) {
//             gc.fillOval(particle.getPosition().get(0), particle.getPosition().get(1), 5, 5);
//         }

//         // Draw emitters
//         gc.setFill(Color.RED);
//         for (Emitter emitter : particleSystem.getEmitters()) {
//             Vector<Float> position = emitter.getPosition();
//             gc.fillOval(position.get(0), position.get(1), 10, 10);
//         }

//         // Draw field points
//         gc.setFill(Color.GREEN);
//         for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
//             Vector<Float> position = fieldPoint.getPosition();
//             gc.fillOval(position.get(0), position.get(1), 10, 10);
//         }
//     }

//     private Slider createSlider(float min, float max, float value, String tooltip) {
//         Slider slider = new Slider(min, max, value);
//         slider.setBlockIncrement(0.1);
//         slider.setMajorTickUnit((max - min) / 5);
//         slider.setMinorTickCount(4);
//         slider.setShowTickMarks(true);
//         slider.setShowTickLabels(true);
//         slider.setTooltip(new javafx.scene.control.Tooltip(tooltip));
//         return slider;
//     }

//     private VBox labeledSlider(String labelText, Slider slider) {
//         Label label = new Label(labelText);
//         label.setTextFill(Color.WHITE);
//         return new VBox(5, label, slider);
//     }

//     private boolean isNear(float x, float y, Vector<Float> position) {
//         float dx = x - position.get(0);
//         float dy = y - position.get(1);
//         return Math.sqrt(dx * dx + dy * dy) < 20;
//     }

//     private boolean isInControlBox(float x) {
//         return x > WIDTH - CONTROL_BOX_WIDTH;
//     }
// }

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class ParticleSimulator extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Emitter emitter;
    private FieldPoint fieldPoint;
    private Slider speedSlider;
    private Slider spreadSlider;
    private Slider countSlider;
    private Slider fieldStrengthSlider;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        
        emitter = new Emitter(400, 300, 2, Math.PI / 12);

        // Create a draggable field point with an initial strength
        fieldPoint = new FieldPoint(600, 400, 500);
        
        // Sliders for particle speed, spread angle, particle count, and field strength
        speedSlider = new Slider(0, 10, 2);
        spreadSlider = new Slider(0, Math.PI / 2, Math.PI / 12);
        countSlider = new Slider(1, 100, 5);
        fieldStrengthSlider = new Slider(-1000, 1000, 500); // Slider for field strength

        Label speedLabel = new Label("Particle Speed: " + speedSlider.getValue());
        Label spreadLabel = new Label("Spread Angle: " + Math.toDegrees(spreadSlider.getValue()) + " degrees");
        Label countLabel = new Label("Particles Emitted: " + (int) countSlider.getValue());
        Label strengthLabel = new Label("Field Strength: " + fieldStrengthSlider.getValue());

        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            speedLabel.setText("Particle Speed: " + String.format("%.1f", newValue.doubleValue()));
            emitter.setSpeed(newValue.doubleValue());
        });

        spreadSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            spreadLabel.setText("Spread Angle: " + String.format("%.1f", Math.toDegrees(newValue.doubleValue())) + " degrees");
            emitter.setSpread(newValue.doubleValue());
        });

        countSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            countLabel.setText("Particles Emitted: " + (int) newValue.doubleValue());
            emitter.setCount((int) newValue.doubleValue());
        });

        // Update field point strength based on slider value
        fieldStrengthSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            strengthLabel.setText("Field Strength: " + String.format("%.1f", newValue.doubleValue()));
            fieldPoint.setStrength(newValue.doubleValue());
        });

        VBox controls = new VBox(10, speedLabel, speedSlider, spreadLabel, spreadSlider, countLabel, countSlider, strengthLabel, fieldStrengthSlider);
        controls.setTranslateY(10);
        root.getChildren().add(controls);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // Add field point to the root for visual representation and interaction
        root.getChildren().add(fieldPoint.getView());

        AnimationTimer timer = new AnimationTimer() {
            private long lastEmission = 0;

            @Override
            public void handle(long now) {
                if (now - lastEmission > 50_000_000) {
                    emitter.emitParticles();
                    lastEmission = now;
                }

                gc.clearRect(0, 0, WIDTH, HEIGHT);

                // Draw emitter
                gc.setFill(Color.BLUE);
                gc.fillOval(emitter.getView().getCenterX() - emitter.getView().getRadius(),
                            emitter.getView().getCenterY() - emitter.getView().getRadius(),
                            emitter.getView().getRadius() * 2, emitter.getView().getRadius() * 2);

                // Apply force from field point to particles and update their positions
                for (Particle particle : emitter.getParticles()) {
                    fieldPoint.applyForce(particle); // Apply force from the field point
                    particle.updatePosition();
                    gc.setFill(Color.ORANGE);
                    gc.fillOval(particle.getView().getCenterX(), particle.getView().getCenterY(),
                                particle.getView().getRadius() * 2, particle.getView().getRadius() * 2);
                }
            }
        };

        timer.start();

        stage.setTitle("Particle Simulator with Draggable Field Point and Adjustable Field Strength");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

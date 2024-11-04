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
    private Slider speedSlider;
    private Slider spreadSlider;
    private Slider countSlider;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        
        // Create a single emitter at the center, emitting particles to the right with a slight spread
        emitter = new Emitter(400, 300, 0, Math.PI / 12); // Initial settings

        // Create sliders for speed, spread angle, and number of particles
        speedSlider = new Slider(0, 10, 2); // Speed range from 0 to 10, default 2
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setBlockIncrement(0.1);
        
        spreadSlider = new Slider(0, Math.PI / 2, Math.PI / 12); // Spread range from 0 to 90 degrees, default 15 degrees
        spreadSlider.setShowTickLabels(true);
        spreadSlider.setShowTickMarks(true);
        spreadSlider.setMajorTickUnit(Math.PI / 12);
        spreadSlider.setBlockIncrement(Math.PI / 36);
        
        countSlider = new Slider(1, 100, 5); // Number of particles to emit, default 5
        countSlider.setShowTickLabels(true);
        countSlider.setShowTickMarks(true);
        countSlider.setMajorTickUnit(10);
        countSlider.setBlockIncrement(1);
        
        // Labels for sliders
        Label speedLabel = new Label("Particle Speed: " + speedSlider.getValue());
        Label spreadLabel = new Label("Spread Angle: " + Math.toDegrees(spreadSlider.getValue()) + " degrees");
        Label countLabel = new Label("Particles Emitted: " + (int) countSlider.getValue());

        // Update labels when sliders are adjusted
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

        // VBox for sliders and labels
        VBox controls = new VBox(10, speedLabel, speedSlider, spreadLabel, spreadSlider, countLabel, countSlider);
        controls.setTranslateY(10); // Position the controls at the top of the window
        
        // Add the controls to the root
        root.getChildren().add(controls);
        
        // Add a canvas for drawing
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // Animation Timer for updating and rendering particles
        AnimationTimer timer = new AnimationTimer() {
            private long lastEmission = 0;

            @Override
            public void handle(long now) {
                // Emit particles every 50 milliseconds
                if (now - lastEmission > 50_000_000) {
                    emitter.emitParticles(); // Changed to emitParticles method
                    lastEmission = now;
                }

                // Clear the canvas for redrawing
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                // Render the emitter as a circle
                gc.setFill(Color.BLUE);
                gc.fillOval(emitter.getView().getCenterX() - emitter.getView().getRadius(),
                            emitter.getView().getCenterY() - emitter.getView().getRadius(),
                            emitter.getView().getRadius() * 2, emitter.getView().getRadius() * 2);

                // Update and render each particle
                for (Particle particle : emitter.getParticles()) {
                    particle.updatePosition();
                    gc.setFill(Color.ORANGE);
                    gc.fillOval(particle.getView().getCenterX(), particle.getView().getCenterY(),
                                particle.getView().getRadius() * 2, particle.getView().getRadius() * 2);
                }
            }
        };

        timer.start();

        stage.setTitle("Particle Simulator with Adjustable Emitter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

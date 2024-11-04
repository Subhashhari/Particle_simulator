import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Emitter {
    private double x, y;
    private List<Particle> particles;
    private double baseAngle; // Base angle in radians for particle emission direction
    private double spread; // Spread in radians for randomness
    private double speed; // Speed of emitted particles
    private int count; // Number of particles to emit
    private Random random;
    private Circle view; // Visual representation of the emitter

    public Emitter(double x, double y, double baseAngle, double spread) {
        this.x = x;
        this.y = y;
        this.baseAngle = baseAngle;
        this.spread = spread;
        this.speed = 2.0; // Default speed
        this.count = 5; // Default number of particles to emit
        this.particles = new ArrayList<>();
        this.random = new Random();
        
        // Initialize the emitter's visual representation
        this.view = new Circle(x, y, 10, Color.BLUE); // Emitter displayed as a blue circle with radius 10
    }

    public void emitParticles() {
        for (int i = 0; i < count; i++) {
            // Calculate a random angle within the spread range
            double angle = baseAngle + (random.nextDouble() - 0.5) * spread;
            double velocityX = speed * Math.cos(angle);
            double velocityY = speed * Math.sin(angle);
            particles.add(new Particle(x, y, velocityX, velocityY, 2, Color.ORANGE));
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Circle getView() {
        return view;
    }

    // Method to set the speed dynamically
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Method to set the spread dynamically
    public void setSpread(double spread) {
        this.spread = spread;
    }

    // Method to set the number of particles emitted
    public void setCount(int count) {
        this.count = count;
    }
}

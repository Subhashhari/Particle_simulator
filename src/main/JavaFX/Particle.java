import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle {
    private double x, y;
    private double velocityX, velocityY;
    private double forceX, forceY; // Forces acting on the particle
    private double size;
    private Circle view; // Visual representation of the particle

    public Particle(double x, double y, double velocityX, double velocityY, double size, Color color) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.forceX = 0;
        this.forceY = 0;
        this.size = size;
        this.view = new Circle(x, y, size, color);
    }

    public void updatePosition() {
        // Update velocity based on accumulated forces
        velocityX += forceX;
        velocityY += forceY;
        
        // Update position based on velocity
        x += velocityX;
        y += velocityY;

        // Reset forces after each update
        forceX = 0;
        forceY = 0;

        // Update visual position
        view.setCenterX(x);
        view.setCenterY(y);
    }

    public void addForce(double fx, double fy) {
        this.forceX += fx;
        this.forceY += fy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Circle getView() {
        return view;
    }
}

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FieldPoint {
    private double x, y;
    private double strength; // Positive for attraction, negative for repulsion
    private Circle view; // Visual representation of the field point

    public FieldPoint(double x, double y, double strength) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.view = new Circle(x, y, 10, Color.GREEN); // Displayed as a green circle with radius 10
        setupDragging(); // Initialize dragging behavior
    }

    // Method to calculate force exerted by the field point on a particle
    public void applyForce(Particle particle) {
        double dx = x - particle.getX();
        double dy = y - particle.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 1) {
            double force = strength / (distance * distance); // Inverse-square law
            particle.addForce(force * dx / distance, force * dy / distance);
        }
    }

    public Circle getView() {
        return view;
    }

    // Method to set up dragging functionality
    private void setupDragging() {
        view.setOnMousePressed(event -> {
            view.setUserData(new double[]{event.getX() - x, event.getY() - y});
        });
        
        view.setOnMouseDragged(event -> {
            double[] offset = (double[]) view.getUserData();
            setPosition(event.getX() - offset[0], event.getY() - offset[1]);
        });
    }

    // Update position of the field point and its visual representation
    public void setPosition(double newX, double newY) {
        this.x = newX;
        this.y = newY;
        view.setCenterX(x);
        view.setCenterY(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Update strength of the field point
    public void setStrength(double newStrength) {
        this.strength = newStrength;
    }
}

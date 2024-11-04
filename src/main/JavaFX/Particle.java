import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle {
    private double x, y;
    private double velocityX, velocityY;
    private Circle view;

    public Particle(double x, double y, double velocityX, double velocityY, double size, Color color) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.view = new Circle(size, color);
        this.view.setCenterX(x);
        this.view.setCenterY(y);
    }

    public void updatePosition() {
        x += velocityX;
        y += velocityY;
        view.setCenterX(x);
        view.setCenterY(y);
    }

    public Circle getView() {
        return view;
    }
}
    

package custom.Pack.FieldPoint;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Vector;

/**
 * Represents a FieldPoint in the simulation with graphical representation in JavaFX.
 * A FieldPoint has a position, a field strength, and can affect particles nearby.
 */
public class FieldPoint {
    // Attributes
    private Vector<Float> position;  // Position vector, assumed to be 2D
    private float fieldStrength;     // Strength of the field (positive/negative for attraction/repulsion)
    private String type;             // Type of field, e.g., "attraction" or "repulsion"

    private Circle visualRepresentation; // JavaFX graphical representation

    // Constructor
    public FieldPoint(Vector<Float> position, float fieldStrength, String type) {
        this.position = position;
        this.fieldStrength = fieldStrength;
        this.type = type;

        // Initialize the graphical representation
        this.visualRepresentation = new Circle(position.get(0), position.get(1), 10);

        // Set the color of the Circle based on the type of field
        if (type.equalsIgnoreCase("attraction")) {
            visualRepresentation.setFill(Color.BLUE); // Blue for attraction
        } else if (type.equalsIgnoreCase("repulsion")) {
            visualRepresentation.setFill(Color.RED);  // Red for repulsion
        } else {
            visualRepresentation.setFill(Color.GRAY); // Default for unknown types
        }
    }

    // Getter for JavaFX graphical representation
    public Circle getVisualRepresentation() {
        return visualRepresentation;
    }

    // Getter and Setter methods
    public Vector<Float> getPosition() {
        return position;
    }

    public void setPosition(Vector<Float> position) {
        this.position = position;

        // Update the graphical representation's position in JavaFX
        visualRepresentation.setCenterX(position.get(0));
        visualRepresentation.setCenterY(position.get(1));
    }

    public float getFieldStrength() {
        return fieldStrength;
    }

    public void setFieldStrength(float fieldStrength) {
        this.fieldStrength = fieldStrength;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;

        // Update the color of the graphical representation based on the new type
        if (type.equalsIgnoreCase("attraction")) {
            visualRepresentation.setFill(Color.BLUE);
        } else if (type.equalsIgnoreCase("repulsion")) {
            visualRepresentation.setFill(Color.RED);
        } else {
            visualRepresentation.setFill(Color.GRAY);
        }
    }

    /**
     * Calculates the force exerted by this FieldPoint on a particle based on distance.
     * (Placeholder for actual force calculation logic)
     * @param particlePosition Position vector of the particle
     * @return Force vector applied to the particle
     */
    public Vector<Float> calculateForce(Vector<Float> particlePosition) {
        // Placeholder logic for force calculation
        Vector<Float> force = new Vector<>();
        force.add(0.0f); // Replace with actual logic
        force.add(0.0f); // Replace with actual logic
        return force;
    }

    @Override
    public String toString() {
        return "FieldPoint{" +
                "position=" + position +
                ", fieldStrength=" + fieldStrength +
                ", type='" + type + '\'' +
                '}';
    }
}

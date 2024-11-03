package Pack.FieldPoint;

import java.util.Vector;

/**
 * Represents a FieldPoint in the simulation.
 * A FieldPoint has a position, a field strength, and can affect particles nearby.
 */
public class FieldPoint {
    // Attributes
    private Vector<Float> position; // Position vector, assumed to be 2D or 3D
    private float fieldStrength; // Strength of the field (could be positive or negative for attraction/repulsion)
    private String type; // Type of field, e.g., "attraction" or "repulsion"

    // Constructor
    public FieldPoint(Vector<Float> position, float fieldStrength, String type) {
        this.position = position;
        this.fieldStrength = fieldStrength;
        this.type = type;
    }

    // Getter and Setter methods
    public Vector<Float> getPosition() {
        return position;
    }

    public void setPosition(Vector<Float> position) {
        this.position = position;
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
    }

    /**
     * Calculates the force exerted by this FieldPoint on a particle based on distance.
     * @param particlePosition Position vector of the particle
     * @return Force vector applied to the particle
     */

    @Override
    public String toString() {
        return "FieldPoint{" +
                "position=" + position +
                ", fieldStrength=" + fieldStrength +
                ", type='" + type + '\'' +
                '}';
    }
}

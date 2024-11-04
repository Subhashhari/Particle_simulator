package Pack.Particle;

import java.util.Vector;

/**
 * Represents a Particle in the simulation with properties such as mass, charge, velocity, etc.
 * This class is part of the simulation package and is intended to interact with the C++ backend via JNI.
 */
public class Particle {
    // Attributes
    private float mass;
    private float charge;
    private Vector<Float> velocity; // Assuming 2D or 3D velocity vector
    private Vector<Float> position; // Position vector in space
    private Vector<Float> force;
    private float size;
    private int lifespan;

    // JavaFX-specific attributes
    private String color; // Color is managed on the Java side
    private boolean hasTrail;

    // Static block to load the native library
    static {
        try {
            System.loadLibrary("ParticleSystem"); // Loads libParticleSystem.so on Unix or ParticleSystem.dll on Windows
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native library failed to load: " + e);
            System.exit(1);
        }
    }

    // Constructor
    public Particle(float mass, float charge, Vector<Float> velocity, Vector<Float> position, Vector<Float> force, float size, int lifespan, String color, boolean hasTrail) {
        this.mass = mass;
        this.charge = charge;
        this.velocity = velocity;
        this.position = position;
        this.force = force;
        this.size = size;
        this.lifespan = lifespan;
        this.color = color;
        this.hasTrail = hasTrail;
    }

    // Getter and Setter methods
    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public Vector<Float> getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector<Float> velocity) {
        this.velocity = velocity;
    }

    public Vector<Float> getPosition() {
        return position;
    }

    public void setPosition(Vector<Float> position) {
        this.position = position;
    }

    public Vector<Float> getForce() {
        return force;
    }

    public void setForce(Vector<Float> force) {
        this.force = force;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean hasTrail() {
        return hasTrail;
    }

    public void setTrail(boolean hasTrail) {
        this.hasTrail = hasTrail;
    }
    
    //public native void calcForce(); // To be implemented in C++ using JNI
    public native void update(); // To be implemented in C++ using JNI

    @Override
    public String toString() {
        return "Particle{" +
                "mass=" + mass +
                ", charge=" + charge +
                ", velocity=" + velocity +
                ", position=" + position +
                ", force=" + force +
                ", size=" + size +
                ", lifespan=" + lifespan +
                ", color='" + color + '\'' +
                ", hasTrail=" + hasTrail +
                '}';
    }
}

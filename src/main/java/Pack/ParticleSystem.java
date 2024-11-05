package Pack;
import java.util.List;
import java.util.Vector;

import Pack.FieldPoint.FieldPoint;
import Pack.Particle.Particle;

public class ParticleSystem
{
    static {
        try {
            System.loadLibrary("ParticleSystem"); // Loads libParticleSystem.so on Unix or ParticleSystem.dll on Windows
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native library failed to load: " + e);
            System.exit(1);
        }
    }

    private Vector<Particle> particles;
    private Vector<FieldPoint> fieldPoints;
    public ParticleSystem()
    {
        particles = new Vector<>();
        fieldPoints = new Vector<>();
        addParticle();
        addFieldPoint();
        setForces();
        update();
        display();
        setForces();
        update();
        display();
    }
    public Vector<Particle> getParticles()
    {
        return particles;
    }
    public Vector<FieldPoint>getFieldPoints()
    {
        return fieldPoints;
    }
    private void addParticle()
    {
        particles.add(new Particle(1.0f, 1.0f, new Vector<>(List.of(0.5f, 0.5f)), new Vector<>(List.of(1.0f, 1.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        particles.add(new Particle(2.0f, 1.0f, new Vector<>(List.of(1.5f, 0.5f)), new Vector<>(List.of(1.0f, 5.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        particles.add(new Particle(3.0f, 1.0f, new Vector<>(List.of(5.5f, 0.5f)), new Vector<>(List.of(2.0f, 1.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));

    }    
    private void addFieldPoint()
    {
        fieldPoints.add(new FieldPoint(new Vector<>(List.of(0.0f, 0.0f)), 1.0f, "A"));
        fieldPoints.add(new FieldPoint(new Vector<>(List.of(5.0f, 0.0f)), 1.0f, "A"));
        fieldPoints.add(new FieldPoint(new Vector<>(List.of(0.0f, 5.0f)), 1.0f, "B"));
    }
    private void display()
    {
        for(int i=0;i<particles.size();i++)
        {
            System.out.println(particles.get(i).toString());
        }
    }
    private void update()
    {
        for(int i=0;i<particles.size();i++)
        {
            particles.get(i).update();
        }
        // System.out.println("updating");
    }
    public native void setForces();
}


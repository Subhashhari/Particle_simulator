package Pack;

import java.util.List;
import java.util.Vector;

import Pack.Emitter.Emitter;
import Pack.Emitter.OscillatingEmitter;
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
    private Vector<Emitter> emitters;
    private Emitter oe;
    public ParticleSystem()
    {
        particles = new Vector<>();
        fieldPoints = new Vector<>();
        emitters= new Vector<>();
        oe = new OscillatingEmitter(new Vector<>(List.of(2.5f, 2.5f)), 1f, (float)Math.PI/4, 0.0f, 0.5f, 0.01f, this);
/*         addParticle();
        addFieldPoint();
        setForces();
        display();*/
        // for(int i = 0; i < 100 ; i++) 
        // {
        //     oe.emitParticles();
        //     display();
        //     oe.updateEmitter();
        // }

    }
    public Vector<Particle> getParticles()
    {
        return particles;
    }
    public Vector<FieldPoint>getFieldPoints()
    {
        return fieldPoints;
    }
    public void addParticle(float mass, float charge, Vector<Float> velocity, Vector<Float> position, Vector<Float> force, float size, int lifespan, String color, boolean hasTrai)
    {
        particles.add(new Particle(mass,charge,velocity,position,force,size,lifespan,color,hasTrai));
        // particles.add(new Particle(2.0f, 1.0f, new Vector<>(List.of(1.5f, 0.5f)), new Vector<>(List.of(1.0f, 5.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        // particles.add(new Particle(3.0f, 1.0f, new Vector<>(List.of(5.5f, 0.5f)), new Vector<>(List.of(2.0f, 1.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));

    }   
    public void addParticles(Vector<Float> position, float[][] velocities)
    {
        for(float[] v : velocities)
        {
            particles.add(new Particle(1.0f, 1.0f, new Vector<>(List.of(v[0], v[1])), position, new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        }
    }
    public void addFieldPoint(Vector<Float> position, float fieldStrength, String type)
    {
        fieldPoints.add(new FieldPoint(position, fieldStrength, type));
        // fieldPoints.add(new FieldPoint(new Vector<>(List.of(5.0f, 0.0f)), 1.0f, "A"));
        // fieldPoints.add(new FieldPoint(new Vector<>(List.of(0.0f, 5.0f)), 1.0f, "B"));
    }
    public void display()
    {
        for(int i=0;i<particles.size();i++)
        {
            System.out.println(particles.get(i).toString());
        }
    }

    public void updateParticlesPosition()
    {
        for(int i=0;i<particles.size();i++)
        {
            particles.get(i).update();
        }
    }

    public Emitter getOscillatingEmitter(){
	    return oe;
    }

    public void setOscillatingEmitter(Emitter emitter){
	    this.oe=emitter;
    }
    


    public native void setForces();
}

package Pack;

import java.util.List;
import java.util.Vector;

import Pack.Emitter.Emitter;
import Pack.Emitter.OscillatingEmitter;
import Pack.Emitter.PulseEmitter;
import Pack.FieldPoint.FieldPoint;
import Pack.Particle.Particle;
import javafx.scene.paint.Color;

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
    private int gravityEnabled;
    private double friction;
    private int maxParticles;

    public ParticleSystem()
    {
        particles = new Vector<>();
        fieldPoints = new Vector<>();
        emitters= new Vector<>();
        gravityEnabled = 0;
    }
    public Vector<Particle> getParticles()
    {
        return particles;
    }
    public Vector<FieldPoint>getFieldPoints()
    {
        return fieldPoints;
    }
    public Vector<Emitter> getEmitters()
    {
        return emitters;
    }
    public double getFriction()
    {
        return friction;
    }

    public int getMaxParticles()
    {
        return maxParticles;
    }

    public void setFriction(double friction)
    {
        this.friction = friction;
    }

    public void setMaxParticles(int maxParticles)
    {
        this.maxParticles = maxParticles;
    }

    public void addParticle(float mass, float charge, Vector<Float> velocity, Vector<Float> position, Vector<Float> force, float size, int lifespan, String color, boolean hasTrai)
    {
        particles.add(new Particle(mass,charge,velocity,position,force,size,lifespan,color,hasTrai));
        // particles.add(new Particle(2.0f, 1.0f, new Vector<>(List.of(1.5f, 0.5f)), new Vector<>(List.of(1.0f, 5.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        // particles.add(new Particle(3.0f, 1.0f, new Vector<>(List.of(5.5f, 0.5f)), new Vector<>(List.of(2.0f, 1.0f)), new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));

    }   
    public void addParticles(float particlesMass, Vector<Float> position, float[][] velocities)
    {
        for(float[] v : velocities)
        {
            particles.add(new Particle(particlesMass, 1.0f, new Vector<>(List.of(v[0], v[1])), position, new Vector<>(List.of(0.0f, 0.0f)),5.0f, 100, "red", true));
        }
    }
    public void addFieldPoint(Vector<Float> position, float fieldStrength, String type)
    {
        fieldPoints.add(new FieldPoint(position, fieldStrength, type));
        // fieldPoints.add(new FieldPoint(new Vector<>(List.of(5.0f, 0.0f)), 1.0f, "A"));
        // fieldPoints.add(new FieldPoint(new Vector<>(List.of(0.0f, 5.0f)), 1.0f, "B"));
    }
    public void addEmitter(Vector<Float> position, float speed, float spread, float angle, float particlesMass)
    {
        emitters.add(new Emitter(position, speed, spread, angle, particlesMass, this));
    }
    public void addOscillatingEmitter(Vector<Float> position, float speed, float spread, float angle, float particlesMass, float amplitude, float frequency)
    {
        emitters.add(new OscillatingEmitter(position, speed, spread, angle, particlesMass, amplitude, frequency, this));
    }
    public void addPulseEmitter(Vector<Float> position, float speed, float spread, float angle, float particlesMass, float pulseWidth, float pulseSpeed)
    {
        emitters.add(new PulseEmitter(position, speed, spread, angle, particlesMass, this));
    }
    public void removeParticlesOutOfScreen(int width, int height)
    {
        for(int i=0 ; i<particles.size() ; i++)
        {
            if(particles.get(i).getPosition().get(0)<0 || particles.get(i).getPosition().get(0)>width || particles.get(i).getPosition().get(1)<0 || particles.get(i).getPosition().get(1)>height)
            {
                particles.remove(i);
            }
        }
    }
    public void display()
    {
        for(int i=0;i<particles.size();i++)
        {
            System.out.println(particles.get(i).toString());
        }
    }

    // public void updateParticlesPosition()
    // {
    //     for(int i=0; i<emitters.size(); i++)
    //     {
    //         emitters.get(i).emitParticles();
    //         if(emitters.get(i) instanceof OscillatingEmitter)
    //             emitters.get(i).updateEmitter();
    //     }
    //     for(int i=0;i<particles.size();i++)
    //     {
    //         particles.get(i).update();
    //     }
    // }

    // public Emitter getOscillatingEmitter(){
	//     return oe;
    // }

    // public void setOscillatingEmitter(Emitter emitter){
	//     this.oe=emitter;
    // }

    public int isGravityEnabled()
    {
        return gravityEnabled;
    }

    public void setGravityEnabled(int gravityEnabled)
    {
        this.gravityEnabled = gravityEnabled;
    }    

    public native void setForces();

    public void updateAll()
    {
        //System.out.println(particles.size());
        for(int i=0; i<emitters.size(); i++)
        {
            emitters.get(i).emitParticles();
            if(emitters.get(i) instanceof OscillatingEmitter)
                emitters.get(i).updateEmitter();
        }
//        setForces();
        for(int i=0;i<particles.size();i++)
        {
            particles.get(i).update();
        }
    }
}
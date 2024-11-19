package Pack.Emitter;
import java.util.Vector;
import Pack.ParticleSystem;

public class Emitter
{
    private Vector<Float> position;
    private int emissionRate;
    private float spread;
    private float angle;
    private float speed;
    private boolean isEmitting;
    private ParticleSystem system;

    public Emitter(Vector<Float> position, float speed, float spread, float angle, ParticleSystem ps)
    {
        this.position = position;
        this.speed = speed;
        this.spread = spread;
        this.angle = angle;
        this.isEmitting = false;
        this.system = ps;
    }
    
    public Vector<Float> getPosition() {
        return position;
    }
    
    public void setPosition(Vector<Float> position) {
        this.position = position;
    }
    
    public int getEmissionRate() {
        return emissionRate;
    }
    
    public void setEmissionRate(int emissionRate) {
        this.emissionRate = emissionRate;
    }
    
    public float getSpread() {
        return spread;
    }
    
    public void setSpread(float spread) {
        this.spread = spread;
    }
    
    public float getAngle() {
        return angle;
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isEmitting() {
        return isEmitting;
    }

    public native float[][] getVelocities();

    public void emitParticles() 
    {
        this.isEmitting = true;
        float[][] velocities = getVelocities();
        system.addParticles(position, velocities);
    }

    public native void updateEmitter();
    
}
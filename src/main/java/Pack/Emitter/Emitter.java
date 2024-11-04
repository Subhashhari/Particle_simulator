import java.util.vector;

public class Emitter
{
    private Vector<Float> position;
    private int emissionRate;
    private float spread;
    private float angle;
    private boolean isEmitting;

    public Emitter(Vector<Float> position, int emissionRate, float spread, float angle)
    {
        this.position = position;
        this.emissionRate = emissionRate;
        this.spread = spread;
        this.angle = angle;
        this.isEmitting = false;
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

    public boolean isEmitting() {
        return isEmitting;
    }

    public void emitParticles() {
        this.isEmitting = true;
    }
    

}
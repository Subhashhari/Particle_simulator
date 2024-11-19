package Pack.Emitter;
import java.util.*;
import Pack.Emitter.Emitter;
import Pack.ParticleSystem;
public class OscillatingEmitter extends Emitter
{
    private float amplitude;
    private float frequency;
    private Vector<Float> position;
    private float theta;

    public OscillatingEmitter(Vector<Float> position, float speed, float spread, float angle, float amplitude, float frequency, ParticleSystem ps)
    {
        super(position, speed, spread, angle, ps);
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.theta = 0;
    }

    public float getAmplitude()
    {
        return amplitude;
    }

    public void setAmplitude(float amplitude)
    {
        this.amplitude = amplitude;
    }

    public float getFrequency()
    {
        return frequency;
    }

    public void setFrequency(float frequency)
    {
        this.frequency = frequency;
    }


    public float getTheta()
    {
        return this.theta;
    }

    public void setTheta(float theta)
    {
        this.theta = theta;
    }

    public native void updateEmitter();
}
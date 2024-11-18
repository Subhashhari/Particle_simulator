package Pack.Emitter;
import java.util.*;
class OscillatingEmitter extends Emitter
{
    private float amplitude;
    private float frequency;
    private Vector<float> position;
    private float theta;

    public OscillatingEmitter(Vector<Float> position, int emissionRate, float spread, float angle, float amplitude, float frequency, float mv)
    {
        super(position, emissionRate, spread, angle);
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.maxVelocity = mv;
        this.theta = 0;
    }

    public void getAmplitude()
    {
        return amplitude;
    }

    public void setAmplitude(float amplitude)
    {
        this.amplitude = amplitude;
    }

    public void getFrequency()
    {
        return frequency;
    }

    public void setFrequency(float frequency)
    {
        this.frequency = frequency;
    }


    public void getTheta()
    {
        retturn this.theta;
    }

    public vois setTheta(float theta)
    {
        this.theta = theta;
    }

    public native void updateEmitter();
}
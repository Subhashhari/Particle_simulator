class OscillatingEmitter extends Emitter
{
    private float amplitude;
    private float frequency;

    public OscillatingEmitter(Vector<Float> position, int emissionRate, float spread, float angle, float amplitude, float frequency)
    {
        super(position, emissionRate, spread, angle);
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public getAmplitude()
    {
        return amplitude;
    }

    public setAmplitude(float amplitude)
    {
        this.amplitude = amplitude;
    }

    public getFrequency()
    {
        return frequency;
    }

    public setFrequency(float frequency)
    {
        this.frequency = frequency;
    }
}
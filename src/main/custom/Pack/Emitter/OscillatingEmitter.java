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

    public OscillatingEmitter(Vector<Float> position, float speed, float spread, float angle, float particlesMass, float amplitude, float frequency, ParticleSystem ps)
    {
        super(position, speed, spread, angle, particlesMass, ps);
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
    
//     @Override
// public String toString() {
//     return "OscillatingEmitter{" +
//         "position=" + getPosition() +
//         ", emissionRate=" + getEmissionRate() +
//         ", spread=" + getSpread() +
//         ", angle=" + getAngle() +
//         ", speed=" + getSpeed() +
//         ", isEmitting=" + isEmitting() +
//         ", amplitude=" + amplitude +
//         ", frequency=" + frequency +
//         ", theta=" + theta +
//         '}';
// }
// public static OscillatingEmitter parse(String line, ParticleSystem ps) {
//     try {
//         // Remove class name and braces
//         line = line.replace("OscillatingEmitter{", "").replace("}", "");

//         // Split attributes by ", "
//         String[] parts = line.split(", (?=[a-zA-Z]+)");

//         // Parse position vector
//         Vector<Float> position = parseVector(parts[0].split("=")[1]);

//         // Parse individual attributes
//         int emissionRate = Integer.parseInt(parts[1].split("=")[1]);
//         float spread = Float.parseFloat(parts[2].split("=")[1]);
//         float angle = Float.parseFloat(parts[3].split("=")[1]);
//         float speed = Float.parseFloat(parts[4].split("=")[1]);
//         boolean isEmitting = Boolean.parseBoolean(parts[5].split("=")[1]);
//         float amplitude = Float.parseFloat(parts[6].split("=")[1]);
//         float frequency = Float.parseFloat(parts[7].split("=")[1]);
//         float theta = Float.parseFloat(parts[8].split("=")[1]);

//         // Create OscillatingEmitter instance
//         OscillatingEmitter emitter = new OscillatingEmitter(position, speed, spread, angle, amplitude, frequency, ps);

//         // Set additional properties
//         emitter.setEmissionRate(emissionRate);
//         emitter.setTheta(theta);
//         emitter.setIsEmitting(isEmitting);

//         return emitter;
//     } catch (Exception e) {
//         System.err.println("Error parsing OscillatingEmitter: " + e.getMessage());
//         e.printStackTrace();
//         return null;
//     }
// }

// Helper function for parsing a vector
// private static Vector<Float> parseVector(String vectorString) {
//     vectorString = vectorString.replace("[", "").replace("]", "");
//     Vector<Float> vector = new Vector<>();
//     for (String value : vectorString.split(", ")) {
//         vector.add(Float.parseFloat(value));
//     }
//     return vector;
// }



    public native void updateEmitter();
}

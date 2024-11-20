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
    private float particlesMass;
    private boolean isEmitting;
    private ParticleSystem system;

    public Emitter(Vector<Float> position, float speed, float spread, float angle, float paticlesMass, ParticleSystem ps)
    {
        this.position = position;
        this.speed = speed;
        this.spread = spread;
        this.angle = angle;
        this.isEmitting = false;
        this.particlesMass = particlesMass;
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
    
    public void setIsEmitting(boolean isEmitting) {
        this.isEmitting = isEmitting;
    }


    public native float[][] getVelocities();

    public void emitParticles() 
    {
        this.isEmitting = true;
        float[][] velocities = getVelocities();
        system.addParticles(particlesMass, position, velocities);
    }
    
    @Override
    public String toString() {
	    return "Emitter{" +
		    "position=" + position.toString() +
		    ", emissionRate=" + emissionRate +
		    ", spread=" + spread +
		    ", angle=" + angle +
		    ", speed=" + speed +
		    ", isEmitting=" + isEmitting +
		    '}';
   }
   public static Emitter parse(String line, ParticleSystem ps) {
    // Example input: Emitter{position=[2.5, 3.5], emissionRate=50, spread=1.5, angle=45.0, speed=10.0, isEmitting=true}
	    try {
		line = line.replace("Emitter{", "").replace("}", "");
		String[] parts = line.split(", ");
		Vector<Float> position = new Vector<>();
		String[] posValues = parts[0].split("=")[1].replace("[", "").replace("]", "").split(", ");
		for (String pos : posValues) {
		    position.add(Float.parseFloat(pos));
		}
		int emissionRate = Integer.parseInt(parts[1].split("=")[1]);
		float spread = Float.parseFloat(parts[2].split("=")[1]);
		float angle = Float.parseFloat(parts[3].split("=")[1]);
		float speed = Float.parseFloat(parts[4].split("=")[1]);
		boolean isEmitting = Boolean.parseBoolean(parts[5].split("=")[1]);

		Emitter emitter = new Emitter(position, speed, spread, angle, ps);
		emitter.setEmissionRate(emissionRate);
		emitter.isEmitting = isEmitting; // Direct field access since it's private
		return emitter;
	    } catch (Exception e) {
		e.printStackTrace();
		return null;
	    }
	}


    public native void updateEmitter();
    
}

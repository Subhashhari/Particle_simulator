package Pack.Emitter;

import java.util.*;
import Pack.Emitter.Emitter;
import Pack.ParticleSystem;

public class PulseEmitter extends Emitter {

    static {
        System.loadLibrary("ParticleSystem"); // Load native library
    }

    private long lastUpdateTime = 0; // Instance-specific timer for toggling emission
    private boolean shouldEmit = true; // Tracks whether this instance should emit particles

    public PulseEmitter(Vector<Float> position, float speed, float spread, float angle, float particlesMass, ParticleSystem ps) {
        super(position, speed, spread, angle, particlesMass, ps);
    }

    @Override
    public void emitParticles() {
        long currentTime = System.currentTimeMillis();

        // Toggle emitting state every 0.5 seconds (500 milliseconds)
        if (currentTime - lastUpdateTime >= 500) {
            shouldEmit = !shouldEmit; // Switch between emitting and not emitting
            lastUpdateTime = currentTime;
        }

        if (shouldEmit) {
            float[][] velocities = getVelocities(); // Fetch new velocities
            system.addParticles(particlesMass, position, velocities); // Add the particles to the system
        }
    }

    // @Override
    // public native float[][] getVelocities(); // Native method to fetch velocities
}

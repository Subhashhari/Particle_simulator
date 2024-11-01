package Pack;
import java.util.List;
import java.util.Vector;

import Pack.Particle.Particle;

public class ParticleSystem
{
    private Vector<Particle> particles;
    public ParticleSystem()
    {
        particles = new Vector<>();
        particles.add(new Particle(1.0f, 1.0f, new Vector<>(List.of(0.5f, 0.5f)), new Vector<>(List.of(1.0f, 1.0f)), 5.0f, 100, "red", true));
        particles.add(new Particle(2.0f, 1.0f, new Vector<>(List.of(0.5f, 0.5f)), new Vector<>(List.of(1.0f, 1.0f)), 5.0f, 100, "red", true));
        particles.add(new Particle(3.0f, 1.0f, new Vector<>(List.of(0.5f, 0.5f)), new Vector<>(List.of(1.0f, 1.0f)), 5.0f, 100, "red", true));
    }
    // private void addParticle(float mass)
    // {
        
    // }    
    public void runSimulation()
    {
        for (Particle p : particles)
        {
            p.update();
        }
    }
}


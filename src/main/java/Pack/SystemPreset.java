package Pack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.List;

import Pack.FieldPoint.FieldPoint;
import Pack.Particle.Particle;
import Pack.Emitter.OscillatingEmitter;

public class SystemPreset {

    private ParticleSystem particleSystem;

    public SystemPreset(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public void savePreset(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Save particles
            writer.write("Particles\n");
            for (Particle particle : particleSystem.getParticles()) {
                writer.write(particle.toString() + "\n");
            }

            // Save field points
            writer.write("FieldPoints\n");
            for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
                writer.write(fieldPoint.toString() + "\n");
            }

            // Save emitter details
            writer.write("Emitter\n");
            if (particleSystem.getOscillatingEmitter() instanceof OscillatingEmitter) {
                OscillatingEmitter oscillatingEmitter = (OscillatingEmitter) particleSystem.getOscillatingEmitter();
                writer.write(oscillatingEmitter.toString() + "\n");
            }

            System.out.println("Preset saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPreset(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        String section = null;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Particles")) {
                section = "Particles";
            } else if (line.equals("FieldPoints")) {
                section = "FieldPoints";
            } else if (line.equals("Emitter")) {
                section = "Emitter";
            } else {
                if (section.equals("Particles")) {
                    Particle particle = Particle.parse(line);
                    if (particle != null) particleSystem.getParticles().add(particle);
                } else if (section.equals("FieldPoints")) {
                    FieldPoint fieldPoint = FieldPoint.parse(line);
                    if (fieldPoint != null) particleSystem.getFieldPoints().add(fieldPoint);
                } else if (section.equals("Emitter")) {
                    OscillatingEmitter emitter = OscillatingEmitter.parse(line, particleSystem);
                    if (emitter != null) particleSystem.setOscillatingEmitter(emitter);
                }
            }
        }
        System.out.println("Preset loaded from " + filename);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void preset1() {
        ParticleSystem particleSystem = new ParticleSystem() {
            @Override
            public void addFieldPoint() {
                // Adding a repulsive field point at position (2.5, 2.5) with maximum strength
                getFieldPoints().add(new FieldPoint(new Vector<>(List.of(2.5f, 2.5f)), -10.0f, "Repulsive"));
            }

            @Override
            public void addParticles(Vector<Float> position, float[][] velocities) {
                // Adding particles emitted with medium velocity
                super.addParticles(position, velocities);
            }

            @Override
            public void display() {
                System.out.println("Particle System State:");
                super.display();
            }
        };

        // Customize emitter to emit particles with medium velocity
        OscillatingEmitter oe = new OscillatingEmitter(
                new Vector<>(List.of(1.0f, 1.0f)), // Emitter's position
                1.0f,                             // Emission rate
                (float) Math.PI / 6,              // Emission angle
                0.0f,                             // Initial angle offset
                2.0f,                             // Medium velocity
                0.01f,                            // Small angular velocity
                particleSystem                    // Reference to the particle system
        );

        particleSystem.setOscillatingEmitter(oe);

        // Adding field point and particles
        particleSystem.addFieldPoint();
        particleSystem.addParticles(new Vector<>(List.of(1.0f, 1.0f)), new float[][]{
                {1.0f, 1.5f},
                {2.0f, 1.0f},
                {1.5f, 1.5f}
        });

        // Run the simulation
        for (int i = 0; i < 100; i++) {
            particleSystem.getOscillatingEmitter().emitParticles();
            particleSystem.display();
            particleSystem.getOscillatingEmitter().updateEmitter();
        }
    }
}


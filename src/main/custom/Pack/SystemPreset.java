package Pack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import Pack.FieldPoint.FieldPoint;
import Pack.Particle.Particle;
import Pack.Emitter.Emitter;
import Pack.Emitter.OscillatingEmitter;

public class SystemPreset {

    private ParticleSystem particleSystem;

    public SystemPreset(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public void savePreset(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Save particles
            // writer.write("Particles\n");
            // for (Particle particle : particleSystem.getParticles()) {
            //     writer.write(particle.toString() + "\n");
            // }

            // Save field points
            writer.write("FieldPoints\n");
            for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
                writer.write(fieldPoint.toString() + "\n");
            }

            // Save emitters
            writer.write("Emitters\n");
            for (Emitter emitter : particleSystem.getEmitters()) {
                writer.write(emitter.toString() + "\n");
            }

            // Save system settings
            writer.write("Settings\n");
            writer.write("GravityEnabled=" + particleSystem.isGravityEnabled() + "\n");
            writer.write("Friction=" + particleSystem.getFriction() + "\n");
            writer.write("MaxParticles=" + particleSystem.getMaxParticles() + "\n");

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
                } else if (line.equals("Emitters")) {
                    section = "Emitters";
                } else if (line.equals("Settings")) {
                    section = "Settings";
                } else {
                    switch (section) {
                        case "Particles":
                            Particle particle = Particle.parse(line);
                            if (particle != null) particleSystem.getParticles().add(particle);
                            break;
                        case "FieldPoints":
                            FieldPoint fieldPoint = FieldPoint.parse(line);
                            if (fieldPoint != null) particleSystem.getFieldPoints().add(fieldPoint);
                            break;
                        case "Emitters":
                            Emitter emitter = OscillatingEmitter.parse(line, particleSystem); // Adjust for other emitter types if needed
                            if (emitter != null) particleSystem.getEmitters().add(emitter);
                            break;
                        case "Settings":
                            if (line.startsWith("GravityEnabled=")) {
                                particleSystem.setGravityEnabled(Integer.parseInt(line.split("=")[1]));
                            } else if (line.startsWith("Friction=")) {
                                particleSystem.setFriction(Double.parseDouble(line.split("=")[1]));
                            } else if (line.startsWith("MaxParticles=")) {
                                particleSystem.setMaxParticles(Integer.parseInt(line.split("=")[1]));
                            }
                            break;
                    }
                }
            }
            System.out.println("Preset loaded from " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

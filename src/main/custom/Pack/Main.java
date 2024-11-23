package Pack;

public class Main {
    public static void main(String[] args) {
        System.out.println("Particle System Preset Management");

        // Create a ParticleSystem instance
        ParticleSystem ps = new ParticleSystem();

        // Create a SystemPreset instance linked to the ParticleSystem
        SystemPreset preset = new SystemPreset(ps);

        // Generate preset1 (or any other presets you have defined)
        preset.preset1();

        // Define the file to save the preset
        String filename = "preset.txt"; // File name for the preset

        // Save the preset to a file
        preset.savePreset(filename);
        System.out.println("Preset saved to " + filename);

        // Clear the ParticleSystem to test loading functionality
        ps.clear();

        // Load the preset from the file
        preset.loadPreset(filename);
        System.out.println("Preset loaded from " + filename);

        // Optionally display the loaded system (uncomment if you have a display method)
        // ps.display();
    }
}

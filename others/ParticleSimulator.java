public class ParticleSimulator {
    // Load the native library (shared library)
    static {
        System.loadLibrary("ParticleSystem");
    }

    // Declare the native method
    public native void runParticleSystem();

    public static void main(String[] args) {
        ParticleSimulator simulator = new ParticleSimulator();
        simulator.runParticleSystem(); // Call the native method
    }
}

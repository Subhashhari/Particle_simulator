package Pack;
import java.util.List;
import java.util.Vector;

public class ParticleSimulator
{
    private ParticleSystem ps;
    private Vector<Float> emitterStartPosition;
    private Vector<Float> fieldStartPosition;
    private float fieldStrength;
    //start simulation
    private ParticleSimulator()
    {
        this.ps = new ParticleSystem();
        emitterStartPosition = new Vector<>(List.of(200.0f, 200.0f));
        fieldStartPosition = new Vector<>(List.of(300.0f, 300.0f));
        fieldStrength=1.0f;
    }
    //add emitter-emitter type
    //drag eimtter
    //change spread,speed,angle
    //delete emitter

    //add fieldpoint-A
    private void addFieldPointA()
    {
        ps.addFieldPoint(fieldStartPosition, 1.0f, "A");
    }
    //add fieldpoint-B
    private void addFieldPointB()
    {
        ps.addFieldPoint(fieldStartPosition, fieldStrength, "B");
    }
    //change position
    
//change strength
//delete field

//pause simulation
//clear particles

//save preset
//load preset
}
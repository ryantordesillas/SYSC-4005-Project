import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.lang.Math;

public class Simulation{

    public static void main(String[] args) throws IOException {
        // There are 3 Components for the simulation
        Component cp1 = new Component("C1");
        Component cp2 = new Component("C2");
        Component cp3 = new Component("C3");

        // Each of the workstations will take a array of components
        Component[] cpBuffer1 = {cp1};

        // This buffer will be used for the second Inspector
        Component[] cpBuffer2 = {cp2,cp3};

        // There will be 3 Workstations
        // Workstation 1 only takes cp1
        Workstation work1 = new Workstation(0.2171827774, false, null);
        // Workstation 2 and 3 will only take two components
        Workstation work2 = new Workstation(0.09015013604, true, cp2);
        Workstation work3 = new Workstation(0.1136934688, true, cp3);

        // Workstation buffer for inspector 1
        Workstation[] workBuffer1 = {work1, work2, work3};
        Workstation[] workBuffer2 = {work2, work3};

        double[] lambdas1 = {0.09654457318};
        double[] lambdas2 = {0.06436288999,0.04846662112};
        // There will be 2 Inspectors
        Inspector ins1 = new Inspector(cpBuffer1,workBuffer1, lambdas1);
        Inspector ins2 = new Inspector(cpBuffer2, workBuffer2, lambdas2);

        work1.start();
        work2.start();
        work3.start();

        ins1.start();
        ins2.start();



    }
}

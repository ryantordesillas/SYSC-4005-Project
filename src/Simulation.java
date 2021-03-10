import java.io.File;
import java.io.IOException;

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

        // Create files for the workstations
        File ws1 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws1.dat");
        File ws2 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws2.dat");
        File ws3 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws3.dat");

        // There will be 3 Workstations
        // Workstation 1 only takes cp1
        Workstation work1 = new Workstation(ws1, false, null);
        // Workstation 2 and 3 will only take two components
        Workstation work2 = new Workstation(ws2, true, cp2);
        Workstation work3 = new Workstation(ws3, true, cp3);

        // Workstation buffer for inspector 1
        Workstation[] workBuffer1 = {work1, work2, work3};
        Workstation[] workBuffer2 = {work2, work3};

        File[] files1 = {new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp1.dat")};
        File[] files2 = {new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp22.dat"), new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp23.dat")};

        // There will be 2 Inspectors
        Inspector ins1 = new Inspector(cpBuffer1,workBuffer1,files1);
        Inspector ins2 = new Inspector(cpBuffer2, workBuffer2, files2);

        work1.start();
        work2.start();
        work3.start();

        ins1.start();
        ins2.start();

    }
}

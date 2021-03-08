import java.io.File;
import java.io.IOException;

public class Simulation {

    public static void main(String[] args) throws IOException {
        // There are 3 Components for the simulation
        Component cp1 = new Component("C1");
        Component cp2 = new Component("C2");
        Component cp3 = new Component("C3");

        // Each of the workstations will take a array of components
        Component[] cpBuffer1 = {cp1};

        Component[] cpBuffer2 = {cp1, cp2};

        Component[] cpBuffer3 = {cp1,cp3};

        // This buffer will be used for the second Inspector
        Component[] cpBuffer4 = {cp2,cp3};

        // Create files for the workstations
        File ws1 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws1.dat");
        File ws2 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws2.dat");
        File ws3 = new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\ws3.dat");

        // There will be 2 Workstations
        Workstation work1 = new Workstation(cpBuffer1,ws1);
        Workstation work2 = new Workstation(cpBuffer2,ws2);
        Workstation work3 = new Workstation(cpBuffer3,ws3);

        // Workstation buffer for inspector 1
        Workstation[] workBuffer1 = {work1, work2, work3};
        Workstation[] workBuffer2 = {work2, work3};

        File[] files1 = {new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp1.dat")};
        File[] files2 = {new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp22.dat"), new File("D:\\Winter 2021 Classes\\SYSC 4005\\Code\\servinsp23.dat")};

        // There will be 2 Inspectors
        Inspector ins1 = new Inspector(cpBuffer1,workBuffer1,files1);
        Inspector ins2 = new Inspector(cpBuffer4, workBuffer2, files2);

        ins1.inspectAndSend();
        ins2.inspectAndSend();
    }
}

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.util.Scanner;

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

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Input the amount of hours you would like to simulate (as an int): ");
        int hours = myObj.nextInt();
        double exec = hours * 3.6e9;

        //Add initialization time
        exec += 6 *3.6e9;

        // The statistics to keep track of times
        Statistic stats = new Statistic();

        // There will be 3 Workstations
        // Workstation 1 only takes cp1
        Workstation work1 = new Workstation(0.2171827774, false, null, stats, exec);
        // Workstation 2 and 3 will only take two components
        Workstation work2 = new Workstation(0.09015013604, true, cp2, stats, exec);
        Workstation work3 = new Workstation(0.1136934688, true, cp3, stats, exec);

        // Workstation buffer for inspector 1
        Workstation[] workBuffer1 = {work1, work2, work3};
        Workstation[] workBuffer2 = {work2, work3};



        double[] lambdas1 = {0.09654457318};
        double[] lambdas2 = {0.06436288999,0.04846662112};
        // There will be 2 Inspectors
        Inspector ins1 = new Inspector(cpBuffer1,workBuffer1, lambdas1, stats, exec);
        Inspector ins2 = new Inspector(cpBuffer2, workBuffer2, lambdas2, stats, exec);



        // Attach a array of inspectors attached to a workstation
        work1.addInspector(ins1);
        work2.addInspector(ins1);
        work3.addInspector(ins1);
        work2.addInspector(ins2);
        work3.addInspector(ins2);


        work1.start();
        work2.start();
        work3.start();

        ins1.start();
        ins2.start();
        stats.startSimTime();

        while(stats.elapsed_time() < exec){
            if(stats.elapsed_time() == 6*3.6e9){
                System.out.println("Init finished!");
                stats.reset();
            }
        }
        stats.endSimTime();
        stats.generateReport();
        double time = stats.getTotal_simulation_time()/3.6e9;
        time -= 6;
        System.out.println("Total Sim time: " + time + " theoretical hours\n\n");

        if (work1.isAlive()) {
            work1.stop();
        }
        if (work2.isAlive()) {
            work2.stop();
        }
        if (work3.isAlive()) {
            work3.stop();
        }

        if (ins1.isAlive()) {
            ins1.stop();
        }
        if (ins2.isAlive()) {
            ins2.stop();
        }


    }
}

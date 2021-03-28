import java.io.*;
import java.util.Random;
import java.lang.Math;

public class Inspector extends Thread {

    /** The statistic object to determine inspector's runtime */
    private  Statistic stats;

    /**
     * The components that this Inspector inspects
     */
    private Component[] inspectComponents;

    /**
     * The Workstations that this Inspector sends Components to
     */
    private Workstation[] attachedWorkstations;

    /**
     * lambda used to generate random numbers
     */
    private double[] lambdas;


    /**
     * The default constructor for the Inspector Object
     *
     * @param components   The components that this
     * @param workstations the workstations attached to the
     * @param lambdas      the lambdas that will be used in the random number generation
     */
    public Inspector(Component[] components, Workstation[] workstations, double[] lambdas, Statistic stats) {
        inspectComponents = components;
        attachedWorkstations = workstations;
        this.lambdas = lambdas;
        this.stats = stats;
    }

    /**
     * Function to make the thread wait while a component is being inspected
     * @param millis the number of milliseconds needed to wait
     * @param nano the number of nanoseconds need to inspect a component
     */
    public void inspectComponent(int millis, int nano){
        synchronized (this){
            try {
                //System.out.println("Waiting...");
                this.wait(millis,nano);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The Main function that the thread will run
     */
    public void run() {

        // if there is one file no need for random variables
        if (lambdas.length == 1) {
            stats.ins1_start();

            // Create a random number generator to generate doubles
            Random C1_rnd = new Random();

            // This will be used for Inspector 1
            int x = 0;
            while (!attachedWorkstations[0].isDone() || !attachedWorkstations[1].isDone() || !attachedWorkstations[2].isDone()) {
                //System.out.println(st);
                double time = (-1 / lambdas[0]) * Math.log(C1_rnd.nextDouble());

                int milli = (int) time;
                int nano = (int) ((time - milli) * 1000000);

                // Create a component now so we can track all of the times
                Component c = new Component("C1");
                c.setInspection_time(time);

                // Do the processing first before picking a available workstation
                inspectComponent(milli,nano);

                // We get the waiting time now because Inspector 1 will look for a buffer to send to
                double start = System.nanoTime();
                // Find the most available workstation
                Workstation availableWorkstation = findAvailableWorkstation();
                //System.out.println(availableWorkstation.isC1Full());

                if(!availableWorkstation.isDone()&& !availableWorkstation.isC1Full()) {

                    synchronized (this) {
                        while (availableWorkstation.isC1Full()) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    c.setDelay_time(System.nanoTime() - start);
                    c.setQueue_time(System.nanoTime());
                    availableWorkstation.add_Component(c);
                    x++;
                    //System.out.println("sent");
                }
            }
            stats.ins1_end();
            System.out.println("C1 Iterations: " + x);


        } else { // there are two components being inspected

            stats.ins2_start();
            // use a random number to determine which file to read from
            Random workstation_rand = new Random();



            // Use integers from 0-99
            int rnd = workstation_rand.nextInt(100);

            // Create a random generator for each component inspection time
            Random C2_rand = new Random();
            Random C3_rand = new Random();

            int x = 0;
            while (!attachedWorkstations[0].isDone() || !attachedWorkstations[1].isDone()) {
                // This will need to be tweaked as it will continue until both files are completely read
                if (rnd <= 49 && !attachedWorkstations[0].isDone()) { // this check will be skipped if the this workstation is done
                    double time = (-1 / lambdas[0]) * Math.log(C2_rand.nextDouble());
                    int milli = (int) time;
                    int nano = (int) ((time - milli) * 1000000);
                    Component c = new Component("C2");
                    c.setInspection_time(time);

                    inspectComponent(milli,nano);

                    synchronized (this) {
                        //System.out.println(attachedWorkstations[0].getBuffer().size());
                        while (attachedWorkstations[0].isBufferFull() && !attachedWorkstations[0].isDone()) {
                            try {
                                long startTime = System.nanoTime();
                                this.wait();
                                long endTime = System.nanoTime();
                                c.setDelay_time(endTime-startTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    c.setQueue_time(System.nanoTime());
                    attachedWorkstations[0].add_Component(c);
                    x++;

                } else {
                    if(!attachedWorkstations[1].isDone()) {
                        double time = (-1 / lambdas[1]) * Math.log(C3_rand.nextDouble());
                        int milli = (int) time;
                        int nano = (int) ((time - milli) * 1000000);
                        Component c = new Component("C3");
                        c.setInspection_time(time);

                        inspectComponent(milli,nano);
                        synchronized (this) {
                            while (attachedWorkstations[1].isBufferFull() && !attachedWorkstations[1].isDone()) {
                                try {
                                    long startTime = System.nanoTime();
                                    this.wait();
                                    long endTime = System.nanoTime();
                                    c.setDelay_time(endTime-startTime);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        c.setQueue_time(System.nanoTime());
                        attachedWorkstations[1].add_Component(c);
                        x++;
                    }


                }
                // Update the random number for the next iteration
                rnd = workstation_rand.nextInt(100);
            }
            stats.ins2_end();
            System.out.println("Other Iterations: " + x);



        }
    }


    // Method to find which Workstation has the smallest number of components in waiting
    // This will only be needed for workstation1 so it will have all three workstations attached

    /**
     * This is a function used by only inspector1 because they have the ability to
     * send a component to any available buffer. This function will determine the
     * best workstation to send C1 to.
     * @return the best available workstation
     */
    public Workstation findAvailableWorkstation() {

        // Workstation 1 has the smallest queue and Workstation 1 has not finished yet
        // NOTE: in case of a tie, workstation 1 has the highest priority
        if(attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[1].getC1_buffer().size() && attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size()
                && !attachedWorkstations[0].isDone()){
            //System.out.println("Sent to Workstation 1");
            return attachedWorkstations[0];
        }
        // Workstation 2 has the smallest queue and Workstation 2 has not finished yet
        else if(attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[0].getC1_buffer().size() && attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size()
                && !attachedWorkstations[1].isDone()){
            //System.out.println("Sent to Workstation 2");
            return attachedWorkstations[1];

        // Workstation 3 has the smallest queue and Workstation 3 has not finished yet
        } else if (attachedWorkstations[2].getC1_buffer().size() <= attachedWorkstations[0].getC1_buffer().size() && attachedWorkstations[2].getC1_buffer().size() <= attachedWorkstations[1].getC1_buffer().size()
                && !attachedWorkstations[2].isDone()){
            //System.out.println("Sent to Workstation 3");
            return attachedWorkstations[2];
        } else { // If all of the workstations have the same size of buffers
            // Send it to the workstation that isn't done starting with 1
            if (!attachedWorkstations[0].isDone()){
                return attachedWorkstations[0];
            }
            else if(!attachedWorkstations[1].isDone()){
                return attachedWorkstations[1];
            } else {
                return attachedWorkstations[2];
            }
        }

    }
}

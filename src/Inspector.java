import java.io.*;
import java.util.Random;
import java.lang.Math;

public class Inspector extends Thread {

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
    public Inspector(Component[] components, Workstation[] workstations, double[] lambdas) {
        inspectComponents = components;
        attachedWorkstations = workstations;
        this.lambdas = lambdas;
    }

    public void run() {
        // if there is one file no need for random variables
        if (lambdas.length == 1) {

            // Create a random number generator to generate doubles
            Random rnd = new Random();

            // This will be used for Inspector 1
            int x = 0;
            while (!attachedWorkstations[0].isDone() || !attachedWorkstations[1].isDone() || !attachedWorkstations[2].isDone()) {
                //System.out.println(st);
                double time = (-1 / lambdas[0]) * Math.log(rnd.nextDouble());
//                System.out.println(time);

                int milli = (int) time;
                int nano = (int) ((time - milli) * 1000000);

                // Create a component now so we can track all of the times
                Component c = new Component("C1");
                c.setInspection_time(time);

                // Do the processing first before picking a available workstation
                synchronized (this){
                    try {
                        System.out.println("Waiting...");
                        this.wait(milli,nano);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Find the most available workstation
                Workstation availableWorkstation = findAvailableWorkstation();

                if(!availableWorkstation.isDone()&& !availableWorkstation.isC1Full()) {
                    synchronized (this) {

                        while (availableWorkstation.isC1Full()) {
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
                    availableWorkstation.add_Component(c);
                    x++;
                    //System.out.println("sent");
                }
            }
            System.out.println("C1 Iterations: " + x);


        } else { // there are two components being inspected

            // use a random number to determine which file to read from
            Random rand = new Random();

            // Use integers from 0-99
            int rnd = rand.nextInt(100);


            int x = 0;
            while (!attachedWorkstations[0].isDone() || !attachedWorkstations[1].isDone()) {
                // This will need to be tweaked as it will continue until both files are completely read
                if (rnd <= 49 && !attachedWorkstations[0].isDone() && !attachedWorkstations[0].isBufferFull()) {
                    double time = (-1 / lambdas[0]) * Math.log(rand.nextDouble());
                    int milli = (int) time;
                    int nano = (int) ((time - milli) * 1000000);
                    Component c = new Component("C2");
                    c.setInspection_time(time);

                    synchronized (this) {

                        try {
                            this.wait(milli,nano);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        while (attachedWorkstations[0].isBufferFull()){
                            try {
                                long startTime = System.nanoTime();
                                this.wait();
                                long endTime = System.nanoTime();
                                c.setDelay_time(time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    c.setQueue_time(System.nanoTime());
                    attachedWorkstations[0].add_Component(c);
                    x++;

//                    if (!attachedWorkstations[0].isBufferFull() && !attachedWorkstations[0].isDone()) {
//                        x++;
//                        double time = (-1 / lambdas[0]) * Math.log(rand.nextDouble());
//                        attachedWorkstations[0].add_Component(new Component("C2"));
//
//                            System.out.println(time);
//                    } else {
//
//                    }

                } else {
                    if(!attachedWorkstations[1].isDone() && !attachedWorkstations[1].isBufferFull()) {
                        double time = (-1 / lambdas[1]) * Math.log(rand.nextDouble());
                        int milli = (int) time;
                        int nano = (int) ((time - milli) * 1000000);
                        Component c = new Component("C3");
                        c.setInspection_time(time);


                        synchronized (this) {

                            try {
                                this.wait(milli,nano);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            while (attachedWorkstations[1].isBufferFull()) {
                                try {
                                    long startTime = System.nanoTime();
                                    this.wait();
                                    long endTime = System.nanoTime();
                                    c.setDelay_time(time);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        c.setQueue_time(System.nanoTime());
                        attachedWorkstations[1].add_Component(c);
                        x++;
                    }

//                    if (!attachedWorkstations[1].isBufferFull() && !attachedWorkstations[1].isDone()) {
//                        x++;
//                        double time = (-1 / lambdas[1]) * Math.log(rand.nextDouble());
//                        int milli = (int) time;
//                        int nano = (int) ((time - milli) * 100);
//                        attachedWorkstations[1].add_Component(new Component("C3"));
//
//                        //System.out.println(time);
//                    } else {
//                        //System.out.println("Waiting...");
//                    }


                }
                rnd = rand.nextInt(100);
            }
            System.out.println("Other Iterations: " + x);



        }
    }


    // Method to find which Workstation has the smallest number of components in waiting
    // This will only be needed for workstation1 so it will have all three workstations attached
    public Workstation findAvailableWorkstation() {

        // Workstation 1 has the smallest queue
        // NOTE: in case of a tie, workstation 1 has the highest priority
        if(attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[1].getC1_buffer().size() && attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size() && !attachedWorkstations[0].isDone()){
            //System.out.println("Sent to Workstation 1");
            return attachedWorkstations[0];
        }
        // Workstation 2 has the smallest queue
        else if(attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[0].getC1_buffer().size() && attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size() && !attachedWorkstations[1].isDone()){
            //System.out.println("Sent to Workstation 2");
            return attachedWorkstations[1];
        } else {
            //System.out.println("Sent to Workstation 3");
            return attachedWorkstations[2];
        }

    }
}

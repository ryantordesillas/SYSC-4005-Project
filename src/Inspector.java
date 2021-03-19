import java.io.*;
import java.util.Random;
import java.lang.Math;

public class Inspector extends Thread{

    /**
     * The components that this Inspector inspects
     */
    private Component[] inspectComponents;

    /**
     * The Workstations that this Inspector sends Components to
     */
    private Workstation[] attachedWorkstations;

    /** lambda used to generate random numbers */
    private double[] lambdas;

    /**
     * The default constructor for the Inspector Object
     * @param components The components that this
     * @param workstations the workstations attached to the
     * @param lambdas the lambdas that will be used in the random number generation
     */
    public Inspector(Component[] components, Workstation[] workstations, double[] lambdas){
        inspectComponents = components;
        attachedWorkstations = workstations;
        this.lambdas = lambdas;
    }

    public void run(){
        // if there is one file no need for random variables
        if (lambdas.length == 1){

            // Create a random number generator to generate doubles
            Random rnd = new Random();

            // This will be used for Inspector 1
            int x = 0;
            while (x <= 200) {
                //System.out.println(st);
                double time = (-1 / lambdas[0]) * Math.log(rnd.nextDouble());
//                System.out.println(time);

                // Find the most available workstation
                Workstation availableWorkstation = findAvailableWorkstation();

                if (!availableWorkstation.isC1Full()){
                    availableWorkstation.add_Component(new Component("C1"));
                    x++;
                }
            }

        } else { // there are two components being inspected
            try{

                // use a random number to determine which file to read from
                Random rand = new Random();

                // Use integers from 0-99
                int rnd = rand.nextInt(100);


                int x = 0;
                while(x <= 200){
                    // This will need to be tweaked as it will continue until both files are completely read
                    if(rnd <= 49){
                        //tba: if str is null while waiting for the other list, do not send a component

                        if (!attachedWorkstations[0].isBufferFull()) {
                            x++;
                            double time = (-1/lambdas[0]) * Math.log(rand.nextDouble());
                            attachedWorkstations[0].add_Component(new Component("C2"));
//                            System.out.println(time);
                        } else {
                            //System.out.println("Waiting...");
                        }

                    } else {


                        if (!attachedWorkstations[1].isBufferFull()) {
                            x++;
                            double time = (-1/lambdas[1]) * Math.log(rand.nextDouble());
                            attachedWorkstations[1].add_Component(new Component("C3"));
                            System.out.println(time);
                        } else {
                            //System.out.println("Waiting...");
                        }


                    }
                    rnd = rand.nextInt(100);
                }
                System.out.println("Iterations: "+ x);




            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    // Method to find which Workstation has the smallest number of components in waiting
    // This will only be needed for workstation1 so it will have all three workstations attached
    public Workstation findAvailableWorkstation() {

        // Workstation 1 has the smallest queue
        // NOTE: in case of a tie, workstation 1 has the highest priority
        if(attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[1].getC1_buffer().size() && attachedWorkstations[0].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size()){
            //System.out.println("Sent to Workstation 1");
            return attachedWorkstations[0];
        }
        // Workstation 2 has the smallest queue
        else if(attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[0].getC1_buffer().size() && attachedWorkstations[1].getC1_buffer().size() <= attachedWorkstations[2].getC1_buffer().size()){
            //System.out.println("Sent to Workstation 2");
            return attachedWorkstations[1];
        } else {
            //System.out.println("Sent to Workstation 3");
            return attachedWorkstations[2];
        }

    }
}

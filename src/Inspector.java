import java.io.*;
import java.util.Random;

public class Inspector extends Thread{

    /**
     * The components that this Inspector inspects
     */
    private Component[] inspectComponents;

    /**
     * The Workstations that this Inspector sends Components to
     */
    private Workstation[] attachedWorkstations;

    /** The file that contains the service times for this inspector **/
    private File[] serviceFiles;

    /**
     * The default constructor for the Inspector Object
     * @param components The components that this
     * @param workstations
     */
    public Inspector(Component[] components, Workstation[] workstations, File[] files){
        inspectComponents = components;
        attachedWorkstations = workstations;
        serviceFiles = files;
    }

    public void run(){
        // if there is one file no need for random variables
        if (serviceFiles.length == 1){
            try{
                // Read an inspection time from a file
                BufferedReader br = new BufferedReader(new FileReader(serviceFiles[0]));

                // This will be used for Inspector 1
                String st;
                while ((st = br.readLine()) != null){
                    //System.out.println(st);

                    // Find the most available workstation
                    Workstation availableWorkstation = findAvailableWorkstation();

                    availableWorkstation.add_Component(new Component("C1"));
                }




            } catch(Exception e){
                e.printStackTrace();
            }
        } else { // there are two components being inspected
            try{
                // Read an inspection time from a file
                BufferedReader br_1 = new BufferedReader(new FileReader(serviceFiles[0]));
                BufferedReader br_2 = new BufferedReader(new FileReader(serviceFiles[1]));

                // use a random number to determine which file to read from
                Random rand = new Random();

                // Use integers from 0-99
                int rnd = rand.nextInt(100);

                String st1 = br_1.readLine();
                String st2 = br_2.readLine();
                int x = 0;
                while(st1 != null || st2 != null){
                    // This will need to be tweaked as it will continue until both files are completely read
                    if(rnd <= 49){
                        //tba: if str is null while waiting for the other list, do not send a component
                        x++;
                        //System.out.println("File1: " + st1);
                        st1 = br_1.readLine();

                        if (st1 != null) {
                            // Make the thread wait using the time read in from the file

                            // There will need to be some type of wait if the ArrayList is full (size == 2)
//                            while(attachedWorkstations[0].get_Buffer().size() == 2){
//                                System.out.println("Waiting...");
//                            }

                            // Send component 2 to Workstation 2
                            attachedWorkstations[0].add_Component(new Component("C2"));
                        }
                    } else {
                        x++;
                        //System.out.println("File2: "+ st2);
                        st2 = br_2.readLine();

                        if (st2 != null) {

                            // Wait for the thread using the time read in from the file

                            // There will need to be some type of wait if the ArrayList is full (size == 2)
//                            while(attachedWorkstations[1].get_Buffer().size() == 2){
//                                System.out.println("Waiting...");
//                            }

                            // Send component 3 to component 3
                            attachedWorkstations[1].add_Component(new Component("C3"));
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

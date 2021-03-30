import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;


public class Workstation extends Thread{

    private final double exec;
    // Boolean to determine if the appropriate amount of products have been made
    private boolean done = false;


    /**
     * The inspectors that are attached to this workstation
     */
    private ArrayList<Inspector> attached_inspectors;

    /**
     * The Created product will be stored as a string for counting purposes
     */
    private String createdProduct = "";

    /**
     * Optional second buffer for another component if needed
     */
    private ArrayList<Component> buffer = null;

    /** The extra component to be stored in the 2nd buffer */
    private Component extra_component = null;

    /** C1 Buffer for C1 component */
    private ArrayList<Component> C1_buffer;

    /** lambda for the processing time **/
    private double lambda;

    /** Created Product Count */
    private int product_count = 0;

    /** The runtime of this thread */
    private double runtime = 0;


    /** Stats for the components */
    private Statistic stat;

    private int workstation_number = 1;

    boolean extra_component_flag = false;
    /**
     * The default constructor for the Workstation
     * @param lambda The value used to determine processing times
     * @param extra_component_flag flag to determine if a 2nd component is needed
     * @param extra_component the extra component if the extra_component_flag is true
     */
    public Workstation(double lambda, boolean extra_component_flag, Component extra_component, Statistic stat, double exec){

        // Create the ArrayList that the Workstation will use
        C1_buffer = new ArrayList<Component>();

        this.lambda = lambda;

        // Create a buffer if there needs to be an extra component for workstations W2 and W3
        if (extra_component_flag){
            buffer = new ArrayList<Component>();
            this.extra_component = extra_component;
            this.extra_component_flag = true;
            if (extra_component.getType().equals("C2")){
                workstation_number = 2;
            } else {
                workstation_number = 3;
            }
        }

        // Create a list of attached inspectors
        attached_inspectors = new ArrayList<Inspector>();

        // Stats to keep track of times
        this.stat = stat;
        
        this.exec = exec;

    }

    public int getWorkstation_number() {
        return workstation_number;
    }

    /**
     * Add a function to this workstations buffer
     * @param c the component to be added
     */
    public void add_Component(Component c){
        // If the type is C1, add it to the C1 buffer
        if(c.getType() == "C1") {
            C1_buffer.add(c);
        }
        // Anything else add it to the secondary buffer
        else if(c.getType() == extra_component.getType()){
            buffer.add(c);
        } else { // error case
            System.out.println("ERROR: This Workstation cannot handle that Component!");
        }
    }

    /**
     * The main function to run the thread
     */
    public void run(){
        runtime = System.nanoTime();
        // Create a random number generator
        Random rnd = new Random();

        // If this is workstation 1
        if(!extra_component_flag){
            double wait_time = System.nanoTime();

            while(stat.elapsed_time() < exec) {

                // Check if the buffer is not empty and is full
                if(C1_buffer.size() > 2){
                    System.out.println("C1 " + C1_buffer.size());
                }
                if(C1_buffer.size() <= 2 && C1_buffer.size() > 0) {
                    stat.addWork1WaitTime(System.nanoTime() - wait_time);
//                    System.out.println("Processing P1");

                    // Get the components from the ArrayList
                    Component c1 = C1_buffer.get(0);
                    //Component c2 = C1_buffer.get(1);

                    if (c1 == null){
                        System.out.println("Null Detected in workstation 1");
                        C1_buffer.remove(0);
                        continue;
                    }
                    // Generate the random time and end queueing time
                    //double time = generateRandomTime(rnd, c1, c2);
                    double queue_start_time1 = c1.getQueue_time();
                    c1.setQueue_time(System.nanoTime() - queue_start_time1);
                    double time = (-1/lambda) * Math.log(rnd.nextDouble());
                    double min_to_sec = time *60;

                    int milli = (int) min_to_sec;
                    int nano = (int) ((min_to_sec - milli) * 1000000);


                    // Set the random processing time
                    c1.setProcessing_time(time);
                    //c2.setProcessing_time(time);

                    // Split the processing time into milliseconds and nanoseconds



                    // Process the component
                    synchronized (this){
                        try {
                            //System.out.println("Creating product...");
                            this.wait(milli,nano);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Create the product and the component information
                    stat.processP1(c1);

                    // Clear the buffer because the buffer will take 2 components
                    C1_buffer.remove(0);
                    product_count++;
                    wait_time = System.nanoTime();
                }

                // Do a check to see if inspector 1 is waiting
                if(!isC1Full() && attached_inspectors.get(0).getState() == State.WAITING){
                    synchronized (attached_inspectors.get(0)) {
                        attached_inspectors.get(0).notify();
                    }
                }



            }
        }else{
            double wait_time = System.nanoTime();
            // This is when the workstation takes another component alongside C1
            while(stat.elapsed_time() < exec) {

                if(C1_buffer.size() > 2){
                    System.out.println("C1: " + C1_buffer.size());
                }
                if(buffer.size() > 2){
                    System.out.println("Other Buffer: " + buffer.size());
                }
                // Ensure that the buffer is not empty of over-filled
                if(C1_buffer.size() <= 2 && buffer.size() <= 2 && buffer.size() > 0 && C1_buffer.size() >0) {

                    if(extra_component.getType() == "C2"){
                        stat.addWork2WaitTime(System.nanoTime() - wait_time);
//                        System.out.println("Processing P2");
                    } else {
                        stat.addWork3WaitTime(System.nanoTime() - wait_time);
//                        System.out.println("Processing P3");
                    }
                    //System.out.println(buffer.size());

                    // Get the two components from each buffer
                    Component c1 = C1_buffer.get(0);
                    Component buffer_component = buffer.get(0);

                    if (buffer_component == null){ // There is a bug right now where null is somehow being passed
//                        System.out.println(buffer.size());
                        System.out.println(buffer +"\n"+product_count);
                        if(extra_component.getType() == "C2"){
                            System.out.println("Null Detected in workstation 2");
                        } else {
                            System.out.println("Null Detected in workstation 3");
                        }
                        // I think this addresses the bug.
                        buffer.remove(0);
                        continue;
                    }

                    // End the queue time and generate a new random time
                    double time = generateRandomTime(rnd, c1, buffer_component);
                    double min_to_sec = time *60;

                    int milli = (int) min_to_sec;
                    int nano = (int) ((min_to_sec - milli) * 1000000);

                    // Set the processing time for both components
                    c1.setProcessing_time(time);
                    buffer_component.setProcessing_time(time);

                    // Process the product using the generated time
                    synchronized (this){
                        try {
                            //System.out.println("Creating product...");
                            this.wait(milli,nano);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Create the product and keep track of the times of each component
                    stat.processProduct(buffer_component.getType(),c1,buffer_component);

                    // Remove the components from the buffer
                    C1_buffer.remove(0);
                    buffer.remove(0);
                    product_count++;
                    wait_time = System.nanoTime();
                }

                //check if inspectors are waiting
                // Do a check to see if inspector 1 is waiting
                if(!isC1Full()){
                    for(Inspector i: attached_inspectors){
                        if(i.getState() == State.WAITING) {
                            synchronized (i) {
                                i.notify();
                            }
                        }
                    }
                }

                // check if inspectors are waiting on buffer
                if(!isBufferFull()){
                    for(Inspector i: attached_inspectors){
                        if(i.getState() == State.WAITING) {
                            synchronized (i) {
                                i.notify();
                            }
                        }
                    }
                }

            }
        }
        done = true;
        runtime = System.nanoTime() - runtime;

        if(!extra_component_flag) {
            stat.setWork1_run_time(runtime);
        }else{
            if (extra_component.getType().equals("C2")){
                stat.setWork2_run_time(runtime);
            } else {
                stat.setWork3_run_time(runtime);
            }
        }

    }

    /**
     * Generate random times for processing and update queue times.
     * @param rnd the random number generator
     * @param c1 the C1 component
     * @param buffer_component the other component needed to create a product
     * @return the random number to be used for processing times
     */
    private double generateRandomTime(Random rnd, Component c1, Component buffer_component) {
        double queue_start_time1 = c1.getQueue_time();
        double queue_start_time2 = buffer_component.getQueue_time();
        c1.setQueue_time(System.nanoTime() - queue_start_time1);
        buffer_component.setQueue_time(System.nanoTime() - queue_start_time2);

        return (-1/lambda) * Math.log(rnd.nextDouble());
    }

    /**
     * Get the created product from this workstation
     * @return String representation of the finished product
     */
    public String getCreatedProduct() {
        return createdProduct;
    }

    /**
     * Set the created product of the workstation
     * @param createdProduct the new created product
     */
    public void setCreatedProduct(String createdProduct) {
        this.createdProduct = createdProduct;
    }

    /**
     * Get the buffer of the other component
     * @return the buffer of the other component
     */
    public ArrayList<Component> getBuffer() {
        return buffer;
    }

    /**
     * Set the buffer of the other component
     * @param buffer the buffer of the other component
     */
    public void setBuffer(ArrayList<Component> buffer) {
        this.buffer = buffer;
    }

    /**
     * Get the extra component used in the workstation
     * @return the extra component used
     */
    public Component getExtra_component() {
        return extra_component;
    }

    /**
     * Set the extra component
     * @param extra_component the new extra component used in the workstation
     */
    public void setExtra_component(Component extra_component) {
        this.extra_component = extra_component;
    }

    /**
     * Get the C1 buffer of the workstation
     * @return the C1 buffer of the workstation
     */
    public ArrayList<Component> getC1_buffer() {
        return C1_buffer;
    }

    /**
     * Set the C1 buffer
     * @param c1_buffer the new C1 buffer
     */
    public void setC1_buffer(ArrayList<Component> c1_buffer) {
        C1_buffer = c1_buffer;
    }

    /**
     * Determine if the C1 buffer is full
     * @return true if full, false otherwise
     */
    public boolean isC1Full(){
        if (C1_buffer.size() >= 2){
            return true;
        }
        return false;
    }

    /**
     * Determine if the other buffer is full
     * @return true if the other buffer is full, false otherwise
     */
    public boolean isBufferFull(){
        if (buffer.size() >= 2){
            return true;
        }
        return false;
    }

    /**
     * Get the product count of the workstation
     * @return
     */
    public int getProduct_count() {
        return product_count;
    }

    /**
     * Determine if the workstation is done making products
     * @return true if finished making products, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Add an inspector to the workstation
     * @param i the inspector to be added.
     */
    public void addInspector(Inspector i){
        attached_inspectors.add(i);
    }
}

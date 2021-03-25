import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;


public class Workstation extends Thread{

    // Boolean to determine if the appropriate amount of products have been made
    private boolean done = false;

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

    /** Stats for the components */
    private Statistic stat;

    boolean extra_component_flag = false;
    /**
     * The default constructor for the Workstation
     * @param lambda The value used to determine processing times
     * @param extra_component_flag flag to determine if a 2nd component is needed
     * @param extra_component the extra component if the extra_component_flag is true
     */
    public Workstation(double lambda, boolean extra_component_flag, Component extra_component, Statistic stat){

        // Create the ArrayList that the Workstation will use
        C1_buffer = new ArrayList<Component>();

        this.lambda = lambda;

        // Create a buffer if there needs to be an extra component for workstations W2 and W3
        if (extra_component_flag){
            buffer = new ArrayList<Component>();
            this.extra_component = extra_component;
            this.extra_component_flag = true;
        }

        attached_inspectors = new ArrayList<Inspector>();
        this.stat = stat;

    }


    public void add_Component(Component c){
        if(c.getType() == "C1") {
            C1_buffer.add(c);
        }
        else if(c.getType() == extra_component.getType()){
            buffer.add(c);
        } else { // error case
            System.out.println("ERROR: This Workstation cannot handle that Component!");
        }
    }

    public void run(){

        Random rnd = new Random();
        if(!extra_component_flag){
            while(product_count < 50) {

                // Check if the buffer is not empty or full
                if(C1_buffer.size() == 2 && C1_buffer.size() > 0) {

                    // Get the component and beginning of the ArrayList
                    Component c1 = C1_buffer.get(0);
                    Component c2 = C1_buffer.get(1);

                    // End the queue time
                    double queue_start_time1 = c1.getQueue_time();
                    double queue_start_time2 = c2.getQueue_time();
                    c1.setQueue_time(System.nanoTime() - queue_start_time1);
                    c2.setQueue_time(System.nanoTime() - queue_start_time2);

                    // Generate a exponential time for processing time
                    double time = (-1/lambda) * Math.log(rnd.nextDouble());

                    c1.setProcessing_time(time);
                    c2.setProcessing_time(time);

                    int milli = (int) time;
                    int nano = (int) ((time - milli) * 1000000);


                    synchronized (this){
                        try {
                            //System.out.println("Creating product...");
                            this.wait(milli,nano);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    stat.processP1(c1,c2);

                    // Clear the buffer because the buffer will take 2 components
                    C1_buffer.clear();
                    product_count++;

                }

                // Do a check to see if inspector 1 is waiting
                if(!isC1Full() && attached_inspectors.get(0).getState() == State.WAITING){
                    synchronized (attached_inspectors.get(0)) {
                        attached_inspectors.get(0).notify();
                    }
                }



            }
        }else{
            while(product_count < 50) {

                if(C1_buffer.size() <= 2 && buffer.size() <= 2 && buffer.size() > 0 && C1_buffer.size() >0) {

                    Component c1 = C1_buffer.get(0);
                    Component buffer_component = buffer.get(0);

                    // End the queue time
                    double queue_start_time1 = c1.getQueue_time();
                    double queue_start_time2 = buffer_component.getQueue_time();
                    c1.setQueue_time(System.nanoTime() - queue_start_time1);
                    buffer_component.setQueue_time(System.nanoTime() - queue_start_time2);

                    double time = (-1/lambda) * Math.log(rnd.nextDouble());
                    int milli = (int) time;
                    int nano = (int) ((time - milli) * 1000000);


                    c1.setProcessing_time(time);
                    buffer_component.setProcessing_time(time);

                    synchronized (this){
                        try {
                            //System.out.println("Creating product...");
                            this.wait(milli,nano);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    stat.processProduct(buffer_component.getType(),c1,buffer_component);

                    C1_buffer.remove(0);
                    buffer.remove(0);
                    product_count++;
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

    }


    public String getCreatedProduct() {
        return createdProduct;
    }

    public void setCreatedProduct(String createdProduct) {
        this.createdProduct = createdProduct;
    }

    public ArrayList<Component> getBuffer() {
        return buffer;
    }

    public void setBuffer(ArrayList<Component> buffer) {
        this.buffer = buffer;
    }

    public Component getExtra_component() {
        return extra_component;
    }

    public void setExtra_component(Component extra_component) {
        this.extra_component = extra_component;
    }

    public ArrayList<Component> getC1_buffer() {
        return C1_buffer;
    }

    public void setC1_buffer(ArrayList<Component> c1_buffer) {
        C1_buffer = c1_buffer;
    }

    public boolean isC1Full(){
        if (C1_buffer.size() >= 2){
            return true;
        }
        return false;
    }

    public boolean isBufferFull(){
        if (buffer.size() >= 2){
            return true;
        }
        return false;
    }

    public int getProduct_count() {
        return product_count;
    }

    public boolean isDone() {
        return done;
    }

    public void addInspector(Inspector i){
        attached_inspectors.add(i);
    }
}

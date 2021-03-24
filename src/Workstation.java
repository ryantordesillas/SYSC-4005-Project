import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;


public class Workstation extends Thread{

    private boolean done = false;

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

    boolean extra_component_flag = false;
    /**
     * The default constructor for the Workstation
     * @param lambda The value used to determine processing times
     * @param extra_component_flag flag to determine if a 2nd component is needed
     * @param extra_component the extra component if the extra_component_flag is true
     */
    public Workstation(double lambda, boolean extra_component_flag, Component extra_component){

        // Create the ArrayList that the Workstation will use
        C1_buffer = new ArrayList<Component>();

        this.lambda = lambda;

        // Create a buffer if there needs to be an extra component for workstations W2 and W3
        if (extra_component_flag){
            buffer = new ArrayList<Component>();
            this.extra_component = extra_component;
            this.extra_component_flag = true;
        }

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
            while(product_count < 200) {

                // Check if the buffer is not empty or full
                if(C1_buffer.size() <= 2 && C1_buffer.size() > 0) {
                    double time = (-1/lambda) * Math.log(rnd.nextDouble());

                    // Separate into miliseconds and nanoseconds
//                    int mili = (int) time;
//                    int nano = (int) ((time - mili) * 100);
//                    try {
//                        // Multiply by 1000 to change it to seconds.milliseconds
//                        sleep(mili,nano);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Workstation 1:Process Time: " + mili + "." + nano);
//                    System.out.println("C1 Buffer Size: " + C1_buffer.size());
                    C1_buffer.remove(0);
                    product_count++;

                }
            }
            done = true;
        }else{
            while(product_count < 200) {

                if(C1_buffer.size() <= 2 && buffer.size() <= 2 && buffer.size() > 0 && C1_buffer.size() >0) {

                    double time = (-1/lambda) * Math.log(rnd.nextDouble());
//                    int mili = (int) time;
//                    int nano = (int) ((time - mili) * 100);
//                    try {
//                        // Multiply by 1000 to change it to seconds.milliseconds
//                        sleep(mili,nano);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Workstation 2 or 3 Process Time: " + time);
//                    System.out.println("C1 Buffer Size: " + C1_buffer.size());
//                    System.out.println("Other component Buffer Size: " + buffer.size());
                    C1_buffer.remove(0);
                    buffer.remove(0);
                    product_count++;
                }
            }
            done = true;
        }

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
        if (C1_buffer.size() == 2){
            return true;
        }
        return false;
    }

    public boolean isBufferFull(){
        if (buffer.size() == 2){
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
}

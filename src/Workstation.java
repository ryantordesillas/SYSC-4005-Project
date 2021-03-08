import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class Workstation {

    /**
     * The Created product will be stored as a string for counting purposes
     */
    private String createdProduct = "";

    /**
     * Booleans for the types of components this Workstation will take
     */
    private boolean acceptsC1 = false;
    private boolean acceptsC2 = false;
    private boolean acceptsC3 = false;

    /**
     * ArrayLists for the component buffers
     */
    private ArrayList<Component> C1_Buffer;
    private ArrayList<Component> C2_Buffer;
    private ArrayList<Component> C3_Buffer;

    /** File for the processing time **/
    private File service_file;

    /**
     * Default constructor for the Workstation
     * @param components The components that this Workstation takes
     */
    public Workstation(Component[] components, File file){
        for(int i = 0; i < components.length; i++){
            // Create the ArrayLists that the Workstation will use
            if(components[i].getType() == "C1"){
                C1_Buffer = new ArrayList<Component>();
            }
            if (components[i].getType() == "C2"){
                C2_Buffer = new ArrayList<Component>();
            }
            if (components[i].getType() == "C3"){
                C3_Buffer = new ArrayList<Component>();
            }

            service_file = file;
        }
    }


    public void add_Component(ArrayList<Component> buffer, Component c){
        buffer.add(c);

        try {
            BufferedReader br = new BufferedReader(new FileReader(service_file));
            String st1 = br.readLine();
            //wait
            System.out.println("Processing Time: "+st1);

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<Component> getC1_Buffer() {
        return C1_Buffer;
    }

    public ArrayList<Component> getC2_Buffer() {
        return C2_Buffer;
    }

    public ArrayList<Component> getC3_Buffer() {
        return C3_Buffer;
    }
}

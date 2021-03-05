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

    /**
     * Default constructor for the Workstation
     * @param components The components that this Workstation takes
     */
    public Workstation(Component[] components){
        for(int i = 0; i < components.length; i++){
            if(components[i].getType() == "C1"){
                C1_Buffer = new ArrayList<Component>();
            }
            else if (components[i].getType() == "C2"){
                C2_Buffer = new ArrayList<Component>();
            } else {
                C3_Buffer = new ArrayList<Component>();
            }
        }
    }


    public void add_Component(ArrayList<Component> buffer, Component c){
        buffer.add(c);
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

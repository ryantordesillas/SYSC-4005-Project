public class Inspector {

    /**
     * The components that this Inspector inspects
     */
    private Component[] inspectComponents;

    /**
     * The Workstations that this Inspector sends Components to
     */
    private Workstation[] attachedWorkstations;

    /**
     * The default constructor for the Inspector Object
     * @param components The components that this
     * @param workstations
     */
    public Inspector(Component[] components, Workstation[] workstations){
        inspectComponents = components;
        attachedWorkstations = workstations;
    }



}

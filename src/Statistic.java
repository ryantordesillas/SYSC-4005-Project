public class Statistic {

    /** The Count of P1 products made */
    private int P1_Count = 0;

    /** The Count of P2 products made */
    private int P2_Count = 0;

    /** The Count of P3 products made */
    private int P3_Count = 0;

    /** Count of C1 components used */
    private int C1_Count = 0;

    /** Count of C2 components used */
    private int C2_Count = 0;

    /** Count of C2 components used */
    private int C3_Count = 0;

    /** The total time the simulation runs */
    private double total_simulation_time = 0;

    /** The total delay of component 1 */
    private double c1_delay = 0;

    /** Total processing time of processing 1 */
    private double p1_processing = 0;

    /** Total queue time of component 1 */
    private double c1_queue = 0;

    /** Total inspection time of component 1 */
    private double c1_inspecting = 0;

    /** The total delay of component 2 */
    private double c2_delay = 0;

    /** Total processing time of processing 2 */
    private double p2_processing = 0;

    /** Total queue time of component 2 */
    private double c2_queue = 0;

    /** Total inspection time of component 2 */
    private double c2_inspecting = 0;

    /** The total delay of component 3 */
    private double c3_delay = 0;

    /** Total processing time of product 3 */
    private double p3_processing = 0;

    /** Total queue time of component 3 */
    private double c3_queue = 0;

    /** Total inspection time of component 3 */
    private double c3_inspecting = 0;


    public Statistic(){

    }

    public void startSimTime(){
        total_simulation_time = System.nanoTime();
    }

    public void endSimTime(){
        double startTime = total_simulation_time;
        total_simulation_time = System.nanoTime() - total_simulation_time;
    }

    public double getTotal_simulation_time(){
        return total_simulation_time;
    }

    public void processP1(Component c1, Component c2) {
        // Get delay times of components
        c1_delay += c1.getDelay_time() + c2.getDelay_time();

        // Get inspection times
        c1_inspecting += c1.getInspection_time() + c2.getInspection_time();

        // Get queue times
        c1_queue += c1.getQueue_time() + c2.getQueue_time();

        // Get process times of product 1
        p1_processing += c1.getProcessing_time() + c2.getProcessing_time();

        //Increment count by 1
        P1_Count++;

        //Increment C1_count by 2 because 2 components are used
        C1_Count+=2;
    }

    public void processProduct(String type, Component c1, Component other_component){

        // Get delay times of components
        c1_delay += c1.getDelay_time();

        // Get inspection times
        c1_inspecting += c1.getInspection_time();

        // Get queue times
        c1_queue += c1.getQueue_time();

        C1_Count++;
        if (type.equals("C2")){
            C2_Count++;
            P2_Count++;
            c2_delay += other_component.getDelay_time();
            c2_inspecting += other_component.getInspection_time();
            c2_queue += other_component.getQueue_time();
            p2_processing += other_component.getProcessing_time();
        } else {
            C3_Count++;
            P3_Count++;
            c3_delay += other_component.getDelay_time();
            c3_inspecting += other_component.getInspection_time();
            c3_queue += other_component.getQueue_time();
            p3_processing += other_component.getProcessing_time();
        }
    }

    public void generateReport() {
        String out = "";
        out += "======================== Count Information ========================\n";
        out += "P1's made: " + P1_Count + "\nP2's made: " + P2_Count + "\nP3's made: " + P3_Count + "\nC1's used: " + C1_Count + "\nC2's used: " + C2_Count + "\nC3's used: " + C3_Count;
        out += "\n===================================================================\n\n";
        out += "======================== C1 Information ========================\n";
        out += "Average inspecting time: " + (c1_inspecting / C1_Count);
        out += "\nAverage delay time: "+ (c1_delay / C1_Count)/1000000;
        out += "\nAverage queue time: "+ (c1_queue / C1_Count)/1000000;
        out += "\n===================================================================\n\n";
        out += "======================== C2 Information ========================\n";
        out += "Average inspecting time: " + (c2_inspecting / C2_Count);
        out += "\nAverage delay time: "+ (c2_delay / C2_Count);
        out += "\nAverage queue time: "+ (c2_queue / C2_Count)/1000000;
        out += "\n===================================================================\n\n";
        out += "======================== C3 Information ========================\n";
        out += "Average inspecting time: " + (c3_inspecting / C3_Count);
        out += "\nAverage delay time: "+ (c3_delay / C3_Count);
        out += "\nAverage queue time: "+ (c3_queue / C3_Count)/1000000;
        out += "\n===================================================================\n\n";
        out += "======================== Product Processing Time ========================\n";
        out += "Average processing time for P1: " + (p1_processing / P1_Count);
        out += "\nAverage processing time for P2: " + (p2_processing / P2_Count);
        out += "\nAverage processing time for P3: " + (p3_processing / P3_Count);
        out += "\n===================================================================\n\n";

        System.out.println(out);
    }
}

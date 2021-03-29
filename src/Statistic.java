import java.lang.reflect.Array;
import java.util.ArrayList;

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

    /** Total time inspector 1 runs */
    private double ins1_run_time;

    /** Total time inspector 1 runs */
    private double ins2_run_time;

    /** Wait times for the workstations */
    private double work1_wait_time;
    private double work2_wait_time;
    private double work3_wait_time;

    /** Input data */
    private ArrayList c1_inspecting_times = new ArrayList<>();
    private ArrayList c2_inspecting_times = new ArrayList<>();
    private ArrayList c3_inspecting_times = new ArrayList<>();
    private ArrayList p1_processing_times = new ArrayList<>();
    private ArrayList p2_processing_times = new ArrayList<>();
    private ArrayList p3_processing_times = new ArrayList<>();

    /**
     * Default constructor of the Statistic object because all objects are initially set to 0
     */
    public Statistic(){ }

    /**
     * Start the simulation time of the system
     */
    public void startSimTime(){
        total_simulation_time = System.nanoTime();
    }

    /**
     * End the simulation time and calculate the total sim time
     */
    public void endSimTime(){
        double startTime = total_simulation_time;
        total_simulation_time = System.nanoTime() - total_simulation_time;
    }

    /**
     * Get the total simulation time when the simulation finishes
     * @return the total simulation time
     */
    public double getTotal_simulation_time(){
        return total_simulation_time;
    }

    /**
     * Get the information that was used to create the P1 product
     * @param c1 The first C1 component used to create the P1 product
     * @param c2 The second C1 used to create the P1 product
     */
    public void processP1(Component c1) {
        // Get delay times of components
        c1_delay += c1.getDelay_time();

        // Get inspection times
        c1_inspecting += c1.getInspection_time();
        c1_inspecting_times.add(c1.getInspection_time());
        //c1_inspecting_times.add(c2.getInspection_time());
        p1_processing_times.add(c1.getProcessing_time());

        // Get queue times
        c1_queue += c1.getQueue_time();

        // Get process times of product 1
        //p1_processing += c1.getProcessing_time() + c2.getProcessing_time();
        p1_processing += c1.getProcessing_time();

        //Increment count by 1
        P1_Count++;

        //Increment C1_count by 2 because 2 components are used
        C1_Count++;
    }

    /**
     * Process the information that was used to create either a P2 or P3 object
     * @param type The type of product created
     * @param c1 the C1 object used to create the product
     * @param other_component the other component, either a C2 or C3, used to create this object.
     */
    public void processProduct(String type, Component c1, Component other_component){

        // Get delay times of components
        c1_delay += c1.getDelay_time();

        // Get inspection times
        c1_inspecting += c1.getInspection_time();
        c1_inspecting_times.add(c1.getInspection_time());

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
            c2_inspecting_times.add(other_component.getInspection_time());
            p2_processing_times.add(other_component.getProcessing_time());
        } else {
            C3_Count++;
            P3_Count++;
            c3_delay += other_component.getDelay_time();
            c3_inspecting += other_component.getInspection_time();
            c3_queue += other_component.getQueue_time();
            p3_processing += other_component.getProcessing_time();
            c3_inspecting_times.add(other_component.getInspection_time());
            p3_processing_times.add(other_component.getProcessing_time());
        }
    }

    /**
     * Start tracking inspector 1's execution time.
     */
    public void ins1_start(){
        ins1_run_time = System.nanoTime();
    }

    /**
     * Stop tracking inspector 1's execution time
     */
    public void ins1_end() {
        ins1_run_time = System.nanoTime() - ins1_run_time;
    }

    /**
     * Start tracking inspector 2's execution time
     */
    public void ins2_start(){
        ins2_run_time = System.nanoTime();
    }

    /**
     * Stop tracking inspector 2's execution time.
     */
    public void ins2_end() {
        ins2_run_time = System.nanoTime() - ins2_run_time;
    }

    /**
     * Generate a report at the end of the simulation with all the information.
     */
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
        out += "\nAverage delay time: "+ (c2_delay / C2_Count)/1000000;
        out += "\nAverage queue time: "+ (c2_queue / C2_Count)/1000000;
        out += "\n===================================================================\n\n";
        out += "======================== C3 Information ========================\n";
        out += "Average inspecting time: " + (c3_inspecting / C3_Count);
        out += "\nAverage delay time: "+ (c3_delay / C3_Count)/1000000;
        out += "\nAverage queue time: "+ (c3_queue / C3_Count)/1000000;
        out += "\n===================================================================\n\n";
        out += "======================== Product Processing Time ========================\n";
        out += "Average processing time for P1: " + (p1_processing / P1_Count);
        out += "\nAverage processing time for P2: " + (p2_processing / P2_Count);
        out += "\nAverage processing time for P3: " + (p3_processing / P3_Count);
        out += "\n===================================================================\n\n";
        out += "======================== Inspector 1 Stats ========================\n";
        out += "Total inspection time: " + (c1_inspecting);
        out += "\nTotal delayed time: " + (c1_delay)/1000000;
        out += "\nTotal run time: " + (ins1_run_time)/1000000;
        out += "\nTotal time: " + (total_simulation_time)/1000000;
        out += "\nUtilization for Inspector 1: " + ((c1_inspecting * 1000000) / ins1_run_time)*100; // the inspection time will need to be multiplied by 1000000 because it is tracked in milliseconds
        out += "\n===================================================================\n\n";
        out += "======================== Inspector 2 Stats ========================\n";
        out += "Total inspection time: " + (c2_inspecting + c3_inspecting);
        out += "\nTotal delayed time: " + (c2_delay + c3_delay)/1000000;
        out += "\nTotal run time: " + (ins2_run_time)/1000000;
        out += "\nTotal time: " + (total_simulation_time)/1000000;
        out += "\nUtilization for Inspector 2: " + (((c2_inspecting + c3_inspecting)*1000000)/ ins2_run_time)*100;
        out += "\n===================================================================\n\n";
        out += "======================== Workstation 1 Stats ========================\n";
        out += "Total inspection time: " + p1_processing;
        out += "\nTotal waiting time: " + work1_wait_time/1000000;
        out += "\nUtilization for Inspector 2: " + ((p1_processing*1000000)/ total_simulation_time)*100;
        out += "\n===================================================================\n\n";
        out += "======================== Workstation 2 Stats ========================\n";
        out += "Total inspection time: " + p2_processing;
        out += "\nTotal waiting time: " + work2_wait_time/1000000;
        out += "\nUtilization for Inspector 2: " + ((p2_processing*1000000)/ total_simulation_time)*100;
        out += "\n===================================================================\n\n";
        out += "======================== Workstation 3 Stats ========================\n";
        out += "Total inspection time: " + p3_processing;
        out += "\nTotal waiting time: " + work3_wait_time/1000000;
        out += "\nUtilization for Inspector 2: " + ((p3_processing*1000000)/ total_simulation_time)*100;
        out += "\n===================================================================\n\n";
        out += "======================== Inputs ========================\n";
        out += "c1 processing time array size: " + c1_inspecting_times.size();
        out += "\nc2 processing time array size: " + c2_inspecting_times.size();
        out += "\nc3 processing time array size: " + c3_inspecting_times.size();
        out += "\np1 processing time array size: " + p1_processing_times.size();
        out += "\np2 processing time array size: " + p2_processing_times.size();
        out += "\np3 processing time array size: " + p3_processing_times.size();


        System.out.println(out);
    }

    public void addWork1WaitTime(double time){
        work1_wait_time+=time;
    }

    public void addWork2WaitTime(double time){
        work2_wait_time+=time;
    }

    public void addWork3WaitTime(double time){
        work3_wait_time+=time;
    }
}

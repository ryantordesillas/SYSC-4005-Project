public class Component {

    /**
     * The Type of component that this component is
     */
    private String type;

    /** Start time used to determine throughput */
    private double start_time;

    /** Time when the component is being processed at the workstation */
    private double processing_time;

    /** Time when the component is waiting to be sent to a workstation */
    private double delay_time;

    /** Time of the duration that a component is in a queue */
    private double queue_time;

    /** Time that a component takes to be inspected */
    private double inspection_time;


    /**
     * The constructor for the Component object
     * @param type the type of Component this object will represent
     */
    public Component(String type){
        start_time = System.nanoTime();
        this.type = type;
        processing_time = 0;
        delay_time = 0;
        queue_time = 0;
        inspection_time = 0;
    }

    /**
     * Get the type of this component
     * @return the type of this component
     */
    public String getType() {
        return type;
    }

    /**
     * Get the processing time of the component
     * @return
     */
    public double getProcessing_time() {
        return processing_time;
    }

    /**
     * Set the processing time of the component
     * @param processing_time the time it took to process this component
     */
    public void setProcessing_time(double processing_time) {
        this.processing_time = processing_time;
    }

    /**
     * Get the delay that an inspector takes before they send it to a workstation
     * @return the delay time
     */
    public double getDelay_time() {
        return delay_time;
    }

    /**
     * Set the delay time of the component
     * @param delay_time the delay time of the component
     */
    public void setDelay_time(double delay_time) {
        this.delay_time = delay_time;
    }

    /**
     * Get the time that this components stays in the workstations buffer
     * @return the time that this component stays in the workstation buffer
     */
    public double getQueue_time() {
        return queue_time;
    }

    /**
     * Set the queue time
     * @param queue_time the time that the components stays in the workstations buffer
     */
    public void setQueue_time(double queue_time) {
        this.queue_time = queue_time;
    }

    /**
     * Get the time that it takes to inspect this component
     * @return the time it takes to inspect this component
     */
    public double getInspection_time() {
        return inspection_time;
    }


    /**
     * Set the time that it takes to inspect this component
     * @param inspection_time the time it takes to inspect the component
     */
    public void setInspection_time(double inspection_time) {
        this.inspection_time = inspection_time;
    }

    public double getStart_time() {
        return start_time;
    }
}

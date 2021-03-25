public class Component {

    /**
     * The Type of component that this component is
     */
    private String type;

    /** Time when the component is being processed at the workstation */
    private double processing_time;

    /** Time when the component is waiting to be sent to a workstation */
    private double delay_time;

    /** Time of the duration that a component is in a queue */
    private double queue_time;

    /** Time that a component takes to be inspected */
    private double inspection_time;


    public Component(String type){
        this.type = type;
        processing_time = 0;
        delay_time = 0;
        queue_time = 0;
        inspection_time = 0;
    }

    public String getType() {
        return type;
    }

    public double getProcessing_time() {
        return processing_time;
    }

    public void setProcessing_time(double processing_time) {
        this.processing_time = processing_time;
    }

    public double getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(double delay_time) {
        this.delay_time = delay_time;
    }

    public double getQueue_time() {
        return queue_time;
    }

    public void setQueue_time(double queue_time) {
        this.queue_time = queue_time;
    }

    public double getInspection_time() {
        return inspection_time;
    }

    public void setInspection_time(double inspection_time) {
        this.inspection_time = inspection_time;
    }
}

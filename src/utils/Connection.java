package utils;

public class Connection {

    //NOTE: DATA FIELDS
    private Node initial;
    private Node last;
    private double weight;
    private boolean isEnabled;


    //NOTE: CONSTRUCTORS
   public Connection(Node initial, Node last, double weight){
       this.initial = initial;
       this.last = last;
       this.weight = weight;
       this.isEnabled = true;
    }


    //NOTE: GETTERS & SETTERS
    public Node getInitial() {
        return initial;
    }
    public Node getLast() {
        return last;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}

package utils;

import java.util.List;

public class Node {

    //NOTE: DATA FIELDS


    String type;
    Double value;
    int id;

    //NOTE: CONSTRUCTOR


    Node(int id, String type){
        this.id = id;
        this.type = type;
    }

    //NOTE: GETTERS


    public double getValue() {
        return this.value;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }


    //NOTE: SETTERS


    public void setValue(double value) {
        this.value = value;
    }

    //NOTE: METHODS


    public double sigmoid(double number){
        return 1/(1+ Math.exp(-number));
    }

    public double compute(List<Connection> connections){
        if(this.type.equals("input")) return this.value;

        this.value = connections.stream().
                filter(Connection::isEnabled)
                .mapToDouble(conn -> conn.getWeight() * conn.getInitial()
                        .getValue()).sum();

        return sigmoid(this.value);
    }


}

package utils;

import breakout.Breakout;
import breakout.BreakoutBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Genome implements GameController{

    //NOTE: DATA FIELDS
    public List<Node> nodes = new ArrayList<Node>();
    public List<Connection> connections = new ArrayList<Connection>();

    private int number_of_input_nodes = 7;
    private int number_of_output_nodes = 1;

    public BreakoutBoard breakout;

    //NOTE: CONSTRUCTORS
    Genome(){

        for (int i  = 0; i < number_of_input_nodes; i++){
         add_node("input");
        }
        for(int i = 0; i < number_of_output_nodes; i++){
         add_node("output");
        }
        for(int i = 0; i < number_of_output_nodes; i++){
            for(int j = 0; j < number_of_input_nodes; j++){
                add_connection(this.nodes.get(j),
                        this.nodes.get(i+number_of_input_nodes-1),
                        get_random_between(-1,1));
            }
        }

        //INIT GAME
        this.breakout = new BreakoutBoard(this, false, (int) Math.random() * 1000);
        this.breakout.runSimulation();
    }


    //NOTE: METHODS
    //FIXME: CHANGE TO ENUMS INSTEAD OF STRINGS
    private List<Node> input_nodes() {
        return nodes.stream()
                .filter(node -> node.getType().equals("input"))
                .collect(Collectors.toList());
    }

    private List<Node> output_nodes() {
        return nodes.stream()
                .filter(node -> node.getType().equals("output"))
                .collect(Collectors.toList());
    }
    public List<Double> foward(double[] inputs){
        for(int i = 0; i < input_nodes().size(); i++){ //O(n)
            input_nodes().get(i).setValue(inputs[i]);
        }
        nodes.forEach(node -> {
            List<Connection> incoming = connections
                    .stream()
                    .filter(connection -> connection.isEnabled() && connection.getInitial().equals(node))
                    .collect(Collectors.toList());
        });
        return output_nodes().stream()
                .map(Node::getValue).collect(Collectors.toList());

    }

    public Node add_node(String node_type){
//        if(node_type != "input" || node_type != "hidden" || node_type != "output")
//            throw new RuntimeException("ERROR: WRONG TYPE AT GENOME add_node");

        int node_id = this.nodes.size();
        Node new_node = new Node(node_id, node_type);
        this.nodes.add(new_node);
        return new_node;
    }

    public Connection add_connection(Node initial, Node last, double weight){
        Connection new_connection = new Connection(initial,last,weight);
        this.connections.add(new_connection);
        return new_connection;
    }

    public Node get_random_node(){
        return this.nodes.get((int) Math.floor(Math.random() * this.nodes.size()));
    }

    public Connection get_random_connection(){
        return this.connections.get((int) Math.floor(Math.random() * this.connections.size()));
    }

    public static double get_random_between(double min, double max){
        return (int) (Math.random() * (max - min) + min);
    }



    //NOTE: AKA FOWARD
    @Override
    public int nextMove(int[] currentState) {
        for(int i = 0; i < this.input_nodes().size(); i++){
            this.input_nodes().get(i).setValue(currentState[i]);
        }
        this.nodes.forEach(node -> {
            List<Connection> incoming = connections
                    .stream()
                    .filter(connection -> connection.isEnabled() && connection.getInitial().equals(node))
                    .collect(Collectors.toList());
            node.compute(incoming);
        });
        double value = output_nodes().getLast().value;
        if(value < 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public double getFitness(){

       return breakout.getFitness();
    }
}

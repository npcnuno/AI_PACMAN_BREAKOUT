package utils;

public enum node_types {
    INPUT("input"),
    HIDDEN("hidden"),
    OUTPUT("output");

    private String type;

    node_types(String type){
        this.type = type;
    }
}

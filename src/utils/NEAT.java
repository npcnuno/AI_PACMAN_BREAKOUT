package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Genome.get_random_between;

public class NEAT {

    //NOTE: DATA FIELDS
    int population_size = 1;
    List<Genome> population = new ArrayList<Genome>();
    int generation = 0;
    double mutate_weight_chance = 0.8;
    double new_connection_chance = 0.05;
    double new_node_chance = 0.03;



    //NOTE: CONSTRUCTORS
    public NEAT(int population_size){
        this.population_size = population_size;


        for(int i = 0; i < population_size; i++){
            this.population.add(new Genome());
        }
    }


    //NOTE: MEtHODS
    private Genome mutate(Genome genome){
        if(Math.random() < this.mutate_weight_chance){
            Connection connection = genome.get_random_connection();
            connection.setWeight(connection.getWeight() + get_random_between(-0.5,0.5));
        }

        if(Math.random() < this.new_connection_chance){
            Node node1 = genome.get_random_node();
            Node node2 = genome.get_random_node();

            if(node1.type != node2.type){
                genome.add_connection(node1,node2,get_random_between(-1,1));
            }
        }

        if(Math.random() < this.new_node_chance){
            Connection connection = genome.get_random_connection();
            connection.setEnabled(false);
            Node middle_node = genome.add_node("hidden");
            genome.add_connection(connection.getInitial(),middle_node,1);
            genome.add_connection(middle_node,connection.getLast(),connection.getWeight());
        }
        return genome;
    }

    private Genome reproduce(Genome parent){
        Genome son = new Genome();

        parent.nodes.forEach(node -> {
            son.add_node(node.type);
        });
        parent.connections.forEach(connection -> {
            son.add_connection(son.nodes.get(connection.getInitial().id),
                    son.nodes.get(connection.getLast().id),
                    connection.getWeight());
        });

       Genome genome =  mutate(son);

       return genome;
    }

    private void evolve(){
        List<Double> fitness = this.population.stream().map(genome -> this.compute_fitness(genome))
                .collect(Collectors.toList());
        double totalFitness = fitness.stream().reduce(0.0, (acc, next) -> acc + next);

        List<Genome> new_population = new ArrayList<>();
        while(new_population.size() < this.population_size){
            double pick = Math.random() * totalFitness;
            double current = 0;

            for(int i = 0; i < this.population.size(); i++){
                Genome genome = this.population.get(i);

                current += fitness.get(i);
                if(current> pick){
                    Genome son = this.reproduce(genome);
                    new_population.add(son);
                    break;
                }
            }
        }

        this.population = new_population;
        this.generation++;
    }

    //FIXME: COULD BREAK GAME
    public double compute_fitness(Genome genome){
        return genome.getFitness();
    }


    //NOTE: FUNCTIONS
    public static Genome find_best(int population_size, int max_generations){
        NEAT neat = new NEAT(population_size);

        Genome best_genome = new Genome();
        double best_fitness = 0.0;

        neat.generation = 0;

        for(int i = 0; i < max_generations; i++){
            neat.evolve();

            List<Double> fitnesses = neat.population.stream().map(genome -> genome.getFitness()).collect(Collectors.toList());
            double current_best_fitness = Collections.max(fitnesses);
            int best_genome_id = fitnesses.indexOf(current_best_fitness);

            if(current_best_fitness > best_fitness){
                best_fitness = current_best_fitness;
                best_genome = neat.population.get(best_genome_id);
            }
        }
        return best_genome;
    }
}

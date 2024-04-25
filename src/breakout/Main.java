package breakout;

import utils.Genome;
import utils.breakout_game_controler;

import static utils.NEAT.find_best;

public class Main {
        public static void main(String[] args){

            Genome find_best_genome = find_best(100,100);
            new Breakout(find_best_genome,1);
        }
    

}

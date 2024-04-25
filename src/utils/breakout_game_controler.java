package utils;

import java.util.Arrays;

public class breakout_game_controler implements GameController{

    public breakout_game_controler(){

    }
    @Override
    public int nextMove(int[] currentState) {
        System.out.println(Arrays.toString(currentState));
        return 0;
    }
}

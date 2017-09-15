package janbiko.urgaining;

import java.util.ArrayList;

/**
 * Created by Jannik on 15.09.2017.
 */

public class Exercise {

    private String name;
    private ArrayList<Integer> weight;
    private ArrayList<int[]> sets;

    public Exercise(String name, ArrayList<Integer> weight, ArrayList<int[]> sets){
        this.name = name;
        this.weight = weight;
        this.sets = sets;

    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getWeight() {
        return weight;
    }

    public ArrayList<int[]> getSets() {
        return sets;
    }
}

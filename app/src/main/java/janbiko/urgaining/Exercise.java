package janbiko.urgaining;

import java.util.ArrayList;

/**
 * Created by Jannik on 15.09.2017.
 */

public class Exercise {

    private String name;
    private ArrayList<Integer> sets;

    public Exercise(String name, ArrayList<Integer> sets){
        this.name = name;
        this.sets = sets;

    }

    public String getName() {
        return name;
    }



    public ArrayList<Integer> getSets() {
        return sets;
    }
}

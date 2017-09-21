package janbiko.urgaining;


/**
 * Created by Jannik on 15.09.2017.
 */

public class Exercise {
    // class to store all crucial values needed for an exercise

    private String name;
    private int sets;
    private String workoutName;

    public Exercise(String name, int sets, String workoutName){
        this.name = name;
        this.sets = sets;
        this.workoutName = workoutName;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public String getWorkoutName() {
        return workoutName;
    }
}

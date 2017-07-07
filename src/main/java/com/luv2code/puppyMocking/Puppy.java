package com.luv2code.puppyMocking;

import java.util.Random;

/**
 * Created by buckl on 07/07/2017.
 */
public class Puppy {
    private final String name;
    public Human owner;
    private int energyLevel;
    private Random puppyRandomness;

    public Puppy(String name, Human owner){
        this.name = name;
        this.owner = owner;
        energyLevel = 100;
        puppyRandomness = new Random();
    }

    public static Puppy createPuppy(String name, Human human) {
        human.isSoHappy();
        return new Puppy(name, human);
    }

    public void chaseTail(){
        bark();
        energyLevel -= getRandomInt(10);
    }
    public void bark(){
        System.out.println("WOOF!");
    }

    public void goOnWalk(int length){
        performBusiness();
        energyLevel -= length;
        wipeOffFeet();
    }

    public void wipeOffFeet(){
        System.out.println("Paws on fleek");
    }

    private void performBusiness() {
        System.out.println("When you gotta go you gotta go...");
    }

    public void performPuppyTasks(){
        eat();
        sleep();
        play();
    }

    private void eat(){
        energyLevel+= getRandomInt(10);
    }

    private void sleep() {
        energyLevel+= getRandomInt(10);
    }

    private void play() {
        energyLevel-= getRandomInt(10);
    }

    private int getRandomInt(int bounds){
        return puppyRandomness.nextInt(bounds);
    }

    public String getName() {
        return name;
    }
}

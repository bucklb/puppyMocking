package com.luv2code.puppyMocking;

/**
 * Created by buckl on 07/07/2017.
 *
 *  from code at http://www.johnmullins.co/blog/2015/02/15/beginners-guide-to-using-mockito-and-powermockito-to-unit-test-java/
 *  and just so I can test stuff
 *
 */
public class Human {

    public String name;
    public Puppy puppy;

    public Human(String name) {
        this.name = name;
    }

    public void buyPuppy(String name){
        puppy = Puppy.createPuppy(name, this);
    }

    public void walkWithPuppy() {
        puppy.goOnWalk(15);
    }

    public static void main(String[] args) {
        Human john = new Human("John");
        john.buyPuppy("Gatsby");
        john.puppy.performPuppyTasks();
        john.walkWithPuppy();
    }

    public void isSoHappy() {
        System.out.println("Yay!");
    }


}

package com.luv2code.puppyMocking;

/**
 * Created by buckl on 08/07/2017.
 *
 * Want something we can create "for" a puppy so I can play with powermock of object instantiation ...
 *
 */
public class Toy {

    private String toyName;

    // Just create something that has a name
    public Toy(String toyName){
        this.toyName=toyName;
    }

    // Just return the toy name
    public String getToyName() {
        return toyName;
    }
}

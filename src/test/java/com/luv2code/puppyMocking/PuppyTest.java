package com.luv2code.puppyMocking;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
//import org.powermock.modules.testng.PowerMockTestCase;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.times;

/**
 * Created by buckl on 07/07/2017.
 *
 * FROM : http://www.johnmullins.co/blog/2015/02/15/beginners-guide-to-using-mockito-and-powermockito-to-unit-test-java/
 *
 * - - NOTE - - unlike the other mocking I've been looking at, this project has NO interfaces so may not be typical
 *
 */
@PrepareForTest({Puppy.class})
@RunWith(PowerMockRunner.class)
public class PuppyTest {

    // Not mocking nor powermocking, but handling exceptions is a good thing
    @Test(expected = IllegalArgumentException.class)
    public void testCreatePuppyWithoutHumanIsException(){
        // Puppy can't exist without a human (?).  The createPuppy method calls the isSoHappy() method
        Puppy puppy = Puppy.createPuppy("Gatsby", null);
    }



    // Override certain events and also note how many times some things happen
    @Test
    public void testCreatePuppy() throws Exception {
        //Mocking
        Human human = Mockito.mock(Human.class);

        // Puppy can't exist without a human (?).  The createPuppy method calls the isSoHappy() method
        Puppy puppy = Puppy.createPuppy("Gatsby", human);

        // Not a sensible use of the mock, but illustrates that "buyPuppy" doesn't do anything with the mock
//        human.buyPuppy("Great");

        // Not much to do with any mocking
        assert(puppy != null);
        assert(puppy.getName().equals("Gatsby"));

        //Verifying.  Check that our mocked human got made happy, but only the once
        //            We won't see the "Yay!" comment as we don't call the REAL isSoHappy on a real human
        //            just calling a dummied up method on a dummied up human
        Mockito.verify(human, times(1)).isSoHappy();

    }

    // Spy - to avoid WOOFing
    @Test
    public void testChaseTail() throws Exception {
        // The human doesn't need to be any more real than necessary to allow puppy creation
        Human human = Mockito.mock(Human.class);
        //Spying
        // We create a real puppy, so when it chases its tail it will also bark.  Being real that involves a "woof" that we don't want to witness
        // Set up a Spy around the puppy so we can override some of its behaviour, without having to mock ALL its behaviour
        Puppy puppy = Mockito.spy(new Puppy("Gatsby", human));

        // We want to know that the bark method get called, but without seeing the "real" outcome, so suppress the "Woof"
        Mockito.doNothing().when(puppy).bark();

        // chaseTail will change the energy and trigger bark
        puppy.chaseTail();

        // Check that bark did get called (which we can't see/hear directly as we suppressed the woof)
        Mockito.verify(puppy, times(1)).bark();
    }

    // Access a private method (so that we can override it)
    @Test
    public void testGoOnWalk() throws Exception {

        // Only need vestigal human
        Human human = Mockito.mock(Human.class);

        //Power Spying - so we can access a private method that normal mocking can't get to.
        // At heart is a "real" puppy that will do puppy stuff unless we interven
        // Once again we'll suppress the outcome of the method - we're just bothered whether it gets called
        Puppy puppy = PowerMockito.spy(new Puppy("Gatsby", human));
        PowerMockito.doNothing().when(puppy, "performBusiness");

        //Can combine regular and power.  WipeOffFeet is public so can be normal mocked.
        Mockito.doNothing().when(puppy).wipeOffFeet();

        // goOnWalk calls private performBusiness, public wipeOffFeet and sets a private variable
        puppy.goOnWalk(15);
        Mockito.verify(puppy, times(1)).wipeOffFeet();
    }

    // Deal with a static method (createPuppy)
    @Test
    public void testBuyPuppy() throws Exception {
        //Mocking static (class) so need PowerMockito
        PowerMockito.mockStatic(Puppy.class);

        // Creating real human
        Human human = new Human("John");

        // Standard mock of a puppy (so it won't do normal puppy stuff)
        Puppy puppy = Mockito.mock(Puppy.class);

        //Static mocking and matchers.  If the Puppy class sees an attempt to create a "Gatsby" puppy then return the mocked (nameless) puppy
        // the "real" createPuppy isn't called (for Gatsby) so the isSoHappy isn't called so the "Yay" doesn't appear
        PowerMockito.when(Puppy.createPuppy(Mockito.eq("Gatsby"), Mockito.any(Human.class))).thenReturn(puppy);

        // Request a Gatsby puppy (under the covers of buyPuppy) and expect the proper buyPuppy not too happen
        human.buyPuppy("Gatsby");
        assert(human.puppy != null);

    }

    // Call private methods directly (which shouldn't be available/visible to us) and ditto private variables
    @Test
    public void testEat() throws Exception {
        // vestigal human
        Human human = Mockito.mock(Human.class);

        // Normal puppy that we can override methods on
        Puppy puppy = PowerMockito.spy(new Puppy("Gatsby",human));

        //Get private variables.  EnergyLevel is not normally available to us
        int energy = Whitebox.getInternalState(puppy, "energyLevel");

        //Call private method directly (in other examples we called private methods via public calls)
        Whitebox.invokeMethod(puppy, "eat");
        int energyAfterwards = Whitebox.getInternalState(puppy, "energyLevel");

        System.out.println(energy + " > " + energyAfterwards);
        assert(energy <= energyAfterwards);
    }


    // Contrived test to make sure we can mock a private object instantiation if we wish
    @Test
    public void testAcquireToy(){

        String givenToyName="given by mock";
        // vestigal human
        Human human = Mockito.mock(Human.class);

        // Choices - a normal puppy that will WOOF or create one in a power mock and quieten the woof
//        Puppy puppy=new Puppy("fred",human);
        Puppy puppy = Mockito.spy(new Puppy("SpiderMonkey", human));
        Mockito.doNothing().when(puppy).bark();

        // Thing we most want to do is to specify/override the Toy that the puppy acquires
        Toy testToy=new Toy(givenToyName);

        // Buggering about mocking New could cause an exception (probs not a surprise).
        // - specifying withNoArguments when he constructor called takes an argument generates an exception ...
        // Specify that any new Toy should be the one we provide -
        try{
            PowerMockito.whenNew(Toy.class).withAnyArguments().thenReturn(testToy);
        } catch (Exception e){
            // Not the best exception handling in the world, but hey
            System.out.println("Exception when doing whenNew !!");
        }

        // Make sure the toy name matches the one that we insisted upon
        Assert.assertEquals(givenToyName,puppy.acquireToy());

    }




}

package edu.pdx.cs399J.family.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.pdx.cs399J.family.Marriage;
import edu.pdx.cs399J.family.Person;

/**
 * This class tests the functionality of the <code>Marriage</code>
 * class.
 */
public class MarriageTest extends FamilyTestCase {

  /**
   * Returns a suite containing all of the tests in this class
   */
  public static Test suite() {
    return(new TestSuite(MarriageTest.class));
  }

  /**
   * Creates a new <code>MarriageTest</code> for running the test of a
   * given name
   */
  public MarriageTest(String name) {
    super(name);
  }

  //////// main program

  /**
   * A program that allow the user to run tests as named on the
   * command line.
   */
  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    if (args.length == 0) {
      suite.addTest(suite());

    } else {
      for (int i = 0; i < args.length; i++) {
        suite.addTest(new MarriageTest(args[i]));
      }
    }

    junit.textui.TestRunner.run(suite);
  }

  ////////  Test cases

  public void testMarriage() {
    Person husband = new Person(1, Person.MALE);
    Person wife = new Person(2, Person.FEMALE);
    Marriage m = new Marriage(husband, wife);
    assertEquals(husband, m.getHusband());
    assertEquals(wife, m.getWife());
  }

  public void testMarriageHusbandNotMale() {
    Person husband = new Person(1, Person.FEMALE);
    Person wife = new Person(2, Person.FEMALE);
    try {
      new Marriage(husband, wife);
      fail("Should have thrown an IllegalArgumentException");

    } catch (IllegalArgumentException ex) {
      // pass ...
    }
  }

  public void testMarriageWifeNotFemale() {
    Person husband = new Person(1, Person.MALE);
    Person wife = new Person(2, Person.MALE);
    try {
      new Marriage(husband, wife);
      fail("Should have thrown an IllegalArgumentException");

    } catch (IllegalArgumentException ex) {
      // pass ...
    }
  }

}
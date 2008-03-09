package edu.pdx.cs399J.lang;

/**
 * This class represents a bee.  Bees can fly.
 */
public class Bee extends Insect implements Flies {

  public Bee(String name) {
    this.name = name;
  }

  public void fly() {
    System.out.println("I'm flying");
  }

}
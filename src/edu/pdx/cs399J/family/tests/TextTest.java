package edu.pdx.cs399J.family.tests;

import edu.pdx.cs399J.family.*;
import java.io.*;
import java.util.*;
import junit.framework.*;

/**
 * This class tests the functionality of the <code>TextDumper</code>
 * and <code>TextParser</code> classes.
 */
public class TextTest extends FamilyTreeConversionTestCase {

  /**
   * Returns a suite containing all of the tests in this class
   */
  public static Test suite() {
    return(new TestSuite(TextTest.class));
  }

  /**
   * Creates a new <code>TextTest</code> for running the test of a
   * given name
   */
  public TextTest(String name) {
    super(name);
  }

  ////////  Helper Methods

  /**
   * Converts a FamilyTree to text and returns the text as a String.
   */
  protected String getStringFor(FamilyTree tree) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    Dumper dumper = new TextDumper(pw);
    dumper.dump(tree);
    String s = sw.toString();
    if (Boolean.getBoolean("TextTest.DUMP_TEXT")) {
      System.out.println(s);
    }
    return s;
  }

  /**
   * Parsers a FamilyTree from a String containing XML
   */
  protected FamilyTree getFamilyTreeFor(String s) 
    throws FamilyTreeException {

    // Parse the XML from the String
    StringReader sr = new StringReader(s);
    Parser parser = new TextParser(sr);
    return parser.parse();
  }

  ////////  Additional test methods

  /**
   * Files contains person with same id multiple times
   */
  public void testSameIdMultipleTimes() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 1");
    pw.println("id: 1");
    pw.println("P 1");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "already has person 1");
    }
  }

  public void testBadId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 1");
    pw.println("id: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "id: Q");
    }
  }

  public void testGenderBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("g: 1");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testBadGender() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("g: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testInvalidGender() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("g: 3");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testMissingGender() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testFemaleGender() {
    int id = 1;
    int gender = Person.FEMALE;

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: " + id);
    pw.println("g: " + gender);
    
    FamilyTree tree = getFamilyTreeFor(sw.toString());
    Person p = tree.getPerson(id);
    assertNotNull(p);
    assertEquals(gender, p.getGender());
  }

  public void testMaleGender() {
    int id = 1;
    int gender = Person.MALE;

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: " + id);
    pw.println("g: " + gender);
    
    FamilyTree tree = getFamilyTreeFor(sw.toString());
    Person p = tree.getPerson(id);
    assertNotNull(p);
    assertEquals(gender, p.getGender());
  }

  public void testFirstNameBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("fn: Test");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testMiddleNameBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("mn: Test");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testLastNameBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("ln: Test");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testFatherBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("f: 2");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testBadFatherId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("f: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "father id: Q");
    }
  }

  public void testNonExistentFatherId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("f: 3");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "Father 3 does not exist");
    }
  }

  public void testMotherBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("m: 2");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testBadMotherId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("m: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "mother id: Q");
    }
  }

  public void testNonExistentMotherId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 4");
    pw.println("id: 1");
    pw.println("g: 1");
    pw.println("m: 3");
    pw.println("f: 2");
    pw.println("P 2");
    pw.println("id: 2");
    pw.println("g: 2");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), 
                     ex.getMessage(), "Mother 3 does not exist");
    }
  }

  public void testDOBBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("dob: 12/30/1998 3:15 PM");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testBadDOB() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("dob: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "date of birth: Q");
    }
  }

  public void testDODBeforeId() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("dod: 12/30/1998 3:15 PM");
    pw.println("id: 1");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      // pass...
    }
  }

  public void testBadDOD() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.println("P 2");
    pw.println("id: 1");
    pw.println("dod: Q");
    
    try {
      getFamilyTreeFor(sw.toString());
      fail("Should have thrown a FamilyTreeException");

    } catch (FamilyTreeException ex) {
      assertContains(ex.getMessage(), "date of death: Q");
    }
  }

}
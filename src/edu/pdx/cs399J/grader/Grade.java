package edu.pdx.cs410J.grader;

import java.io.*;
import java.util.*;

import edu.pdx.cs410J.ParserException;

/**
 * This class represent the grade a student got on an assignment.
 */
public class Grade implements Notable {
  /**
   * The assignment is not complete
   */
  public static final double INCOMPLETE = -1.0;

  /**
   * Some work has been submitted, but no grade has been assigned
   */
  public static final double NOGRADE = -2.0;

  private String assignmentName;
  private double score;         // Score student received
  List notes;
  private boolean isDirty;
  

  /**
   * Creates a <code>Grade</code> for a given assignment
   */
  public Grade(String assignmentName, double score) {
    this.assignmentName = assignmentName;
    this.score = score;
    this.notes = new ArrayList();
    this.setDirty(true);  // Initially dirty
  }

  /**
   * Returns the name of the assignment that this <code>Grade</code>
   * is for.
   */
  public String getAssignmentName() {
    return this.assignmentName;
  }

  /**
   * Returns the score the student received on the assignment.
   */
  public double getScore() {
    return this.score;
  }

  /**
   * Sets the score the student received on the assignment.
   */
  public void setScore(double score) {
    this.score = score;
    this.setDirty(true);
  }

  /**
   * Returns the notes that have been made about this
   * <code>Grade</code>
   */
  public List getNotes() {
    return this.notes;
  }

  /**
   * Adds a notes about this <code>Grade</code>
   */
  public void addNote(String note) {
    this.notes.add(note);
    this.setDirty(true);
  }

  /**
   * Marks this <code>Grade</code> as dirty.
   */
  void setDirty(boolean isDirty) {
    this.isDirty = isDirty;
  }

  /**
   * Makes this <code>Grade</code> clean
   */
  void makeClean() {
    this.setDirty(false);
  }

  /**
   * Returns the "dirtiness" of this <code>Grade</code>
   */
  boolean isDirty() {
    return this.isDirty;
  }

  private static PrintWriter out = new PrintWriter(System.out, true);
  private static PrintWriter err = new PrintWriter(System.err, true);

  /**
   * Returns a brief textual description of this <code>Grade</code>
   */
  public String toString() {
    return this.assignmentName + ": " + this.score;
  }

  /**
   * Prints usage information about the main program.
   */
  private static void usage() {
    err.println("\njava Grade [options] -xmlFile xmFile " + 
                "-id id -assignment name");
    err.println("  where [options] ares:");
    err.println("  -score points          How many points student got");
    err.println("  -incomplete            Notes that the assignment "
                + "was INCOMPLETE");
    err.println("  -no-grade              No grade was given for the "
                + "assignment");
    err.println("  -note note             A note about the grade");
    err.println("\n");
    System.exit(1);
  }

  /**
   * A main program that creates/edits a student's grade.
   */
  public static void main(String[] args) {
    String xmlFile = null;
    String id = null;
    String assignment = null;
    String score = null;
    boolean incomplete = false;
    boolean noGrade = false;
    String note = null;

    // Parse the command line
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-xmlFile")) {
        if (++i >= args.length) {
          err.println("** Missing XML file name");
          usage();
        }

        xmlFile = args[i];

      } else if (args[i].equals("-id")) {
        if (++i >= args.length) {
          err.println("** Missing student id");
          usage();
        }

        id = args[i];

      } else if (args[i].equals("-assignment")) {
        if (++i >= args.length) {
          err.println("** Missing assignment name");
          usage();
        }

        assignment = args[i];

      } else if (args[i].equals("-score")) {
        if (++i >= args.length) {
          err.println("** Missing score");
          usage();
        }

        score = args[i];

      } else if (args[i].equals("-incomplete")) {
        incomplete = true;

      } else if (args[i].equals("-no-grade")) {
        noGrade = true;

      } else if (args[i].equals("-note")) {
        if (++i >= args.length) {
          err.println("** Missing note text");
          usage();
        }

        note = args[i];

      } else if (args[i].startsWith("-")) {
        err.println("** Unknown command line option: " + args[i]);
        usage();

      } else {
        err.println("** Spurious command line: " + args[i]);
        usage();
      }
    }

    // Verify command line
    if (xmlFile == null) {
      err.println("** No XML file specified");
      usage();
    }

    if (id == null) {
      err.println("** No student id specified");
      usage();
    }

    if (assignment == null) {
      err.println("** No assignment name specified");
      usage();
    }

    File file = new File(xmlFile);
    if (!file.exists()) {
      err.println("** Grade book file " + xmlFile + 
                  " does not exist");
      System.exit(1);
    }

    // Parse XML file
    GradeBook book = null;
    try {
      XmlGradeBookParser parser = new XmlGradeBookParser(file);
      book = parser.parse();

    } catch (FileNotFoundException ex) {
      err.println("** Could not find file: " + ex.getMessage());
      System.exit(1);

    } catch (IOException ex) {
      err.println("** IOException during parsing: " + ex.getMessage());
      System.exit(1);

    } catch (ParserException ex) {
      err.println("** Exception while parsing " + file + ": " + ex);
      System.exit(1);
    }

    // Get the student
    Student student = book.getStudent(id);
    if (student == null) {
      err.println("** No student with id: " + id);
      System.exit(1);
    }

    // Get the grade
    Grade grade = student.getGrade(assignment);
    if (grade == null) {
      // Create a new Grade
      double s;

      if (incomplete) {
        s = Grade.INCOMPLETE;

      } else if (noGrade) {
        s = Grade.NOGRADE;

      } else if (score == null) {
        err.println("** No score for " + assignment);
        System.exit(1);
        s = -4.2;

      } else {
        try {
          s = Double.parseDouble(score);

        } catch (NumberFormatException ex) {
          err.println("** Score is not a double: " + score);
          System.exit(1);
          s = -4.2;
        }
      }

      grade = new Grade(assignment, s);
      student.setGrade(assignment, grade);

    } else {
      // Set the grade
      if (incomplete) {
        grade.setScore(Grade.INCOMPLETE);

      } else if (noGrade) {
        grade.setScore(Grade.NOGRADE);

      } else if (score != null) {
        try {
          grade.setScore(Double.parseDouble(score));

        } catch (NumberFormatException ex) {
          err.println("** Score is not a double: " + score);
        }
      }
    }

    if (note != null) {
      grade.addNote(note);
    }

    // Write the changes back out to the XML file
    try {
      XmlDumper dumper = new XmlDumper(file);
      dumper.dump(book);

    } catch (IOException ex) {
      err.println("** While dumping to " + file + ": " + ex);
      System.exit(1);
    }
  }
}

package edu.pdx.cs410J.grader;

import edu.pdx.cs410J.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * This panel is used to display and edit <code>Assignment</code>s.
 */
public class AssignmentPanel extends JPanel {
  private static final String QUIZ = "Quiz";
  private static final String PROJECT = "Project";
  private static final String OTHER = "Other";

  // GUI components we care about
  private JTextField nameField;
  private JComboBox typeBox;
  private JTextField pointsField;
  private JTextField descriptionField;
  private NotesPanel notes;

  /**
   * Creates and adds GUI components to a new
   * <code>AssignmentPanel</code>.
   */
  public AssignmentPanel(boolean canCreate) {
    this.setLayout(new BorderLayout());

    // Panel containing information about an assignment
    JPanel infoPanel = new JPanel();
    infoPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    infoPanel.setLayout(new BorderLayout());

    JPanel labels = new JPanel();
    labels.setLayout(new GridLayout(0, 1));
    labels.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
    labels.add(new JLabel("Name:"));
    labels.add(new JLabel("Max points:"));
    labels.add(new JLabel("Type:"));
    labels.add(new JLabel("Description:"));

    JPanel fields = new JPanel();
    fields.setLayout(new GridLayout(0, 1));
    this.nameField = new JTextField(8);
    fields.add(this.nameField);
    this.pointsField = new JTextField(5);
    fields.add(this.pointsField);

    this.typeBox = new JComboBox();
    this.typeBox.addItem(QUIZ);
    this.typeBox.addItem(PROJECT);
    this.typeBox.addItem(OTHER);
    fields.add(this.typeBox);

    this.descriptionField = new JTextField(20);
    fields.add(this.descriptionField);

    infoPanel.add(labels, BorderLayout.WEST);
    infoPanel.add(fields, BorderLayout.CENTER);

    this.add(infoPanel, BorderLayout.NORTH);

    if(!canCreate) {
      this.nameField.setEditable(false);
    }

    // Add a NotePanel
    this.notes = new NotesPanel();
    this.add(notes, BorderLayout.CENTER);
  }

  /**
   * Creates a new <code>Assignment</code> based on the contents of
   * this <code>AssignmentPanel</code>.
   */
  public Assignment createAssignment() {
    // Get the name and max points of the assignment
    String name = nameField.getText();
    if(name == null || name.equals("")) {
      String s = "No assignment name specified";
      JOptionPane.showMessageDialog(AssignmentPanel.this, 
                                    new String[] {s},
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
      return(null);
    }

    String points = pointsField.getText();
    if(points == null || points.equals("")) {
      String s = "No points value specified";
      JOptionPane.showMessageDialog(AssignmentPanel.this, 
                                    new String[] {s},
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
      return(null);
    }

    // Create a new Assignment object
    try {
      double d = Double.parseDouble(points);
      Assignment newAssign = new Assignment(name, d);
      updateAssignment(newAssign);
      this.notes.addAllNotesTo(newAssign);
      return(newAssign);
      
    } catch(NumberFormatException ex) {
      String s = points + " is not a number";
      JOptionPane.showMessageDialog(AssignmentPanel.this, 
                                    new String[] {s},
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
      return(null);
    }    
  }

  /**
   * Displays the contents of a given <code>Assignment</code> in this
   * <code>AssignmentPanel</code>.
   */
  public void displayAssignment(Assignment assign) {
    this.nameField.setText(assign.getName());
    this.pointsField.setText("" + assign.getPoints());
    if(assign.getType() == Assignment.QUIZ) {
      this.typeBox.setSelectedItem(QUIZ);

    } else if(assign.getType() == Assignment.PROJECT) {
      this.typeBox.setSelectedItem(PROJECT);

    } else if(assign.getType() == Assignment.OTHER) {
      this.typeBox.setSelectedItem(OTHER);

    } else {
      String s = "Invalid assignment type: " + assign.getType();
      throw new IllegalArgumentException(s);
    }

    String description = assign.getDescription();
    if(description != null) {
      this.descriptionField.setText(description);
    }

    this.notes.setNotable(assign);
  }

  /**
   * Fills in the contents of an <code>Assignment</code> based on the
   * contents of this <code>AssignmentPanel</code>.
   */
  public void updateAssignment(Assignment assign) {
    String points = pointsField.getText();
    if(points == null || points.equals("")) {
      String s = "No points value specified";
      JOptionPane.showMessageDialog(AssignmentPanel.this, 
                                    new String[] {s},
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      double d = Double.parseDouble(points);
      assign.setPoints(d);
      
    } catch(NumberFormatException ex) {
      String s = points + " is not a number";
      JOptionPane.showMessageDialog(AssignmentPanel.this, 
                                    new String[] {s},
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }    

    String type = (String) this.typeBox.getSelectedItem();

    if(type.equals(QUIZ)) {
      assign.setType(Assignment.QUIZ);

    } else if(type.equals(PROJECT)) {
      assign.setType(Assignment.PROJECT);

    } else if(type.equals(OTHER)) {
      assign.setType(Assignment.OTHER);

    } else {
      String s = "Unknown assignment type: " + type;
      throw new IllegalArgumentException(s);
    }

    String description = this.descriptionField.getText();

    if(description != null && !description.equals("")) {
      assign.setDescription(description);
    }

    // Adding notes is taken care of by the NotesPanel
  }

  /**
   * Test program
   */
  public static void main(String[] args) {
    String fileName = args[0];
    String assignName = args[1];

    GradeBook book = null;
    try {
      XmlParser parser = new XmlParser(fileName);
      book = parser.parse();

    } catch(FileNotFoundException ex) {
      System.err.println("** Could not find file: " + ex.getMessage());
      System.exit(1);
      
    } catch(IOException ex) {
      System.err.println("** IOException during parsing: " + ex.getMessage());
      System.exit(1);

    } catch(ParserException ex) {
      System.err.println("** Error during parsing: " + ex);
      System.exit(1);
    }

    Assignment assign = book.getAssignment(assignName);
    if(assign == null) {
      System.err.println("Not such assignment: " + assignName);
      System.exit(1);
    }

    AssignmentPanel assignPanel = new AssignmentPanel(false);
    assignPanel.displayAssignment(assign);

    JFrame frame = new JFrame("AssignmentPanel test");
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(1);
        }
      });

    frame.getContentPane().add(assignPanel);
    
    frame.pack();
    frame.setVisible(true);
  }
}
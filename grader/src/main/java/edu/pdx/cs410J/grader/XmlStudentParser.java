package edu.pdx.cs410J.grader;

import edu.pdx.cs410J.ParserException;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * This class parses XML data that describes a <code>Student</code>.
 */
class XmlStudentParser extends XmlHelper {

  /** The Reader from which the XML Data is read */ 
  private Reader reader;

  ///////////////////////  Constructors  ////////////////////////

  /**
   * Creates a new <code>XmlStudentParser</code> that reads XML data
   * from a given <code>Reader</code>
   */
  public XmlStudentParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Creates a new <code>XmlStudentParser</code> that reads XML data
   * from a given <code>File</code>
   */
  public XmlStudentParser(File file) throws IOException {
    this(new FileReader(file));
  }

  /////////////////////  Instance Methods  //////////////////////

  /**
   * Extracts a <code>Grade</code> from a chuck of a DOM tree
   */ 
  private Grade extractGradeFrom(Element root) throws ParserException {
    String name = null;
    String score = null;
    List notes = null;

    NodeList kids = root.getChildNodes();
    for (int j = 0; j < kids.getLength(); j++) {
      Node kidNode = kids.item(j);
      if (!(kidNode instanceof Element)) {
        continue;
      }
      
      Element kid = (Element) kidNode;
      if (kid.getTagName().equals("name")) {
        name = extractTextFrom(kid);
        
      } else if (kid.getTagName().equals("score")) {
        score = extractTextFrom(kid);
        
      } else if (kid.getTagName().equals("notes")) {
        notes = extractNotesFrom(kid);
      }
    }
    
    if (name == null || score == null) {
      throw new ParserException("Malformed grade");
    }
    
    try {
      double s = Double.parseDouble(score);
      Grade grade = new Grade(name, s);
      if (notes != null) {
	Iterator iter = notes.iterator();
	while (iter.hasNext()) {
	  String note = (String) iter.next();
	  grade.addNote(note);
	}
      }

      return grade;

    } catch (NumberFormatException ex) {
      throw new ParserException("Malformatted number: " + score);
    }
  }

  /**
   * Creates a new <code>Student</code> from the contents of a file.
   */
  public Student parseStudent() throws ParserException {

    // Parse the XML file
    Document doc = null;
    try {
      DocumentBuilderFactory factory =
	DocumentBuilderFactory.newInstance();
      factory.setValidating(true);

      DocumentBuilder builder = 
	factory.newDocumentBuilder();
      builder.setErrorHandler(this);
      builder.setEntityResolver(this);

      doc = builder.parse(new InputSource(this.reader));;

    } catch (ParserConfigurationException ex) {
      throw new ParserException("While parsing XML source: " + ex);

    } catch (SAXException ex) {
      throw new ParserException("While parsing XML source: " + ex);

    } catch (IOException ex) {
      throw new ParserException("While parsing XML source: " + ex);
    }

    Element root = null;
    if (doc != null) {
      root = doc.getDocumentElement();
    }
    if (doc == null || root == null) {
      throw new ParserException("Document parsing failed");
    }

    if (!root.getTagName().equals("student")) {
      String s = "XML data does not contain a student";
      throw new ParserException(s);
    }

    Student student = null;

    NodeList children = root.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
      if (!(node instanceof Element)) {
        continue;
      }

      Element child = (Element) node;
      if (child.getTagName().equals("id")) {
        String idFromFile = extractTextFrom(child);
//         if (id != null && !id.equals(idFromFile)) {
//           throw new ParserException(file + 
//                                     " does not contain student " +
//                                     student.getId());
//         }

        student = new Student(idFromFile);

      } else if (child.getTagName().equals("firstName")) {
        String firstName = extractTextFrom(child);
        student.setFirstName(firstName);

      } else if (child.getTagName().equals("lastName")) {
        String lastName = extractTextFrom(child);
        student.setLastName(lastName);

      } else if (child.getTagName().equals("nickName")) {
        String nickName = extractTextFrom(child);
        student.setNickName(nickName);

      } else if (child.getTagName().equals("email")) {
        String email = extractTextFrom(child);
        student.setEmail(email);

      } else if (child.getTagName().equals("ssn")) {
        String ssn = extractTextFrom(child);
        student.setSsn(ssn);

      } else if (child.getTagName().equals("major")) {
        String major = extractTextFrom(child);
        student.setMajor(major);

      } else if (child.getTagName().equals("grades")) {
        NodeList kids = child.getChildNodes();
        for (int j = 0; j < kids.getLength(); j++) {
          Node kidNode = kids.item(j);
          if (!(kidNode instanceof Element)) {
            continue;
          }

          Element kid = (Element) kidNode;
          if (kid.getTagName().equals("grade")) {
            Grade grade = extractGradeFrom(kid);
            student.setGrade(grade.getAssignmentName(), grade);
          }
        }

      } else if (child.getTagName().equals("late")) {
        NodeList kids = child.getChildNodes();
        for (int j = 0; j < kids.getLength(); j++) {
          Node kidNode = kids.item(j);
          if (!(kidNode instanceof Element)) {
            continue;
          }

          Element kid = (Element) kidNode;
          if (kid.getTagName().equals("name")) {
            student.addLate(extractTextFrom(kid));
          }
        }

      } else if (child.getTagName().equals("resubmitted")) {
        NodeList kids = child.getChildNodes();
        for (int j = 0; j < kids.getLength(); j++) {
          Node kidNode = kids.item(j);
          if (!(kidNode instanceof Element)) {
            continue;
          }

          Element kid = (Element) kidNode;
          if (kid.getTagName().equals("name")) {
            student.addResubmitted(extractTextFrom(kid));
          }
        }

      } else if (child.getTagName().equals("notes")) {
        Iterator notes = extractNotesFrom(child).iterator();
        while (notes.hasNext()) {
          String note = (String) notes.next();
          student.addNote(note);
        }
      }
    }

    if (student != null) {
      // Students are initially clean
      student.makeClean();
    }

    return student;
  }


}

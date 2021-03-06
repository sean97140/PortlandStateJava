package edu.pdx.cs410J.grader;

import com.google.common.io.ByteStreams;
import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SurveySubmissionsProcessor extends StudentEmailAttachmentProcessor {
  public SurveySubmissionsProcessor(File directory, GradeBook gradeBook) {
    super(directory, gradeBook);
  }

  @Override
  public String getEmailFolder() {
    return "Student Surveys";
  }

  @Override
  public void processAttachment(String fileName, InputStream inputStream) {
    warn("    File name: " + fileName);
    warn("    InputStream: " + inputStream);

    File file = new File(directory, fileName);
    try {

      if (file.exists()) {
        warnOfPreExistingFile(file);
        return;
      }

      ByteStreams.copy(inputStream, new FileOutputStream(file));
    } catch (IOException ex) {
      logException("While writing \"" + fileName + "\" to \"" + directory + "\"", ex);
    }

    addStudentFromFileToGradeBook(file, gradeBook);
  }

  private void addStudentFromFileToGradeBook(File file, GradeBook gradeBook) {
    Student student;

    try {
      XmlStudentParser parser = new XmlStudentParser(file);
      student = parser.parseStudent();

    } catch (IOException | ParserException ex) {
      logException("While parsing \"" + file + "\"", ex);
      return;
    }

    if (gradeBook.containsStudent(student.getId())) {
      warn("Student \"" + student.getId() + "\" already exists in " + gradeBook.getClassName());
      return;
    }

    gradeBook.addStudent(student);
  }

  private void warnOfPreExistingFile(File file) {
    warn("Not processing existing file \"" + file + "\"");
  }

}

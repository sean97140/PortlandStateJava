package edu.pdx.cs399J.reflect;

import java.io.*;

/**
 * This class provides an example of anonymous inner classes.  It uses
 * inner classes to provide implementations of the {@link FileFilter}
 * and {@link FilenameFilter} interfaces that find all files whose
 * name ends with <code>.class</code>.
 *
 * <P align="center"><EM><A href =
 * "{@docRoot}/../src/edu/pdx/cs399J/reflect/FindClassFiles.java"> View
 * Source</A></EM></P>
 *
 * @see edu.pdx.cs399J.core.FindJavaFiles
 *
 * @author David Whitlock
 * @version $Revision: 1.1 $
 *
 * @since Spring 2003
 */
public class FindClassFiles {
  private static void findFiles(File dir) {
    final String suffix = ".class";

    File[] classFiles = 
      dir.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith(suffix);
        }
      });

    for (int i = 0; i < classFiles.length; i++)
      System.out.println(classFiles[i]);

    File[] subdirs = 
      dir.listFiles(new FileFilter() {
        public boolean accept(File file) {
          return file.isDirectory();
        }
      });

    for (int i = 0; i < subdirs.length; i++)
      findFiles(subdirs[i]);
  }
}

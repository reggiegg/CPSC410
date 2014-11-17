/*
 * @(#) $Id$
 *
 * Copyright (c) 2010 Steven Drinovsky. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.sdrinovsky.sdsvn.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.sdrinovsky.sdsvn.diff.DiffView;
import org.sdrinovsky.sdsvn.files.FileRow;
import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNPropertyHandler;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Class DiffTask
 *
 *
 * @author
 * @version $Revision$
 */
public class DiffTask extends Task<Void, Void> {
  private static ServiceLoader<DiffView> diffViewLoader = ServiceLoader.load(DiffView.class);

  /**
   * Method isInternalDiffAvailable
   *
   *
   * @return
   *
   */
  public static boolean isInternalDiffAvailable() {
    return diffViewLoader.iterator().hasNext();
  }

  SVNApp                  app;
  List<? extends FileRow> rows;
  boolean                 diffTogether = false;
  SVNRevision             revision1;
  SVNRevision             revision2;

  class PropertyHandler implements ISVNPropertyHandler {
    Map<String, String> properties;

    /**
     * Method setMap
     *
     *
     * @param properties
     *
     */
    public void setMap(Map<String, String> properties) {
      this.properties = properties;
    }

    /**
     * Method handleProperty
     *
     *
     * @param path
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(File path, SVNPropertyData property) throws SVNException {
      if(properties != null) {
        properties.put(property.getName(), property.getValue().toString());
      }
    }

    /**
     * Method handleProperty
     *
     *
     * @param url
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(SVNURL url, SVNPropertyData property) throws SVNException {
      if(properties != null) {
        properties.put(property.getName(), property.getValue().toString());
      }
    }

    /**
     * Method handleProperty
     *
     *
     * @param revision
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(long revision, SVNPropertyData property) throws SVNException {
      if(properties != null) {
        properties.put(property.getName(), property.getValue().toString());
      }
    }
  }

  ;
  PropertyHandler propertyHandler = new PropertyHandler();

  /**
   * Constructor DiffTask
   *
   *
   * @param app
   * @param rows
   *
   */
  public DiffTask(SVNApp app, List<? extends FileRow> rows) {
    this(app, rows, false);
  }

  /**
   * Constructor DiffTask
   *
   *
   * @param app
   * @param rows
   * @param diffTogether
   *
   */
  public DiffTask(SVNApp app, List<? extends FileRow> rows, boolean diffTogether) {
    this(app, rows, null, null);

    this.diffTogether = diffTogether;
  }

  /**
   * Constructor DiffTask
   *
   *
   * @param app
   * @param rows
   * @param revision
   *
   */
  public DiffTask(SVNApp app, List<? extends FileRow> rows, SVNRevision revision) {
    this(app, rows, revision, null);
  }

  /**
   * Constructor DiffTask
   *
   *
   * @param app
   * @param rows
   * @param revision1
   * @param revision2
   *
   */
  public DiffTask(SVNApp app, List<? extends FileRow> rows, SVNRevision revision1, SVNRevision revision2) {
    super(app);

    this.app       = app;
    this.rows      = rows;
    this.revision1 = revision1;
    this.revision2 = revision2;
  }

  @Override
  protected Void doInBackground() throws Exception {
    boolean internalDiff = app.getPreferences().getBoolean("internalDiff", false);
    String  diffProgram  = app.getPreferences().get("diffProgram", null);
    String  mergeProgram = app.getPreferences().get("mergeProgram", null);

    if(internalDiff && !isInternalDiffAvailable()) {
      internalDiff = false;
    }

    if(diffProgram != null) {
      if(revision1 != null) {
        diffWithRepo(diffProgram, internalDiff);
      } else if(diffTogether == false) {
        diffWithTextBase(diffProgram, mergeProgram, internalDiff);
        diffProperties(diffProgram, internalDiff);
      } else {
        diffTogether(diffProgram, internalDiff);
      }
    } else {
      setMessage("No diff program set");
    }

    return null;
  }

  private File getMergeFile(final File file, final String match) {
    File[] files = file.getParentFile().listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.startsWith(file.getName() + "." + match);
      }
    });

    if((files != null) && (files.length == 1)) {
      return files[0];
    }

    return null;
  }

  /**
   *  Diff the local copy with the base revision
   * @param diffProgram is the path to the diff program (normally stored in the settings)
   * @param mergeProgram  is the path to the merge program (normally stored in the settings)
   * @param internal will override the diffProgram and use an internal diff if this is true
   * @throws IOException
   */
  private void diffWithTextBase(String diffProgram, String mergeProgram, boolean internal) throws IOException {
    int counter = 0;
    int total   = rows.size();

    for(FileRow r : rows) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      if(FileTableRow.isConflicted(r.getStatus()) && (mergeProgram != null)) {
        int rc = JOptionPane.showConfirmDialog(app.getMainFrame(), "Did you want to do a three way merge?",
                                               "Conflicts found for " + r.getFile().getName(),
                                               JOptionPane.YES_NO_OPTION);

        if(rc == JOptionPane.YES_OPTION) {
          File f1 = getMergeFile(r.getFile(), "merge-left");
          //File f2 = getMergeFile(r.getFile(), "working");
          File f2 = r.getFile();
          File f3 = getMergeFile(r.getFile(), "merge-right");

          System.out.println("merging local changes in " + r.getFile());
          processDiff(Arrays.asList(new String[]{mergeProgram, f1.getPath(), f2.getPath(), f3.getPath()}));

          break;
        }
      }

      if(FileTableRow.isModified(r.getStatus())) {
        File file = r.getFile();

        if(file.isFile() || !file.exists()) {
          File parent = file.getParentFile();
          File svn    = new File(parent, ".svn/text-base");

          if(svn.exists()) {
            File base    = new File(svn, file.getName() + ".svn-base");
            File baseTmp = File.createTempFile("svn-base", "." + file.getName());

            copyTo(base, baseTmp);
            baseTmp.deleteOnExit();
            baseTmp.setReadOnly();
            System.out.println("diffing local changes in " + r.getFile());

            if(internal) {
              processInternalDiff(baseTmp, file, false, true);
            } else {
              processDiff(Arrays.asList(new String[]{diffProgram, baseTmp.getPath(), file.getPath()}));
            }
          }
        } else {
          System.out.println("svn directory diff not supported");
        }
      } else {
        System.out.println("file not modified");
      }
    }
  }

  private void diffWithRepo(String diffProgram, boolean internalDiff) throws SVNException, IOException {
    SVNUpdateClient client  = app.getSVNClientManager().getUpdateClient();
    int             counter = 0;
    int             total   = rows.size();

    for(FileRow r : rows) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      File   file1       = r.getFile();
      SVNURL url2        = SVNURL.parseURIDecoded(r.getLocation().getURL());
      File   svn1        = File.createTempFile("diff" + file1.getName(), ".r" + revision1);
      File   svn2        = file1;
      String diffFileOne = "";
      String diffFileTwo = "";

      if(revision2 != null) {
        diffFileOne = url2 + "@" + revision2;
      } else {
        diffFileOne = file1.getPath();
      }

      diffFileTwo = url2 + "@" + revision1;

      System.out.println("diffing " + diffFileOne + " with " + diffFileTwo);
      client.doExport(url2, svn1, SVNRevision.UNDEFINED, revision1, null, true, SVNDepth.UNKNOWN);

      if(svn1.exists()) {
        svn1.deleteOnExit();
        svn1.setReadOnly();
      }

      if(file1.isDirectory() || (revision2 != null)) {
        if(revision2 != null) {
          svn2 = File.createTempFile("diff" + file1.getName(), ".r" + revision2);
        }

        if(file1.isDirectory()) {
          client.doExport(SVNURL.parseURIDecoded(r.getLocation().getURL()), svn2, SVNRevision.UNDEFINED, revision2,
                          null, true, SVNDepth.IMMEDIATES);
        } else {
          client.doExport(url2, svn2, SVNRevision.UNDEFINED, revision2, null, true, SVNDepth.UNKNOWN);
        }

        if(svn2.exists()) {
          svn2.deleteOnExit();
          svn2.setReadOnly();
        }
      }

      if(svn1.exists() && svn2.exists()) {
        if(internalDiff) {
          processInternalDiff(svn1, svn2, true, true);
        } else {
          processDiff(Arrays.asList(new String[]{diffProgram, svn1.getPath(), svn2.getPath()}));
        }
      }
    }
  }

  private void diffProperties(String diffProgram, boolean internalDiff) throws SVNException, IOException {
    int counter = 0;
    int total   = rows.size();

    for(FileRow r : rows) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      if(FileTableRow.isPropertyModified(r.getStatus())) {
        File                file         = r.getFile();
        File                svn1         = File.createTempFile("diff" + file.getName() + ".properties", ".wc");
        File                svn2         = File.createTempFile("diff" + file.getName() + ".properties", ".base");
        Map<String, String> currentProps = new TreeMap<String, String>();
        Map<String, String> baseProps    = new TreeMap<String, String>();

        propertyHandler.setMap(currentProps);
        app.getSVNClientManager().getWCClient().doGetProperty(r.getFile(), null, SVNRevision.UNDEFINED,
                                                              SVNRevision.WORKING, SVNDepth.EMPTY, propertyHandler,
                                                              null);
        propertyHandler.setMap(baseProps);
        app.getSVNClientManager().getWCClient().doGetProperty(r.getFile(), null, SVNRevision.UNDEFINED,
                                                              SVNRevision.BASE, SVNDepth.EMPTY, propertyHandler, null);

        PrintWriter pw = null;

        try {
          pw = new PrintWriter(new FileWriter(svn1));

          for(String name : currentProps.keySet()) {
            pw.println(name + ": " + currentProps.get(name));
          }
        } finally {
          if(pw != null) {
            pw.close();
          }

          pw = null;
        }

        try {
          pw = new PrintWriter(new FileWriter(svn2));

          for(String name : baseProps.keySet()) {
            pw.println(name + ": " + baseProps.get(name));
          }
        } finally {
          if(pw != null) {
            pw.close();
          }

          pw = null;
        }

        if(svn1.exists() && svn2.exists()) {
          System.out.println("diffing " + r.getFile());

          if(internalDiff) {
            processInternalDiff(svn1, svn2, false, false);
          } else {
            processDiff(Arrays.asList(new String[]{diffProgram, svn1.getPath(), svn2.getPath()}));
          }
        }
      }
    }
  }

  private void diffTogether(String diffProgram, boolean internalDiff) throws IOException {
    if(rows.size() == 2) {
      File firstFile  = rows.get(0).getFile();
      File secondFile = rows.get(1).getFile();

      System.out.println("diffing " + firstFile + " with " + secondFile);

      if(internalDiff) {
        processInternalDiff(firstFile, secondFile, true, true);
      } else {
        processDiff(Arrays.asList(new String[]{diffProgram, firstFile.getPath(), secondFile.getPath()}));
      }
    }
  }

  @Override
  protected void finished() {
    System.out.println("Diff Finished");
  }

  /**
   * Method copyTo
   *
   *
   * @param from
   * @param to
   *
   * @throws IOException
   *
   */
  public static void copyTo(File from, File to) throws IOException {
    if( !from.exists() || from.getCanonicalPath().equals(to.getCanonicalPath())) {
      //The files are the same, no need to copy.
      return;
    }

    InputStream  is = new BufferedInputStream(new FileInputStream(from));
    OutputStream os = new BufferedOutputStream(new FileOutputStream(to));

    try {
      final int BUFSIZ = 1024 * 8;
      byte      buf[]  = new byte[BUFSIZ];
      int       len    = 0;

      while((len = is.read(buf)) >= 0) {
        os.write(buf, 0, len);
      }
    } finally {
      if(os != null) {
        try {
          os.close();
        } catch(IOException ioe) {
        }
      }

      if(is != null) {
        try {
          is.close();
        } catch(IOException ioe) {
        }
      }
    }
  }

  /**
   * Use the internal diff function to do the comparison
   *
   * @param leftFile      is the file on the left hand side of the comparison
   * @param rightFile     is the file on the right hand side of the comparison
   * @param leftReadOnly  true will make the left file read-only in the diff, false will allow edits to the file
   * @param rightReadOnly true will make the right file read-only in the diff, false will allow edits to the file
   */
  private void processInternalDiff(File leftFile, File rightFile, boolean leftReadOnly, boolean rightReadOnly) {
    for(DiffView diffView : diffViewLoader) {
      try {
        diffView.executeDiff(leftFile.getPath(), rightFile.getPath(), leftReadOnly, rightReadOnly);
      } catch(Exception ex) {
        java.util.logging.Logger.getLogger(DiffTask.class.getName()).log(Level.SEVERE, null, ex);
      }

      break;
    }
  }

  private void processDiff(List<String> commangLine) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(commangLine);
    Process        process        = processBuilder.start();

    new Thread(new StreamLogger(process.getInputStream())).start();
    new Thread(new StreamLogger(process.getErrorStream())).start();
  }

  class StreamLogger implements Runnable {
    BufferedReader reader;

    /**
     * Constructor StreamLogger
     *
     *
     * @param input
     *
     */
    public StreamLogger(InputStream input) {
      this.reader = new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Method run
     *
     *
     */
    @Override
    public void run() {
      try {
        String line = null;

        while((line = reader.readLine()) != null) {
          System.err.println(line);
        }

        System.err.flush();
      } catch(IOException ex) {
        Logger.getLogger(DiffTask.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}

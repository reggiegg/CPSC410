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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

/**
 * Class JIndentTask
 *
 *
 * @author
 * @version $Revision$
 */
public class JIndentTask extends Task<Void, Void> {
  SVNApp             app;
  List<FileTableRow> rows;
  int                counter = 0;
  int                errors  = 0;
  int                total   = 0;
  String             settingsFile;
  String             jindentProgram;

  /**
   * Constructor JIndentTask
   *
   *
   * @param app
   * @param rows
   *
   */
  public JIndentTask(SVNApp app, List<FileTableRow> rows) {
    super(app);

    this.app       = app;
    this.rows      = rows;
    settingsFile   = app.getPreferences().get("jindentSettings", null);
    jindentProgram = app.getPreferences().get("jindentProgram", null);
  }

  @Override
  protected Void doInBackground() throws Exception {
    StringBuilder cmd = new StringBuilder();

    cmd.append(java.lang.System.getProperty("java.home"));
    cmd.append(java.io.File.separator);
    cmd.append("bin");
    cmd.append(java.io.File.separator);
    cmd.append("java");

    List<String> commandLine = new ArrayList<String>();

    commandLine.add(cmd.toString());
    commandLine.add("-jar");
    commandLine.add(jindentProgram);
    commandLine.add("-p");
    commandLine.add(settingsFile);

    for(FileTableRow row : rows) {
      if(row.getFileName().endsWith(".java")) {
        commandLine.add(row.getFile().getPath());

        total++;
      }
    }

    ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
    Process        process        = processBuilder.start();

    new Thread(new StreamLogger(process.getInputStream())).start();
    new Thread(new StreamLogger(process.getErrorStream())).start();
    process.waitFor();

    return null;
  }

  class StreamLogger implements Runnable {
    BufferedReader reader;

    /**
     * Constructor StreamLogger
     *
     *
     * @param stream
     *
     */
    public StreamLogger(InputStream stream) {
      this.reader = new BufferedReader(new InputStreamReader(stream));
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
          if(line.startsWith("  Java Error")) {
            errors++;

            firePropertyChange("error", null, line);
          } else if(line.startsWith("  Java Formatter")) {
            firePropertyChange("error", null, line);
          } else if(line.startsWith("  Java Warning")) {
            firePropertyChange("error", null, line);
          } else if(line.startsWith("  Java Documentation")) {
            firePropertyChange("error", null, line);
          } else {
            if(line.startsWith("Formatting Java file:")) {
              counter++;
            }

            firePropertyChange("message", null, line);
          }

          if((counter + errors) <= total) {
            setProgress((counter + errors) / (float)total);
          }
        }
      } catch(IOException ex) {
        Logger.getLogger(JIndentTask.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  protected void finished() {
    firePropertyChange("refresh", null, "JIndent Finished. Formatted " + counter + " files with " + errors + " errors");
  }
}

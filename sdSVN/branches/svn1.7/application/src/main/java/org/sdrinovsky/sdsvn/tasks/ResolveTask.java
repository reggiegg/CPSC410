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

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNConflictChoice;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 *
 * @author sdrinovsky
 */
public class ResolveTask extends Task<Void, Void> {
  List<FileTableRow> files;
  SVNWCClient        wcClient;
  SVNApp             app;

  /**
   * Constructor CommitTask
   *
   *
   * @param app
   * @param files
   *
   */
  public ResolveTask(SVNApp app, List<FileTableRow> files) {
    super(app);

    this.files = files;
    this.app   = app;
    wcClient   = app.getSVNClientManager().getWCClient();
  }

  @Override
  protected Void doInBackground() throws Exception {
    int counter = 0;
    int total   = files.size();

    for(FileTableRow f : files) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      wcClient.doResolve(f.getFile(), SVNDepth.EMPTY, SVNConflictChoice.MERGED);
    }

    return null;
  }

  @Override
  protected void failed(Throwable t) {
    setMessage("Resolve failed.");

    JTextArea textArea = new JTextArea();

    textArea.setRows(15);
    textArea.setColumns(75);
    textArea.setLineWrap(false);
    textArea.setText(t.getMessage());
    textArea.setCaretPosition(0);

    JScrollPane pane = new JScrollPane(textArea);

    JOptionPane.showMessageDialog(app.getMainFrame(), pane, "Resolve Task Failed", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  protected void succeeded(Void result) {
    firePropertyChange("refresh", null, "Resolve finished.");
  }
}

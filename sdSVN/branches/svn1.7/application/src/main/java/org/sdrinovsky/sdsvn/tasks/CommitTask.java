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

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNEvent;

/**
 * Class CommitTask
 *
 *
 * @author
 * @version $Revision$
 */
public class CommitTask extends Task<SVNCommitInfo, Void> {
  List<File>      files;
  String          message;
  boolean         recurse;
  SVNCommitClient commitClient;
  SVNApp          app;

  /**
   * Constructor CommitTask
   *
   *
   * @param app
   * @param rows
   * @param message
   * @param recurse
   *
   */
  public CommitTask(SVNApp app, List<FileTableRow> rows, String message, boolean recurse) {
    super(app);

    files = new LinkedList<File>();

    for(FileTableRow row : rows) {
      files.add(row.getFile());
    }

    this.message = message;
    this.recurse = recurse;
    this.app     = app;
    commitClient = app.getSVNClientManager().getCommitClient();
  }

  @Override
  protected SVNCommitInfo doInBackground() throws Exception {
    System.out.println("Committing...");

    // the commit messages are:
    //   commit_modified ...
    //   commit_delta_sent ...
    //   commit_completed
    final int total = files.size() * 2 + 1;

    commitClient.setEventHandler(new ISVNEventHandler() {
      int counter = 0;
      @Override
      public void handleEvent(SVNEvent event, double progress) throws SVNException {
        counter++;

        if(counter <= total) {
          setProgress(counter / (float)total);
        }

        System.out.println(event);
      }
      @Override
      public void checkCancelled() throws SVNCancelException {}
    });

    return commitClient.doCommit(files.toArray(new File[files.size()]), true, message, null, null, false, false,
                                 SVNDepth.fromRecurse(recurse));
  }

  @Override
  protected void failed(Throwable t) {
    System.err.println(t.getMessage());
    setMessage("Commit failed.");

    JTextArea textArea = new JTextArea();

    textArea.setRows(15);
    textArea.setColumns(75);
    textArea.setLineWrap(false);
    textArea.setText(t.getMessage());
    textArea.setCaretPosition(0);

    JScrollPane pane = new JScrollPane(textArea);

    JOptionPane.showMessageDialog(app.getMainFrame(), pane);
  }

  @Override
  protected void succeeded(SVNCommitInfo info) {
    firePropertyChange("refresh", null, "Commit finished: revision " + info.getNewRevision());
  }
}

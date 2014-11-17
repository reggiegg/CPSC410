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
import java.io.FilenameFilter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import java.util.LinkedList;
import java.util.List;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;

/**
 * Class RefreshTask
 *
 *
 * @author
 * @version $Revision$
 */
public class RefreshTask extends Task<Void, Void> {
  SVNStatusClient    statusClient;
  File               workingCopy;
  List<File>         selected;
  boolean            remote;
  String             item;
  String             message;
  long               start;
  List<FileTableRow> rows    = new LinkedList<FileTableRow>();
  ISVNStatusHandler  handler = new ISVNStatusHandler() {
    @Override
    public void handleStatus(SVNStatus status) throws SVNException {
      rows.add(new FileTableRow(status));
    }
  };

  /**
   * Constructor RefreshTask
   *
   *
   * @param app
   * @param workingCopy
   * @param selected
   * @param remote
   *
   */
  public RefreshTask(SVNApp app, File workingCopy, List<File> selected, boolean remote) {
    super(app);

    statusClient     = app.getSVNClientManager().getStatusClient();
    this.workingCopy = workingCopy;
    this.selected    = selected;
    this.remote      = remote;
  }

  /**
   * Method setMessageText
   *
   *
   * @param message
   *
   */
  public void setMessageText(String message) {
    this.message = message;
  }

  @Override
  protected Void doInBackground() throws Exception {
    start = System.currentTimeMillis();

    setMessage("Refreshing " + workingCopy);

    if(new File(workingCopy, ".svn").exists()) {
      doStatus(workingCopy);
    } else {
      String[] subfolders = workingCopy.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return new File(dir, name).isDirectory();
        }
      });

      if((subfolders == null) || (subfolders.length == 0)) {
        throw new Exception("No working copy found");
      }

      for(String file : workingCopy.list()) {
        doStatus(new File(workingCopy, file));
      }
    }

    return null;
  }

  private void doStatus(File file) throws SVNException {
    try {
      statusClient.doStatus(file, SVNRevision.UNDEFINED, SVNDepth.UNKNOWN, remote, true, true, true, handler, null);
    } catch(SVNException se) {
      if(remote) {
        firePropertyChange("error", null, "Unable to do a remote refresh: " + se.getErrorMessage().getMessage());

        // todo: just refresh with remote=false?
        if((se.getCause() instanceof UnknownHostException) || (se.getCause() instanceof SocketTimeoutException)) {
          statusClient.doStatus(file, SVNRevision.UNDEFINED, SVNDepth.UNKNOWN, false, true, true, true, handler, null);

          return;
        }
      }

      throw se;
    }
  }

  @Override
  protected void finished() {
    firePropertyChange("status", null, rows);

    for(FileTableRow row : rows) {
      if((selected != null) && selected.contains(row.getFile())) {
        firePropertyChange("selected", null, row);
      }
    }
  }

  @Override
  protected void succeeded(Void arg0) {
    if(message != null) {
      setMessage(message);
    } else {
      long finish = System.currentTimeMillis() - start;

      setMessage("Refresh Finished in " + finish + "ms");
    }
  }

  @Override
  protected void failed(Throwable t) {
    if(t instanceof SVNException) {
      SVNException ex = (SVNException)t;

      firePropertyChange("error", null, "Unable to refresh: " + ex.getErrorMessage().getMessage());
    } else {
      firePropertyChange("error", null, "Unexpected Error: " + t.getClass().getName() + ": " + t.getMessage());
    }
  }
}

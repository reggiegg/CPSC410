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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.files.FileRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Class CheckoutTask
 *
 *
 * @author
 * @version $Revision$
 */
public class UpdateTask extends Task<Void, Void> {
  SVNUpdateClient         updateClient;
  List<? extends FileRow> rows;
  SVNRevision             revision;
  long[]                  revisionNumbers;

  /**
   * Constructor CheckoutTask
   *
   *
   * @param app
   * @param rows
   *
   */
  public UpdateTask(SVNApp app, List<? extends FileRow> rows) {
    this(app, rows, SVNRevision.HEAD);
  }

  /**
   * Constructor UpdateTask
   *
   *
   * @param app
   * @param rows
   * @param revision
   *
   */
  public UpdateTask(SVNApp app, List<? extends FileRow> rows, SVNRevision revision) {
    super(app);

    updateClient = app.getSVNClientManager().getUpdateClient();

    updateClient.setEventHandler(new ISVNEventHandler() {
      @Override
      public void handleEvent(SVNEvent event, double progress) throws SVNException {
        if(progress >= 0) {
          setProgress((float)progress);
        }

        if(event.getFile() != null) {
          if(event.getFile().isFile()) {
            setMessage(event.getFile().toString());
          }
        } else {
          System.out.println("getFile() returned null!");
        }
      }
      @Override
      public void checkCancelled() throws SVNCancelException {}
    });

    this.rows     = rows;
    this.revision = revision;
  }

  @Override
  protected Void doInBackground() throws Exception {
    File[] destinations = new File[rows.size()];

    for(int i = 0; i < destinations.length; i++) {
      destinations[i] = rows.get(i).getFile();
    }

    if((destinations.length > 1) || !destinations[0].isDirectory() || new File(destinations[0], ".svn").exists()) {
      revisionNumbers = updateClient.doUpdate(destinations, revision, SVNDepth.UNKNOWN, false, false);
    }

    return null;
  }

  @Override
  protected void succeeded(Void result) {
    if((revisionNumbers != null) && (revisionNumbers.length == 1)) {
      firePropertyChange("refresh", null, "Update Finished. Updated to revision " + revisionNumbers[0]);
    } else {
      firePropertyChange("refresh", null, "Update Finished.");
    }
  }

  @Override
  protected void failed(Throwable cause) {
    firePropertyChange("error", null, "Update Failed: " + cause.getMessage());
    Logger.getLogger(UpdateTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

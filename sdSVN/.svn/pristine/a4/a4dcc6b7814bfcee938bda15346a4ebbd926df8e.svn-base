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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Class RevertTask
 *
 *
 * @author
 * @version $Revision$
 */
public class RevertTask extends Task<Void, Void> {
  List<FileTableRow> selected;
  SVNWCClient        wcClient;
  SVNRevision        revision;

  /**
   * Constructor RevertTask
   *
   *
   * @param app
   * @param files
   *
   */
  public RevertTask(SVNApp app, List<FileTableRow> files) {
    this(app, files, null);
  }

  /**
   * Constructor RevertTask
   *
   *
   * @param app
   * @param selected
   * @param revision
   *
   */
  public RevertTask(SVNApp app, List<FileTableRow> selected, SVNRevision revision) {
    super(app);

    wcClient = app.getSVNClientManager().getWCClient();

    wcClient.setEventHandler(new ISVNEventHandler() {
      @Override
      public void handleEvent(SVNEvent event, double progress) throws SVNException {
        setMessage(event.getFile().toString());
      }
      @Override
      public void checkCancelled() throws SVNCancelException {}
    });

    this.selected = selected;
  }

  @Override
  protected Void doInBackground() throws Exception {
    List<File> files   = new LinkedList<File>();
    int        counter = 0;
    int        total   = selected.size();

    for(FileTableRow row : selected) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      File file = row.getFile();

      files.add(file);

      if(file.isFile()) {
        File backFile = new File(file.getParentFile(), file.getName() + "~");

        if(backFile.exists()) {
          backFile.delete();
        }

        file.renameTo(backFile);
      }
    }

    wcClient.doRevert(files.toArray(new File[files.size()]), SVNDepth.EMPTY, null);

    return null;
  }

  @Override
  protected void succeeded(Void result) {
    firePropertyChange("refresh", null, "Revert Finished");
  }

  @Override
  protected void failed(Throwable cause) {
    firePropertyChange("refresh", null, "Revert Failed: " + cause.getMessage());
    Logger.getLogger(CheckoutTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

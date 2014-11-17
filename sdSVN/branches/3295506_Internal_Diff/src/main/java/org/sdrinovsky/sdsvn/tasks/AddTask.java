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

import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Class AddTask
 *
 *
 * @author
 * @version $Revision: 132 $
 */
public class AddTask extends Task<Void, Void> {
  List<File>  files;
  SVNWCClient wcClient;
  SVNApp      app;

  /**
   * Constructor AddTask
   *
   *
   * @param app
   * @param files
   *
   */
  public AddTask(SVNApp app, List<File> files) {
    super(app);

    this.files = files;
    this.app   = app;
    wcClient   = app.getSVNClientManager().getWCClient();
  }

  @Override
  protected Void doInBackground() throws Exception {
    int counter = 0;
    int total   = files.size();

    for(File file : files) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      wcClient.doAdd(file, false, false, true, SVNDepth.INFINITY, true, false);
    }

    return null;
  }

  @Override
  protected void failed(Throwable cause) {
    setMessage("Add failed. " + cause.getMessage());
    Logger.getLogger(CheckoutTask.class.getName()).log(Level.WARNING, null, cause);
  }

  @Override
  protected void succeeded(Void info) {
    firePropertyChange("refresh", null, "Files have been scheduled for Add.");
  }
}

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

import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class CleanupTask extends Task<Void, Void> {
  SVNApp app;
  File   path;

  /**
   * Constructor CleanupTask
   *
   *
   * @param app
   * @param path
   *
   */
  public CleanupTask(SVNApp app, File path) {
    super(app);

    this.app  = app;
    this.path = path;
  }

  @Override
  protected Void doInBackground() throws Exception {
    setMessage("Running cleanup...");
    setProgress(0);
    app.getSVNClientManager().getWCClient().doCleanup(path);

    return null;
  }

  @Override
  protected void succeeded(Void result) {
    setMessage("Cleanup Finished");
  }

  @Override
  protected void failed(Throwable cause) {
    setMessage("Cleanup Failed: " + cause.getMessage());
  }
}

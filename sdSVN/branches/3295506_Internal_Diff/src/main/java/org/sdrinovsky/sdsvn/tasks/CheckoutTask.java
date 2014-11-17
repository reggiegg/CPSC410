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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
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
public class CheckoutTask extends Task<Void, Void> {
  SVNUpdateClient updateClient;
  SVNURL          url;
  File            destination;
  SVNRevision     revision;
  long            revisionNumber = 0;

  /**
   * Constructor CheckoutTask
   *
   *
   * @param app
   * @param url
   * @param destination
   * @param revision
   *
   */
  public CheckoutTask(SVNApp app, SVNURL url, File destination, SVNRevision revision) {
    super(app);

    updateClient = app.getSVNClientManager().getUpdateClient();

    updateClient.setEventHandler(new ISVNEventHandler() {
      @Override
      public void handleEvent(SVNEvent event, double progress) throws SVNException {
        if(event.getFile() != null) {
          setMessage(event.getFile().toString());
        } else {
          System.out.println("getFile() returned null!");
        }
      }
      @Override
      public void checkCancelled() throws SVNCancelException {}
    });

    this.url         = url;
    this.destination = destination;
    this.revision    = revision;
  }

  @Override
  protected Void doInBackground() throws Exception {
    SVNDepth immediates = SVNDepth.EMPTY;
    SVNDepth files      = SVNDepth.FILES;
    SVNDepth infinity   = SVNDepth.INFINITY;

    revisionNumber = updateClient.doCheckout(url, destination, revision, revision, immediates, false);

    updateClient.doUpdate(destination, revision, infinity, false, true);

    return null;
  }

  @Override
  protected void succeeded(Void result) {
    firePropertyChange("refresh", null, "Checkout Finished. Updated to revision " + revisionNumber);
  }

  @Override
  protected void failed(Throwable cause) {
    firePropertyChange("refresh", null, "Checkout Failed: " + cause.getMessage());
    Logger.getLogger(CheckoutTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

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
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNCommitHandler;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNCommitItem;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 *
 * @author sdrinovsky
 */
public class BranchTask extends Task<Void, Void> {
  SVNApp          app;
  SVNCopyClient   copyClient;
  SVNUpdateClient updateClient;
  File            source;
  SVNURL          destination;
  long            switchRevisionNumber = 0;

  /**
   * Constructor BranchTask
   *
   *
   * @param app
   * @param source
   * @param destination
   *
   */
  public BranchTask(SVNApp app, File source, SVNURL destination) {
    super(app);

    this.app         = app;
    this.source      = source;
    this.destination = destination;
    copyClient       = app.getSVNClientManager().getCopyClient();

    copyClient.setCommitHandler(new ISVNCommitHandler() {
      @Override
      public String getCommitMessage(String message, SVNCommitItem[] commitables) throws SVNException {
        return message;
      }
      @Override
      public SVNProperties getRevisionProperties(String message, SVNCommitItem[] commitables,
                                                 SVNProperties revisionProperties) throws SVNException {
        return revisionProperties;
      }
    });

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
  }

  @Override
  protected Void doInBackground() throws Exception {
    SVNCopySource[] sources = new SVNCopySource[]{new SVNCopySource(SVNRevision.HEAD, SVNRevision.BASE, source)};

    setMessage("Creating Branch " + destination);

    String  message = null;
    SVNInfo info    = SVNApp.getApplication().getFileInfo(source);

    if(info != null) {
      message = "Working copy branch from " + info.getURL().toString() + "@" + info.getRevision().getNumber();
    }

    copyClient.doCopy(sources, destination, false, false, true, "Working copy branch from " + app.getRootURL(source),
                      null);
    setMessage("Switching to Branch " + destination);

    switchRevisionNumber = updateClient.doSwitch(source, destination, SVNRevision.HEAD, SVNRevision.HEAD,
                                                 SVNDepth.UNKNOWN, false, false);

    return null;
  }

  @Override
  protected void succeeded(Void result) {
    firePropertyChange("refresh", null, "Branch Succeded. Updated to revision " + switchRevisionNumber);
  }

  @Override
  protected void finished() {
    if(isCancelled()) {
      firePropertyChange("refresh", null, "Branch Canceled.");
    }
  }

  @Override
  protected void failed(Throwable cause) {
    firePropertyChange("refresh", null, "Branch Failed: " + cause.getMessage());
    Logger.getLogger(BranchTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

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

import java.awt.Cursor;

import java.io.File;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.files.FileRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.dialogs.HistoryDialog;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNRevisionRange;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class MergeTask extends Task<Void, Void> {
  SVNApp                  app;
  SVNDiffClient           diffClient;
  SVNURL                  start;
  SVNURL                  from;
  List<? extends FileRow> rows;
  SVNRevision             revision;
  List<SVNRevisionRange>  mergeRevisions;

  /**
   * Constructor MergeTask
   *
   *
   * @param app
   * @param rows
   * @param revision
   *
   */
  public MergeTask(SVNApp app, List<? extends FileRow> rows, SVNRevision revision) {
    super(app);

    this.app      = app;
    this.rows     = rows;
    this.revision = revision;
    diffClient    = app.getSVNClientManager().getDiffClient();

    diffClient.setEventHandler(new ISVNEventHandler() {
      @Override
      public void handleEvent(SVNEvent event, double progress) throws SVNException {
        if(isCancelled()) {
          throw new SVNException(SVNErrorMessage.create(SVNErrorCode.CANCELLED, "Merge task canceled"));
        }

        if((event.getFile() != null) && (event.getContentsStatus() != SVNStatusType.INAPPLICABLE)) {
          setMessage(event.getFile().toString() + ": " + event.getContentsStatus());
        }
      }
      @Override
      public void checkCancelled() throws SVNCancelException {}
    });
  }

  /**
   * Constructor MergeTask
   *
   *
   * @param app
   * @param start
   * @param rows
   * @param from
   * @param revision
   *
   */
  public MergeTask(SVNApp app, SVNURL start, List<FileRow> rows, SVNURL from, SVNRevision revision) {
    this(app, rows, revision);

    this.start = start;
    this.from  = from;

    try {
      if(rows.size() > 0) {
        mergeRevisions = getEligible(rows.get(0));
      }
    } catch(SVNException ex) {
      Logger.getLogger(MergeTask.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected Void doInBackground() throws Exception {
    int counter = 0;
    int total   = rows.size();

    for(FileRow row : rows) {
      counter++;

      if(counter <= total) {
        setProgress(counter / (float)total);
      }

      File destination = row.getFile();

      doMerge(destination, "");
    }

    return null;
  }

  private List<SVNRevisionRange> getEligible(FileRow row) throws SVNException {
    File     destination = row.getFile();
    Location location    = row.getLocation();

    app.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    SVNURL                  url     = SVNURL.parseURIDecoded(start.toString());
    final List<SVNLogEntry> entries = new LinkedList<SVNLogEntry>();

    diffClient.doGetLogEligibleMergeInfo(destination, SVNRevision.HEAD, url, SVNRevision.HEAD, true, null,
                                         new ISVNLogEntryHandler() {
      @Override
      public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
        entries.add(logEntry);
      }
    });

    HistoryDialog dialog = new HistoryDialog(app.getMainFrame(), true);

    dialog.setFileRow(location, true, false, false);
    dialog.setLog(entries);
    dialog.setCommittedRevision(revision.getNumber(), false);
    dialog.setTitle("SVN Merge Revision Listing");
    dialog.setAcceptText("Merge");
    dialog.setMultiSelect(true);
    app.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    dialog.setVisible(true);

    if(dialog.didAccept()) {
      return dialog.getSelectedRevisions();
    } else {
      return null;
    }
  }

  private void doMerge(File destination, String path) throws Exception {
    if(start == null) {
      doRevert(destination, path);
    } else if(mergeRevisions != null) {
      SVNURL url  = SVNURL.parseURIDecoded(start.toString() + path);
      File   file = new File(destination, path);

      diffClient.doMerge(url, SVNRevision.HEAD, mergeRevisions, file, SVNDepth.UNKNOWN, true, false, false, false);
    } else {
      cancel(true);
    }
  }

  private void doRevert(File destination, String path) throws Exception {
    File file = new File(destination, path);

    diffClient.doMerge(file, revision, file, SVNRevision.create(revision.getNumber() - 1), file, SVNDepth.UNKNOWN,
                       false, false, false, false);
  }

  @Override
  protected void succeeded(Void result) {
    String revisions = "";

    if(mergeRevisions != null) {
      for(SVNRevisionRange range : mergeRevisions) {
        if(revisions.length() > 0) {
          revisions += ", ";
        }

        long startRevision = range.getStartRevision().getNumber();
        long endRevision   = range.getEndRevision().getNumber();

        if(endRevision - startRevision == 1) {
          revisions += startRevision;
        } else {
          revisions += startRevision + "-" + endRevision;
        }
      }
    }

    firePropertyChange("refresh", null, "Succeded merging revisions " + revisions);
  }

  @Override
  protected void finished() {
    if(isCancelled()) {
      firePropertyChange("refresh", null, "Merge Canceled.");
    }
  }

  @Override
  protected void failed(Throwable cause) {
    firePropertyChange("refresh", null, "Merge Failed: " + cause.getMessage());
    Logger.getLogger(MergeTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

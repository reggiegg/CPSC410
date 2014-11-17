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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sdrinovsky.sdsvn.tasks;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.application.Task;

import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.dialogs.AnnotateDialog;
import org.sdrinovsky.sdsvn.files.FileTableRow;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNAnnotateHandler;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author sdrinovsky
 */
public class AnnotateTask extends Task<ArrayList<Object[]>, Object[]> {
  FileTableRow        row;
  SVNLogClient        logClient;
  SVNApp              app;
  ArrayList<Object[]> blame    = new ArrayList<Object[]>();
  long                fileLine = 0;

  class AnnotationHandler implements ISVNAnnotateHandler {
    ISVNOptions options;

    /**
     * Constructor AnnotationHandler
     *
     *
     * @param options
     *
     */
    public AnnotationHandler(ISVNOptions options) {
      this.options = options;
    }

    /**
     * Method handleLine
     *
     *
     * @param date
     * @param revision
     * @param author
     * @param line
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleLine(Date date, long revision, String author, String line) throws SVNException {
      handleLine(date, revision, author, line, null, -1, null, null, 0);
    }

    /**
     * Method handleLine
     *
     *
     * @param date
     * @param revision
     * @param author
     * @param line
     * @param mergedDate
     * @param mergedRevision
     * @param mergedAuthor
     * @param mergedPath
     * @param lineNumber
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleLine(Date date, long revision, String author, String line, Date mergedDate, long mergedRevision,
                           String mergedAuthor, String mergedPath, int lineNumber) throws SVNException {
      ArrayList list = new ArrayList<Object[]>();
      Object[]  row  = new Object[4];

      row[0] = fileLine++;
      row[1] = revision;
      row[2] = author;
      row[3] = line;

      list.add(row);
      process(list);
      blame.add(row);
    }

    /**
     * Method handleRevision
     *
     *
     * @param date
     * @param revision
     * @param author
     * @param contents
     *
     * @return
     *
     * @throws SVNException
     *
     */
    @Override
    public boolean handleRevision(Date date, long revision, String author, File contents) throws SVNException {
      // for now
      return false;
    }

    /**
     * Method handleEOF
     *
     *
     */
    @Override
    public void handleEOF() {}
  }

  /**
   * Constructor AnnotateTask
   *
   *
   * @param app
   * @param row
   *
   */
  public AnnotateTask(SVNApp app, FileTableRow row) {
    super(app);

    this.app  = app;
    this.row  = row;
    logClient = app.getSVNClientManager().getLogClient();
  }

  @Override
  protected ArrayList<Object[]> doInBackground() throws Exception {
    String url = row.getLocation().getURL();

    logClient.doAnnotate(SVNURL.parseURIDecoded(url), SVNRevision.UNDEFINED, SVNRevision.create(1), SVNRevision.HEAD,
                         false, true, new AnnotationHandler(logClient.getOptions()), null);

    return blame;
  }

  @Override
  protected void succeeded(ArrayList<Object[]> result) {
    AnnotateDialog dialog = new AnnotateDialog(app.getMainFrame(), false);

    dialog.setTitle("SVN Annotate - " + row.getFileName());

    for(Object[] row : result) {
      dialog.addRow(row);
    }

    dialog.setVisible(true);
  }

  @Override
  protected void process(List<Object[]> values) {
    //TODO: add rows to table
  }

  @Override
  protected void failed(Throwable cause) {
    setMessage("Failed to annotate " + row + ": " + cause.getMessage());
    Logger.getLogger(AnnotateTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

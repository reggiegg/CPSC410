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

import java.awt.Color;
import java.awt.Component;

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.dialogs.HistoryDialog;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class HistoryTask extends Task<List<SVNLogEntry>, Void> {
  SVNApp       app;
  Location     location;
  FileTableRow row;

  /**
   * Constructor HistoryTask
   *
   *
   * @param app
   * @param location
   * @param row
   *
   */
  public HistoryTask(SVNApp app, Location location, FileTableRow row) {
    super(app);

    this.app      = app;
    this.location = location;
    this.row      = row;
  }

  @Override
  protected List<SVNLogEntry> doInBackground() throws Exception {
    final List<SVNLogEntry> history = new LinkedList<SVNLogEntry>();
    final int               total   = 500;

    app.getSVNClientManager().getLogClient().doLog(new File[]{row.getFile()}, SVNRevision.HEAD, SVNRevision.create(1),
                                                   false, true, total, new ISVNLogEntryHandler() {
      int counter = 0;
      @Override
      public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
        counter++;

        if(counter <= total) {
          setProgress(counter / (float)total);
        }

        setMessage("getting revision " + logEntry.getRevision());
        history.add(logEntry);
      }
    });

    return history;
  }

  @Override
  protected void succeeded(final List<SVNLogEntry> history) {
    HistoryDialog dialog = new HistoryDialog(app.getMainFrame(), false, false);

    dialog.setFileRow(location, true, true, true);
    dialog.setLog(history);
    dialog.setCommittedRevision(row.getCommitRevision(), row.isCommitable());
    dialog.setTitle("SVN History - " + row.getFileName());
    dialog.setVisible(true);
  }

  @Override
  protected void failed(Throwable arg0) {
    JOptionPane.showMessageDialog(null, arg0.getMessage());
  }

  DefaultTableCellRenderer coloredRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int r, int c) {
      Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, r, c);
      Long      rev  = (Long)table.getModel().getValueAt(table.convertRowIndexToModel(r), 0);

      if(row.getCommitRevision() == rev) {
        comp.setForeground(Color.blue);
      } else {
        comp.setForeground(Color.black);
      }

      return comp;
    }
  };
}

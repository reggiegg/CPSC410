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

package org.sdrinovsky.sdsvn.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.sdrinovsky.sdsvn.files.FileRowImpl;
import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.tasks.DiffTask;
import org.sdrinovsky.sdsvn.tasks.JIndentTask;
import org.sdrinovsky.sdsvn.tasks.MergeTask;
import org.sdrinovsky.sdsvn.tasks.UpdateTask;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Class HistoryPopupMenu
 *
 *
 * @author
 * @version $Revision$
 */
public class HistoryPopupMenu extends JPopupMenu {
  public static final String     DIFF_WC          = "Diff File to WC";
  public static final String     DIFF_PREVIOUS    = "Show File Changes";
  public static final String     DIFF_CS_WC       = "Diff Changeset to WC";
  public static final String     DIFF_CS_PREVIOUS = "Show Changeset Changes";
  public static final String     UPDATE           = "Update File to Revision";
  public static final String     UPDATE_CS        = "Update Changeset to Revision";
  public static final String     REVERT           = "Revert File Revision";
  public static final String     REVERT_CS        = "Revert Changeset Revision";
  public static final String     JINDENT          = "JIndent file";
  private SVNRevision            revision;
  private Location               location;
  private Set                    changedPaths;
  private Map<String, JMenuItem> items = new HashMap<String, JMenuItem>();

  /**
   * Constructor HistoryPopupMenu
   *
   *
   *
   *
   * @param filesOnly
   * @param diff
   * @param update
   * @param revert
   * @param jindent
   */
  public HistoryPopupMenu(boolean filesOnly, boolean diff, boolean update, boolean revert, boolean jindent) {
    init(filesOnly, diff, update, revert, jindent);
  }

  /**
   * Method setRevision
   *
   *
   * @param rev
   *
   */
  public void setRevision(SVNRevision rev) {
    this.revision = rev;
  }

  /**
   * Method setRows
   *
   *
   * @param location
   * @param changedPaths
   *
   */
  public void setRows(Location location, Set changedPaths) {
    this.location     = location;
    this.changedPaths = changedPaths;

    if(changedPaths.size() > 0) {
      for(Object path : changedPaths) {
        boolean enabled = location.getBranch().equals(FileTableRow.parseURL(path.toString(),
                                                                            FileTableRow.PARSE_URL_BRANCH));

        enableItems(enabled, UPDATE_CS, REVERT_CS);

        break;
      }
    }
  }

  /**
   * Method setRow
   *
   *
   * @param location
   * @param path
   *
   */
  public void setRow(Location location, Object path) {
    this.location     = location;
    this.changedPaths = new HashSet();

    changedPaths.add(path);
    enableItems(true, UPDATE, REVERT);
  }

  private void init(boolean filesOnly, boolean diff, boolean update, boolean revert, boolean jindent) {
    ActionListener menuListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        Task task = null;

        if(DIFF_WC.equals(event.getActionCommand()) || DIFF_CS_WC.equals(event.getActionCommand())) {
          task = new DiffTask(SVNApp.getApplication(), FileRowImpl.getList(changedPaths, location), revision);
        } else if(DIFF_PREVIOUS.equals(event.getActionCommand()) || DIFF_CS_PREVIOUS.equals(event.getActionCommand())) {
          task = new DiffTask(SVNApp.getApplication(), FileRowImpl.getList(changedPaths, location),
                              SVNRevision.create(revision.getNumber() - 1), revision);
        } else if(UPDATE.equals(event.getActionCommand()) || UPDATE_CS.equals(event.getActionCommand())) {
          task = new UpdateTask(SVNApp.getApplication(), FileRowImpl.getList(changedPaths, location), revision);
        } else if(REVERT.equals(event.getActionCommand()) || REVERT_CS.equals(event.getActionCommand())) {
          task = new MergeTask(SVNApp.getApplication(), FileRowImpl.getList(changedPaths, location), revision);
        } else if(JINDENT.equals(event.getActionCommand())) {
          List<FileTableRow> rows = new LinkedList<FileTableRow>();

          for(Object o : changedPaths) {
            if(o instanceof FileTableRow) {
              rows.add((FileTableRow)o);
            }
          }

          task = new JIndentTask(SVNApp.getApplication(), rows);
        }

        if(task != null) {
          SVNApp.getApplication().executeTask(task);
        }
      }
    };

    if(jindent) {
      newMenuItem(JINDENT, menuListener);
    }

    if(filesOnly) {
      if(diff) {
        newMenuItem(DIFF_WC, menuListener);

        if( !jindent) {
          newMenuItem(DIFF_PREVIOUS, menuListener);
        }
      }

      if(update) {
        newMenuItem(UPDATE, menuListener);
      }

      if(revert) {
        newMenuItem(REVERT, menuListener);
      }
    } else {
      if(diff) {
        newMenuItem(DIFF_CS_WC, menuListener);

        if( !jindent) {
          newMenuItem(DIFF_CS_PREVIOUS, menuListener);
        }
      }

      if(update) {
        newMenuItem(UPDATE_CS, menuListener);
      }

      if(revert) {
        newMenuItem(REVERT_CS, menuListener);
      }
    }
  }

  private void enableItems(boolean enabled, String... names) {
    for(String name : names) {
      JMenuItem item = items.get(name);

      if(item != null) {
        item.setEnabled(enabled);
      }
    }
  }

  private void newMenuItem(String name, ActionListener listener) {
    JMenuItem item = add(name);

    item.addActionListener(listener);
    items.put(name, item);
  }
}

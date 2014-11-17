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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.graph.GraphLogEntry;
import org.sdrinovsky.sdsvn.graph.GraphTree;
import org.sdrinovsky.sdsvn.graph.GraphView;

import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class GraphTask extends Task<Map<String, List<GraphLogEntry>>, Void> {
  FileTableRow row;
  Location     location;
  List<String> branches;
  Set<Long>    revisions = new HashSet<Long>();
  SVNLogClient logClient;
  long         starttime = 0;
  int          current   = 0;
  int          max       = 0;
  float        step      = 0f;

  class Handler implements ISVNLogEntryHandler {
    private File                file;
    private String              url;
    private String              branch;
    private List<GraphLogEntry> entries;
    private long                firstEntry     = -1;
    private Set<Long>           localRevisions = new HashSet<Long>();
    private SVNLogEntry         last           = null;

    /**
     * Constructor Handler
     *
     *
     * @param file
     * @param url
     * @param branch
     * @param entries
     *
     */
    public Handler(File file, String url, String branch, List<GraphLogEntry> entries) {
      this.file    = file;
      this.url     = url;
      this.branch  = branch;
      this.entries = entries;
    }

    /**
     * Method handleLogEntry
     *
     *
     *
     * @param svnLogEntry
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleLogEntry(SVNLogEntry svnLogEntry) throws SVNException {
      float progress = (current - 1) * step;

      if(progress < 0) {
        progress = 0f;
      } else if(progress > 1) {
        progress = 1f;
      }

      if(firstEntry == -1) {
        firstEntry = svnLogEntry.getRevision();

        setProgress(progress);
      } else {
        float f1 = (float)firstEntry;
        float f2 = (float)svnLogEntry.getRevision();

        setProgress(progress + (f1 - f2) / f1 * step);
      }

      Location location = new Location(file, url);
      //if(fileInPaths(svnLogEntry, location.getPath())) {
      GraphLogEntry node = new GraphLogEntry(svnLogEntry, location);

      if( !entries.contains(node)) {
        System.out.println("adding revision " + svnLogEntry.getRevision() + " for url " + location.getPath());
        entries.add(node);
        setMessage(file + "@" + branch + ": adding revision " + svnLogEntry.getRevision());
        localRevisions.add(svnLogEntry.getRevision());
      }

      //} 
      last = svnLogEntry;
    }

    /**
     * Method getLastEntry
     *
     *
     * @return
     *
     */
    public SVNLogEntry getLastEntry() {
      return last;
    }

    /**
     * Method getAddedRevisions
     *
     *
     * @return
     *
     */
    public Set<Long> getAddedRevisions() {
      return localRevisions;
    }

    private boolean fileInPaths(SVNLogEntry svnLogEntry, String path) {
      for(Object key : svnLogEntry.getChangedPaths().keySet()) {
        if(key.toString().endsWith(path) && (svnLogEntry.getChangedPaths().get(key).toString().indexOf('(') < 0)) {
          return true;
        }
      }

      return false;
    }
  }

  /**
   * Constructor GraphTask
   *
   *
   * @param app
   * @param row
   * @param location
   * @param folders
   *
   */
  public GraphTask(SVNApp app, FileTableRow row, Location location, List<String> folders) {
    super(app);

    this.row      = row;
    this.location = location;
    this.branches = folders;
    logClient     = app.getSVNClientManager().getLogClient();
    this.max      = branches.size();
    this.step     = 1f / max;
  }

  @Override
  protected Map<String, List<GraphLogEntry>> doInBackground() throws Exception {
    starttime = System.currentTimeMillis();

    Map<String, List<GraphLogEntry>> entries = new HashMap<String, List<GraphLogEntry>>();

    for(String branch : branches) {
      List<GraphLogEntry> list = new LinkedList<GraphLogEntry>();

      entries.put(branch, list);

      String url = row.getLocation().getURL();

      url = FileTableRow.switchBranch(url, branch);

      Handler handler = new Handler(row.getFile(), url, branch, list);

      System.out.println("graph url " + ++current + " of " + max + ": " + url);

      try {
        logClient.doLog(SVNURL.parseURIDecoded(url), null, SVNRevision.UNDEFINED, SVNRevision.HEAD,
                        SVNRevision.create(0), true, true, Long.MAX_VALUE, handler);

        long previous = 0;

        // look back one more revision on the copied branch so we can tie them together.
        for(long last = handler.getLastEntry().getRevision();
              (previous != last) && (revisions.size() > 0) && !revisions.contains(last);
              last = handler.getLastEntry().getRevision()) {
          previous = last;

          logClient.doLog(SVNURL.parseURIDecoded(url), null, SVNRevision.UNDEFINED, SVNRevision.create(last),
                          SVNRevision.create(0), false, true, 2, handler);
        }

        revisions.addAll(handler.getAddedRevisions());
      } catch(SVNException svne) {
        Logger.getLogger(GraphTask.class.getName()).log(Level.SEVERE, null, svne);
      }
    }

    return entries;
  }

  @Override
  protected void succeeded(Map<String, List<GraphLogEntry>> result) {
    setMessage("Building Graph...");

    GraphTree root = new GraphTree(row.getFileName());
    GraphTree tree = null;

    for(String branch : branches) {
      tree = null;

      for(GraphLogEntry entry : result.get(branch)) {
        if(tree == null) {
          tree = new GraphTree(entry);
        } else {
          tree = new GraphTree(entry, tree);
        }
      }

      if(tree != null) {
        root.add(new GraphTree(branch, tree));
      }
    }

    new GraphView(row, location, root).setVisible(true);

    String message = "Graph of " + row + " finished (" + (System.currentTimeMillis() - starttime) / 1000.0 + ").";

    setMessage(message);
    System.out.println(message);
  }

  @Override
  protected void failed(Throwable cause) {
    setMessage("Failed to graph " + row + ": " + cause.getMessage());
    Logger.getLogger(GraphTask.class.getName()).log(Level.WARNING, null, cause);
  }
}

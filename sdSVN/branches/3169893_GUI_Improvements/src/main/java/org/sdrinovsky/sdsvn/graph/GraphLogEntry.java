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

package org.sdrinovsky.sdsvn.graph;

import java.util.Date;
import java.util.Map;

import org.sdrinovsky.sdsvn.files.Location;

import org.tmatesoft.svn.core.SVNLogEntry;

/**
 * Class GraphLogEntry
 *
 *
 * @author
 * @version $Revision: 60 $
 */
public class GraphLogEntry {
  final private SVNLogEntry logEntry;
  final private Location    logLocation;

  /**
   * Constructor GraphLogEntry
   *
   *
   * @param entry
   * @param location
   *
   */
  public GraphLogEntry(SVNLogEntry entry, Location location) {
    logEntry    = entry;
    logLocation = location;
  }

  /**
   * Method getLogEntry
   *
   *
   * @return
   *
   */
  public SVNLogEntry getLogEntry() {
    return logEntry;
  }

  /**
   * Method getLogLocation
   *
   *
   * @return
   *
   */
  public Location getLogLocation() {
    return logLocation;
  }

  /**
   * Method getDate
   *
   *
   * @return
   *
   */
  public Date getDate() {
    return logEntry.getDate();
  }

  /**
   * Method getAuthor
   *
   *
   * @return
   *
   */
  public String getAuthor() {
    return logEntry.getAuthor();
  }

  /**
   * Method getMessage
   *
   *
   * @return
   *
   */
  public String getMessage() {
    return logEntry.getMessage();
  }

  /**
   * Method getRevision
   *
   *
   * @return
   *
   */
  public long getRevision() {
    return logEntry.getRevision();
  }

  /**
   * Method getChangedPaths
   *
   *
   * @return
   *
   */
  public Map getChangedPaths() {
    return logEntry.getChangedPaths();
  }

  String getURL() {
    return logLocation.getURL();
  }

  /**
   * Method hashCode
   *
   *
   * @return
   *
   */
  @Override
  public int hashCode() {
    return(int)logEntry.getRevision();
  }

  /**
   * Method equals
   *
   *
   * @param obj
   *
   * @return
   *
   */
  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }

    if(getClass() != obj.getClass()) {
      return false;
    }

    final GraphLogEntry other = (GraphLogEntry)obj;

    return logEntry.getRevision() == other.getRevision();
  }
}

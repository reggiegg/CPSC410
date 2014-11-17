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

package org.sdrinovsky.sdsvn.files;

import java.io.File;

import static org.sdrinovsky.sdsvn.files.FileTableRow.*;

/**
 * Class Location
 *
 *
 * @author
 * @version $Revision: 86 $
 */
public class Location {
  final private String server;
  final private String branch;
  final private String path;
  final private File   workingCopyBase;

  /**
   * Constructor Location
   *
   *
   *
   * @param file
   * @param url
   *
   */
  public Location(File file, String url) {
    this(file, parseURL(url, PARSE_URL_SERVER), parseURL(url, PARSE_URL_BRANCH), parseURL(url, PARSE_URL_PATH));
  }

  /**
   * Constructor Location
   *
   *
   *
   * @param file
   * @param server
   * @param branch
   * @param path
   *
   */
  public Location(File file, String server, String branch, String path) {
    this.server = server;
    this.branch = branch;
    this.path   = path;

    String tmpwc = file.toString().replace('\\', '/');

    if(tmpwc.length() > path.length() + 1) {
      this.workingCopyBase = new File(tmpwc.substring(0, tmpwc.length() - path.length()));
    } else {
      this.workingCopyBase = null;
    }
  }

  /**
   * Method getServer
   *
   *
   * @return
   *
   */
  public String getServer() {
    return server;
  }

  /**
   * Method getBranch
   *
   *
   * @return
   *
   */
  public String getBranch() {
    return branch;
  }

  /**
   * Method getPath
   *
   *
   * @return
   *
   */
  public String getPath() {
    return path;
  }

  /**
   * Method getURL
   *
   *
   * @return
   *
   */
  public String getURL() {
    return server + "/" + branch + "/" + path;
  }

  /**
   * Method getURL
   *
   *
   * @param newPath
   *
   * @return
   *
   */
  public String getURL(String newPath) {
    return server + "/" + branch + "/" + newPath;
  }

  /**
   * Method getWorkingCopyBase
   *
   *
   * @return
   *
   */
  public File getWorkingCopyBase() {
    return workingCopyBase;
  }

  /**
   * Method toString
   *
   *
   * @return
   *
   */
  @Override
  public String toString() {
    return branch;
  }
}

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

package org.sdrinovsky.sdsvn;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.tmatesoft.svn.util.SVNDebugLogAdapter;
import org.tmatesoft.svn.util.SVNLogType;

/**
 *
 * @author sdrinovsky
 */
public class SVNLogger extends SVNDebugLogAdapter {
  /**
   * Method log
   *
   *
   * @param logType
   * @param th
   * @param logLevel
   *
   */
  @Override
  public void log(SVNLogType logType, Throwable th, Level logLevel) {
    Logger.getLogger(SVNLogger.class.getName()).log(Level.SEVERE, null, th);
  }

  /**
   * Method log
   *
   *
   * @param logType
   * @param message
   * @param logLevel
   *
   */
  @Override
  public void log(SVNLogType logType, String message, Level logLevel) {
    System.out.println(message);
  }

  /**
   * Method log
   *
   *
   * @param logType
   * @param message
   * @param data
   *
   */
  @Override
  public void log(SVNLogType logType, String message, byte[] data) {
    System.out.println(message);
  }
}

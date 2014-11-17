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

package org.sdrinovsky.sdsvn.diff;

/**
 * Interface description
 *
 *
 * @version        Enter version here..., Sun, May 29, '11
 * @author         Enter your name here...
 */
public interface DiffView {
  /**
   * Method executeDiff
   *
   *
   * @param leftFile
   * @param rightFile
   * @param leftReadOnly
   * @param rightReadOnly
   *
   * @throws Exception
   *
   */
  public void executeDiff(String leftFile, String rightFile, boolean leftReadOnly,
                          boolean rightReadOnly) throws Exception;
}

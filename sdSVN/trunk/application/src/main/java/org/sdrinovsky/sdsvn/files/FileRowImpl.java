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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sdrinovsky
 */
public class FileRowImpl implements FileRow {
  /**
   * Method getList
   *
   *
   * @param file
   * @param location
   *
   * @return
   *
   */
  public static List<FileRow> getList(File file, Location location) {
    // TODO: need to make sure location path == file path
    return getList(new FileRowImpl(file, location));
  }

  /**
   * Method getList
   *
   *
   * @param file
   *
   * @return
   *
   */
  public static List<FileRow> getList(FileRow file) {
    List<FileRow> list = new LinkedList<FileRow>();

    list.add(file);

    return list;
  }

  /**
   * Method getList
   *
   *
   * @param files
   * @param location
   *
   * @return
   *
   */
  public static List<FileRow> getList(Set files, Location location) {
    // TODO: need to make sure location path == files path
    List<FileRow> list = new LinkedList<FileRow>();

    for(Object object : files) {
      if(object instanceof FileRow) {
        list.add((FileRow)object);
      } else {
        String path = FileTableRow.parseURL(object.toString(), FileTableRow.PARSE_URL_PATH);
        File   file = new File(location.getWorkingCopyBase(), path);

        list.add(new FileRowImpl(file, new Location(file, location.getURL(path))));
      }
    }

    return list;
  }

  private File     file;
  private Location location;

  /**
   * Constructor FileRowImpl
   *
   *
   * @param file
   * @param location
   *
   */
  public FileRowImpl(File file, Location location) {
    this.file     = file;
    this.location = location;
  }

  /**
   * Method getFile
   *
   *
   * @return
   *
   */
  @Override
  public File getFile() {
    return file;
  }

  /**
   * Method getLocation
   *
   *
   * @return
   *
   */
  @Override
  public Location getLocation() {
    return location;
  }

  /**
   * Method getStatus
   *
   *
   * @return
   *
   */
  @Override
  public String getStatus() {
    return null;
  }
}

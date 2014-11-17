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

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author sdrinovsky
 */
public class FileTableModel extends AbstractTableModel {
  private List<FileTableRow> rows = new LinkedList<FileTableRow>();

  /**
   * Method getColumnName
   *
   *
   * @param column
   *
   * @return
   *
   */
  @Override
  public String getColumnName(int column) {
    return FileTableRow.getColumnName(column);
  }

  /**
   * Method getColumnClass
   *
   *
   * @param columnIndex
   *
   * @return
   *
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return FileTableRow.getColumnClass(columnIndex);
  }

  /**
   * Method getRowCount
   *
   *
   * @return
   *
   */
  @Override
  public int getRowCount() {
    return rows.size();
  }

  /**
   * Method getColumnCount
   *
   *
   * @return
   *
   */
  @Override
  public int getColumnCount() {
    return FileTableRow.getColumnCount();
  }

  /**
   * Method getValueAt
   *
   *
   * @param rowIndex
   * @param columnIndex
   *
   * @return
   *
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return rows.get(rowIndex).get(columnIndex);
  }

  /**
   * Method getRow
   *
   *
   * @param rowIndex
   *
   * @return
   *
   */
  public FileTableRow getRow(int rowIndex) {
    return rows.get(rowIndex);
  }

  /**
   * Method indexOf
   *
   *
   * @param object
   *
   * @return
   *
   */
  public int indexOf(Object object) {
    return rows.indexOf(object);
  }

  /**
   * Method addRow
   *
   *
   * @param fileTableRow
   *
   */
  public void addRow(FileTableRow fileTableRow) {
    int index = rows.size();

    rows.add(fileTableRow);
    fireTableRowsInserted(index, index);
  }

  /**
   * Method addRows
   *
   *
   * @param fileTableRows
   *
   */
  public void addRows(List<FileTableRow> fileTableRows) {
    if((fileTableRows != null) && (fileTableRows.size() > 0)) {
      int index = rows.size();
      int count = 0;

      for(FileTableRow fileTableRow : new LinkedList<FileTableRow>(fileTableRows)) {
        rows.add(fileTableRow);

        count++;
      }

      fireTableRowsInserted(index, index + count - 1);
    }
  }

  /**
   * Method clear
   *
   *
   */
  public void clear() {
    int size = rows.size() - 1;

    if(size >= 0) {
      rows.clear();
      fireTableDataChanged();
    }
  }
}

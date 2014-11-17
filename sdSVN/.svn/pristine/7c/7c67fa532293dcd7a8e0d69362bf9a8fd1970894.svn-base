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

import java.awt.Color;
import java.awt.Component;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Class FileTableRenderer
 *
 *
 * @author
 * @version $Revision$
 */
public class FileTableRenderer extends DefaultTableCellRenderer {
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy KK:mm:ss a");
  //private Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
  public static final Color alternateColor = new Color(242, 242, 242);
  public static final Color red            = new Color(255, 212, 212);
  public static final Color alternateRed   = new Color(255, 200, 200);
  public static final Color blue           = new Color(212, 212, 255);
  public static final Color alternateBlue  = new Color(200, 200, 255);
  public static final Color green          = new Color(212, 255, 212);
  public static final Color alternateGreen = new Color(200, 255, 200);
  public static final Color white          = Color.white;

  /**
   * Method getTableCellRendererComponent
   *
   *
   * @param table
   * @param value
   * @param isSelected
   * @param hasFocus
   * @param row
   * @param column
   *
   * @return
   *
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                 int row, int column) {
    FileTableModel model = (FileTableModel)table.getModel();

    if(value instanceof Date) {
      value = dateFormat.format((Date)value);
    }

    Component comp     = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    int       modelRow = table.getRowSorter().convertRowIndexToModel(row);

    if( !isSelected && !hasFocus) {
      if(row % 2 == 0) {
        if(model.getRow(modelRow).isCommitable()) {
          comp.setBackground(alternateRed);
        } else if(model.getRow(modelRow).isIgnored()) {
          comp.setBackground(alternateBlue);
        } else if(model.getRow(modelRow).needsUpdate()) {
          comp.setBackground(alternateGreen);
        } else {
          comp.setBackground(alternateColor);
        }
      } else {
        if(model.getRow(modelRow).isCommitable()) {
          comp.setBackground(red);
        } else if(model.getRow(modelRow).isIgnored()) {
          comp.setBackground(blue);
        } else if(model.getRow(modelRow).needsUpdate()) {
          comp.setBackground(green);
        } else {
          comp.setBackground(white);
        }
      }
    }

    if(column == 3) {
      setToolTipText(value.toString());
    } else {
      setToolTipText(null);
    }

    return comp;
  }
}

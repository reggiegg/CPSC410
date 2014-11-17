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

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SortedListModel;
import org.sdrinovsky.sdsvn.files.FileTableRenderer;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNRevisionRange;

/**
 *
 * @author sdrinovsky
 */
public class HistoryDialog extends javax.swing.JDialog {
  private long                commitRevision = 0;
  private boolean             modified       = false;
  private Location            location;
  private HistoryPopupMenu    changeSetPopupMenu;
  private HistoryPopupMenu    filePopupMenu;
  private long                minRevision;
  private long                maxRevision;
  private Vector<SVNLogEntry> data       = new Vector<SVNLogEntry>();
  private AbstractTableModel  tableModel = new AbstractTableModel() {
    @Override
    public String getColumnName(int column) {
      switch(column) {
        case 0:
          return "Revision";
        case 1:
          return "Date";
        case 2:
          return "Author";
        case 3:
          return "Message";
        default:
          return "Unknown Column";
      }
    }
    @Override
    public Object getValueAt(int row, int column) {
      switch(column) {
        case 0:
          return data.get(row).getRevision();
        case 1:
          return data.get(row).getDate();
        case 2:
          return data.get(row).getAuthor();
        case 3:
          return data.get(row).getMessage();
        default:
          return "Unknown Column";
      }
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      switch(columnIndex) {
        case 0:
          return Long.class;
        case 1:
          return Date.class;
        default:
          return String.class;
      }
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }
    @Override
    public int getRowCount() {
      return data.size();
    }
    @Override
    public int getColumnCount() {
      return 4;
    }
  };
  private DefaultTableCellRenderer coloredRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int r, int c) {
      Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, r, c);
      Long      rev  = (Long)table.getModel().getValueAt(table.convertRowIndexToModel(r), 0);

      if(commitRevision == rev) {
        if(modified) {
          comp.setForeground(Color.red);
        } else {
          comp.setForeground(Color.blue);
        }
      } else {
        comp.setForeground(Color.black);
      }

      if(c == 3) {
        setToolTipText(value.toString());
      } else {
        setToolTipText(null);
      }

      return comp;
    }
  };
  private DefaultListModel listModel       = new DefaultListModel();
  private ListModel        sortedListModel = new SortedListModel(listModel);
  private Action           acceptAction    = new AbstractAction("Accept") {
    @Override
    public void actionPerformed(ActionEvent e) {
      accept = true;

      setVisible(false);
    }
  };
  private Action closeAction = new AbstractAction("Close") {
    @Override
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
    }
  };
  private boolean accept = false;

  /**
   * Creates new form HistoryDialog
   *
   * @param parent
   * @param modal
   * @param merge
   */
  public HistoryDialog(java.awt.Frame parent, boolean modal, boolean merge) {
    super(parent, modal);

    initComponents();
    acceptButton.setVisible(false);
    jTable1.setDefaultRenderer(Object.class, coloredRenderer);
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(95);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(105);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(95);
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(655);
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    jTable1.getRowSorter().setSortKeys(Collections.singletonList(new SortKey(0, SortOrder.DESCENDING)));
    jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if( !e.getValueIsAdjusting()) {
          int selected = jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow());

          listModel.clear();

          if(changeSetPopupMenu != null) {
            changeSetPopupMenu.setRevision(SVNRevision.create(data.get(selected).getRevision()));
          }

          if(filePopupMenu != null) {
            filePopupMenu.setRevision(SVNRevision.create(data.get(selected).getRevision()));
          }

          Map map = data.get(selected).getChangedPaths();

          if(changeSetPopupMenu != null) {
            changeSetPopupMenu.setRows(location, map.keySet());
          }

          for(Object o : map.values()) {
            listModel.addElement(o);
          }
        }
      }
    });
    jList1.setVisibleRowCount(6);
    jList1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if(filePopupMenu != null) {
          filePopupMenu.setRow(location, jList1.getSelectedValue());
        }
      }
    });
    jList1.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                    boolean cellHasFocus) {
        Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if( !isSelected && !cellHasFocus) {
          if((value != null) && value.toString().endsWith(location.getPath())) {
            comp.setBackground(FileTableRenderer.blue);
          }
        }

        return comp;
      }
    });
    cbReintegrate.setVisible(merge);
  }

  /**
   * Method setLog
   *
   *
   * @param history
   *
   */
  public void setLog(List<SVNLogEntry> history) {
    data.clear();

    minRevision = Long.MAX_VALUE;
    maxRevision = Long.MIN_VALUE;

    for(SVNLogEntry entry : history) {
      minRevision = Math.min(minRevision, entry.getRevision());
      maxRevision = Math.max(maxRevision, entry.getRevision());

      data.add(entry);
    }

    tableModel.fireTableDataChanged();
  }

  /**
   * Method setCommittedRevision
   *
   *
   * @param commitRevision
   * @param modified
   *
   */
  public void setCommittedRevision(long commitRevision, boolean modified) {
    this.commitRevision = commitRevision;
    this.modified       = modified;
  }

  /**
   * Method setFileRow
   *
   *
   * @param location
   * @param diff
   * @param update
   * @param revert
   *
   */
  public void setFileRow(Location location, boolean diff, boolean update, boolean revert) {
    this.location      = location;
    changeSetPopupMenu = new HistoryPopupMenu(false, diff, update, revert, false);
    filePopupMenu      = new HistoryPopupMenu(true, diff, update, revert, false);

    jTable1.setComponentPopupMenu(changeSetPopupMenu);
    jList1.setComponentPopupMenu(filePopupMenu);
  }

  /**
   * Method didAccept
   *
   *
   * @return
   *
   */
  public boolean didAccept() {
    return accept;
  }

  /**
   * Method isReintegrate
   *
   *
   * @return
   *
   */
  public boolean isReintegrate() {
    return cbReintegrate.isSelected();
  }

  /**
   * Method setAcceptText
   *
   *
   * @param text
   *
   */
  public void setAcceptText(String text) {
    acceptAction.putValue(Action.NAME, text);
    acceptButton.setVisible(true);
  }

  /**
   * Method setMultiSelect
   *
   *
   * @param b
   *
   */
  public void setMultiSelect(boolean b) {
    if(b) {
      jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    } else {
      jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }
  }

  /**
   * Method getSelectedRevisions
   *
   *
   * @return
   *
   */
  public List<SVNRevisionRange> getSelectedRevisions() {
    List<SVNRevisionRange> revisions = new LinkedList<SVNRevisionRange>();

    if((jTable1.getSelectedRowCount() == 0) || (jTable1.getSelectedRowCount() == jTable1.getRowCount())) {
      revisions.add(new SVNRevisionRange(SVNRevision.create(minRevision), SVNRevision.create(maxRevision)));
    } else {
      for(int i : jTable1.getSelectedRows()) {
        int  selected = jTable1.getRowSorter().convertRowIndexToModel(i);
        long r        = data.get(selected).getRevision();

        revisions.add(new SVNRevisionRange(SVNRevision.create(r - 1), SVNRevision.create(r)));
      }
    }

    return revisions;
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    buttonPanel   = new javax.swing.JPanel();
    closeButton   = new javax.swing.JButton(closeAction);
    acceptButton  = new javax.swing.JButton(acceptAction);
    cbReintegrate = new javax.swing.JCheckBox();
    jSplitPane1   = new javax.swing.JSplitPane();
    jScrollPane1  = new javax.swing.JScrollPane();
    jTable1       = new JTable() {
      public Point getToolTipLocation(MouseEvent e) {
        if(getToolTipText(e) != null) {
          Rectangle rect = jTable1.getCellRect(jTable1.rowAtPoint(e.getPoint()), jTable1.columnAtPoint(e.getPoint()),
                                               true);

          return new Point(rect.x + 2, rect.y - 1);
        } else {
          return null;
        }
      }
    };
    jScrollPane2 = new javax.swing.JScrollPane();
    jList1       = new javax.swing.JList();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setName("Form");                       // NOI18N
    buttonPanel.setName("buttonPanel");    // NOI18N
    closeButton.setMnemonic('C');

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getResourceMap(HistoryDialog.class);

    closeButton.setText(resourceMap.getString("closeButton.text"));        // NOI18N
    closeButton.setName("closeButton");                                    // NOI18N
    acceptButton.setText(resourceMap.getString("acceptButton.text"));      // NOI18N
    acceptButton.setName("acceptButton");                                  // NOI18N
    cbReintegrate.setText(resourceMap.getString("cbReintegrate.text"));    // NOI18N
    cbReintegrate.setName("cbReintegrate");                                // NOI18N

    javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);

    buttonPanel.setLayout(buttonPanelLayout);
    buttonPanelLayout.setHorizontalGroup(
       buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         buttonPanelLayout.createSequentialGroup().addComponent(cbReintegrate).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED, 459, Short.MAX_VALUE).addComponent(
           acceptButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           closeButton).addContainerGap()));
    buttonPanelLayout.setVerticalGroup(
       buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         buttonPanelLayout.createSequentialGroup().addGroup(
           buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
             buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
               closeButton).addComponent(acceptButton)).addComponent(cbReintegrate)).addContainerGap(
                 javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    jSplitPane1.setDividerLocation(200);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setResizeWeight(1.0);
    jSplitPane1.setName("jSplitPane1");      // NOI18N
    jScrollPane1.setName("jScrollPane1");    // NOI18N
    jTable1.setAutoCreateRowSorter(true);
    jTable1.setModel(tableModel);
    jTable1.setComponentPopupMenu(changeSetPopupMenu);
    jTable1.setName("jTable1");              // NOI18N
    jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(jTable1);
    jSplitPane1.setTopComponent(jScrollPane1);
    jScrollPane2.setName("jScrollPane2");    // NOI18N
    jList1.setModel(sortedListModel);
    jList1.setName("jList1");                // NOI18N
    jScrollPane2.setViewportView(jList1);
    jSplitPane1.setRightComponent(jScrollPane2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addContainerGap().addComponent(
           buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
           Short.MAX_VALUE)).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE));
    layout.setVerticalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addComponent(
           jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
           javax.swing.GroupLayout.PREFERRED_SIZE)));

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    setBounds((screenSize.width - 718) / 2, (screenSize.height - 417) / 2, 718, 417);
  }    // </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton     acceptButton;
  private javax.swing.JPanel      buttonPanel;
  private javax.swing.JCheckBox   cbReintegrate;
  private javax.swing.JButton     closeButton;
  private javax.swing.JList       jList1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane  jSplitPane1;
  private javax.swing.JTable      jTable1;
  // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PropertiesDialog.java
 *
 * Created on May 29, 2011, 9:59:03 PM
 */

package org.sdrinovsky.sdsvn.dialogs;

import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.wc.SVNPropertyData;

/**
 *
 * @author sdrinovsky
 */
public class PropertiesDialog extends javax.swing.JDialog {
  public static final String[] DIR_PROPS = new String[]{"svn:externals", "svn:ignore"};
  public static final String[] FILE_PROPS = new String[]{"svn:eol-style", "svn:keywords", "svn:mime-type",
                                                         "svn:needs-lock", "svn:special"};

  class PropertyTableModel extends AbstractTableModel {
    private final List<SVNPropertyData> data;

    /**
     * Constructor PropertyTableModel
     *
     *
     * @param data
     *
     */
    public PropertyTableModel(List<SVNPropertyData> data) {
      this.data = data;
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
      return data.size();
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
      return 2;
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
      switch(columnIndex) {
        case 0:
          return data.get(rowIndex).getName();
        case 1:
          return data.get(rowIndex).getValue().getString();
        default:
          return "error";
      }
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
      return String.class;
    }

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
      switch(column) {
        case 0:
          return "Name";
        case 1:
          return "Value";
        default:
          return "error";
      }
    }

    /**
     * Method getProperty
     *
     *
     * @param name
     *
     * @return
     *
     */
    public String getProperty(String name) {
      for(SVNPropertyData row : data) {
        if(row.getName().equals(name)) {
          return row.getValue().getString();
        }
      }

      return null;
    }

    /**
     * Method addProperty
     *
     *
     * @param name
     * @param value
     *
     */
    public void addProperty(String name, String value) {
      int rowIndex = 0;

      for(SVNPropertyData row : data) {
        if(row.getName().equals(name)) {
          data.remove(row);

          if((value == null) || (value.trim().length() == 0)) {
            fireTableRowsDeleted(rowIndex, rowIndex);
          } else {
            data.add(rowIndex, new SVNPropertyData(name, SVNPropertyValue.create(value), null));
            fireTableCellUpdated(rowIndex, 1);
          }

          return;
        }

        rowIndex++;
      }

      if((value != null) && (value.trim().length() > 0)) {
        data.add(rowIndex, new SVNPropertyData(name, SVNPropertyValue.create(value), null));
        fireTableRowsInserted(rowIndex, rowIndex);
      }
    }

    /**
     * Method getProperties
     *
     *
     * @return
     *
     */
    public List<SVNPropertyData> getProperties() {
      return data;
    }
  }

  private boolean            okPressed;
  private PropertyTableModel data;

  /**
   * Creates new form PropertiesDialog 
   *
   * @param parent
   * @param modal
   */
  public PropertiesDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);

    initComponents();
    propertyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if( !e.getValueIsAdjusting()) {
          if(propertyTable.getSelectedRow() >= 0) {
            propertyEdit.setSelectedItem(data.getValueAt(propertyTable.getSelectedRow(), 0));
            valueEdit.setText(data.getValueAt(propertyTable.getSelectedRow(), 1).toString());
          } else {
            propertyEdit.setSelectedItem(null);
            valueEdit.setText(null);
          }
        }
      }
    });
  }

  /**
   * Method setProperties
   *
   *
   * @param title
   * @param folder
   * @param properties
   *
   */
  public void setProperties(String title, boolean folder, List<SVNPropertyData> properties) {
    setTitle("SVN Properties - " + title);

    if(folder) {
      propertyEdit.setModel(new DefaultComboBoxModel(DIR_PROPS));
      executableCheckBox.setEnabled(false);
    } else {
      propertyEdit.setModel(new DefaultComboBoxModel(FILE_PROPS));
      executableCheckBox.setEnabled(true);
    }

    executableCheckBox.setSelected(false);

    List<SVNPropertyData> propertiesCopy = new LinkedList<SVNPropertyData>();

    for(SVNPropertyData property : properties) {
      if("svn:executable".equals(property.getName())) {
        executableCheckBox.setSelected(true);
      } else {
        propertiesCopy.add(new SVNPropertyData(property.getName(),
                                               SVNPropertyValue.create(property.getValue().getString()), null));
      }
    }

    propertyTable.setModel(data = new PropertyTableModel(propertiesCopy));

    if(propertiesCopy.size() > 0) {
      propertyTable.getSelectionModel().addSelectionInterval(0, 0);
    }
  }

  /**
   * Method getProperties
   *
   *
   * @return
   *
   */
  public List<SVNPropertyData> getProperties() {
    List<SVNPropertyData> properties = data.getProperties();

    if(executableCheckBox.isSelected()) {
      properties.add(new SVNPropertyData("svn:executable", SVNPropertyValue.create("*"), null));
    }

    return properties;
  }

  /**
   * Method setVisible
   *
   *
   * @param b
   *
   */
  @Override
  public void setVisible(boolean b) {
    if(b) {
      okPressed = false;
    }

    super.setVisible(b);
  }

  /**
   * Method wasOkPressed
   *
   *
   * @return
   *
   */
  public boolean wasOkPressed() {
    return okPressed;
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
    jScrollPane1       = new javax.swing.JScrollPane();
    propertyTable      = new javax.swing.JTable();
    lblName            = new javax.swing.JLabel();
    propertyEdit       = new javax.swing.JComboBox();
    lblValue           = new javax.swing.JLabel();
    jScrollPane2       = new javax.swing.JScrollPane();
    valueEdit          = new javax.swing.JTextArea();
    cancelButton       = new javax.swing.JButton();
    okButton           = new javax.swing.JButton();
    addButton          = new javax.swing.JButton();
    removeButton       = new javax.swing.JButton();
    executableCheckBox = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setName("Form");                          // NOI18N
    jScrollPane1.setName("propertyTable");    // NOI18N
    propertyTable.setAutoCreateRowSorter(true);
    propertyTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{
    }, new String[]{"Name", "Value"}) {
      Class[]   types   = new Class[]{java.lang.String.class, java.lang.String.class};
      boolean[] canEdit = new boolean[]{false, false};
      public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
      }
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
      }
    });
    propertyTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(propertyTable);
    lblName.setLabelFor(propertyEdit);

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getResourceMap(PropertiesDialog.class);

    lblName.setText(resourceMap.getString("lblName.text"));    // NOI18N
    lblName.setName("lblName");                                // NOI18N
    propertyEdit.setEditable(true);
    propertyEdit.setName("propertyEdit");                      // NOI18N
    propertyEdit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        propertyEditActionPerformed(evt);
      }
    });
    lblValue.setLabelFor(valueEdit);
    lblValue.setText(resourceMap.getString("lblValue.text"));            // NOI18N
    lblValue.setName("lblValue");                                        // NOI18N
    jScrollPane2.setName("jScrollPane2");                                // NOI18N
    valueEdit.setColumns(20);
    valueEdit.setRows(5);
    valueEdit.setName("valueEdit");                                      // NOI18N
    jScrollPane2.setViewportView(valueEdit);
    cancelButton.setText(resourceMap.getString("cancelButton.text"));    // NOI18N
    cancelButton.setName("cancelButton");                                // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });
    okButton.setText(resourceMap.getString("okButton.text"));    // NOI18N
    okButton.setName("okButton");                                // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    addButton.setText(resourceMap.getString("addButton.text"));    // NOI18N
    addButton.setName("addButton");                                // NOI18N
    addButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });
    removeButton.setText(resourceMap.getString("removeButton.text"));    // NOI18N
    removeButton.setName("removeButton");                                // NOI18N
    removeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        removeButtonActionPerformed(evt);
      }
    });
    executableCheckBox.setText(resourceMap.getString("executableCheckBox.text"));    // NOI18N
    executableCheckBox.setName("executableCheckBox");                                // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addContainerGap().addGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
             jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 521,
             Short.MAX_VALUE).addGroup(
               layout.createSequentialGroup().addGroup(
                 layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                   lblName).addComponent(lblValue)).addPreferredGap(
                     javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                     layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                       javax.swing.GroupLayout.Alignment.TRAILING,
                       layout.createSequentialGroup().addComponent(addButton).addPreferredGap(
                         javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeButton).addPreferredGap(
                         javax.swing.LayoutStyle.ComponentPlacement.RELATED, 258, Short.MAX_VALUE).addComponent(
                         okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                         cancelButton)).addComponent(propertyEdit, 0, 468, Short.MAX_VALUE).addComponent(
                           jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))).addComponent(
                             executableCheckBox, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap()));
    layout.setVerticalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addContainerGap().addComponent(executableCheckBox).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             propertyEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblName)).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
               layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                 lblValue).addComponent(
                 jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66,
                 javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                   javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                   layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                     okButton).addComponent(addButton).addComponent(removeButton).addComponent(
                     cancelButton)).addContainerGap()));

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    setBounds((screenSize.width - 545) / 2, (screenSize.height - 342) / 2, 545, 342);
  }                                                                             // </editor-fold>//GEN-END:initComponents

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_cancelButtonActionPerformed
    setVisible(false);
  }                                                                         //GEN-LAST:event_cancelButtonActionPerformed

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_okButtonActionPerformed
    okPressed = true;

    setVisible(false);
  }                                                                          //GEN-LAST:event_okButtonActionPerformed

  private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_addButtonActionPerformed
    data.addProperty(propertyEdit.getSelectedItem().toString(), valueEdit.getText());
  }                                                                             //GEN-LAST:event_addButtonActionPerformed

  private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_removeButtonActionPerformed
    data.addProperty(propertyEdit.getSelectedItem().toString(), null);
  }                                                                             //GEN-LAST:event_removeButtonActionPerformed

  private void propertyEditActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_propertyEditActionPerformed
    Object selected = propertyEdit.getSelectedItem();

    if(selected != null) {
      valueEdit.setText(data.getProperty(selected.toString()));
    } else {
      valueEdit.setText(null);
    }
  }    //GEN-LAST:event_propertyEditActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton     addButton;
  private javax.swing.JButton     cancelButton;
  private javax.swing.JCheckBox   executableCheckBox;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JLabel      lblName;
  private javax.swing.JLabel      lblValue;
  private javax.swing.JButton     okButton;
  private javax.swing.JComboBox   propertyEdit;
  private javax.swing.JTable      propertyTable;
  private javax.swing.JButton     removeButton;
  private javax.swing.JTextArea   valueEdit;
  // End of variables declaration//GEN-END:variables
}

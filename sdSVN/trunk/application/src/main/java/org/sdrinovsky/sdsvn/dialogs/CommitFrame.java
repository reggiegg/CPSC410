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

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.swing.JTextComponentSpellChecker;

import java.awt.Frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.sdrinovsky.sdsvn.SortedListModel;
import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;

import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author  sdrinovsky
 */
public class CommitFrame extends javax.swing.JDialog {
  private boolean          okPressed;
  private DefaultListModel model         = new DefaultListModel();
  private ListModel        sortedModel   = new SortedListModel(model);
  private Location         location      = null;
  private HistoryPopupMenu filePopupMenu = new HistoryPopupMenu(true, true, false, false, true);

  /**
   * Creates new form CommitFrame
   *
   * @param owner
   */
  public CommitFrame(Frame owner) {
    super(owner, true);

    initComponents();

    try {
      URL resource = CommitFrame.class.getResource("/dict/english.dic");

      if(resource != null) {
        URLConnection resourceConnection = resource.openConnection();
        InputStream   in                 = resourceConnection.getInputStream();
        SpellDictionaryHashMap spellDictionary =
          new SpellDictionaryHashMap(new BufferedReader(new InputStreamReader(in)));
        JTextComponentSpellChecker spellCheckMessageArea = new JTextComponentSpellChecker(spellDictionary, null, null);

        spellCheckMessageArea.startAutoSpellCheck(messageArea);
      }
    } catch(IOException ioe) {
    }

    fileList.setComponentPopupMenu(filePopupMenu);
    fileList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if(filePopupMenu != null) {
          filePopupMenu.setRows(location, new HashSet(Arrays.asList(fileList.getSelectedValues())));
        }
      }
    });
    filePopupMenu.setRevision(SVNRevision.HEAD);
  }

  /**
   * Method setMessage
   *
   *
   *
   * @param messages
   *
   */
  public void setMessages(List<String> messages) {
    messageComboBox.setModel(new javax.swing.DefaultComboBoxModel(messages.toArray()));

    if(messages.size() > 0) {
      messageComboBox.setSelectedIndex(0);
    }
  }

  /**
   * Method setRecurvive
   *
   *
   * @param b
   *
   */
  public void setRecurvive(boolean b) {
    recursiveCheckBox.setSelected(b);
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
    messageArea.grabFocus();
  }

  /**
   * Method setFiles
   *
   *
   * @param files
   *
   */
  public void setFiles(List<FileTableRow> files) {
    model.clear();

    location = null;

    for(FileTableRow file : files) {
      if(location == null) {
        location = file.getLocation();
      }

      model.addElement(file);
    }
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
   * Method getMessage
   *
   *
   * @return
   *
   */
  public String getMessage() {
    return messageArea.getText();
  }

  /**
   * Method isRecursive
   *
   *
   * @return
   *
   */
  public boolean isRecursive() {
    return recursiveCheckBox.isSelected();
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
    jSplitPane1       = new javax.swing.JSplitPane();
    jScrollPane2      = new javax.swing.JScrollPane();
    fileList          = new javax.swing.JList();
    messagePanel      = new javax.swing.JPanel();
    jScrollPane1      = new javax.swing.JScrollPane();
    messageArea       = new javax.swing.JTextPane();
    recursiveCheckBox = new javax.swing.JCheckBox();
    messageComboBox   = new javax.swing.JComboBox();
    okButton          = new javax.swing.JButton();
    cancelButton      = new javax.swing.JButton();

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getResourceMap(CommitFrame.class);

    setTitle(resourceMap.getString("Form.title"));                                               // NOI18N
    setName("Form");                                                                             // NOI18N
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setName("jSplitPane1");                                                          // NOI18N
    jScrollPane2.setName("jScrollPane2");                                                        // NOI18N
    fileList.setModel(sortedModel);
    fileList.setName("fileList");                                                                // NOI18N
    jScrollPane2.setViewportView(fileList);
    jSplitPane1.setRightComponent(jScrollPane2);
    messagePanel.setName("messagePanel");                                                        // NOI18N
    jScrollPane1.setName("jScrollPane1");                                                        // NOI18N
    messageArea.setName("messageArea");                                                          // NOI18N
    messageArea.setPreferredSize(new java.awt.Dimension(6, 128));
    jScrollPane1.setViewportView(messageArea);
    recursiveCheckBox.setText(resourceMap.getString("recursiveCheckBox.text"));                  // NOI18N
    recursiveCheckBox.setToolTipText(resourceMap.getString("recursiveCheckBox.toolTipText"));    // NOI18N
    recursiveCheckBox.setName("recursiveCheckBox");                                              // NOI18N
    messageComboBox.setToolTipText(resourceMap.getString("messageComboBox.toolTipText"));        // NOI18N
    messageComboBox.setName("messageComboBox");                                                  // NOI18N
    messageComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        messageComboBoxActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);

    messagePanel.setLayout(messagePanelLayout);
    messagePanelLayout.setHorizontalGroup(
       messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         messagePanelLayout.createSequentialGroup().addComponent(recursiveCheckBox).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           messageComboBox, 0, 544, Short.MAX_VALUE)).addComponent(
             jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE));
    messagePanelLayout.setVerticalGroup(
       messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         messagePanelLayout.createSequentialGroup().addComponent(
           jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
           messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             recursiveCheckBox).addComponent(
             messageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE))));
    jSplitPane1.setLeftComponent(messagePanel);
    okButton.setText(resourceMap.getString("okButton.text"));    // NOI18N
    okButton.setName("okButton");                                // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    cancelButton.setText(resourceMap.getString("cancelButton.text"));    // NOI18N
    cancelButton.setName("cancelButton");                                // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addContainerGap().addGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
             jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE).addGroup(
             layout.createSequentialGroup().addComponent(okButton).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))).addContainerGap()));
    layout.setVerticalGroup(
       layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         javax.swing.GroupLayout.Alignment.TRAILING,
         layout.createSequentialGroup().addContainerGap().addComponent(
           jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             cancelButton).addComponent(okButton)).addContainerGap()));

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    setBounds((screenSize.width - 666) / 2, (screenSize.height - 503) / 2, 666, 503);
  }                                                                         // </editor-fold>//GEN-END:initComponents

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_okButtonActionPerformed
    okPressed = true;

    setVisible(false);
  }                                                                             //GEN-LAST:event_okButtonActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_cancelButtonActionPerformed
    setVisible(false);
  }                                                                                //GEN-LAST:event_cancelButtonActionPerformed

  private void messageComboBoxActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_messageComboBoxActionPerformed
    String message = messageComboBox.getSelectedItem().toString();

    messageArea.setText(message);
    messageArea.setSelectionStart(0);
    messageArea.setSelectionEnd(message.length());
  }    //GEN-LAST:event_messageComboBoxActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton     cancelButton;
  private javax.swing.JList       fileList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane  jSplitPane1;
  private javax.swing.JTextPane   messageArea;
  private javax.swing.JComboBox   messageComboBox;
  private javax.swing.JPanel      messagePanel;
  private javax.swing.JButton     okButton;
  private javax.swing.JCheckBox   recursiveCheckBox;
  // End of variables declaration//GEN-END:variables
}

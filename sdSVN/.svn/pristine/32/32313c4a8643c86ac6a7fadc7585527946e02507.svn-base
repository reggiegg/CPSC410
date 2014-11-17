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

package org.sdrinovsky.sdsvn.tree;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import java.util.Enumeration;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.sdrinovsky.sdsvn.SVNApp;
import org.sdrinovsky.sdsvn.dialogs.LoginDialog;

import org.openide.util.Exceptions;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 *
 * @author  sdrinovsky
 */
public class RepoTreeView extends javax.swing.JDialog {
  private static void setupLibrary() {
    DAVRepositoryFactory.setup();
    SVNRepositoryFactoryImpl.setup();
    FSRepositoryFactory.setup();
  }

  /**
   * Method createRepoTreeModel
   *
   *
   * @param url
   * @param recursive
   *
   * @return
   *
   */
  public static DefaultMutableTreeNode createRootNode(String url, boolean recursive) {
    setupLibrary();

    try {
      SVNRepository repo = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));

      repo.setAuthenticationManager(LoginDialog.authManager);

      return new DefaultMutableTreeNode(new RepoNodeData(repo, recursive));
    } catch(SVNException svne) {
      System.err.println("error while creating an SVNRepository for location '" + url + "': " + svne.getMessage());
    }

    return null;
  }

  /**
   * Method getRepoPathFromPath
   *
   *
   * @param path
   *
   * @return
   *
   */
  public static String getRepoPathFromPath(TreePath path) {
    if(path != null) {
      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();

      return((RepoNodeData)selectedNode.getUserObject()).getRepoPath();
    } else {
      return null;
    }
  }

  private String                 url;
  private boolean                recursive        = false;
  private DefaultTreeModel       model            = null;
  private DefaultMutableTreeNode root             = null;
  private String                 selectedRepoPath = null;

  /**
   * Creates new form RepoTreeView
   *
   *
   * @param owner
   * @param modal
   * @param url
   * @param recursive
   */
  public RepoTreeView(Frame owner, boolean modal, String url, boolean recursive) {
    super(owner, modal);

    initComponents();

    this.recursive = recursive;

    repoTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent e) {
        selectedRepoTextField.setText(getRepoPathFromPath(e.getPath()));
      }
    });
    loadRepo(url, true);
  }

  /**
   * Method getModel
   *
   *
   * @return
   *
   */
  public TreeModel getModel() {
    return model;
  }

  private String getLocationURL() {
    String item         = null;
    Object selectedItem = locationField.getSelectedItem();

    if(selectedItem != null) {
      item = selectedItem.toString();

      DefaultComboBoxModel cbModel = (DefaultComboBoxModel)locationField.getModel();

      if(cbModel.getIndexOf(item) != 0) {
        cbModel.removeElement(item);
        cbModel.insertElementAt(item, 0);
        locationField.setSelectedIndex(0);
      }
    }

    return item;
  }

  /**
   * Method loadRepo
   *
   *
   * @param url
   * @param focusSearch
   *
   */
  public void loadRepo(String url, boolean focusSearch) {
    if((url != null) && !url.equals(this.url)) {
      this.url = url;
      root     = createRootNode(url, recursive);
      model    = new DefaultTreeModel(root);

      model.setAsksAllowsChildren(true);
      repoTree.setModel(model);
      repoTree.expandRow(0);

      if(focusSearch) {
        searchTextField.requestFocus();
      }

      startWorker((DefaultMutableTreeNode)model.getRoot());
      locationField.setSelectedItem(url);
    }
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
    repoPopupMenu         = new javax.swing.JPopupMenu();
    miCreate              = new javax.swing.JMenuItem();
    miCopy                = new javax.swing.JMenuItem();
    miDelete              = new javax.swing.JMenuItem();
    jScrollPane1          = new javax.swing.JScrollPane();
    repoTree              = new javax.swing.JTree();
    searchPanel           = new javax.swing.JPanel();
    searchTextField       = new javax.swing.JTextField();
    findButton            = new javax.swing.JButton();
    selectedRepoTextField = new javax.swing.JTextField();
    okButton              = new javax.swing.JButton();
    cancelButton          = new javax.swing.JButton();
    locationPanel         = new javax.swing.JPanel();
    locationLabel         = new javax.swing.JLabel();
    locationButton        = new javax.swing.JButton();
    locationField         = new javax.swing.JComboBox();

    repoPopupMenu.setName("repoPopupMenu");    // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getResourceMap(RepoTreeView.class);

    miCreate.setText(resourceMap.getString("miCreate.text"));    // NOI18N
    miCreate.setName("miCreate");                                // NOI18N
    miCreate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miCreateActionPerformed(evt);
      }
    });
    repoPopupMenu.add(miCreate);
    miCopy.setText(resourceMap.getString("miCopy.text"));    // NOI18N
    miCopy.setName("miCopy");                                // NOI18N
    miCopy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miCopyActionPerformed(evt);
      }
    });
    repoPopupMenu.add(miCopy);
    miDelete.setText(resourceMap.getString("miDelete.text"));    // NOI18N
    miDelete.setName("miDelete");                                // NOI18N
    miDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miDeleteActionPerformed(evt);
      }
    });
    repoPopupMenu.add(miDelete);
    setTitle(resourceMap.getString("Form.title"));    // NOI18N
    setName("Form");                                  // NOI18N
    jScrollPane1.setName("jScrollPane1");             // NOI18N
    repoTree.setModel(getModel());
    repoTree.setComponentPopupMenu(repoPopupMenu);
    repoTree.setName("repoTree");                     // NOI18N
    repoTree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
      public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {}
      public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
        repoTreeTreeExpanded(evt);
      }
    });
    jScrollPane1.setViewportView(repoTree);
    getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
    searchPanel.setName("searchPanel");                                        // NOI18N
    searchTextField.setText(resourceMap.getString("searchTextField.text"));    // NOI18N
    searchTextField.setName("searchTextField");                                // NOI18N
    searchTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        searchTextFieldActionPerformed(evt);
      }
    });
    searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        searchTextFieldKeyReleased(evt);
      }
    });
    findButton.setText(resourceMap.getString("findButton.text"));    // NOI18N
    findButton.setName("findButton");                                // NOI18N
    findButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findButtonActionPerformed(evt);
      }
    });
    selectedRepoTextField.setEditable(false);
    selectedRepoTextField.setText(resourceMap.getString("selectedRepoTextField.text"));    // NOI18N
    selectedRepoTextField.setName("selectedRepoTextField");                                // NOI18N
    okButton.setText(resourceMap.getString("okButton.text"));                              // NOI18N
    okButton.setName("okButton");                                                          // NOI18N
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

    javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);

    searchPanel.setLayout(searchPanelLayout);
    searchPanelLayout.setHorizontalGroup(
       searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         searchPanelLayout.createSequentialGroup().addContainerGap().addComponent(
           searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 133,
           javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(findButton).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
             selectedRepoTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(okButton).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()));
    searchPanelLayout.setVerticalGroup(
       searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         searchPanelLayout.createSequentialGroup().addContainerGap().addGroup(
           searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(findButton).addComponent(
               selectedRepoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
               javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(okButton).addComponent(
                 cancelButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    getContentPane().add(searchPanel, java.awt.BorderLayout.SOUTH);
    locationPanel.setName("locationPanel");                                              // NOI18N
    locationLabel.setDisplayedMnemonic('R');
    locationLabel.setLabelFor(locationField);
    locationLabel.setText(resourceMap.getString("locationLabel.text"));                  // NOI18N
    locationLabel.setToolTipText(resourceMap.getString("locationLabel.toolTipText"));    // NOI18N
    locationLabel.setName("locationLabel");                                              // NOI18N
    locationButton.setMnemonic('A');
    locationButton.setText(resourceMap.getString("locationButton.text"));                // NOI18N
    locationButton.setName("locationButton");                                            // NOI18N
    locationButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        locationButtonActionPerformed(evt);
      }
    });
    locationField.setEditable(true);
    locationField.setName("locationField");    // NOI18N
    locationField.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        locationFieldItemStateChanged(evt);
      }
    });
    locationField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        locationFieldActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout locationPanelLayout = new javax.swing.GroupLayout(locationPanel);

    locationPanel.setLayout(locationPanelLayout);
    locationPanelLayout.setHorizontalGroup(
       locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         locationPanelLayout.createSequentialGroup().addContainerGap().addComponent(locationLabel).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           locationField, 0, 478, Short.MAX_VALUE).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(locationButton).addContainerGap()));
    locationPanelLayout.setVerticalGroup(
       locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         locationPanelLayout.createSequentialGroup().addContainerGap().addGroup(
           locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             locationLabel).addComponent(locationButton).addComponent(
             locationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(
               javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    getContentPane().add(locationPanel, java.awt.BorderLayout.PAGE_START);
    pack();
  }                                                                           // </editor-fold>//GEN-END:initComponents

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_buttonCloseActionPerformed
  {
    close(false);
  }                                                                       //GEN-LAST:event_buttonCloseActionPerformed

  private void jTextField1KeyTyped(java.awt.event.KeyEvent evt)           //GEN-FIRST:event_jTextField1KeyTyped
  {}                                                                      //GEN-LAST:event_jTextField1KeyTyped

  private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt)    //GEN-FIRST:event_jTextField1KeyReleased
  {
    findNode(false);
  }                                                                       //GEN-LAST:event_jTextField1KeyReleased

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_okButtonActionPerformed
  {
    close(true);
  }                                                                         //GEN-LAST:event_okButtonActionPerformed

  private void findButtonActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_findButtonActionPerformed
  {
    findNode(true);
  }                                                                                //GEN-LAST:event_findButtonActionPerformed

  private void repoTreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {    //GEN-FIRST:event_repoTreeTreeExpanded
    startWorker((DefaultMutableTreeNode)evt.getPath().getLastPathComponent());
  }                                                                               //GEN-LAST:event_repoTreeTreeExpanded

  private void locationButtonActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_locationButtonActionPerformed
    loadRepo(getLocationURL(), true);
  }                                                                              //GEN-LAST:event_locationButtonActionPerformed

  private void locationFieldActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_locationFieldActionPerformed
    loadRepo(getLocationURL(), false);
  }                                                                     //GEN-LAST:event_locationFieldActionPerformed

  private void miCopyActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_miCopyActionPerformed
  {                                                                     //GEN-HEADEREND:event_miCopyActionPerformed
    String     current = getRepoPathFromPath(repoTree.getSelectionPath());
    URLChooser panel   = new URLChooser("Enter the destination URL:", current);

    if(JOptionPane.showConfirmDialog(this, panel, "Copy", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      try {
        System.out.println("copy to " + panel.getURL());

        SVNCopySource[] sources = new SVNCopySource[]{
          new SVNCopySource(SVNRevision.HEAD, SVNRevision.BASE, SVNURL.parseURIEncoded(current))};

        SVNApp.getApplication().getSVNClientManager().getCopyClient().doCopy(sources,
                                                                             SVNURL.parseURIEncoded(panel.getURL()),
                                                                             false, false, true, "SimpleSVN copy",
                                                                             null);
      } catch(SVNException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }                                                                       //GEN-LAST:event_miCopyActionPerformed

  private void miDeleteActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_miDeleteActionPerformed
  {                                                                       //GEN-HEADEREND:event_miDeleteActionPerformed
    String current = getRepoPathFromPath(repoTree.getSelectionPath());

    if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + current, "Confirm Delete",
                                     JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
      System.out.println("delete it!");
    }
  }                                                                       //GEN-LAST:event_miDeleteActionPerformed

  private void miCreateActionPerformed(java.awt.event.ActionEvent evt)    //GEN-FIRST:event_miCreateActionPerformed
  {                                                                       //GEN-HEADEREND:event_miCreateActionPerformed
    String     current = getRepoPathFromPath(repoTree.getSelectionPath());
    URLChooser panel   = new URLChooser("Enter the destination URL:", current);

    if(JOptionPane.showConfirmDialog(this, panel, "Create", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      try {
        System.out.println("copy to " + panel.getURL());

        SVNCopySource[] sources = new SVNCopySource[]{
          new SVNCopySource(SVNRevision.HEAD, SVNRevision.BASE, SVNURL.parseURIEncoded(current))};

        SVNApp.getApplication().getSVNClientManager().getCopyClient().doCopy(sources,
                                                                             SVNURL.parseURIEncoded(panel.getURL()),
                                                                             false, false, true, "SimpleSVN copy",
                                                                             null);
      } catch(SVNException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }                                                                             //GEN-LAST:event_miCreateActionPerformed

  private void locationFieldItemStateChanged(java.awt.event.ItemEvent evt) {    //GEN-FIRST:event_locationFieldItemStateChanged
    loadRepo(getLocationURL(), true);
  }                                                                                //GEN-LAST:event_locationFieldItemStateChanged

  private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_searchTextFieldActionPerformed
    if(findNode(false)) {
      close(true);
    }
  }    //GEN-LAST:event_searchTextFieldActionPerformed

  protected void close(boolean selected) {
    if(selected) {
      selectedRepoPath = getRepoPathFromPath(repoTree.getSelectionPath());
    } else {
      selectedRepoPath = null;
    }

    searchTextField.requestFocus();
    setVisible(false);
  }

  protected void startWorker(final DefaultMutableTreeNode node) {
    if((node != null) && (node.getChildCount() > 0)) {
      return;
    }

    final RepoNodeData data = (RepoNodeData)node.getUserObject();

    if(data.isLoaded()) {
      return;
    }

    data.startLoading();
    model.nodeChanged(node);

    SwingWorker<DefaultMutableTreeNode[], Void> worker = new SwingWorker<DefaultMutableTreeNode[], Void>() {
      @Override
      protected DefaultMutableTreeNode[] doInBackground() throws Exception {
        TreeSet<SVNDirEntry>     entries = new TreeSet<SVNDirEntry>();
        DefaultMutableTreeNode[] nodes   = null;

        try {
          data.getChildEntries(entries);

          nodes = new DefaultMutableTreeNode[entries.size()];

          int i = 0;

          for(SVNDirEntry entry : entries) {
            RepoNodeData newData = new RepoNodeData(data.getRepository(), data, entry, true);

            nodes[i] = new DefaultMutableTreeNode(newData);

            nodes[i].setAllowsChildren(newData.isFolder());

            i++;
          }
        } catch(Exception e) {
          e.printStackTrace();
        }

        return nodes;
      }
      @Override
      protected void done() {
        try {
          data.stopLoading();

          DefaultMutableTreeNode[] children = get();

          for(int i = 0; (children != null) && (i < children.length); i++) {
            node.insert(children[i], i);
          }

          model.nodeStructureChanged(node);
          model.nodeChanged(node);
        } catch(InterruptedException ex) {
        } catch(ExecutionException ex) {
        }
      }
    };

    worker.execute();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        RepoTreeView view = new RepoTreeView(null, true, "https://svn.java.net/svn/sdsvn~svn", true);

        view.setVisible(true);
        System.out.println("selected repo: " + view.getSelectedPath());
        view.dispose();
      }
    });
  }

  /**
   * Method getSelectedPath
   *
   *
   * @return
   *
   */
  public String getSelectedPath() {
    return selectedRepoPath;
  }

  /**
   * Method findNode
   *
   *
   * @param next
   *
   *
   * @return
   */
  public boolean findNode(boolean next) {
    TreeNode node = null;
    TreePath path = repoTree.getSelectionPath();

    if(path != null) {
      node = (TreeNode)path.getLastPathComponent();
    }

    if(node == null) {
      node = root;
    }

    return findNode((DefaultMutableTreeNode)node, next);
  }

  /**
   * Method findNode
   *
   *
   * @param start
   * @param next
   *
   *
   * @return
   */
  public boolean findNode(DefaultMutableTreeNode start, boolean next) {
    TreeNode found = findNodeByName(searchTextField.getText(), start, next);

    if(found != null) {
      TreePath path = new TreePath(model.getPathToRoot(found));

      repoTree.scrollPathToVisible(path);
      repoTree.setSelectionPath(path);

      return true;
    } else {
      Toolkit.getDefaultToolkit().beep();

      if(start != root) {
        findNode(root, false);
      }

      return false;
    }
  }

  private DefaultMutableTreeNode findNodeByName(String name, DefaultMutableTreeNode node, boolean next) {
    if( !next && node.toString().toLowerCase().contains(name.toLowerCase())) {
      return node;
    } else {
      String text = node.toString();

      if("trunk".equals(text) || "branches".equals(text) || "tags".equals(text)) {
        startWorker(node);
      }

      DefaultMutableTreeNode nextNode = node.getNextNode();

      if(nextNode != null) {
        return findNodeByName(name, node.getNextNode(), false);
      } else {
        return null;
      }
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton     cancelButton;
  private javax.swing.JButton     findButton;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JButton     locationButton;
  private javax.swing.JComboBox   locationField;
  private javax.swing.JLabel      locationLabel;
  private javax.swing.JPanel      locationPanel;
  private javax.swing.JMenuItem   miCopy;
  private javax.swing.JMenuItem   miCreate;
  private javax.swing.JMenuItem   miDelete;
  private javax.swing.JButton     okButton;
  private javax.swing.JPopupMenu  repoPopupMenu;
  private javax.swing.JTree       repoTree;
  private javax.swing.JPanel      searchPanel;
  private javax.swing.JTextField  searchTextField;
  private javax.swing.JTextField  selectedRepoTextField;
  // End of variables declaration//GEN-END:variables
}

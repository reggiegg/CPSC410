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

package org.sdrinovsky.sdsvn.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.io.File;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.SVNApp;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class RevisionGraph {
  RevisionGraphScene    scene;
  JTextArea             messagePane;
  List<GraphTreeWidget> selected        = new LinkedList<GraphTreeWidget>();
  FileTableRow          row             = null;
  boolean               controlDown     = false;
  Action                compareWCAction = new AbstractAction("Compare to wc") {
    @Override
    public boolean isEnabled() {
      return selected.size() == 1;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        String        diffProgram = SVNApp.getApplication().getPreferences().get("diffProgram", null);
        GraphLogEntry entry1      = selected.get(0).getEntry().getLogEntry();
        SVNURL        url1        = SVNURL.parseURIDecoded(entry1.getURL());
        File          svn1        = File.createTempFile("svndiff", ".r" + entry1.getRevision());

        svn1.delete();
        System.out.println("getting " + url1 + "@" + entry1.getRevision());
        SVNApp.getApplication().getSVNClientManager().getUpdateClient().doExport(
           url1, svn1, SVNRevision.UNDEFINED, SVNRevision.create(entry1.getRevision()), null, false, SVNDepth.INFINITY);
        svn1.deleteOnExit();
        svn1.setReadOnly();
        Runtime.getRuntime().exec(new String[]{diffProgram, svn1.getPath(), row.getFile().toString()});
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  };
  Action compareAction = new AbstractAction("Compare") {
    @Override
    public boolean isEnabled() {
      return selected.size() == 2;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        Collections.sort(selected);

        String        diffProgram = SVNApp.getApplication().getPreferences().get("diffProgram", null);
        GraphLogEntry entry1      = selected.get(0).getEntry().getLogEntry();
        GraphLogEntry entry2      = selected.get(1).getEntry().getLogEntry();
        SVNURL        url1        = SVNURL.parseURIDecoded(entry1.getURL());
        SVNURL        url2        = SVNURL.parseURIDecoded(entry2.getURL());
        File          svn1        = File.createTempFile("svndiff", ".r" + entry1.getRevision());
        File          svn2        = File.createTempFile("svndiff", ".r" + entry2.getRevision());

        svn1.delete();
        svn2.delete();
        System.out.println("getting " + url1 + "@" + entry1.getRevision());
        SVNApp.getApplication().getSVNClientManager().getUpdateClient().doExport(
           url1, svn1, SVNRevision.UNDEFINED, SVNRevision.create(entry1.getRevision()), null, false, SVNDepth.INFINITY);
        svn1.deleteOnExit();
        System.out.println("getting " + url2 + "@" + entry2.getRevision());
        SVNApp.getApplication().getSVNClientManager().getUpdateClient().doExport(
           url2, svn2, SVNRevision.UNDEFINED, SVNRevision.create(entry2.getRevision()), null, false, SVNDepth.INFINITY);
        svn2.deleteOnExit();
        svn1.setReadOnly();
        svn2.setReadOnly();
        Runtime.getRuntime().exec(new String[]{diffProgram, svn1.getPath(), svn2.getPath()});
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  };
  private WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(new PopupMenuProvider() {
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
      JPopupMenu menu              = new JPopupMenu();
      JMenuItem  compareMenuItem   = new JMenuItem(compareAction);
      JMenuItem  compareWCMenuItem = new JMenuItem(compareWCAction);

      menu.add(compareMenuItem);
      menu.add(compareWCMenuItem);

      return menu;
    }
  });
  private WidgetAction selectAction = ActionFactory.createSelectAction(new SelectProvider() {
    ObjectState normalState   = ObjectState.createNormal();
    ObjectState selectedState = ObjectState.createNormal().deriveSelected(true);
    @Override
    public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
      return true;
    }
    @Override
    public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
      return true;
    }
    @Override
    public void select(Widget widget, Point localLocation, boolean invertSelection) {
      GraphTreeWidget graphTreeWidget = (GraphTreeWidget)widget;

      if( !graphTreeWidget.getEntry().isLabel()) {
        if( !controlDown) {
          for(GraphTreeWidget w : selected) {
            w.setState(normalState);
          }

          selected.clear();
        }

        if( !selected.contains(graphTreeWidget)) {
          selected.add(graphTreeWidget);
        }

        graphTreeWidget.setState(selectedState);

        GraphLogEntry log = graphTreeWidget.getEntry().getLogEntry();

        if(log != null) {
          messagePane.append("------------------------\n");
          messagePane.append("Revision: " + log.getRevision() + "\n");
          messagePane.append("Date: " + log.getDate() + "\n");
          messagePane.append("Auther: " + log.getAuthor() + "\n");
          messagePane.append("Description:\n" + log.getMessage() + "\n");
          messagePane.append("Changed Paths:\n");

          for(Object key : log.getChangedPaths().values()) {
            messagePane.append("  " + key + "\n");
          }

          messagePane.setCaretPosition(messagePane.getDocument().getLength());
        }
      }
    }
  });

  /**
   * Constructor RevisionGraph
   *
   *
   * @param row
   * @param location
   *
   */
  public RevisionGraph(FileTableRow row, Location location) {
    scene = new RevisionGraphScene();

    //      scene.getActions().addAction(ActionFactory.createZoomAction(1.2, false));
    scene.getActions().addAction(ActionFactory.createPanAction());

    this.row = row;
  }

  /**
   * Method setMesagePane
   *
   *
   * @param textArea
   *
   */
  public void setMesagePane(JTextArea textArea) {
    messagePane = textArea;
  }

  /**
   * Method setZoom
   *
   *
   * @param d
   *
   */
  public void setZoom(double d) {
    scene.setZoomFactor(d);
    scene.validate();
  }

  /**
   * Method getZomm
   *
   *
   * @return
   *
   */
  public double getZomm() {
    return scene.getZoomFactor();
  }

  /**
   * Method showScene
   *
   *
   * @param tree
   *
   */
  public void showScene(GraphTree tree) {
    JFrame frame           = new JFrame(row.getFileName() + ":" + row.getRevision());
    int    width           = 800;
    int    height          = 600;
    int    satelliteHeight = 100;

    if(messagePane == null) {
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

      splitPane.setLeftComponent(new JScrollPane(buildScene(tree)));

      messagePane = new JTextArea();

      messagePane.setRows(12);
      messagePane.setLineWrap(true);
      splitPane.setRightComponent(new JScrollPane(messagePane));
      splitPane.setDividerLocation(.65);
      frame.getContentPane().add(splitPane, BorderLayout.CENTER);
    } else {
      frame.getContentPane().add(new JScrollPane(buildScene(tree)), BorderLayout.CENTER);
    }

    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    frame.setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
    frame.setVisible(true);

    JFrame satelliteView = new JFrame();

    satelliteView.getContentPane().add(getSatelliteView(), BorderLayout.CENTER);
    satelliteView.setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2 - satelliteHeight, width,
                            satelliteHeight);
    satelliteView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    satelliteView.setVisible(true);
  }

  /**
   * Method buildScene
   *
   *
   * @param tree
   *
   * @return
   *
   */
  public JComponent buildScene(GraphTree tree) {
    buildScene(scene, null, 0, tree);

    final JComponent component = scene.createView();

    component.setFocusable(true);
    component.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        component.getParent().dispatchEvent(e);
      }
    });
    component.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        component.grabFocus();
      }
    });
    component.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if( !controlDown && e.isControlDown()) {
          controlDown = true;
        }
      }
      @Override
      public void keyReleased(KeyEvent e) {
        controlDown = false;
      }
    });

    return component;
  }

  /**
   * Method getSatelliteView
   *
   *
   * @return
   *
   */
  public JComponent getSatelliteView() {
    return scene.createSatelliteView();
  }

  /**
   * Method buildScene
   *
   *
   * @param scene
   * @param parent
   * @param index
   * @param tree
   *
   */
  public void buildScene(RevisionGraphScene scene, GraphTreeWidget parent, int index, GraphTree tree) {
    GraphTreeWidget w          = addNode(scene, tree, parent, index);
    int             localIndex = 0;

    for(GraphTree node : tree) {
      if(node.isLabel()) {
        localIndex++;
      }

      buildScene(scene, w, localIndex, node);
    }
  }

  private int globalIndex = 0;

  /**
   * Method addNode
   *
   *
   * @param scene
   * @param node
   * @param parent
   * @param branch
   *
   * @return
   *
   */
  public GraphTreeWidget addNode(RevisionGraphScene scene, GraphTree node, GraphTreeWidget parent, int branch) {
    GraphTreeWidget w        = (GraphTreeWidget)scene.addNode(node);
    Point           location = null;

    if(parent != null) {
      scene.addEdge(w.getLabel() + "edge");
      scene.setEdgeSource(w.getLabel() + "edge", parent.getEntry());
      scene.setEdgeTarget(w.getLabel() + "edge", node);

      int x = parent.getPreferredLocation().x;
      int y = parent.getPreferredLocation().y;

      if(branch > 0) {
        if(globalIndex == 0) {
          y += 60;
        } else {
          x += 250 * globalIndex;
        }

        globalIndex++;
      } else {
        y += 60;
      }

      location = new Point(x, y);
    } else {
      location = new Point(25, 50);
    }

    w.setPreferredLocation(location);
    w.getActions().addAction(selectAction);
    w.getActions().addAction(popupMenuAction);

    if( !node.isLabel()) {
      GraphLogEntry entry = node.getLogEntry();

      if((entry.getRevision() == row.getCommitRevision()) ||
            (node.isLeaf() && (entry.getRevision() == row.getCommitRevision()))) {
        if(row.isCommitable()) {
          w.setForeground(Color.red);
        } else {
          w.setForeground(Color.blue);
        }
      }

      w.setToolTipText(entry.getDate() + " : " + entry.getAuthor() + " : " + entry.getMessage());
    }

    return w;
  }
}

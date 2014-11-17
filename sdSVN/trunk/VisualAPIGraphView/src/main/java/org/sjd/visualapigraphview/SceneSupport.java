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

package org.sjd.visualapigraphview;

import org.netbeans.api.visual.widget.Scene;

import javax.swing.*;

import java.awt.*;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class SceneSupport {
  /**
   * Method show
   *
   *
   * @param scene
   *
   */
  public static void show(final Scene scene) {
    if(SwingUtilities.isEventDispatchThread()) {
      showEDT(scene);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          showEDT(scene);
        }
      });
    }
  }

  private static void showEDT(Scene scene) {
    JComponent sceneView = scene.getView();

    if(sceneView == null) {
      sceneView = scene.createView();
    }

    show(sceneView);
  }

  /**
   * Method show
   *
   *
   * @param sceneView
   *
   */
  public static void show(final JComponent sceneView) {
    if(SwingUtilities.isEventDispatchThread()) {
      showEDT(sceneView);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          showEDT(sceneView);
        }
      });
    }
  }

  private static void showEDT(JComponent sceneView) {
    JScrollPane panel = new JScrollPane(sceneView);

    panel.getHorizontalScrollBar().setUnitIncrement(32);
    panel.getHorizontalScrollBar().setBlockIncrement(256);
    panel.getVerticalScrollBar().setUnitIncrement(32);
    panel.getVerticalScrollBar().setBlockIncrement(256);
    showCoreEDT(panel);
  }

  /**
   * Method showCore
   *
   *
   * @param view
   *
   */
  public static void showCore(final JComponent view) {
    if(SwingUtilities.isEventDispatchThread()) {
      showCoreEDT(view);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          showCoreEDT(view);
        }
      });
    }
  }

  private static void showCoreEDT(JComponent view) {
    int
      width      = 800,
      height     = 600;
    JFrame frame = new JFrame();    //new JDialog (), true);

    frame.add(view, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    frame.setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
    frame.setVisible(true);
  }

  /**
   * Method randInt
   *
   *
   * @param max
   *
   * @return
   *
   */
  public static int randInt(int max) {
    return(int)(Math.random() * max);
  }

  /**
   * Method sleep
   *
   *
   * @param delay
   *
   */
  public static void sleep(int delay) {
    try {
      Thread.sleep(delay);
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
}

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

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.JMeldPanel;
import org.jmeld.util.prefs.WindowPreference;

import org.openide.util.lookup.ServiceProvider;

/**
 * Use components from the JMeld project to render a comparison of two files in a JFrame.
 */
@ServiceProvider(service = DiffView.class)
public class JMeldDiffView implements DiffView {
  /**
   * The frame to place the JMeldPanel in
   */
  private JFrame frame;

  /**
   * Create an instance of JMeld to compare two files.
   *
   * @param leftFile      is the file on the left hand side of the comparison
   * @param rightFile     is the file on the right hand side of the comparison
   * @param leftReadOnly  true will make the left file read-only in the diff, false will allow edits to the file
   * @param rightReadOnly true will make the right file read-only in the diff, false will allow edits to the file
   *
   * @throws Exception
   *
   */
  public void executeDiff(String leftFile, String rightFile, boolean leftReadOnly,
                          boolean rightReadOnly) throws Exception {
    final EditorSettings editorSettings = JMeldSettings.getInstance().getEditor();

    editorSettings.setLeftsideReadonly(leftReadOnly);
    editorSettings.setRightsideReadonly(rightReadOnly);

    //TODO: Move this to a setting
    Font monospacedFont = new Font("Courier New", Font.PLAIN, 11);

    editorSettings.setFont(monospacedFont);

    frame = new JFrame();

    final JMeldPanel panel = new NonExitingJMeldPanel();

    panel.SHOW_TABBEDPANE_OPTION.disable();
    panel.SHOW_TOOLBAR_OPTION.disable();
    frame.add(panel);
    frame.addWindowListener(new WindowCloseListener());
    new WindowPreference(frame.getTitle(), frame);
    frame.addWindowListener(panel.getWindowListener());
    frame.setVisible(true);
    frame.toFront();
    panel.openComparison(leftFile, rightFile);
  }

  /**
   * A WindowListener that responds to windowClosed events by hiding the JMeld frame and setting it to null
   * so it can be garbage-collected.
   */
  private class WindowCloseListener extends WindowAdapter {
    /**
     * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {
      if(frame != null) {
        frame.setVisible(false);

        frame = null;
      }
    }
  }

  /**
   * The JMeldPanel registers a window listener that exits the JVM when it detects that the frame it resides
   * in was closed. This subclass overrides this behavior with a no-op window listener.
   * Thanks to Rob Manning.
   */
  private class NonExitingJMeldPanel extends JMeldPanel {
    /**
     * @see org.jmeld.ui.JMeldPanel#getWindowListener()
     *
     * @return
     */
    @Override
    public WindowListener getWindowListener() {
      return new WindowAdapter() {}
      ;
    }
  }
}

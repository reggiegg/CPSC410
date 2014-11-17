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

package org.sdrinovsky.sdsvn.properties;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;

/**
 *
 * @author sdrinovsky
 */
public class SelectedProperty implements org.jdesktop.application.session.PropertySupport {
  /**
   * Class ButtonState
   *
   *
   * @author
   * @version $Revision: 75 $
   */
  public static class SelectedState {
    public boolean selected = false;

    /**
     * Constructor ButtonState
     *
     *
     */
    public SelectedState() {}

    /**
     * Constructor ButtonState
     *
     *
     * @param state
     *
     */
    public SelectedState(boolean state) {
      selected = state;
    }

    /**
     * Method setPushed
     *
     *
     * @param state
     *
     */
    public void setSelected(boolean state) {
      selected = state;
    }

    /**
     * Method getPushed
     *
     *
     * @return
     *
     */
    public boolean isSelected() {
      return selected;
    }
  }

  private void checkComponent(Component component) {
    if(component == null) {
      throw new IllegalArgumentException("null component");
    }

    if((component instanceof JToggleButton) || (component instanceof JCheckBox) ||
          (component instanceof JCheckBoxMenuItem)) {
      return;
    }

    throw new IllegalArgumentException("invalid component");
  }

  /**
   * Method getSessionState
   *
   *
   * @param c
   *
   * @return
   *
   */
  @Override
  public Object getSessionState(Component c) {
    checkComponent(c);

    SelectedState state = new SelectedState();

    if(c instanceof JToggleButton) {
      state.selected = ((JToggleButton)c).isSelected();
    } else if(c instanceof JCheckBox) {
      state.selected = ((JCheckBox)c).isSelected();
    } else if(c instanceof JCheckBoxMenuItem) {
      state.selected = ((JCheckBoxMenuItem)c).isSelected();
    }

    return state;
  }

  /**
   * Method setSessionState
   *
   *
   * @param c
   * @param state
   *
   */
  @Override
  public void setSessionState(Component c, Object state) {
    checkComponent(c);

    if(c instanceof JToggleButton) {
      ((JToggleButton)c).setSelected(((SelectedState)state).selected);
    } else if(c instanceof JCheckBox) {
      ((JCheckBox)c).setSelected(((SelectedState)state).selected);
    } else if(c instanceof JCheckBoxMenuItem) {
      ((JCheckBoxMenuItem)c).setSelected(((SelectedState)state).selected);
    }
  }
}

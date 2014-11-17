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

import javax.swing.JComboBox;

/**
 * Class ComboBoxProperty
 *
 *
 * @author
 * @version $Revision: 16 $
 */
public class ComboBoxProperty implements org.jdesktop.application.session.PropertySupport {
  /**
   * Class ComboBoxState
   *
   *
   * @author
   * @version $Revision: 16 $
   */
  public static class ComboBoxState {
    public String[] items = new String[0];

    /**
     * Constructor ComboBoxState
     *
     *
     */
    public ComboBoxState() {}

    /**
     * Constructor ComboBoxState
     *
     *
     * @param state
     *
     */
    public ComboBoxState(String[] state) {
      copyState(state);
    }

    /**
     * Method setItems
     *
     *
     * @param state
     *
     */
    public void setItems(String[] state) {
      copyState(state);
    }

    /**
     * Method getItems
     *
     *
     * @return
     *
     */
    public String[] getItems() {
      return items;
    }

    private void copyState(String[] state) {
      items = new String[state.length];

      System.arraycopy(state, 0, items, 0, state.length);
    }
  }

  private void checkComponent(Component component) {
    if(component == null) {
      throw new IllegalArgumentException("null component");
    }

    if( !(component instanceof JComboBox)) {
      throw new IllegalArgumentException("invalid component");
    }
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

    String[] items = new String[((JComboBox)c).getItemCount()];

    for(int i = 0; i < items.length; i++) {
      items[i] = ((JComboBox)c).getItemAt(i).toString();
    }

    return new ComboBoxState(items);
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

    String[] items = ((ComboBoxState)state).getItems();

    for(int i = 0; i < items.length; i++) {
      ((JComboBox)c).addItem(items[i]);
    }
  }
}

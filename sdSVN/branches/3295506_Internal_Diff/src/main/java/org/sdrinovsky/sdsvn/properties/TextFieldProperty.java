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

import javax.swing.JTextField;

/**
 * Class TextFieldProperty
 *
 *
 * @author
 * @version $Revision: 49 $
 */
public class TextFieldProperty implements org.jdesktop.application.SessionStorage.Property {
  /**
   * Class FieldState
   *
   *
   * @author
   * @version $Revision: 49 $
   */
  public static class FieldState {
    public String text = "";

    /**
     * Constructor FieldState
     *
     *
     */
    public FieldState() {}

    /**
     * Constructor FieldState
     *
     *
     * @param state
     *
     */
    public FieldState(String state) {
      text = state;
    }

    /**
     * Method setText
     *
     *
     * @param state
     *
     */
    public void setText(String state) {
      text = state;
    }

    /**
     * Method getText
     *
     *
     * @return
     *
     */
    public String getText() {
      return text;
    }
  }

  private void checkComponent(Component component) {
    if(component == null) {
      throw new IllegalArgumentException("null component");
    }

    if( !(component instanceof JTextField)) {
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

    FieldState state = new FieldState();

    state.text = ((JTextField)c).getText();

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

    JTextField jTextField = (JTextField)c;

    if(jTextField.isEditable()) {
      jTextField.setText(((FieldState)state).text);
    }
  }
}

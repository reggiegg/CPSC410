/*
 * Author: Albert L. M. Ting <alt@artisan.com>
 *
 * Released into the public domain.
 *
 * $Revision: 1.24 $
 * $Id: MultiLineToolTipUI.java,v 1.24 1999/01/27 17:39:15 alt Exp $
 */

package org.sdrinovsky.sdsvn.tooltip;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ToolTipUI;
import javax.swing.plaf.ComponentUI;

/**
 * To add multiline tooltip support to your swing applications, just add this
 * static call to your main method.  Note, you only need to do this once, even
 * if you change LookAndFeel as the UIManager knows not to overwrite the user
 * defaults.  Moreover, it uses the current L&F foreground/background colors
 * <p><pre>
 *        MultiLineToolTipUI.initialize();
 * </pre><p>
 * @author Albert L. M. Ting
 */
public class MultiLineToolTipUI extends ToolTipUI {
  static MultiLineToolTipUI SINGLETON          = new MultiLineToolTipUI();
  static boolean            displayAccelerator = true;
  int                       accelerator_offset = 15;
  int                       inset              = 3;

  private MultiLineToolTipUI() {}

  /**
   * Method initialize
   *
   *
   */
  public static void initialize() {
    // don't hardcode class name, this way we can obfuscate.
    String key  = "ToolTipUI";
    Class  cls  = SINGLETON.getClass();
    String name = cls.getName();

    UIManager.put(key, name);
    UIManager.put(name, cls);
  }

  /**
   * Method createUI
   *
   *
   * @param c
   *
   * @return
   *
   */
  public static ComponentUI createUI(JComponent c) {
    return SINGLETON;
  }

  /**
   * Method installUI
   *
   *
   * @param c
   *
   */
  @Override
  public void installUI(JComponent c) {
    LookAndFeel.installColorsAndFont(c, "info", "ToolTip.foreground", "ToolTip.font");
    LookAndFeel.installBorder(c, "nimbusBorder");
  }

  /**
   * Method uninstallUI
   *
   *
   * @param c
   *
   */
  @Override
  public void uninstallUI(JComponent c) {
    LookAndFeel.uninstallBorder(c);
  }

  /**
   * Method setDisplayAcceleratorKey
   *
   *
   * @param val
   *
   */
  public static void setDisplayAcceleratorKey(boolean val) {
    displayAccelerator = val;
  }

  /**
   * Method getPreferredSize
   *
   *
   * @param c
   *
   * @return
   *
   */
  @Override
  public Dimension getPreferredSize(JComponent c) {
    Font        font        = c.getFont();
    FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
    int         fontHeight  = fontMetrics.getHeight();
    String      tipText     = ((JToolTip)c).getTipText();

    if(tipText == null) {
      tipText = "";
    }

    String    lines[]   = PlafMacros.breakupLines(tipText);
    int       num_lines = lines.length;
    Dimension dimension;
    int       width, height, onewidth;

    height = num_lines * fontHeight;
    width  = 0;

    for(int i = 0; i < num_lines; i++) {
      onewidth = fontMetrics.stringWidth(lines[i]);

      if(displayAccelerator && (i == num_lines - 1)) {
        String keyText = getAcceleratorString((JToolTip)c);

        if( !keyText.equals("")) {
          onewidth += fontMetrics.stringWidth(keyText) + accelerator_offset;
        }
      }

      width = Math.max(width, onewidth);
    }

    return new Dimension(width + inset * 2, height + inset * 2);
  }

  /**
   * Method getMinimumSize
   *
   *
   * @param c
   *
   * @return
   *
   */
  @Override
  public Dimension getMinimumSize(JComponent c) {
    return getPreferredSize(c);
  }

  /**
   * Method getMaximumSize
   *
   *
   * @param c
   *
   * @return
   *
   */
  @Override
  public Dimension getMaximumSize(JComponent c) {
    return getPreferredSize(c);
  }

  /**
   * Method paint
   *
   *
   * @param g
   * @param c
   *
   */
  @Override
  public void paint(Graphics g, JComponent c) {
    Font        font        = c.getFont();
    FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
    Dimension   dimension   = c.getSize();
    int         fontHeight  = fontMetrics.getHeight();
    int         fontAscent  = fontMetrics.getAscent();
    String      tipText     = ((JToolTip)c).getTipText();
    String      lines[]     = PlafMacros.breakupLines(tipText);
    int         num_lines   = lines.length;
    int         height;
    int         i;
    Graphics2D  g2d = (Graphics2D)g;

    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setColor(c.getBackground());
    g.fillRect(0, 0, dimension.width, dimension.height);
    g.setColor(c.getForeground());

    for(i = 0, height = 2 + fontAscent; i < num_lines; i++, height += fontHeight) {
      g.drawString(lines[i], inset, height);

      if(displayAccelerator && (i == num_lines - 1)) {
        String keyText = getAcceleratorString((JToolTip)c);

        if( !keyText.equals("")) {
          Font smallFont = new Font(font.getName(), font.getStyle(), font.getSize() - 2);

          g.setFont(smallFont);
          g.drawString(keyText, fontMetrics.stringWidth(lines[i]) + accelerator_offset, height);
        }
      }
    }
  }

  /**
   * Method getAcceleratorString
   *
   *
   * @param tip
   *
   * @return
   *
   */
  public String getAcceleratorString(JToolTip tip) {
    JComponent comp = tip.getComponent();

    if(comp == null) {
      return "";
    }

    KeyStroke[] keys          = comp.getRegisteredKeyStrokes();
    String      controlKeyStr = "";
    KeyStroke   postTip       = KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK);

    for(int i = 0; i < keys.length; i++) {
      // Ignore ToolTipManager postTip action, 
      // in swing1.1beta3 and onward
      if(postTip.equals(keys[i])) {
        continue;
      }

      char c   = (char)keys[i].getKeyCode();
      int  mod = keys[i].getModifiers();

      if(mod == InputEvent.CTRL_MASK) {
        controlKeyStr = "Ctrl+" + (char)keys[i].getKeyCode();

        break;
      } else if(mod == InputEvent.ALT_MASK) {
        controlKeyStr = "Alt+" + (char)keys[i].getKeyCode();

        break;
      }
    }

    return controlKeyStr;
  }
}

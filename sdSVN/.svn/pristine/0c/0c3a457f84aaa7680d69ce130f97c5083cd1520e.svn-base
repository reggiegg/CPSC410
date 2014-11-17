/*
 * Author: Albert L. M. Ting <alt@artisan.com>
 *
 * Released into the public domain.
 *
 * $Revision: 1.24 $
 * $Id: MultiLineToolTipUI.java,v 1.24 1999/01/27 17:39:15 alt Exp $
 */

package org.sdrinovsky.sdsvn.tooltip;

import java.util.Vector;

import javax.swing.SwingConstants;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Sun, May 29, '11
 * @author         Enter your name here...
 */
public class PlafMacros implements SwingConstants {
  // don't make these final, since the value is 
  // different on each platform
  private static String LINE_SEPARATOR     = System.getProperty("line.separator");
  private static int    LINE_SEPARATOR_LEN = LINE_SEPARATOR.length();

  /**
   * Method breakupLines
   *
   *
   * @param text
   *
   * @return
   *
   */
  public static String[] breakupLines(String text) {
    int len = text.length();

    if(len == 0) {
      return new String[]{""};
    } else {
      Vector data  = new Vector(10);
      int    start = 0;
      int    i     = 0;

      while(i < len) {
        if(text.startsWith(LINE_SEPARATOR, i)) {
          data.addElement(text.substring(start, i));

          start = i + LINE_SEPARATOR_LEN;
          i     = start;
        } else if(text.charAt(i) == '\n') {
          data.addElement(text.substring(start, i));

          start = i + 1;
          i     = start;
        } else {
          i++;
        }
      }

      if(start != len) {
        data.addElement(text.substring(start));
      }

      int    numlines = data.size();
      String lines[]  = new String[numlines];

      data.copyInto(lines);

      return lines;
    }
  }
  // See included source for the rest of this class
}

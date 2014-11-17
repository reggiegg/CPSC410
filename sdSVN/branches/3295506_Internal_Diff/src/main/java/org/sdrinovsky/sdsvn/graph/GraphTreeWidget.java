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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 * Class GraphTreeWidget
 *
 *
 * @author
 * @version $Revision: 32 $
 */
public class GraphTreeWidget extends LabelWidget implements Comparable<GraphTreeWidget> {
  private static final Border NORMAL_BORDER = new Border() {
    @Override
    public Insets getInsets() {
      return new Insets(1, 1, 4, 4);
    }
    @Override
    public void paint(Graphics2D g, Rectangle bounds) {
      int x      = bounds.x;
      int y      = bounds.y;
      int width  = bounds.width;
      int height = bounds.height;

      g.setColor(Color.GRAY);
      g.fillRect(x + 4, y + 4, width, height);
      g.setColor(Color.BLACK);
      g.fillRect(x, y, width - 3, height - 3);
    }
    @Override
    public boolean isOpaque() {
      return true;
    }
  };
  private static final Border SELECTED_BORDER = NORMAL_BORDER;
  private static final Color  NORMAL_COLOR    = Color.WHITE;
  private static final Color  SELECTED_COLOR  = new Color(212, 212, 255);

  private static String getGraphTreeLabel(GraphTree entry) {
    if(entry == null) {
      return null;
    } else if(entry.isLabel()) {
      return entry.getLabel();
    } else {
      return Long.toString(entry.getLogEntry().getRevision());
    }
  }

  private GraphTree    entry;
  private WidgetAction moveAction = ActionFactory.createMoveAction();
  private Rectangle    size       = new Rectangle(0, -17, 200, 21);

  /**
   * Constructor GraphTreeWidget
   *
   *
   * @param scene
   *
   */
  public GraphTreeWidget(Scene scene) {
    this(scene, null);
  }

  /**
   * Constructor GraphTreeWidget
   *
   *
   * @param scene
   * @param entry
   *
   */
  public GraphTreeWidget(Scene scene, GraphTree entry) {
    super(scene, getGraphTreeLabel(entry));

    this.entry = entry;
    //getActions().addAction(moveAction);
  }

  /**
   * Method getEntry
   *
   *
   * @return
   *
   */
  public GraphTree getEntry() {
    return entry;
  }

  /**
   * Method setEntry
   *
   *
   * @param entry
   *
   */
  public void setEntry(GraphTree entry) {
    this.entry = entry;

    setLabel(getGraphTreeLabel(entry));
  }

  /**
   * Method notifyStateChanged
   *
   *
   * @param oldState
   * @param newState
   *
   */
  @Override
  public void notifyStateChanged(ObjectState oldState, ObjectState newState) {
    if(newState.isSelected()) {
      setBorder(SELECTED_BORDER);
      setBackground(SELECTED_COLOR);
    } else {
      setBorder(NORMAL_BORDER);
      setBackground(NORMAL_COLOR);
    }
  }

  @Override
  protected Rectangle calculateClientArea() {
    return size;
  }

  @Override
  protected void paintWidget() {
    if(getLabel() == null) {
      return;
    }

    Graphics2D gr = getGraphics();

    gr.setFont(getFont());

    FontMetrics fontMetrics = gr.getFontMetrics();
    Rectangle   clientArea  = getClientArea();
    Rectangle   bounds      = getBounds();
    int         x;

    switch(getAlignment()) {
      case BASELINE:
        x = 0;

        break;
      case LEFT:
        x = clientArea.x;

        break;
      case CENTER:
        x = clientArea.x + (clientArea.width - fontMetrics.stringWidth(getLabel())) / 2;

        break;
      case RIGHT:
        x = clientArea.x + clientArea.width - fontMetrics.stringWidth(getLabel());

        break;
      default:
        return;
    }

    int y;

    switch(getVerticalAlignment()) {
      case BASELINE:
        y = 0;

        break;
      case TOP:
        y = clientArea.y + fontMetrics.getAscent();

        break;
      case CENTER:
        y = clientArea.y + (clientArea.height + fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;

        break;
      case BOTTOM:
        y = clientArea.y + fontMetrics.getAscent() + clientArea.height - fontMetrics.getDescent();

        break;
      default:
        return;
    }

    Paint background = getBackground();
    Color bgColor    = null;

    if(background instanceof Color) {
      bgColor = (Color)background;
    }

    if(isPaintAsDisabled() && (bgColor != null)) {
      gr.setColor(bgColor.brighter());
      gr.drawString(getLabel(), x + 1, y + 1);
      gr.setColor(bgColor.darker());
      gr.drawString(getLabel(), x, y);
    } else {
      if(bgColor != null) {
        gr.setColor(bgColor);
        gr.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
      }

      gr.setColor(getForeground());
      gr.drawString(getLabel(), x, y);
    }
  }

  /**
   * Method compareTo
   *
   *
   * @param o
   *
   * @return
   *
   */
  @Override
  public int compareTo(GraphTreeWidget o) {
    return entry.compareTo(o.entry);
  }
}

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

import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget.Alignment;
import org.netbeans.api.visual.widget.LabelWidget.VerticalAlignment;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class RevisionGraphScene extends GraphScene<GraphTree, String> {
  private LayerWidget mainLayer;
  private LayerWidget connectionLayer;

  /**
   * Constructor RevisionGraphScene
   *
   *
   */
  public RevisionGraphScene() {
    mainLayer = new LayerWidget(this);

    addChild(mainLayer);

    connectionLayer = new LayerWidget(this);

    addChild(connectionLayer);
  }

  @Override
  protected Widget attachNodeWidget(GraphTree node) {
    GraphTreeWidget widget = new GraphTreeWidget(this, node);

    widget.setAlignment(Alignment.CENTER);
    widget.setVerticalAlignment(VerticalAlignment.CENTER);
    mainLayer.addChild(widget);

    return widget;
  }

  @Override
  protected Widget attachEdgeWidget(String edge) {
    ConnectionWidget connection = new ConnectionWidget(this);

    connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
    connectionLayer.addChild(connection);

    return connection;
  }

  @Override
  protected void attachEdgeSourceAnchor(String edge, GraphTree oldSourceNode, GraphTree sourceNode) {
    Widget w = (sourceNode != null) ? findWidget(sourceNode) : null;

    ((ConnectionWidget)findWidget(edge)).setSourceAnchor(AnchorFactory.createRectangularAnchor(w));
  }

  @Override
  protected void attachEdgeTargetAnchor(String edge, GraphTree oldTargetNode, GraphTree targetNode) {
    Widget w = (targetNode != null) ? findWidget(targetNode) : null;

    ((ConnectionWidget)findWidget(edge)).setTargetAnchor(AnchorFactory.createRectangularAnchor(w));
  }
}

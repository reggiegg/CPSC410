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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sjd.visualapigraphview;

import java.util.List;
import java.util.Map;

import org.openide.util.lookup.ServiceProvider;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.graph.GraphLogEntry;
import org.sdrinovsky.sdsvn.graph.GraphView;

/**
 *
 * @author sdrinovsky
 */
@ServiceProvider(service = GraphView.class)
public class VisualAPIGraphView implements GraphView {
  /**
   * Method createGraph
   *
   *
   * @param row
   * @param location
   * @param entries
   * @param branches
   *
   */
  @Override
  public void createGraph(FileTableRow row, Location location, Map<String, List<GraphLogEntry>> entries,
                          List<String> branches) {
    GraphTree root = new GraphTree(row.getFileName());
    GraphTree tree = null;

    for(String branch : branches) {
      tree = null;

      for(GraphLogEntry entry : entries.get(branch)) {
        if(tree == null) {
          tree = new GraphTree(entry);
        } else {
          tree = new GraphTree(entry, tree);
        }
      }

      if(tree != null) {
        root.add(new GraphTree(branch, tree));
      }
    }

    new GraphViewFrame(row, location, root).setVisible(true);
  }
}

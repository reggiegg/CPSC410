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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class GraphTree
 *
 *
 * @author
 * @version $Revision: 29 $
 */
public class GraphTree implements Iterable<GraphTree>, Comparable<GraphTree> {
  private List<GraphTree> subtree = new LinkedList<GraphTree>();
  private GraphLogEntry   logEntry;
  private String          label;

  /**
   * Constructor GraphTree
   *
   *
   */
  public GraphTree() {
    this.logEntry = null;
  }

  /**
   * Constructor GraphTree
   *
   *
   * @param logEntry
   *
   */
  public GraphTree(GraphLogEntry logEntry) {
    this.logEntry = logEntry;
  }

  /**
   * Constructor GraphTree
   *
   *
   * @param label
   *
   */
  public GraphTree(String label) {
    this.label = label;
  }

  /**
   * Constructor GraphTree
   *
   *
   * @param label
   * @param subtree
   *
   */
  public GraphTree(String label, GraphTree subtree) {
    this.label = label;

    add(subtree);
  }

  /**
   * Constructor GraphTree
   *
   *
   * @param logEntry
   * @param subtree
   *
   */
  public GraphTree(GraphLogEntry logEntry, GraphTree subtree) {
    this.logEntry = logEntry;

    add(subtree);
  }

  /**
   * Constructor GraphTree
   *
   *
   * @param logEntry
   * @param subtree
   *
   */
  public GraphTree(GraphLogEntry logEntry, List<GraphTree> subtree) {
    this.logEntry = logEntry;

    for(GraphTree tree : subtree) {
      add(tree);
    }
  }

  /**
   * Method setLogEntry
   *
   *
   * @param logEntry
   *
   */
  public void setLogEntry(GraphLogEntry logEntry) {
    this.logEntry = logEntry;
  }

  /**
   * Method add
   *
   *
   * @param logEntry
   *
   * @return
   *
   */
  public GraphTree add(GraphLogEntry logEntry) {
    return add(new GraphTree(logEntry));
  }

  /**
   * Method add
   *
   *
   *
   * @param tree
   *
   * @return
   *
   */
  public GraphTree add(GraphTree tree) {
    if(tree.isLabel() && !tree.isLeaf()) {
      long      revision = tree.getChildAt(0).getLogEntry().getRevision();
      GraphTree match    = findMatchingRevision(revision);

      if(match != null) {
        // pop the matching node
        GraphTree pop = tree.subtree.remove(0).getChildAt(0);

        if(pop != null) {
          tree.subtree.add(0, pop);
        }

        match.subtree.add(tree);
      } else {
        this.subtree.add(tree);
      }
    } else {
      this.subtree.add(tree);
    }

    return tree;
  }

  /**
   * Method isLeaf
   *
   *
   * @return
   *
   */
  public boolean isLeaf() {
    return subtree.size() == 0;
  }

  /**
   * Method isLabel
   *
   *
   * @return
   *
   */
  public boolean isLabel() {
    return label != null;
  }

  /**
   * Method getNumberChildren
   *
   *
   * @return
   *
   */
  public int getNumberChildren() {
    return subtree.size();
  }

  /**
   * Method getChildAt
   *
   *
   * @param index
   *
   * @return
   *
   */
  public GraphTree getChildAt(int index) {
    if((index >= 0) && (index < subtree.size())) {
      return subtree.get(index);
    } else {
      return null;
    }
  }

  /**
   * Method iterator
   *
   *
   * @return
   *
   */
  @Override
  public Iterator<GraphTree> iterator() {
    return subtree.iterator();
  }

  /**
   * Method getLabel
   *
   *
   * @return
   *
   */
  public String getLabel() {
    return label;
  }

  /**
   * Method getLogEntry
   *
   *
   * @return
   *
   */
  public GraphLogEntry getLogEntry() {
    return logEntry;
  }

  private GraphTree findMatchingRevision(long revision) {
    for(GraphTree tree : subtree) {
      if( !tree.isLabel()) {
        if(tree.getLogEntry().getRevision() == revision) {
          return tree;
        }
      }

      GraphTree found = tree.findMatchingRevision(revision);

      if(found != null) {
        return found;
      }
    }

    return null;
  }

  /**
   * Method toString
   *
   *
   * @return
   *
   */
  @Override
  public String toString() {
    String ret = "";

    if(isLabel()) {
      ret += label;
    } else {
      ret += logEntry.getRevision();
    }

    if( !isLeaf()) {
      boolean start = true;

      for(GraphTree tree : subtree) {
        if(start) {
          ret += ": ";
        } else {
          ret += ", ";
        }

        start = false;
        ret   += tree;
      }

      return ret;
    } else {
      return ret;
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
  public int compareTo(GraphTree o) {
    if(isLabel()) {
      if(o.isLabel()) {
        return label.compareTo(o.label);
      }

      return 1;
    } else if(o.isLabel()) {
      return -1;
    } else {
      return(int)(logEntry.getRevision() - o.logEntry.getRevision());
    }
  }
}

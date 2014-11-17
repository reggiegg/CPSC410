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

package org.sdrinovsky.sdsvn;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNConflictHandler;
import org.tmatesoft.svn.core.wc.SVNConflictChoice;
import org.tmatesoft.svn.core.wc.SVNConflictDescription;
import org.tmatesoft.svn.core.wc.SVNConflictResult;
import org.tmatesoft.svn.core.wc.SVNMergeFileSet;

/**
 *
 * @author sdrinovsky
 */
public class ConflictResolverHandler implements ISVNConflictHandler {
  private class ConflictAction extends AbstractAction {
    SVNConflictChoice choice;

    /**
     * Constructor ConflictAction
     *
     *
     * @param name
     * @param choice
     *
     */
    public ConflictAction(String name, SVNConflictChoice choice) {
      super(name);

      this.choice = choice;
    }

    /**
     * Method actionPerformed
     *
     *
     * @param e
     *
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      selectedAction = this;
    }
  }

  SVNApp       app;
  JRadioButton baseButton = new JRadioButton(new ConflictAction("Base Version", SVNConflictChoice.BASE));
  JRadioButton mineConflictsButton = new JRadioButton(new ConflictAction("Mine Conflicts",
                                                                         SVNConflictChoice.MINE_CONFLICT));
  JRadioButton mineFullyButton = new JRadioButton(new ConflictAction("Mine Fully", SVNConflictChoice.MINE_FULL));
  JRadioButton theirsConflictsButton = new JRadioButton(new ConflictAction("Theirs Conflicts",
                                                                           SVNConflictChoice.THEIRS_CONFLICT));
  JRadioButton   theirsFullyButton = new JRadioButton(new ConflictAction("Theirs Fully",
                                                                         SVNConflictChoice.THEIRS_FULL));
  JRadioButton   postponeButton    = new JRadioButton(new ConflictAction("Postpone", SVNConflictChoice.POSTPONE));
  ButtonGroup    group             = new ButtonGroup();
  JPanel         choicePanel       = new JPanel(new GridLayout(0, 1));
  ConflictAction selectedAction    = null;

  /**
   * Constructor ConflictResolverHandler
   *
   *
   * @param app
   *
   */
  public ConflictResolverHandler(SVNApp app) {
    this.app = app;

    group.add(baseButton);
    group.add(mineConflictsButton);
    group.add(mineFullyButton);
    group.add(theirsConflictsButton);
    group.add(theirsFullyButton);
    group.add(postponeButton);
    choicePanel.add(baseButton);
    choicePanel.add(mineConflictsButton);
    choicePanel.add(mineFullyButton);
    choicePanel.add(theirsConflictsButton);
    choicePanel.add(theirsFullyButton);
    choicePanel.add(postponeButton);
    postponeButton.doClick();
  }

  /**
   * Method handleConflict
   *
   *
   * @param conflictDescription
   *
   * @return
   *
   * @throws SVNException
   *
   */
  @Override
  public SVNConflictResult handleConflict(SVNConflictDescription conflictDescription) throws SVNException {
    SVNMergeFileSet mergeFiles = conflictDescription.getMergeFiles();
    int rc = JOptionPane.showConfirmDialog(app.getMainFrame(), choicePanel,
                                           "Conflict merging " + mergeFiles.getWCPath(), JOptionPane.OK_CANCEL_OPTION);

    if((rc == JOptionPane.OK_OPTION) && (selectedAction != null)) {
      System.out.println("Resolving conflict for " + mergeFiles.getWCFile() + ", choosing " +
                         selectedAction.getValue(Action.NAME));

      return new SVNConflictResult(selectedAction.choice, mergeFiles.getResultFile());
    } else {
      throw new SVNException(SVNErrorMessage.create(SVNErrorCode.CANCELLED, "Merge Canceled"));
    }
  }
}

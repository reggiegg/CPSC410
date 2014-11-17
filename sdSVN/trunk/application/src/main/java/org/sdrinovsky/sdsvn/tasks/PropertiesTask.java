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

package org.sdrinovsky.sdsvn.tasks;

import java.io.File;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.SVNApp;

import org.jdesktop.application.Task;

import org.sdrinovsky.sdsvn.dialogs.PropertiesDialog;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNPropertyHandler;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Class description
 *
 *
 * @version        Enter version here..., Thu, Dec 2, '10
 * @author         Enter your name here...
 */
public class PropertiesTask extends Task<List<SVNPropertyData>, Void> {
  SVNApp       app;
  FileTableRow row;

  class PropertyHandler implements ISVNPropertyHandler {
    List<SVNPropertyData> properties;

    /**
     * Constructor PropertyHandler
     *
     *
     * @param properties
     *
     */
    public PropertyHandler(List<SVNPropertyData> properties) {
      this.properties = properties;
    }

    /**
     * Method handleProperty
     *
     *
     * @param path
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(File path, SVNPropertyData property) throws SVNException {
      properties.add(property);
    }

    /**
     * Method handleProperty
     *
     *
     * @param url
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(SVNURL url, SVNPropertyData property) throws SVNException {
      properties.add(property);
    }

    /**
     * Method handleProperty
     *
     *
     * @param revision
     * @param property
     *
     * @throws SVNException
     *
     */
    @Override
    public void handleProperty(long revision, SVNPropertyData property) throws SVNException {
      properties.add(property);
    }
  }

  /**
   * Constructor PropertiesTask
   *
   *
   * @param app
   * @param row
   *
   */
  public PropertiesTask(SVNApp app, FileTableRow row) {
    super(app);

    this.app = app;
    this.row = row;
  }

  @Override
  protected List<SVNPropertyData> doInBackground() throws SVNException {
    List<SVNPropertyData> properties = new LinkedList<SVNPropertyData>();

    app.getSVNClientManager().getWCClient().doGetProperty(row.getFile(), null, SVNRevision.UNDEFINED,
                                                          SVNRevision.UNDEFINED, SVNDepth.EMPTY,
                                                          new PropertyHandler(properties), null);

    return properties;
  }

  @Override
  protected void succeeded(List<SVNPropertyData> properties) {
    List<SVNPropertyData> stringProperties = new LinkedList<SVNPropertyData>();

    for(SVNPropertyData propertyData : properties) {
      if(propertyData.getValue().isString()) {
        stringProperties.add(propertyData);
      }
    }

    PropertiesDialog dialog = new PropertiesDialog(app.getMainFrame(), true);

    dialog.setProperties(row.getFileName(), row.getFile().isDirectory(), stringProperties);
    dialog.setVisible(true);

    if(dialog.wasOkPressed()) {
      try {
        boolean changed = false;

        for(SVNPropertyData property : dialog.getProperties()) {
          if(checkModified(property, stringProperties)) {
            changed = true;

            app.getSVNClientManager().getWCClient().doSetProperty(row.getFile(), property.getName(),
                                                                  property.getValue(), false, SVNDepth.EMPTY,
                                                                  ISVNPropertyHandler.NULL, null);
          }
        }

        for(SVNPropertyData property : stringProperties) {
          changed = true;

          app.getSVNClientManager().getWCClient().doSetProperty(row.getFile(), property.getName(), null, false,
                                                                SVNDepth.EMPTY, ISVNPropertyHandler.NULL, null);
        }

        if(changed) {
          firePropertyChange("refresh", null, "Properties for " + row.getFileName() + " updated");
        }
      } catch(SVNException ex) {
        Logger.getLogger(PropertiesTask.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private boolean checkModified(SVNPropertyData property, List<SVNPropertyData> properties) {
    for(SVNPropertyData check : properties) {
      if(check.getName().equals(property.getName())) {
        properties.remove(check);

        return check.getValue().getString().equals(property.getValue().getString());
      }
    }

    return true;
  }

  @Override
  protected void failed(Throwable arg0) {
    JOptionPane.showMessageDialog(null, arg0.getMessage());
  }
}

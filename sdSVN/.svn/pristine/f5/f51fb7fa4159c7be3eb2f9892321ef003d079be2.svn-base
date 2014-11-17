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

import java.io.File;

import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.sdrinovsky.sdsvn.dialogs.LoginDialog;
import org.sdrinovsky.sdsvn.properties.ComboBoxProperty;
import org.sdrinovsky.sdsvn.properties.SelectedProperty;
import org.sdrinovsky.sdsvn.properties.TextFieldProperty;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * The main class of the application.
 */
public class SVNApp extends SingleFrameApplication {
  SVNClientManager  clientManager;
  Preferences       preferences;
  String            currentUUID = "";
  DefaultSVNOptions options     = SVNWCUtil.createDefaultOptions(true);

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    DAVRepositoryFactory.setup();
    SVNRepositoryFactoryImpl.setup();
    FSRepositoryFactory.setup();
    getContext().getSessionStorage().putProperty(JToggleButton.class, new SelectedProperty());
    getContext().getSessionStorage().putProperty(JCheckBox.class, new SelectedProperty());
    getContext().getSessionStorage().putProperty(JCheckBoxMenuItem.class, new SelectedProperty());
    getContext().getSessionStorage().putProperty(JTextField.class, new TextFieldProperty());
    getContext().getSessionStorage().putProperty(JComboBox.class, new ComboBoxProperty());

    SVNAppView                           view        = new SVNAppView(this);
    org.jdesktop.application.ResourceMap resourceMap = getContext().getResourceMap(SVNApp.class);

    options.setConflictHandler(new ConflictResolverHandler(this));
    show(view);
    view.fixButtons();
    getMainFrame().setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
    System.out.println(org.tmatesoft.svn.util.Version.getVersionString());
  }

  /**
   * This method is to initialize the specified window by injecting resources.
   * Windows shown in our application come fully initialized from the GUI
   * builder, so this additional configuration is not needed.
   */
  @Override
  protected void configureWindow(java.awt.Window root) {}

  /**
   * A convenient static getter for the application instance.
   * @return the instance of DesktopApplication1
   */
  public static SVNApp getApplication() {
    return Application.getInstance(SVNApp.class);
  }

  /**
   * Main method launching the application.
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(SVNApp.class, args);
  }

  /**
   * Method executeTask
   *
   *
   * @param task
   *
   */
  public void executeTask(Task task) {
    getContext().getTaskService().execute(task);
  }

  /**
   * Method getPreferences
   *
   *
   * @return
   *
   */
  public Preferences getPreferences() {
    if(preferences == null) {
      preferences = Preferences.userNodeForPackage(SVNApp.class);
    }

    return preferences;
  }

  /**
   * Method setPreferencesUUID
   *
   *
   *
   * @param repoName
   * @param uuid
   *
   */
  public void setPreferences(String repoName, String uuid) {
    if( !currentUUID.equals(uuid)) {
      if(uuid != null) {
        currentUUID = uuid;
        preferences = Preferences.userNodeForPackage(SVNApp.class).node(uuid);

        preferences.put("repo", repoName);
      } else {
        currentUUID = "";
        preferences = Preferences.userNodeForPackage(SVNApp.class);
      }

      resetSVNClientManager();
    }
  }

  /**
   * Method getSVNClientManager
   *
   *
   * @return
   *
   */
  public SVNClientManager getSVNClientManager() {
    if(clientManager == null) {
      resetSVNClientManager();
    }

    return clientManager;
  }

  /**
   * Method resetSVNClientManager
   *
   *
   */
  public void resetSVNClientManager() {
    if(clientManager != null) {
      clientManager.dispose();
    }

    clientManager = SVNClientManager.newInstance(options, null, null);

    clientManager.setAuthenticationManager(LoginDialog.authManager);
  }

  /**
   * Method getFileInfo
   *
   *
   * @param file
   *
   * @return
   *
   */
  public SVNInfo getFileInfo(File file) {
    try {
      return getSVNClientManager().getWCClient().doInfo(file, SVNRevision.WORKING);
    } catch(SVNException svne) {
    }

    return null;
  }

  /**
   * Method getRootURL
   *
   *
   * @param file
   *
   * @return
   *
   */
  public SVNURL getRootURL(File file) {
    SVNInfo info = getFileInfo(file);

    if(info != null) {
      return info.getRepositoryRootURL();
    } else {
      return null;
    }
  }
}

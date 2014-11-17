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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import javax.swing.event.ListSelectionEvent;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jnlp.ServiceManager;

import javax.swing.text.AttributeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.sdrinovsky.sdsvn.dialogs.CommitFrame;
import org.sdrinovsky.sdsvn.dialogs.LoginDialog;
import org.sdrinovsky.sdsvn.files.FileRow;
import org.sdrinovsky.sdsvn.files.FileRowImpl;
import org.sdrinovsky.sdsvn.files.FileTableModel;
import org.sdrinovsky.sdsvn.files.FileTableRenderer;
import org.sdrinovsky.sdsvn.files.FileTableRow;
import org.sdrinovsky.sdsvn.files.Location;
import org.sdrinovsky.sdsvn.tasks.AddTask;
import org.sdrinovsky.sdsvn.tasks.BranchTask;
import org.sdrinovsky.sdsvn.tasks.CheckoutTask;
import org.sdrinovsky.sdsvn.tasks.CleanupTask;
import org.sdrinovsky.sdsvn.tasks.CommitTask;
import org.sdrinovsky.sdsvn.tasks.DiffTask;
import org.sdrinovsky.sdsvn.tasks.GraphTask;
import org.sdrinovsky.sdsvn.tasks.HistoryTask;
import org.sdrinovsky.sdsvn.tasks.JIndentTask;
import org.sdrinovsky.sdsvn.tasks.MergeTask;
import org.sdrinovsky.sdsvn.tasks.PropertiesTask;
import org.sdrinovsky.sdsvn.tasks.RefreshTask;
import org.sdrinovsky.sdsvn.tasks.ResolveTask;
import org.sdrinovsky.sdsvn.tasks.RevertTask;
import org.sdrinovsky.sdsvn.tasks.SwitchTask;
import org.sdrinovsky.sdsvn.tasks.UpdateTask;
import org.sdrinovsky.sdsvn.tree.RepoTreeView;

import org.jdesktop.application.Task;

import org.sdrinovsky.sdsvn.tasks.AnnotateTask;
import org.sdrinovsky.sdsvn.tasks.UpdateAppTask;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * The application's main frame.
 */
public class SVNAppView extends FrameView {
  public static final SimpleDateFormat dateFormat               = new SimpleDateFormat("MMM dd, yyyy KK:mm:ss a");
  private boolean                      showChanged              = false;
  private boolean                      showUpdated              = false;
  private boolean                      showAdded                = false;
  private boolean                      showRemoved              = false;
  private boolean                      showConflicts            = false;
  private boolean                      showMissing              = false;
  private boolean                      hideMissing              = false;
  private boolean                      showUnknown              = false;
  private boolean                      hideUnknown              = false;
  private boolean                      showIgnored              = false;
  private Matcher                      filterMatches            = null;
  private FileTableModel               model                    = new FileTableModel();
  private SVNApp                       svnApp                   = SVNApp.getApplication();
  private long                         lastRefresh              = 0;
  private int                          debugTotalFoldersChecked = 0;
  private boolean                      debugRefresh             = false;
  private Thread                       refreshThread            = new Thread() {
    @Override
    public void run() {
      while(true) {
        try {
          Thread.sleep(10000);
        } catch(InterruptedException ex) {
          return;
        }

        String workingFolder = getWorkingFolder();

        if(workingFolder != null) {
          long debug_start = System.currentTimeMillis();

          debugTotalFoldersChecked = 0;

          long lastModifiedTime = getLastModifiedTime(new File(workingFolder), lastRefresh);
          long debug_total      = System.currentTimeMillis() - debug_start;

          if(lastModifiedTime > lastRefresh) {
            if(debugRefresh) {
              System.out.println("auto-refresh(" + debugTotalFoldersChecked + ", " + debug_total + ")");
            }

            doRefresh(true);
          } else {
            if(debugRefresh) {
              System.out.println("up-to-date(" + debugTotalFoldersChecked + ", " + debug_total + ")");
            }
          }
        }
      }
    }
  };

  private long getLastModifiedTime(File file, long min) {
    File[]  children = file.listFiles();
    boolean check    = false;

    if(children != null) {
      // look for a .svn folder in directories.
      // if this is a directory with no .svn folder stop looking for mod times.
      for(File child : children) {
        if(".svn".equals(child.getName())) {
          check = true;
        }
      }

      if(check) {
        for(File child : children) {
          if( !".svn".equals(child.getName())) {
            min = Math.max(min, getLastModifiedTime(child, min));
          }
        }
      }

      return min;
    } else if(new File(new File(new File(file.getParentFile(), ".svn"), "text-base"),
                       file.getName() + ".svn-base").exists()) {
      debugTotalFoldersChecked++;

      if(debugRefresh) {
        if(min < file.lastModified()) {
          System.out.println(file + " was updated");
        }
      }

      return Math.max(min, file.lastModified());
    } else {
      return min;
    }
  }

  /**
   * Constructor SimpleSVNView
   *
   *
   * @param app
   *
   */
  public SVNAppView(SingleFrameApplication app) {
    super(app);

    refreshThread.setPriority(Thread.MIN_PRIORITY);
    refreshThread.setDaemon(true);
    refreshThread.start();
    redirectSystemStreams();
    initComponents();

    for(int i = 0; i < FileTableRow.getColumnCount(); i++) {
      fileListing.getColumnModel().getColumn(i).setPreferredWidth(FileTableRow.getColumnWidth(i));
    }

    fileListing.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    fileListing.setDragEnabled(true);
    fileListing.getTableHeader().setReorderingAllowed(false);
    //fileListing.setTransferHandler(new TableTransferHandler());
    fileListing.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if((e.getClickCount() == 2) && (MouseEvent.BUTTON1 == e.getButton())) {
          int row      = fileListing.rowAtPoint(e.getPoint());
          int modelRow = fileListing.getRowSorter().convertRowIndexToModel(row);

          try {
            File file = model.getRow(modelRow).getFile();

            if(Desktop.isDesktopSupported()) {
              statusMessageLabel.setText("Editing file " + file);
              messageTimer.restart();
              Desktop.getDesktop().open(file);
            } else {
              statusMessageLabel.setText("Editing file " + file);
              messageTimer.restart();

              String str = "RUNDLL32.EXE SHELL32.DLL,";

              if(e.isControlDown()) {
                str += "OpenAs_RunDLL ";
              } else {
                str += "ShellExec_RunDLL ";
              }

              Runtime r = Runtime.getRuntime();
              Process p = r.exec(str + file);
            }
          } catch(IOException ex) {
            Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    });
    fileListing.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        setRowSelected(fileListing.getSelectedRow() != -1);
      }
    });
    fileListing.setDefaultRenderer(Object.class, new FileTableRenderer());
    fileListing.setDefaultRenderer(Long.class, new FileTableRenderer());
    fileListing.setDefaultRenderer(Date.class, new FileTableRenderer());

    RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
      @Override
      public boolean include(Entry entry) {
        return includeFilter(entry) && includeButtons(entry);
      }
      private boolean includeFilter(Entry entry) {
        if(filterMatches != null) {
          File file = new File((File)entry.getValue(FileTableRow.FOLDER_COLUMN),
                               entry.getStringValue(FileTableRow.FILE_COLUMN));

          filterMatches.reset(file.toString());

          return filterMatches.find();
        }

        return true;
      }
      private boolean includeButtons(Entry entry) {
        boolean showAll = !(showAdded || showChanged || showConflicts || showMissing || showRemoved || showUnknown);
        String  status  = entry.getStringValue(FileTableRow.STATUS_COLUMN);

        switch(status.charAt(0)) {
          case 'X':
            return showAll;
          case 'A':
          case 'R':
          case '~':
            return showAll || showAdded;
          case 'D':
            return showAll || showRemoved;
          case 'M':
            return showAll || showChanged;
          case 'C':
            return showAll || showConflicts;
          case 'I':
            return(showAll || showUnknown) && showIgnored;
          case '?':
            return(showAll && !hideUnknown) || showUnknown;
          case '!':
            return(showAll && !hideMissing) || showMissing;
        }

        switch(status.charAt(1)) {
          case 'M':
            return showAll || showChanged;
          case 'C':
            return showAll || showConflicts;
        }

        if(status.charAt(2) == 'L') {
          return showAll || showChanged;
        }

        if(status.charAt(3) == '+') {
          return showAll || showAdded || showRemoved;
        }

        if(status.charAt(4) == 'S') {
          return showAll || showChanged;
        }

        if(status.charAt(5) == 'K') {
          return showAll || showChanged;
        }

        if(status.charAt(6) == '*') {
          return showAll || showUpdated;
        }

        return showAll;
      }
    };
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(fileListing.getModel());

    sorter.setRowFilter(filter);
    sorter.toggleSortOrder(FileTableRow.FILE_COLUMN);
    sorter.toggleSortOrder(FileTableRow.FOLDER_COLUMN);
    fileListing.setRowSorter(sorter);

    // status bar initialization - message timeout, idle icon and busy animation, etc
    ResourceMap resourceMap    = getResourceMap();
    int         messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");

    messageTimer = new Timer(messageTimeout, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        statusMessageLabel.setText("");
      }
    });

    messageTimer.setRepeats(false);

    int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");

    for(int i = 0; i < busyIcons.length; i++) {
      busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
    }

    busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
      }
    });
    idleIcon = resourceMap.getIcon("StatusBar.idleIcon");

    statusAnimationLabel.setIcon(idleIcon);
    progressBar.setVisible(false);

    // connecting action tasks to status bar via TaskMonitor
    TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());

    taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      @Override
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if("started".equals(propertyName)) {
          if( !busyIconTimer.isRunning()) {
            statusAnimationLabel.setIcon(busyIcons[0]);

            busyIconIndex = 0;

            busyIconTimer.start();
          }

          progressBar.setVisible(true);
          progressBar.setIndeterminate(true);
        } else if("done".equals(propertyName)) {
          busyIconTimer.stop();
          statusAnimationLabel.setIcon(idleIcon);
          progressBar.setVisible(false);
          progressBar.setValue(0);
        } else if("message".equals(propertyName)) {
          String text = (String)(evt.getNewValue());

          if(text != null) {
            statusMessageLabel.setText(text);
            messageTimer.restart();
            System.out.println(text);
          }
        } else if("error".equals(propertyName)) {
          String text = (String)(evt.getNewValue());

          if(text != null) {
            statusMessageLabel.setText(text);
            messageTimer.restart();
            System.err.println(text);
          }
        } else if("progress".equals(propertyName)) {
          int value = (Integer)(evt.getNewValue());

          progressBar.setVisible(true);
          progressBar.setIndeterminate(false);
          progressBar.setValue(value);
        } else if("status".equals(propertyName)) {
          model.clear();
          model.addRows((List<FileTableRow>)evt.getNewValue());
          setTablePopulated(true);
        } else if("refresh".equals(propertyName)) {
          doRefresh((String)evt.getNewValue(), false, showUpdated);
        } else if("selected".equals(propertyName)) {
          int row     = model.indexOf(evt.getNewValue());
          int viewRow = fileListing.getRowSorter().convertRowIndexToView(row);

          fileListing.getSelectionModel().addSelectionInterval(viewRow, viewRow);
        }
      }
    });
    resetButtons();

    try {
      setJnlpEnabled(ServiceManager.lookup("javax.jnlp.DownloadService2") != null);
    } catch(Throwable e) {
      setJnlpEnabled(false);

      try {
        ServiceManager.lookup("javax.jnlp.DownloadService");
        jnlpUpdatemenuItem.setToolTipText("Update only available with Java VM 6u18 or later.");
      } catch(Throwable ex) {
        jnlpUpdatemenuItem.setToolTipText("Update only available when run from Java Web Start");
      }
    }
  }

  /**
   * Method getModel
   *
   *
   * @return
   *
   */
  public FileTableModel getModel() {
    return model;
  }

  /**
   * Method showAboutBox
   *
   *
   */
  @Action
  public void showAboutBox() {
    if(aboutBox == null) {
      JFrame mainFrame = svnApp.getMainFrame();

      aboutBox = new SVNAppAboutBox(mainFrame);

      aboutBox.setLocationRelativeTo(mainFrame);
    }

    svnApp.show(aboutBox);
  }

  /**
   *  Method login
   *
   *
   */
  @Action
  public void login() {
    svnApp.show(new LoginDialog());
    SVNApp.getApplication().resetSVNClientManager();
    setLoggedIn(svnApp.getPreferences().get("username", null) != null);
  }

  /**
   * Method internalDiff
   *
   *
   */
  @Action(enabledProperty = "internalDiffAvailalbe")
  public void internalDiff() {
    svnApp.getPreferences().putBoolean("internalDiff", diffCheckBoxMenuItem.isSelected());
    resetButtons();
  }

  /**
   * Method isInternalDiffAvailalbe
   *
   *
   * @return
   *
   */
  public boolean isInternalDiffAvailalbe() {
    return DiffTask.isInternalDiffAvailable();
  }

  /**
   * Method diffProgram
   *
   *
   */
  @Action
  public void diffProgram() {
    String       current     = svnApp.getPreferences().get("diffProgram", null);
    JFileChooser diffChooser = new JFileChooser(current);

    diffChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if(JFileChooser.APPROVE_OPTION == diffChooser.showOpenDialog(svnApp.getMainFrame())) {
      svnApp.getPreferences().put("diffProgram", diffChooser.getSelectedFile().toString());
      resetButtons();
    }
  }

  /**
   * Method mergeProgram
   *
   *
   */
  @Action
  public void mergeProgram() {
    String       current     = svnApp.getPreferences().get("mergeProgram", null);
    JFileChooser diffChooser = new JFileChooser(current);

    diffChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if(JFileChooser.APPROVE_OPTION == diffChooser.showOpenDialog(svnApp.getMainFrame())) {
      svnApp.getPreferences().put("mergeProgram", diffChooser.getSelectedFile().toString());
      resetButtons();
    }
  }

  /**
   * Method jindentSettings
   *
   *
   */
  @Action
  public void jindentSettings() {
    String       current     = svnApp.getPreferences().get("jindentSettings", null);
    JFileChooser diffChooser = new JFileChooser(current);

    diffChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if(JFileChooser.APPROVE_OPTION == diffChooser.showOpenDialog(svnApp.getMainFrame())) {
      svnApp.getPreferences().put("jindentSettings", diffChooser.getSelectedFile().toString());
      resetButtons();
    }
  }

  /**
   * Method jindentLicense
   *
   *
   */
  @Action
  public void jindentProgram() {
    String       current     = svnApp.getPreferences().get("jindentProgram", null);
    JFileChooser diffChooser = new JFileChooser(current);

    diffChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if(JFileChooser.APPROVE_OPTION == diffChooser.showOpenDialog(svnApp.getMainFrame())) {
      svnApp.getPreferences().put("jindentProgram", diffChooser.getSelectedFile().toString());
      resetButtons();
    }
  }

  /**
   * Method showChooser
   *
   *
   */
  @Action
  public void showChooser() {
    showChooser("Select Project Folder");
  }

  private void showChooser(String dialogTitle) {
    if(chooser == null) {
      chooser = new JFileChooser();

      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    chooser.setDialogTitle(dialogTitle);

    String workingFolder = getWorkingFolder();

    if(workingFolder != null) {
      chooser.setSelectedFile(new File(workingFolder));
    }

    if(JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(svnApp.getMainFrame())) {
      cbWorkingFolder.setSelectedItem(chooser.getSelectedFile().toString());
      doRefresh(false);
    }
  }

  /**
   * Method checkout
   *
   *
   * @return
   *
   */
  @Action(block = Task.BlockingScope.APPLICATION, enabledProperty = "repoEmpty")
  public Task checkout() {
    String workingFolder = getWorkingFolder();
    File   destination   = null;

    if(workingFolder != null) {
      destination = new File(workingFolder);
    }

    if(destination != null) {
      String repo = showRepoView(true, false);

      if(repo != null) {
        try {
          SVNURL      url      = SVNURL.parseURIEncoded(repo);
          SVNRevision revision = SVNRevision.HEAD;

          return new CheckoutTask(svnApp, url, destination, revision);
        } catch(SVNException ex) {
          Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return null;
  }

  /**
   * Method update
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "repoSet", block = Task.BlockingScope.ACTION)
  public Task update() {
    String workingFolder = getWorkingFolder();
    File   destination   = null;

    if(workingFolder != null) {
      destination = new File(workingFolder);
    }

    if(destination != null) {
      return new UpdateTask(svnApp, FileRowImpl.getList(destination, null));
    }

    return null;
  }

  /**
   * Method cleanup
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "repoSet", block = Task.BlockingScope.APPLICATION)
  public Task cleanup() {
    String workingFolder = getWorkingFolder();

    if(workingFolder != null) {
      File destination = new File(workingFolder);

      return new CleanupTask(svnApp, destination);
    }

    return null;
  }

  /**
   * Method revert
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "revertableSelected")
  public Task revert() {
    List<FileTableRow> files = getSelectRows();

    if(askConfirm("Confirm Revert", "revert", files)) {
      return new RevertTask(svnApp, files);
    }

    return null;
  }

  private boolean askConfirm(String title, String question, List<FileTableRow> files) {
    StringBuilder message = new StringBuilder("Are you sure you want to ");

    message.append(question);
    message.append(" the following files:\n");

    int counter = 0;

    for(FileTableRow file : files) {
      if(counter > 20) {
        message.append("...\n");

        break;
      }

      message.append(file.getFile());
      message.append("\n");

      counter++;
    }

    return JOptionPane.YES_OPTION ==
           JOptionPane.showConfirmDialog(svnApp.getMainFrame(), message, title, JOptionPane.YES_NO_OPTION,
                                         JOptionPane.WARNING_MESSAGE);
  }

  List<String> lastMessages  = new ArrayList<String>();
  boolean      lastRecursive = false;

  /**
   * Method commit
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "commitableSelected")
  public Task commit() {
    List<FileTableRow> files       = getCommitableFiles();
    CommitFrame        commitFrame = new CommitFrame(svnApp.getMainFrame());

    commitFrame.setMessages(lastMessages);
    commitFrame.setRecurvive(lastRecursive);
    commitFrame.setFiles(files);
    commitFrame.setVisible(true);

    String message = commitFrame.getMessage();

    if((message != null) && (message.length() > 0)) {
      lastMessages.remove(message);
      lastMessages.add(0, message);
    }

    lastRecursive = commitFrame.isRecursive();

    if(commitFrame.wasOkPressed()) {
      return new CommitTask(svnApp, files, message, lastRecursive);
    }

    return null;
  }

  /**
   * Method add
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "addableSelected")
  public Task add() {
    return new AddTask(svnApp, getAddableFiles());
  }

  /**
   * Method refresh
   *
   *
   * @return
   *
   */
  @Action(block = Task.BlockingScope.ACTION, enabledProperty = "repoSet")
  public Task refresh() {
    return refresh(null, showUpdated);
  }

  /**
   * Method refresh
   *
   *
   * @param message
   * @param updated
   *
   * @return
   *
   */
  public Task refresh(String message, boolean updated) {
    lastRefresh = System.currentTimeMillis();

    List<File> selected      = getSelectFiles();
    String     workingFolder = getWorkingFolder();

    if(workingFolder != null) {
      SVNInfo info = svnApp.getFileInfo(new File(workingFolder));

      if(info != null) {
        setRepo(info.getRepositoryRootURL().toString(), info.getRevision().getNumber(), info.getRepositoryUUID());
      } else {
        setRepo("", 0L, null);
      }

      RefreshTask task = new RefreshTask(svnApp, new File(workingFolder), selected, updated);

      task.setMessageText(message);

      return task;
    }

    return null;
  }

  /**
   * Method jindent
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "jindentEnabled")
  public Task jindent() {
    return new JIndentTask(svnApp, getSelectRows());
  }

  /**
   * Method diff
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "diffEnabled")
  public Task diff() {
    return new DiffTask(svnApp, getSelectRows());
  }

  /**
   * Method graph
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "graphEnabled")
  public Task graph() {
    FileTableRow       row      = getSelectRows().get(0);
    LinkedList<String> branches = new LinkedList<String>();

    branches.add("trunk");

    Location location = row.getLocation();

    if((location != null) && !"trunk".equals(location.getBranch())) {
      branches.add(location.getBranch());
    }

    if(showBranchesToggleButton.isSelected()) {
      try {
        SVNURL        url        = getRootURL();
        SVNRepository repo       = svnApp.getSVNClientManager().createRepository(url, true);
        Collection    collection = repo.getDir("branches", -1, null, (Collection)null);

        for(Object o : collection) {
          SVNDirEntry entry = (SVNDirEntry)o;

          branches.add("branches/" + entry.getName());
        }

        collection = repo.getDir("tags", -1, null, (Collection)null);

        for(Object o : collection) {
          SVNDirEntry entry = (SVNDirEntry)o;

          branches.add("tags/" + entry.getName());
        }
      } catch(SVNException ex) {
        Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    return new GraphTask(svnApp, row, row.getLocation(), branches);
  }

  /**
   * Method switchWorkingCopy
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "repoSet", block = Task.BlockingScope.APPLICATION)
  public Task switchWorkingCopy() {
    String repo = showRepoView(true, false);

    if(repo != null) {
      String switchTo = FileTableRow.switchBranch(getURL().toString(),
                                                  FileTableRow.parseURL(repo, FileTableRow.PARSE_URL_BRANCH));

      try {
        SVNURL      url           = SVNURL.parseURIEncoded(switchTo);
        SVNRevision revision      = SVNRevision.HEAD;
        String      workingFolder = getWorkingFolder();

        if(workingFolder != null) {
          File destination = new File(workingFolder);

          return new SwitchTask(svnApp, url, destination, revision);
        }
      } catch(SVNException ex) {
        Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    return null;
  }

  /**
   * Method isFalse
   *
   *
   * @return
   *
   */
  public boolean isFalse() {
    return false;
  }

  /**
   * Method setFalse
   *
   *
   * @param b
   *
   */
  public void setFalse(boolean b) {}

  /**
   * Method mergeWorkingCopy
   *
   *
   * @return
   *
   */
  @Action(block = Task.BlockingScope.APPLICATION, enabledProperty = "repoSet")
  public Task mergeWorkingCopy() {
    String repo = showRepoView(true, false);

    if(repo != null) {
      try {
        SVNURL      url           = SVNURL.parseURIEncoded(repo);
        SVNRevision revision      = SVNRevision.HEAD;
        String      workingFolder = getWorkingFolder();

        if(workingFolder != null) {
          SVNURL        from        = getURL();
          File          destination = new File(workingFolder);
          List<FileRow> rows        = FileRowImpl.getList(destination, new Location(destination, repo));

          return new MergeTask(svnApp, url, rows, from, revision);
        }
      } catch(SVNException ex) {
        Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      System.out.println("Merge Canceled");
    }

    return null;
  }

  /**
   * Method getWorkingFolder
   *
   *
   * @return
   *
   */
  public String getWorkingFolder() {
    String item         = null;
    Object selectedItem = cbWorkingFolder.getSelectedItem();

    if(selectedItem != null) {
      item = selectedItem.toString();

      DefaultComboBoxModel cbModel = (DefaultComboBoxModel)cbWorkingFolder.getModel();

      if(cbModel.getIndexOf(item) != 0) {
        cbModel.removeElement(item);
        cbModel.insertElementAt(item, 0);
        cbWorkingFolder.setSelectedIndex(0);
      }
    }

    return item;
  }

  /**
   * Method getRootURL
   *
   *
   * @return
   *
   */
  public SVNURL getRootURL() {
    return svnApp.getRootURL(new File(getWorkingFolder()));
  }

  /**
   * Method getURL
   *
   *
   * @return
   *
   */
  public SVNURL getURL() {
    SVNInfo info = svnApp.getFileInfo(new File(getWorkingFolder()));

    return info.getURL();
  }

  private RepoTreeView repoView = null;

  /**
   * Method showRepoView
   *
   *
   *
   * @param modal
   * @param recursive
   *
   * @return
   *
   */
  public String showRepoView(boolean modal, boolean recursive) {
    SVNURL url       = getRootURL();
    String urlString = null;

    if(url != null) {
      urlString = url.toString();
    }

    if(repoView == null) {
      repoView = new RepoTreeView(svnApp.getMainFrame(), modal, urlString, recursive);
    }

    repoView.loadRepo(urlString, true);
    repoView.setVisible(true);

    if(modal) {
      String repo = repoView.getSelectedPath();

      System.out.println("repo: " + repo);

      return repo;
    } else {
      return null;
    }
  }

  /**
   * Method getSelectedFiles
   *
   *
   * @return
   *
   */
  public List<FileTableRow> getSelectRows() {
    List<FileTableRow> list = new LinkedList<FileTableRow>();

    for(int row : fileListing.getSelectedRows()) {
      int realRow = fileListing.getRowSorter().convertRowIndexToModel(row);

      list.add(model.getRow(realRow));
    }

    return list;
  }

  /**
   * Method getSelectFiles
   *
   *
   * @return
   *
   */
  public List<File> getSelectFiles() {
    List<File> list = new LinkedList<File>();

    for(int row : fileListing.getSelectedRows()) {
      int realRow = fileListing.getRowSorter().convertRowIndexToModel(row);

      list.add(model.getRow(realRow).getFile());
    }

    return list;
  }

  /**
   * Method getCommitableFiles
   *
   *
   * @return
   *
   */
  public List<FileTableRow> getCommitableFiles() {
    List<FileTableRow> list = new LinkedList<FileTableRow>();

    for(int row : fileListing.getSelectedRows()) {
      int          realRow = fileListing.getRowSorter().convertRowIndexToModel(row);
      FileTableRow ftr     = model.getRow(realRow);

      if(ftr.isCommitable()) {
        list.add(ftr);
      }
    }

    return list;
  }

  /**
   * Method getAddableFiles
   *
   *
   * @return
   *
   */
  public List<File> getAddableFiles() {
    List<File> list = new LinkedList<File>();

    for(int row : fileListing.getSelectedRows()) {
      int          realRow = fileListing.getRowSorter().convertRowIndexToModel(row);
      FileTableRow ftr     = model.getRow(realRow);

      if(ftr.isAddable()) {
        list.add(ftr.getFile());
      }
    }

    return list;
  }

  /**
   * Method fixButtons
   *
   *
   */
  public void fixButtons() {
    showChanged   = tbChanged.isSelected();
    showUpdated   = tbUpdated.isSelected();
    showAdded     = tbAdded.isSelected();
    showRemoved   = tbRemoved.isSelected();
    showConflicts = tbConflicts.isSelected();
    showMissing   = tbShowMissing.isSelected();
    hideMissing   = tbHideMissing.isSelected();
    showUnknown   = tbShowUnknown.isSelected();
    hideUnknown   = tbHideUnknown.isSelected();
    showIgnored   = tbShowIgnored.isSelected();

    tableFilter.setText("");
    updateToolBar();
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    mainPanel                = new javax.swing.JPanel();
    RepoActionPanel          = new javax.swing.JPanel();
    btnCheckout              = new javax.swing.JButton();
    btnRefresh               = new javax.swing.JButton();
    btnUpdate                = new javax.swing.JButton();
    btnMerge                 = new javax.swing.JButton();
    jButton1                 = new javax.swing.JButton();
    btnSwitch                = new javax.swing.JButton();
    btnCleanup               = new javax.swing.JButton();
    jToolBar1                = new javax.swing.JToolBar();
    tbModified               = new javax.swing.JToggleButton();
    tbChanged                = new javax.swing.JToggleButton();
    tbAdded                  = new javax.swing.JToggleButton();
    tbRemoved                = new javax.swing.JToggleButton();
    tbConflicts              = new javax.swing.JToggleButton();
    jSeparator2              = new javax.swing.JToolBar.Separator();
    tbShowMissing            = new javax.swing.JToggleButton();
    tbHideMissing            = new javax.swing.JToggleButton();
    jSeparator3              = new javax.swing.JToolBar.Separator();
    tbShowUnknown            = new javax.swing.JToggleButton();
    tbHideUnknown            = new javax.swing.JToggleButton();
    jSeparator4              = new javax.swing.JToolBar.Separator();
    tbShowIgnored            = new javax.swing.JToggleButton();
    tbUpdated                = new javax.swing.JToggleButton();
    jSeparator6              = new javax.swing.JToolBar.Separator();
    tableFilter              = new javax.swing.JTextField();
    jSeparator7              = new javax.swing.JToolBar.Separator();
    showBranchesToggleButton = new javax.swing.JToggleButton();
    InfoPanel                = new javax.swing.JSplitPane();
    jScrollPane1             = new javax.swing.JScrollPane();
    fileListing              = new JTable() {
      public Point getToolTipLocation(MouseEvent e) {
        if(getToolTipText(e) != null) {
          Rectangle rect = fileListing.getCellRect(fileListing.rowAtPoint(e.getPoint()),
                                                   fileListing.columnAtPoint(e.getPoint()), true);

          return new Point(rect.x + 2, rect.y - 1);
        } else {
          return null;
        }
      }
    };
    jScrollPane2            = new javax.swing.JScrollPane();
    messageTextPane         = new javax.swing.JTextPane();
    FileActionPanel         = new javax.swing.JPanel();
    btnJIndent              = new javax.swing.JButton();
    btnDiff                 = new javax.swing.JButton();
    btnGraph                = new javax.swing.JButton();
    btnHistory              = new javax.swing.JButton();
    btnProperties           = new javax.swing.JButton();
    btnResolve              = new javax.swing.JButton();
    btnCommit               = new javax.swing.JButton();
    btnAdd                  = new javax.swing.JButton();
    btnRemove               = new javax.swing.JButton();
    btnRevert               = new javax.swing.JButton();
    btnQuit                 = new javax.swing.JButton();
    btnAnnotate             = new javax.swing.JButton();
    WorkingCopySettingPanel = new javax.swing.JPanel();
    lblRepoPath             = new javax.swing.JLabel();
    tfRepository            = new javax.swing.JTextField();
    lblWorkingFolder        = new javax.swing.JLabel();
    cbWorkingFolder         = new javax.swing.JComboBox();
    btnBrowse               = new javax.swing.JButton();
    menuBar                 = new javax.swing.JMenuBar();

    javax.swing.JMenu fileMenu = new javax.swing.JMenu();

    newProjectmenuItem  = new javax.swing.JMenuItem();
    openProjectMenuItem = new javax.swing.JMenuItem();
    repoViewMenuItem    = new javax.swing.JMenuItem();
    jSeparator8         = new javax.swing.JSeparator();

    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();

    settingsMenu            = new javax.swing.JMenu();
    loginMenuItem           = new javax.swing.JMenuItem();
    jSeparator1             = new javax.swing.JSeparator();
    jindentProgramMenuItem  = new javax.swing.JMenuItem();
    jindentSettingsMenuItem = new javax.swing.JMenuItem();
    jSeparator9             = new javax.swing.JSeparator();
    diffCheckBoxMenuItem    = new javax.swing.JCheckBoxMenuItem();
    diffProgramMenuItem     = new javax.swing.JMenuItem();
    mergeProgramMenuItem    = new javax.swing.JMenuItem();
    RepoMenu                = new javax.swing.JMenu();
    checkoutMenuItem        = new javax.swing.JMenuItem();
    refreshMenuItem         = new javax.swing.JMenuItem();
    updateMenuItem          = new javax.swing.JMenuItem();
    mergeMenuItem           = new javax.swing.JMenuItem();
    branchMenuItem          = new javax.swing.JMenuItem();
    switchMenuItem          = new javax.swing.JMenuItem();
    CleanUpMenuItem         = new javax.swing.JMenuItem();
    ModifyMenu              = new javax.swing.JMenu();
    JIndentMenuItem1        = new javax.swing.JMenuItem();
    CommitMenuItem          = new javax.swing.JMenuItem();
    AddMenuItem             = new javax.swing.JMenuItem();
    RemoveMenuItem          = new javax.swing.JMenuItem();
    RevertMenuItem          = new javax.swing.JMenuItem();
    UpdateREvisionMenuItem  = new javax.swing.JMenuItem();
    QueryMenu               = new javax.swing.JMenu();
    DiffMenuItem            = new javax.swing.JMenuItem();
    DiffRevisionMenuItem    = new javax.swing.JMenuItem();
    GraphMenuItem           = new javax.swing.JMenuItem();
    HistoryMenuItem         = new javax.swing.JMenuItem();
    AnnotateMenuItem        = new javax.swing.JMenuItem();
    PropertiesMenuItem      = new javax.swing.JMenuItem();

    javax.swing.JMenu     helpMenu      = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

    jnlpUpdatemenuItem = new javax.swing.JMenuItem();
    statusPanel        = new javax.swing.JPanel();

    javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();

    statusMessageLabel        = new javax.swing.JLabel();
    statusAnimationLabel      = new javax.swing.JLabel();
    progressBar               = new javax.swing.JProgressBar();
    actionPopup               = new javax.swing.JPopupMenu();
    jindentMenuItem           = new javax.swing.JMenuItem();
    diffMenuItem              = new javax.swing.JMenuItem();
    diffRevisionMenuItem      = new javax.swing.JMenuItem();
    diffSelectedFilesMenuItem = new javax.swing.JMenuItem();
    graphMenuItem             = new javax.swing.JMenuItem();
    historyMenuItem           = new javax.swing.JMenuItem();
    annotateMenuItem          = new javax.swing.JMenuItem();
    propertiesMenuItem        = new javax.swing.JMenuItem();
    resolveMenuItem           = new javax.swing.JMenuItem();
    jSeparator5               = new javax.swing.JSeparator();
    commitMenuItem            = new javax.swing.JMenuItem();
    addMenuItem               = new javax.swing.JMenuItem();
    removeMenuItem            = new javax.swing.JMenuItem();
    revertMenuItem            = new javax.swing.JMenuItem();
    updateRevisionMenuItem    = new javax.swing.JMenuItem();
    messagePopup              = new javax.swing.JPopupMenu();
    copyMenuItem              = new javax.swing.JMenuItem();
    clearMenuItem             = new javax.swing.JMenuItem();

    mainPanel.setName("mainPanel");                // NOI18N
    RepoActionPanel.setName("RepoActionPanel");    // NOI18N

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getActionMap(SVNAppView.class, this);

    btnCheckout.setAction(actionMap.get("checkout"));           // NOI18N
    btnCheckout.setName("btnCheckout");                         // NOI18N
    btnRefresh.setAction(actionMap.get("refresh"));             // NOI18N
    btnRefresh.setName("btnRefresh");                           // NOI18N
    btnUpdate.setAction(actionMap.get("update"));               // NOI18N
    btnUpdate.setName("btnUpdate");                             // NOI18N
    btnMerge.setAction(actionMap.get("mergeWorkingCopy"));      // NOI18N
    btnMerge.setName("btnMerge");                               // NOI18N
    jButton1.setAction(actionMap.get("copyWorkingCopy"));       // NOI18N
    jButton1.setName("jButton1");                               // NOI18N
    btnSwitch.setAction(actionMap.get("switchWorkingCopy"));    // NOI18N
    btnSwitch.setName("btnSwitch");                             // NOI18N
    btnCleanup.setAction(actionMap.get("cleanup"));             // NOI18N
    btnCleanup.setName("btnCleanup");                           // NOI18N
    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);
    jToolBar1.setName("jToolBar1");                             // NOI18N
    tbModified.setAction(actionMap.get("showModified"));        // NOI18N
    tbModified.setFocusable(false);
    tbModified.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbModified.setName("tbModified");                           // NOI18N
    tbModified.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbModified);
    tbChanged.setAction(actionMap.get("showChanged"));          // NOI18N
    tbChanged.setFocusable(false);
    tbChanged.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbChanged.setName("tbChanged");                             // NOI18N
    tbChanged.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbChanged);
    tbAdded.setAction(actionMap.get("showAdded"));              // NOI18N
    tbAdded.setFocusable(false);
    tbAdded.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbAdded.setName("tbAdded");                                 // NOI18N
    tbAdded.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbAdded);
    tbRemoved.setAction(actionMap.get("showRemoved"));          // NOI18N
    tbRemoved.setFocusable(false);
    tbRemoved.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbRemoved.setName("tbRemoved");                             // NOI18N
    tbRemoved.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbRemoved);
    tbConflicts.setAction(actionMap.get("showConflicts"));      // NOI18N
    tbConflicts.setFocusable(false);
    tbConflicts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbConflicts.setName("tbConflicts");                         // NOI18N
    tbConflicts.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbConflicts);
    jSeparator2.setName("jSeparator2");                         // NOI18N
    jToolBar1.add(jSeparator2);
    tbShowMissing.setAction(actionMap.get("showMissing"));      // NOI18N
    tbShowMissing.setFocusable(false);
    tbShowMissing.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbShowMissing.setName("tbShowMissing");                     // NOI18N
    tbShowMissing.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbShowMissing);
    tbHideMissing.setAction(actionMap.get("hideMissing"));      // NOI18N
    tbHideMissing.setFocusable(false);
    tbHideMissing.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbHideMissing.setName("tbHideMissing");                     // NOI18N
    tbHideMissing.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbHideMissing);
    jSeparator3.setName("jSeparator3");                         // NOI18N
    jToolBar1.add(jSeparator3);
    tbShowUnknown.setAction(actionMap.get("showUnknown"));      // NOI18N
    tbShowUnknown.setFocusable(false);
    tbShowUnknown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbShowUnknown.setName("tbShowUnknown");                     // NOI18N
    tbShowUnknown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbShowUnknown);
    tbHideUnknown.setAction(actionMap.get("hideUnknown"));      // NOI18N
    tbHideUnknown.setFocusable(false);
    tbHideUnknown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbHideUnknown.setName("tbHideUnknown");                     // NOI18N
    tbHideUnknown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbHideUnknown);
    jSeparator4.setName("jSeparator4");                         // NOI18N
    jToolBar1.add(jSeparator4);
    tbShowIgnored.setAction(actionMap.get("showIgnored"));      // NOI18N
    tbShowIgnored.setFocusable(false);
    tbShowIgnored.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbShowIgnored.setName("tbShowIgnored");                     // NOI18N
    tbShowIgnored.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbShowIgnored);
    tbUpdated.setAction(actionMap.get("showUpdated"));          // NOI18N
    tbUpdated.setFocusable(false);
    tbUpdated.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    tbUpdated.setName("tbUpdated");                             // NOI18N
    tbUpdated.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(tbUpdated);
    jSeparator6.setName("jSeparator6");                         // NOI18N
    jToolBar1.add(jSeparator6);
    tableFilter.setColumns(12);
    tableFilter.setName("tableFilter");                         // NOI18N
    tableFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        tableFilterActionPerformed(evt);
      }
    });
    jToolBar1.add(tableFilter);
    jSeparator7.setName("jSeparator7");                                          // NOI18N
    jToolBar1.add(jSeparator7);
    showBranchesToggleButton.setAction(actionMap.get("showBranchesInGraph"));    // NOI18N
    showBranchesToggleButton.setFocusable(false);
    showBranchesToggleButton.setHideActionText(true);
    showBranchesToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    showBranchesToggleButton.setName("showBranchesToggleButton");                // NOI18N
    showBranchesToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar1.add(showBranchesToggleButton);

    javax.swing.GroupLayout RepoActionPanelLayout = new javax.swing.GroupLayout(RepoActionPanel);

    RepoActionPanel.setLayout(RepoActionPanelLayout);
    RepoActionPanelLayout.setHorizontalGroup(
       RepoActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         RepoActionPanelLayout.createSequentialGroup().addComponent(btnCheckout).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnRefresh).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnUpdate).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnMerge).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSwitch).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCleanup).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
           jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
           javax.swing.GroupLayout.PREFERRED_SIZE)));
    RepoActionPanelLayout.setVerticalGroup(
       RepoActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         RepoActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
           btnCleanup).addComponent(btnSwitch).addComponent(btnMerge).addComponent(btnUpdate).addComponent(
           btnRefresh).addComponent(btnCheckout).addComponent(jButton1)).addComponent(
             jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE));
    InfoPanel.setDividerLocation(250);
    InfoPanel.setDividerSize(7);
    InfoPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    InfoPanel.setResizeWeight(1.0);
    InfoPanel.setName("InfoPanel");                            // NOI18N
    InfoPanel.setOneTouchExpandable(true);
    jScrollPane1.setName("jScrollPane1");                      // NOI18N
    fileListing.setModel(model);
    fileListing.setComponentPopupMenu(actionPopup);
    fileListing.setName("fileListing");                        // NOI18N
    jScrollPane1.setViewportView(fileListing);
    InfoPanel.setLeftComponent(jScrollPane1);
    jScrollPane2.setName("jScrollPane2");                      // NOI18N
    messageTextPane.setComponentPopupMenu(messagePopup);
    messageTextPane.setName("messageTextPane");                // NOI18N
    jScrollPane2.setViewportView(messageTextPane);
    InfoPanel.setRightComponent(jScrollPane2);
    FileActionPanel.setName("FileActionPanel");                // NOI18N
    btnJIndent.setAction(actionMap.get("jindent"));            // NOI18N
    btnJIndent.setName("btnJIndent");                          // NOI18N
    btnDiff.setAction(actionMap.get("diff"));                  // NOI18N
    btnDiff.setName("btnDiff");                                // NOI18N
    btnGraph.setAction(actionMap.get("graph"));                // NOI18N
    btnGraph.setName("btnGraph");                              // NOI18N
    btnHistory.setAction(actionMap.get("history"));            // NOI18N
    btnHistory.setName("btnHistory");                          // NOI18N
    btnProperties.setAction(actionMap.get("properties"));      // NOI18N
    btnProperties.setName("btnProperties");                    // NOI18N
    btnResolve.setAction(actionMap.get("resolveConflict"));    // NOI18N
    btnResolve.setName("btnResolve");                          // NOI18N
    btnCommit.setAction(actionMap.get("commit"));              // NOI18N
    btnCommit.setName("btnCommit");                            // NOI18N
    btnAdd.setAction(actionMap.get("add"));                    // NOI18N
    btnAdd.setName("btnAdd");                                  // NOI18N
    btnRemove.setAction(actionMap.get("remove"));              // NOI18N
    btnRemove.setName("btnRemove");                            // NOI18N
    btnRevert.setAction(actionMap.get("revert"));              // NOI18N
    btnRevert.setName("btnRevert");                            // NOI18N
    btnQuit.setAction(actionMap.get("quit"));                  // NOI18N
    btnQuit.setName("btnQuit");                                // NOI18N
    btnAnnotate.setAction(actionMap.get("blame"));             // NOI18N
    btnAnnotate.setName("btnAnnotate");                        // NOI18N

    javax.swing.GroupLayout FileActionPanelLayout = new javax.swing.GroupLayout(FileActionPanel);

    FileActionPanel.setLayout(FileActionPanelLayout);
    FileActionPanelLayout.setHorizontalGroup(
       FileActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         FileActionPanelLayout.createSequentialGroup().addComponent(btnJIndent).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnDiff).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnResolve).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnGraph).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnHistory).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAnnotate).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnProperties).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCommit).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAdd).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnRemove).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnRevert).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE).addComponent(btnQuit)));
    FileActionPanelLayout.setVerticalGroup(
       FileActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         FileActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
           btnJIndent).addComponent(btnQuit).addComponent(btnDiff).addComponent(btnResolve).addComponent(
           btnGraph).addComponent(btnHistory).addComponent(btnProperties).addComponent(btnCommit).addComponent(
           btnAdd).addComponent(btnRemove).addComponent(btnRevert).addComponent(btnAnnotate)));
    WorkingCopySettingPanel.setName("WorkingCopySettingPanel");    // NOI18N
    lblRepoPath.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    lblRepoPath.setLabelFor(tfRepository);

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(
       org.sdrinovsky.sdsvn.SVNApp.class).getContext().getResourceMap(SVNAppView.class);

    lblRepoPath.setText(resourceMap.getString("lblRepoPath.text"));                            // NOI18N
    lblRepoPath.setToolTipText(resourceMap.getString("lblRepoPath.toolTipText"));              // NOI18N
    lblRepoPath.setName("lblRepoPath");                                                        // NOI18N
    tfRepository.setEditable(false);
    tfRepository.setName("tfRepository");                                                      // NOI18N
    lblWorkingFolder.setDisplayedMnemonic('W');
    lblWorkingFolder.setLabelFor(cbWorkingFolder);
    lblWorkingFolder.setText(resourceMap.getString("lblWorkingFolder.text"));                  // NOI18N
    lblWorkingFolder.setToolTipText(resourceMap.getString("lblWorkingFolder.toolTipText"));    // NOI18N
    lblWorkingFolder.setName("lblWorkingFolder");                                              // NOI18N
    cbWorkingFolder.setEditable(true);
    cbWorkingFolder.setName("cbWorkingFolder");                                                // NOI18N
    cbWorkingFolder.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbWorkingFolderActionPerformed(evt);
      }
    });
    btnBrowse.setAction(actionMap.get("showChooser"));    // NOI18N
    btnBrowse.setName("btnBrowse");                       // NOI18N

    javax.swing.GroupLayout WorkingCopySettingPanelLayout = new javax.swing.GroupLayout(WorkingCopySettingPanel);

    WorkingCopySettingPanel.setLayout(WorkingCopySettingPanelLayout);
    WorkingCopySettingPanelLayout.setHorizontalGroup(
       WorkingCopySettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         WorkingCopySettingPanelLayout.createSequentialGroup().addContainerGap().addGroup(
           WorkingCopySettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
             lblWorkingFolder).addComponent(lblRepoPath)).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
               WorkingCopySettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(
                 WorkingCopySettingPanelLayout.createSequentialGroup().addComponent(
                   cbWorkingFolder, 0, 944, Short.MAX_VALUE).addPreferredGap(
                   javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                   btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 79,
                   javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(
                     tfRepository, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE))));
    WorkingCopySettingPanelLayout.setVerticalGroup(
       WorkingCopySettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         WorkingCopySettingPanelLayout.createSequentialGroup().addGroup(
           WorkingCopySettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
             cbWorkingFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnBrowse).addComponent(
               lblWorkingFolder)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                 WorkingCopySettingPanelLayout.createParallelGroup(
                   javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                   tfRepository, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                   javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblRepoPath))));

    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);

    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(
       mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(
           mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
             mainPanelLayout.createSequentialGroup().addComponent(
               WorkingCopySettingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
               Short.MAX_VALUE).addGap(24, 24, 24)).addGroup(
                 mainPanelLayout.createSequentialGroup().addComponent(
                   RepoActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1174,
                   Short.MAX_VALUE).addContainerGap()).addGroup(
                     javax.swing.GroupLayout.Alignment.TRAILING,
                     mainPanelLayout.createSequentialGroup().addGroup(
                       mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
                         InfoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE,
                         1174, Short.MAX_VALUE).addComponent(
                           FileActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                           Short.MAX_VALUE)).addContainerGap()))));
    mainPanelLayout.setVerticalGroup(
       mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(
           WorkingCopySettingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
           javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
             RepoActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
             javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
               InfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
               FileActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
               javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    menuBar.setName("menuBar");                                                          // NOI18N
    fileMenu.setMnemonic('F');
    fileMenu.setText(resourceMap.getString("fileMenu.text"));                            // NOI18N
    fileMenu.setName("fileMenu");                                                        // NOI18N
    newProjectmenuItem.setAction(actionMap.get("newProject"));                           // NOI18N
    newProjectmenuItem.setName("newProjectmenuItem");                                    // NOI18N
    fileMenu.add(newProjectmenuItem);
    openProjectMenuItem.setAction(actionMap.get("openProject"));                         // NOI18N
    openProjectMenuItem.setName("openProjectMenuItem");                                  // NOI18N
    fileMenu.add(openProjectMenuItem);
    repoViewMenuItem.setAction(actionMap.get("repoview"));                               // NOI18N
    repoViewMenuItem.setName("repoViewMenuItem");                                        // NOI18N
    fileMenu.add(repoViewMenuItem);
    jSeparator8.setName("jSeparator8");                                                  // NOI18N
    fileMenu.add(jSeparator8);
    exitMenuItem.setAction(actionMap.get("quit"));                                       // NOI18N
    exitMenuItem.setText(resourceMap.getString("exitMenuItem.text"));                    // NOI18N
    exitMenuItem.setName("exitMenuItem");                                                // NOI18N
    fileMenu.add(exitMenuItem);
    menuBar.add(fileMenu);
    settingsMenu.setMnemonic('S');
    settingsMenu.setText(resourceMap.getString("settingsMenu.text"));                    // NOI18N
    settingsMenu.setName("settingsMenu");                                                // NOI18N
    loginMenuItem.setAction(actionMap.get("login"));                                     // NOI18N
    loginMenuItem.setName("loginMenuItem");                                              // NOI18N
    settingsMenu.add(loginMenuItem);
    jSeparator1.setName("jSeparator1");                                                  // NOI18N
    settingsMenu.add(jSeparator1);
    jindentProgramMenuItem.setAction(actionMap.get("jindentProgram"));                   // NOI18N
    jindentProgramMenuItem.setName("jindentProgramMenuItem");                            // NOI18N
    settingsMenu.add(jindentProgramMenuItem);
    jindentSettingsMenuItem.setAction(actionMap.get("jindentSettings"));                 // NOI18N
    jindentSettingsMenuItem.setName("jindentSettingsMenuItem");                          // NOI18N
    settingsMenu.add(jindentSettingsMenuItem);
    jSeparator9.setName("jSeparator9");                                                  // NOI18N
    settingsMenu.add(jSeparator9);
    diffCheckBoxMenuItem.setAction(actionMap.get("internalDiff"));                       // NOI18N
    diffCheckBoxMenuItem.setSelected(true);
    diffCheckBoxMenuItem.setText(resourceMap.getString("diffCheckBoxMenuItem.text"));    // NOI18N
    diffCheckBoxMenuItem.setName("diffCheckBoxMenuItem");                                // NOI18N
    settingsMenu.add(diffCheckBoxMenuItem);
    diffProgramMenuItem.setAction(actionMap.get("diffProgram"));                         // NOI18N
    diffProgramMenuItem.setName("diffProgramMenuItem");                                  // NOI18N
    settingsMenu.add(diffProgramMenuItem);
    mergeProgramMenuItem.setAction(actionMap.get("mergeProgram"));                       // NOI18N
    mergeProgramMenuItem.setName("mergeProgramMenuItem");                                // NOI18N
    settingsMenu.add(mergeProgramMenuItem);
    menuBar.add(settingsMenu);
    RepoMenu.setMnemonic('R');
    RepoMenu.setText(resourceMap.getString("RepoMenu.text"));                            // NOI18N
    RepoMenu.setName("RepoMenu");                                                        // NOI18N
    checkoutMenuItem.setAction(actionMap.get("checkout"));                               // NOI18N
    checkoutMenuItem.setName("checkoutMenuItem");                                        // NOI18N
    RepoMenu.add(checkoutMenuItem);
    refreshMenuItem.setAction(actionMap.get("refresh"));                                 // NOI18N
    refreshMenuItem.setName("refreshMenuItem");                                          // NOI18N
    RepoMenu.add(refreshMenuItem);
    updateMenuItem.setAction(actionMap.get("update"));                                   // NOI18N
    updateMenuItem.setName("updateMenuItem");                                            // NOI18N
    RepoMenu.add(updateMenuItem);
    mergeMenuItem.setAction(actionMap.get("mergeWorkingCopy"));                          // NOI18N
    mergeMenuItem.setName("mergeMenuItem");                                              // NOI18N
    RepoMenu.add(mergeMenuItem);
    branchMenuItem.setAction(actionMap.get("copyWorkingCopy"));                          // NOI18N
    branchMenuItem.setName("branchMenuItem");                                            // NOI18N
    RepoMenu.add(branchMenuItem);
    switchMenuItem.setAction(actionMap.get("switchWorkingCopy"));                        // NOI18N
    switchMenuItem.setName("switchMenuItem");                                            // NOI18N
    RepoMenu.add(switchMenuItem);
    CleanUpMenuItem.setAction(actionMap.get("cleanup"));                                 // NOI18N
    CleanUpMenuItem.setName("CleanUpMenuItem");                                          // NOI18N
    RepoMenu.add(CleanUpMenuItem);
    menuBar.add(RepoMenu);
    ModifyMenu.setMnemonic('M');
    ModifyMenu.setText(resourceMap.getString("ModifyMenu.text"));                        // NOI18N
    ModifyMenu.setName("ModifyMenu");                                                    // NOI18N
    JIndentMenuItem1.setAction(actionMap.get("jindent"));                                // NOI18N
    JIndentMenuItem1.setName("JIndentMenuItem1");                                        // NOI18N
    ModifyMenu.add(JIndentMenuItem1);
    CommitMenuItem.setAction(actionMap.get("commit"));                                   // NOI18N
    CommitMenuItem.setName("CommitMenuItem");                                            // NOI18N
    ModifyMenu.add(CommitMenuItem);
    AddMenuItem.setAction(actionMap.get("add"));                                         // NOI18N
    AddMenuItem.setName("AddMenuItem");                                                  // NOI18N
    ModifyMenu.add(AddMenuItem);
    RemoveMenuItem.setAction(actionMap.get("remove"));                                   // NOI18N
    RemoveMenuItem.setName("RemoveMenuItem");                                            // NOI18N
    ModifyMenu.add(RemoveMenuItem);
    RevertMenuItem.setAction(actionMap.get("revert"));                                   // NOI18N
    RevertMenuItem.setName("RevertMenuItem");                                            // NOI18N
    ModifyMenu.add(RevertMenuItem);
    UpdateREvisionMenuItem.setAction(actionMap.get("updateToRevision"));                 // NOI18N
    UpdateREvisionMenuItem.setName("UpdateREvisionMenuItem");                            // NOI18N
    ModifyMenu.add(UpdateREvisionMenuItem);
    menuBar.add(ModifyMenu);
    QueryMenu.setMnemonic('Q');
    QueryMenu.setText(resourceMap.getString("QueryMenu.text"));                          // NOI18N
    QueryMenu.setName("QueryMenu");                                                      // NOI18N
    DiffMenuItem.setAction(actionMap.get("diff"));                                       // NOI18N
    DiffMenuItem.setName("DiffMenuItem");                                                // NOI18N
    QueryMenu.add(DiffMenuItem);
    DiffRevisionMenuItem.setAction(actionMap.get("diffWithRevision"));                   // NOI18N
    DiffRevisionMenuItem.setName("DiffRevisionMenuItem");                                // NOI18N
    QueryMenu.add(DiffRevisionMenuItem);
    GraphMenuItem.setAction(actionMap.get("graph"));                                     // NOI18N
    GraphMenuItem.setName("GraphMenuItem");                                              // NOI18N
    QueryMenu.add(GraphMenuItem);
    HistoryMenuItem.setAction(actionMap.get("history"));                                 // NOI18N
    HistoryMenuItem.setName("HistoryMenuItem");                                          // NOI18N
    QueryMenu.add(HistoryMenuItem);
    AnnotateMenuItem.setAction(actionMap.get("blame"));                                  // NOI18N
    AnnotateMenuItem.setName("AnnotateMenuItem");                                        // NOI18N
    QueryMenu.add(AnnotateMenuItem);
    PropertiesMenuItem.setAction(actionMap.get("properties"));                           // NOI18N
    PropertiesMenuItem.setName("PropertiesMenuItem");                                    // NOI18N
    QueryMenu.add(PropertiesMenuItem);
    menuBar.add(QueryMenu);
    helpMenu.setMnemonic('H');
    helpMenu.setText(resourceMap.getString("helpMenu.text"));                            // NOI18N
    helpMenu.setName("helpMenu");                                                        // NOI18N
    aboutMenuItem.setAction(actionMap.get("showAboutBox"));                              // NOI18N
    aboutMenuItem.setName("aboutMenuItem");                                              // NOI18N
    helpMenu.add(aboutMenuItem);
    jnlpUpdatemenuItem.setAction(actionMap.get("jnlpUpdate"));                           // NOI18N
    jnlpUpdatemenuItem.setName("jnlpUpdatemenuItem");                                    // NOI18N
    helpMenu.add(jnlpUpdatemenuItem);
    menuBar.add(helpMenu);
    statusPanel.setName("statusPanel");                                                  // NOI18N
    statusPanel.setPreferredSize(new java.awt.Dimension(0, 28));
    statusPanelSeparator.setName("statusPanelSeparator");                                // NOI18N
    statusMessageLabel.setName("statusMessageLabel");                                    // NOI18N
    statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    statusAnimationLabel.setName("statusAnimationLabel");                                // NOI18N
    progressBar.setMinimumSize(new java.awt.Dimension(146, 19));
    progressBar.setName("progressBar");                                                  // NOI18N
    progressBar.setStringPainted(true);

    javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);

    statusPanel.setLayout(statusPanelLayout);
    statusPanelLayout.setHorizontalGroup(
       statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
         statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE).addGroup(
         statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel).addPreferredGap(
           javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1014, Short.MAX_VALUE).addComponent(
           progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
           javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(statusAnimationLabel).addContainerGap()));
    statusPanelLayout.setVerticalGroup(
       statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
         statusPanelLayout.createSequentialGroup().addComponent(
           statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2,
           javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE,
             Short.MAX_VALUE).addGroup(
               statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                 statusMessageLabel).addComponent(statusAnimationLabel).addComponent(
                 progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                 javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(3, 3, 3)));
    actionPopup.setName("actionPopup");                                                      // NOI18N
    jindentMenuItem.setAction(actionMap.get("jindent"));                                     // NOI18N
    jindentMenuItem.setName("jindentMenuItem");                                              // NOI18N
    actionPopup.add(jindentMenuItem);
    diffMenuItem.setAction(actionMap.get("diff"));                                           // NOI18N
    diffMenuItem.setName("diffMenuItem");                                                    // NOI18N
    actionPopup.add(diffMenuItem);
    diffRevisionMenuItem.setAction(actionMap.get("diffWithRevision"));                       // NOI18N
    diffRevisionMenuItem.setName("diffRevisionMenuItem");                                    // NOI18N
    actionPopup.add(diffRevisionMenuItem);
    diffSelectedFilesMenuItem.setAction(actionMap.get("diffSelectedFiles"));                 // NOI18N
    diffSelectedFilesMenuItem.setName("diffSelectedFilesMenuItem");                          // NOI18N
    actionPopup.add(diffSelectedFilesMenuItem);
    graphMenuItem.setAction(actionMap.get("graph"));                                         // NOI18N
    graphMenuItem.setName("graphMenuItem");                                                  // NOI18N
    actionPopup.add(graphMenuItem);
    historyMenuItem.setAction(actionMap.get("history"));                                     // NOI18N
    historyMenuItem.setName("historyMenuItem");                                              // NOI18N
    actionPopup.add(historyMenuItem);
    annotateMenuItem.setAction(actionMap.get("blame"));                                      // NOI18N
    annotateMenuItem.setName("annotateMenuItem");                                            // NOI18N
    actionPopup.add(annotateMenuItem);
    propertiesMenuItem.setAction(actionMap.get("properties"));                               // NOI18N
    propertiesMenuItem.setName("propertiesMenuItem");                                        // NOI18N
    actionPopup.add(propertiesMenuItem);
    resolveMenuItem.setAction(actionMap.get("resolveConflict"));                             // NOI18N
    resolveMenuItem.setName("resolveMenuItem");                                              // NOI18N
    actionPopup.add(resolveMenuItem);
    jSeparator5.setName("jSeparator5");                                                      // NOI18N
    actionPopup.add(jSeparator5);
    commitMenuItem.setAction(actionMap.get("commit"));                                       // NOI18N
    commitMenuItem.setName("commitMenuItem");                                                // NOI18N
    actionPopup.add(commitMenuItem);
    addMenuItem.setAction(actionMap.get("add"));                                             // NOI18N
    addMenuItem.setName("addMenuItem");                                                      // NOI18N
    actionPopup.add(addMenuItem);
    removeMenuItem.setAction(actionMap.get("remove"));                                       // NOI18N
    removeMenuItem.setName("removeMenuItem");                                                // NOI18N
    actionPopup.add(removeMenuItem);
    revertMenuItem.setAction(actionMap.get("revert"));                                       // NOI18N
    revertMenuItem.setName("revertMenuItem");                                                // NOI18N
    actionPopup.add(revertMenuItem);
    updateRevisionMenuItem.setAction(actionMap.get("updateToRevision"));                     // NOI18N
    updateRevisionMenuItem.setName("updateRevisionMenuItem");                                // NOI18N
    actionPopup.add(updateRevisionMenuItem);
    messagePopup.setName("messagePopup");                                                    // NOI18N
    copyMenuItem.setAction(actionMap.get("copy"));                                           // NOI18N
    copyMenuItem.setText(resourceMap.getString("copyMenuItem.text"));                        // NOI18N
    copyMenuItem.setToolTipText(resourceMap.getString("copyMenuItem.toolTipText"));          // NOI18N
    copyMenuItem.setName("copyMenuItem");                                                    // NOI18N
    messagePopup.add(copyMenuItem);
    clearMenuItem.setAction(actionMap.get("clear"));                                         // NOI18N
    clearMenuItem.setText(resourceMap.getString("clearMenuItem.text"));                      // NOI18N
    clearMenuItem.setToolTipText(resourceMap.getString("clearMenuItem.toolTipText"));        // NOI18N
    clearMenuItem.setActionCommand(resourceMap.getString("clearMenuItem.actionCommand"));    // NOI18N
    clearMenuItem.setName("clearMenuItem");                                                  // NOI18N
    messagePopup.add(clearMenuItem);
    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }                                                                                          // </editor-fold>//GEN-END:initComponents

  private void cbWorkingFolderActionPerformed(java.awt.event.ActionEvent evt) {              //GEN-FIRST:event_cbWorkingFolderActionPerformed
    doRefresh(false);
  }                                                                            //GEN-LAST:event_cbWorkingFolderActionPerformed

  private void tableFilterActionPerformed(java.awt.event.ActionEvent evt) {    //GEN-FIRST:event_tableFilterActionPerformed
    updateToolBar();
  }    //GEN-LAST:event_tableFilterActionPerformed

  /**
   * Method doRefresh
   *
   *
   *
   * @param wait
   */
  public void doRefresh(boolean wait) {
    doRefresh(null, wait, showUpdated);
  }

  /**
   * Method doRefresh
   *
   *
   * @param message
   * @param wait
   * @param updated
   *
   */
  public void doRefresh(String message, boolean wait, boolean updated) {
    final Task refresh = refresh(message, updated);

    if(refresh != null) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(250);
          } catch(InterruptedException ex) {
            Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
          }

          svnApp.getContext().getTaskService().execute(refresh);
        }
      };

      if(wait) {
        try {
          SwingUtilities.invokeAndWait(runnable);
        } catch(InterruptedException ex) {
        } catch(InvocationTargetException ex) {
        }
      } else {
        SwingUtilities.invokeLater(runnable);
      }
    }
  }

  private boolean loggedIn = false;

  /**
   * Method isLoggedIn
   *
   *
   * @return
   *
   */
  public boolean isLoggedIn() {
    return loggedIn;
  }

  /**
   * Method setLoggedIn
   *
   *
   * @param b
   *
   */
  public void setLoggedIn(boolean b) {
    boolean old = isLoggedIn();

    if(old != b) {
      this.loggedIn = b;

      firePropertyChange("loggedIn", old, isLoggedIn());
    }

    loginMenuItem.setToolTipText("Currently logged in as " + svnApp.getPreferences().get("username", "no one"));
  }

  private boolean tablePopulated = false;

  /**
   * Method isTablePopulated
   *
   *
   * @return
   *
   */
  public boolean isTablePopulated() {
    return tablePopulated;
  }

  /**
   * Method setTablePopulated
   *
   *
   * @param b
   *
   */
  public void setTablePopulated(boolean b) {
    boolean old = isTablePopulated();

    if(old != b) {
      this.tablePopulated = b;

      firePropertyChange("tablePopulated", old, isTablePopulated());
    }
  }

  private boolean rowSelected        = false;
  private boolean singleRowSelected  = false;
  private boolean twoRowsSelected    = false;
  private boolean commitableSelected = false;
  private boolean revertableSelected = false;
  private boolean conflictSelected   = false;
  private boolean addableSelected    = false;

  /**
   * Method isRowSelected
   *
   *
   * @return
   *
   */
  public boolean isRowSelected() {
    return rowSelected;
  }

  /**
   * Method isRowSelected
   *
   *
   * @return
   *
   */
  public boolean isSingleRowSelected() {
    return singleRowSelected;
  }

  /**
   * Method isTwoRowsSelected
   *
   *
   * @return
   *
   */
  public boolean isTwoRowsSelected() {
    return twoRowsSelected;
  }

  /**
   * Method isCommitableSelected
   *
   *
   * @return
   *
   */
  public boolean isCommitableSelected() {
    return commitableSelected;
  }

  /**
   * Method isCommitableSelected
   *
   *
   * @return
   *
   */
  public boolean isRevertableSelected() {
    return revertableSelected;
  }

  /**
   * Method isConflictSelected
   *
   *
   * @return
   *
   */
  public boolean isConflictSelected() {
    return conflictSelected;
  }

  /**
   * Method isAddableSelected
   *
   *
   * @return
   *
   */
  public boolean isAddableSelected() {
    return addableSelected;
  }

  /**
   * Method resetButtons
   *
   *
   */
  public void resetButtons() {
    boolean wasSelected = rowSelected;

    rowSelected       = false;
    singleRowSelected = false;
    twoRowsSelected   = false;

    setRowSelected(wasSelected);
    jindentProgramMenuItem.setToolTipText("Current JIntend program is " +
                                          svnApp.getPreferences().get("jindentProgram", "not set"));
    jindentSettingsMenuItem.setToolTipText("Current Settings file is " +
                                           svnApp.getPreferences().get("jindentSettings", "not set"));
    diffProgramMenuItem.setToolTipText("Current Diff program is " +
                                       svnApp.getPreferences().get("diffProgram", "not set"));
    mergeProgramMenuItem.setToolTipText("Current Merge program is " +
                                        svnApp.getPreferences().get("mergeProgram", "not set"));
    diffCheckBoxMenuItem.setSelected(svnApp.getPreferences().getBoolean("internalDiff", false));
    setLoggedIn(svnApp.getPreferences().get("username", null) != null);
  }

  /**
   * Method setRowSelected
   *
   *
   * @param b
   *
   */
  public void setRowSelected(boolean b) {
    statusMessageLabel.setText("Selected " + fileListing.getSelectedRowCount() + " files");
    messageTimer.restart();

    int     numberSelectedRows = fileListing.getSelectedRowCount();
    boolean singleOld          = isSingleRowSelected();

    if(singleOld != (1 == numberSelectedRows)) {
      singleRowSelected = (1 == numberSelectedRows);

      firePropertyChange("singleRowSelected", singleOld, singleRowSelected);
    }

    boolean twoOld = isTwoRowsSelected();

    if(twoOld != (2 == numberSelectedRows)) {
      twoRowsSelected = (2 == numberSelectedRows);

      firePropertyChange("twoRowsSelected", twoOld, twoRowsSelected);
    }

    boolean old = isRowSelected();

    if(old != b) {
      boolean oldJIndentEnabled = (svnApp.getPreferences().get("jindentProgram", null) != null) &&
                                  (svnApp.getPreferences().get("jindentSettings", null) != null);
      boolean oldDiffEnabled  = isInternalDiffAvailalbe() || (svnApp.getPreferences().get("diffProgram", null) != null);
      boolean oldGraphEnabled = GraphTask.isGraphViewAvailable();

      rowSelected = b;

      firePropertyChange("rowSelected", old, isRowSelected());
      firePropertyChange("jindentEnabled", old && oldJIndentEnabled, rowSelected && oldJIndentEnabled);
      firePropertyChange("diffEnabled", old && oldDiffEnabled, rowSelected && oldDiffEnabled);
      firePropertyChange("graphEnabled", old && oldGraphEnabled, singleRowSelected && oldGraphEnabled);
    }

    boolean commitable = false;
    boolean revertable = false;
    boolean conflict   = false;

    for(int row : fileListing.getSelectedRows()) {
      int          realRow = fileListing.getRowSorter().convertRowIndexToModel(row);
      FileTableRow ftr     = model.getRow(realRow);

      if(ftr.isCommitable()) {
        if(ftr.isConflict()) {
          conflict = true;
        }

        commitable = true;
        revertable = true;

        break;
      } else if(ftr.isMissing()) {
        revertable = true;
      }
    }

    if(commitable != commitableSelected) {
      commitableSelected = commitable;

      firePropertyChange("commitableSelected", !commitable, commitable);
    }

    if(revertable != revertableSelected) {
      revertableSelected = revertable;

      firePropertyChange("revertableSelected", !revertable, revertable);
    }

    if(conflict != conflictSelected) {
      conflictSelected = conflict;

      firePropertyChange("conflictSelected", !conflict, conflict);
    }

    boolean addable = false;

    for(int row : fileListing.getSelectedRows()) {
      int realRow = fileListing.getRowSorter().convertRowIndexToModel(row);

      if(model.getRow(realRow).isAddable()) {
        addable = true;

        break;
      }
    }

    if(addable != addableSelected) {
      addableSelected = addable;

      firePropertyChange("addableSelected", !addable, addable);
    }
  }

  /**
   * Method isJindentEnabled
   *
   *
   * @return
   *
   */
  public boolean isJindentEnabled() {
    return rowSelected && (svnApp.getPreferences().get("jindentProgram", null) != null);
  }

  /**
   * Method isDiffEnabled
   *
   *
   * @return
   *
   */
  public boolean isDiffEnabled() {
    return rowSelected && (isInternalDiffAvailalbe() || (svnApp.getPreferences().get("diffProgram", null) != null));
  }

  /**
   * Method isDiffEnabled
   *
   *
   * @return
   *
   */
  public boolean isGraphEnabled() {
    return rowSelected && GraphTask.isGraphViewAvailable();
  }

  /**
   * Method isRepoEmpty
   *
   *
   * @return
   *
   */
  public boolean isRepoEmpty() {
    String repo = tfRepository.getText();

    return(repo == null) || (repo.length() == 0);
  }

  /**
   * Method isRepoSet
   *
   *
   * @return
   *
   */
  public boolean isRepoSet() {
    String repo = tfRepository.getText();

    return(repo != null) && (repo.length() > 0);
  }

  /**
   * Method setRepo
   *
   *
   * @param string
   * @param version
   * @param uuid
   *
   */
  public void setRepo(String string, long version, String uuid) {
    String oldString = tfRepository.getText();
    String newString = string;

    if(version > 0) {
      newString += "@" + version;
    }

    if( !oldString.equals(newString)) {
      System.out.println("setting repo: " + newString);

      boolean oldValue = (oldString != null) && (oldString.length() > 0);
      boolean newValue = (newString != null) && (newString.length() > 0);

      svnApp.setPreferences(string, uuid);
      tfRepository.setText(newString);
      firePropertyChange("repoSet", oldValue, newValue);
      firePropertyChange("repoEmpty", !oldValue, !newValue);
      resetButtons();
    }
  }

  private boolean jnlpEnabled = false;

  /**
   * Method isJnlpEnabled
   *
   *
   * @return
   *
   */
  public boolean isJnlpEnabled() {
    return jnlpEnabled;
  }

  /**
   * Method setJnlpEnabled
   *
   *
   * @param b
   *
   */
  public void setJnlpEnabled(boolean b) {
    boolean old = isJnlpEnabled();

    this.jnlpEnabled = b;

    firePropertyChange("jnlpEnabled", old, isJnlpEnabled());
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem          AddMenuItem;
  private javax.swing.JMenuItem          AnnotateMenuItem;
  private javax.swing.JMenuItem          CleanUpMenuItem;
  private javax.swing.JMenuItem          CommitMenuItem;
  private javax.swing.JMenuItem          DiffMenuItem;
  private javax.swing.JMenuItem          DiffRevisionMenuItem;
  private javax.swing.JPanel             FileActionPanel;
  private javax.swing.JMenuItem          GraphMenuItem;
  private javax.swing.JMenuItem          HistoryMenuItem;
  private javax.swing.JSplitPane         InfoPanel;
  private javax.swing.JMenuItem          JIndentMenuItem1;
  private javax.swing.JMenu              ModifyMenu;
  private javax.swing.JMenuItem          PropertiesMenuItem;
  private javax.swing.JMenu              QueryMenu;
  private javax.swing.JMenuItem          RemoveMenuItem;
  private javax.swing.JPanel             RepoActionPanel;
  private javax.swing.JMenu              RepoMenu;
  private javax.swing.JMenuItem          RevertMenuItem;
  private javax.swing.JMenuItem          UpdateREvisionMenuItem;
  private javax.swing.JPanel             WorkingCopySettingPanel;
  private javax.swing.JPopupMenu         actionPopup;
  private javax.swing.JMenuItem          addMenuItem;
  private javax.swing.JMenuItem          annotateMenuItem;
  private javax.swing.JMenuItem          branchMenuItem;
  private javax.swing.JButton            btnAdd;
  private javax.swing.JButton            btnAnnotate;
  private javax.swing.JButton            btnBrowse;
  private javax.swing.JButton            btnCheckout;
  private javax.swing.JButton            btnCleanup;
  private javax.swing.JButton            btnCommit;
  private javax.swing.JButton            btnDiff;
  private javax.swing.JButton            btnGraph;
  private javax.swing.JButton            btnHistory;
  private javax.swing.JButton            btnJIndent;
  private javax.swing.JButton            btnMerge;
  private javax.swing.JButton            btnProperties;
  private javax.swing.JButton            btnQuit;
  private javax.swing.JButton            btnRefresh;
  private javax.swing.JButton            btnRemove;
  private javax.swing.JButton            btnResolve;
  private javax.swing.JButton            btnRevert;
  private javax.swing.JButton            btnSwitch;
  private javax.swing.JButton            btnUpdate;
  private javax.swing.JComboBox          cbWorkingFolder;
  private javax.swing.JMenuItem          checkoutMenuItem;
  private javax.swing.JMenuItem          clearMenuItem;
  private javax.swing.JMenuItem          commitMenuItem;
  private javax.swing.JCheckBoxMenuItem  diffCheckBoxMenuItem;
  private javax.swing.JMenuItem          copyMenuItem;
  private javax.swing.JMenuItem          diffMenuItem;
  private javax.swing.JMenuItem          diffProgramMenuItem;
  private javax.swing.JMenuItem          diffRevisionMenuItem;
  private javax.swing.JMenuItem          diffSelectedFilesMenuItem;
  private javax.swing.JTable             fileListing;
  private javax.swing.JMenuItem          graphMenuItem;
  private javax.swing.JMenuItem          historyMenuItem;
  private javax.swing.JButton            jButton1;
  private javax.swing.JScrollPane        jScrollPane1;
  private javax.swing.JScrollPane        jScrollPane2;
  private javax.swing.JSeparator         jSeparator1;
  private javax.swing.JToolBar.Separator jSeparator2;
  private javax.swing.JToolBar.Separator jSeparator3;
  private javax.swing.JToolBar.Separator jSeparator4;
  private javax.swing.JSeparator         jSeparator5;
  private javax.swing.JToolBar.Separator jSeparator6;
  private javax.swing.JToolBar.Separator jSeparator7;
  private javax.swing.JSeparator         jSeparator8;
  private javax.swing.JSeparator         jSeparator9;
  private javax.swing.JToolBar           jToolBar1;
  private javax.swing.JMenuItem          jindentMenuItem;
  private javax.swing.JMenuItem          jindentProgramMenuItem;
  private javax.swing.JMenuItem          jindentSettingsMenuItem;
  private javax.swing.JMenuItem          jnlpUpdatemenuItem;
  private javax.swing.JLabel             lblRepoPath;
  private javax.swing.JLabel             lblWorkingFolder;
  private javax.swing.JMenuItem          loginMenuItem;
  private javax.swing.JPanel             mainPanel;
  private javax.swing.JMenuBar           menuBar;
  private javax.swing.JMenuItem          mergeMenuItem;
  private javax.swing.JMenuItem          mergeProgramMenuItem;
  private javax.swing.JPopupMenu         messagePopup;
  private javax.swing.JTextPane          messageTextPane;
  private javax.swing.JMenuItem          newProjectmenuItem;
  private javax.swing.JMenuItem          openProjectMenuItem;
  private javax.swing.JProgressBar       progressBar;
  private javax.swing.JMenuItem          propertiesMenuItem;
  private javax.swing.JMenuItem          refreshMenuItem;
  private javax.swing.JMenuItem          removeMenuItem;
  private javax.swing.JMenuItem          repoViewMenuItem;
  private javax.swing.JMenuItem          resolveMenuItem;
  private javax.swing.JMenuItem          revertMenuItem;
  private javax.swing.JMenu              settingsMenu;
  private javax.swing.JToggleButton      showBranchesToggleButton;
  private javax.swing.JLabel             statusAnimationLabel;
  private javax.swing.JLabel             statusMessageLabel;
  private javax.swing.JPanel             statusPanel;
  private javax.swing.JMenuItem          switchMenuItem;
  private javax.swing.JTextField         tableFilter;
  private javax.swing.JToggleButton      tbAdded;
  private javax.swing.JToggleButton      tbChanged;
  private javax.swing.JToggleButton      tbConflicts;
  private javax.swing.JToggleButton      tbHideMissing;
  private javax.swing.JToggleButton      tbHideUnknown;
  private javax.swing.JToggleButton      tbModified;
  private javax.swing.JToggleButton      tbRemoved;
  private javax.swing.JToggleButton      tbShowIgnored;
  private javax.swing.JToggleButton      tbShowMissing;
  private javax.swing.JToggleButton      tbShowUnknown;
  private javax.swing.JToggleButton      tbUpdated;
  private javax.swing.JTextField         tfRepository;
  private javax.swing.JMenuItem          updateMenuItem;
  private javax.swing.JMenuItem          updateRevisionMenuItem;
  // End of variables declaration//GEN-END:variables
  private final Timer  messageTimer;
  private final Timer  busyIconTimer;
  private final Icon   idleIcon;
  private final Icon[] busyIcons     = new Icon[15];
  private int          busyIconIndex = 0;
  private JDialog      aboutBox;
  private JFileChooser chooser;

  /**
   * Method shortenFileName
   *
   *
   * @param name
   * @param width
   *
   * @return
   *
   */
  public static String shortenFileName(String name, int width) {
    if(name.length() > width) {
      String[] split = name.replace('\\', '/').split("/");

      name = "";

      String prefix = "";
      String tmp    = "";
      int    i      = 0;
      int    j      = split.length - 1;

      for(int k = 1; tmp.length() + name.length() + prefix.length() < width; k++) {
        if(k % 2 == 0) {
          name = tmp + name;
          tmp  = split[i++] + '/';
        } else {
          prefix += tmp;
          tmp    = '/' + split[j--];
        }
      }

      name = prefix + "..." + name;
    }

    return name;
  }

  private void updateToolBar() {
    tbModified.setSelected(showChanged && showAdded && showRemoved && showConflicts);
    tbChanged.setSelected(showChanged);
    tbUpdated.setSelected(showUpdated);
    tbAdded.setSelected(showAdded);
    tbRemoved.setSelected(showRemoved);
    tbConflicts.setSelected(showConflicts);
    tbShowMissing.setSelected(showMissing);
    tbHideMissing.setSelected(hideMissing);
    tbShowUnknown.setSelected(showUnknown);
    tbHideUnknown.setSelected(hideUnknown);
    tbShowIgnored.setSelected(showIgnored);
    tbHideMissing.setEnabled( !(showChanged || showAdded || showRemoved || showConflicts));
    tbHideUnknown.setEnabled( !(showChanged || showAdded || showRemoved || showConflicts || showMissing));

    Pattern pattern = Pattern.compile(tableFilter.getText(), Pattern.CASE_INSENSITIVE);

    filterMatches = pattern.matcher("");

    fileListing.getRowSorter().allRowsChanged();
  }

  /**
   * Method showModified
   *
   *
   */
  @Action
  public void showModified() {
    boolean set = tbModified.isSelected();

    showChanged   = set;
    showAdded     = set;
    showRemoved   = set;
    showConflicts = set;

    updateToolBar();
  }

  /**
   * Method showChanged
   *
   *
   */
  @Action
  public void showChanged() {
    showChanged = tbChanged.isSelected();

    updateToolBar();
  }

  /**
   * Method showUpdated
   *
   *
   */
  @Action
  public void showUpdated() {
    showUpdated = tbUpdated.isSelected();

    updateToolBar();
    doRefresh(false);
  }

  /**
   * Method showAdded
   *
   *
   */
  @Action
  public void showAdded() {
    showAdded = tbAdded.isSelected();

    updateToolBar();
  }

  /**
   * Method showRemoved
   *
   *
   */
  @Action
  public void showRemoved() {
    showRemoved = tbRemoved.isSelected();

    updateToolBar();
  }

  /**
   * Method showConflicts
   *
   *
   */
  @Action
  public void showConflicts() {
    showConflicts = tbConflicts.isSelected();

    updateToolBar();
  }

  /**
   * Method showMissing
   *
   *
   */
  @Action
  public void showMissing() {
    if(tbShowMissing.isSelected()) {
      showMissing = true;
      hideMissing = false;
    } else {
      showMissing = false;
    }

    updateToolBar();
  }

  /**
   * Method hideMissing
   *
   *
   */
  @Action
  public void hideMissing() {
    if(tbHideMissing.isSelected()) {
      showMissing = false;
      hideMissing = true;
    } else {
      hideMissing = false;
    }

    updateToolBar();
  }

  /**
   * Method showUnknown
   *
   *
   */
  @Action
  public void showUnknown() {
    if(tbShowUnknown.isSelected()) {
      showUnknown = true;
      hideUnknown = false;
    } else {
      showUnknown = false;
    }

    updateToolBar();
  }

  /**
   * Method hideUnknown
   *
   *
   */
  @Action
  public void hideUnknown() {
    if(tbHideUnknown.isSelected()) {
      showUnknown = false;
      hideUnknown = true;
    } else {
      hideUnknown = false;
    }

    updateToolBar();
  }

  /**
   * Method showIgnored
   *
   *
   */
  @Action
  public void showIgnored() {
    showIgnored = tbShowIgnored.isSelected();

    updateToolBar();
  }

  /**
   * Method showBranchesInGraph
   *
   *
   */
  @Action
  public void showBranchesInGraph() {}

  /**
   * Method remove
   *
   *
   */
  @Action(enabledProperty = "rowSelected")
  public void remove() {
    List<FileTableRow> files = getSelectRows();

    if(askConfirm("Confirm Remove", "remove", files)) {
      for(FileTableRow row : files) {
        File file = row.getFile();

        if(file.isFile() && row.isCommitable()) {
          // the file is commitable, make a backup of the changes.
          boolean backup = true;

          try {
            backup = !(svnApp.getSVNClientManager().getStatusClient().doStatus(file, false).getNodeStatus() ==
                       SVNStatusType.STATUS_ADDED);
          } catch(SVNException se) {
          }

          if(backup) {
            File backFile = new File(file.getParentFile(), file.getName() + "~");

            if(backFile.exists()) {
              backFile.delete();
            }

            file.renameTo(backFile);
          }
        }

        try {
          svnApp.getSVNClientManager().getWCClient().doDelete(file, true, false);
        } catch(SVNException ex) {
          Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      doRefresh("Remove finished", false, showUpdated);
    }
  }

  /**
   * Method history
   *
   *
   *
   * @return
   */
  @Action(enabledProperty = "singleRowSelected")
  public Task history() {
    return new HistoryTask(svnApp, getSelectRows().get(0).getLocation(), getSelectRows().get(0));
  }

  /**
   * Method properties
   *
   *
   *
   * @return
   */
  @Action(enabledProperty = "singleRowSelected")
  public Task properties() {
    return new PropertiesTask(svnApp, getSelectRows().get(0));
  }

  SVNRevision lastRevision = null;

  private SVNRevision askForRevision() {
    String      revision = JOptionPane.showInputDialog(svnApp.getMainFrame(), "Revision Number", lastRevision);
    SVNRevision rev      = SVNRevision.parse(revision);

    if(rev != SVNRevision.UNDEFINED) {
      lastRevision = rev;

      return rev;
    } else if(revision != null) {
      JOptionPane.showMessageDialog(svnApp.getMainFrame(), "Revision " + revision + " is not valid.",
                                    "Invalid Revision", JOptionPane.ERROR_MESSAGE);
    }

    return null;
  }

  /**
   * Method updateToRevision
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "rowSelected")
  public Task updateToRevision() {
    SVNRevision revision = askForRevision();

    if(revision != null) {
      return new UpdateTask(svnApp, getSelectRows(), revision);
    }

    return null;
  }

  /**
   * Method diffWithRevision
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "rowSelected")
  public Task diffWithRevision() {
    SVNRevision revision = askForRevision();

    if(revision != null) {
      return new DiffTask(svnApp, getSelectRows(), revision);
    }

    return null;
  }

  /**
   * Method diffSelectedFiles
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "twoRowsSelected")
  public Task diffSelectedFiles() {
    return new DiffTask(svnApp, getSelectRows(), true);
  }

  String lastBranchName = null;

  /**
   * Method copyWorkingCopy
   *
   *
   * @return
   *
   */
  @Action(block = Task.BlockingScope.APPLICATION, enabledProperty = "repoSet")
  public Task copyWorkingCopy() {
    String workingFolder = getWorkingFolder();

    if(workingFolder != null) {
      String branchName = JOptionPane.showInputDialog(svnApp.getMainFrame(), "Branch Name", lastBranchName);

      if(branchName != null) {
        lastBranchName = branchName;

        try {
          File   source = new File(workingFolder);
          SVNURL destination;

          destination = getRootURL().appendPath("branches", false).appendPath(branchName, false);

          return new BranchTask(svnApp, source, destination);
        } catch(SVNException ex) {
          Logger.getLogger(SVNAppView.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return null;
  }

  /**
   * Method resolveConflict
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "conflictSelected")
  public Task resolveConflict() {
    List<FileTableRow> files = getCommitableFiles();

    return new ResolveTask(svnApp, files);
  }

  /**
   * Method repoview
   *
   *
   */
  @Action(enabledProperty = "repoSet")
  public void repoview() {
    showRepoView(false, true);
    //return new RepoviewTask(getApplication());
  }

  private void updateTextPane(final String text, final AttributeSet attrs) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        Document doc = messageTextPane.getDocument();

        try {
          doc.insertString(doc.getLength(), text, attrs);
        } catch(BadLocationException e) {
        }

        messageTextPane.setCaretPosition(doc.getLength() - 1);
      }
    });
  }

  class ColoredOutputStream extends OutputStream {
    SimpleAttributeSet attributes;

    /**
     * Constructor ColoredOutputStream
     *
     *
     * @param color
     *
     */
    public ColoredOutputStream(Color color) {
      attributes = new SimpleAttributeSet();

      StyleConstants.setForeground(attributes, color);
    }

    /**
     * Method write
     *
     *
     * @param b
     *
     * @throws IOException
     *
     */
    @Override
    public void write(int b) throws IOException {
      updateTextPane(String.valueOf((char)b), attributes);
    }

    /**
     * Method write
     *
     *
     * @param b
     * @param off
     * @param len
     *
     * @throws IOException
     *
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      updateTextPane(new String(b, off, len), attributes);
    }

    /**
     * Method write
     *
     *
     * @param b
     *
     * @throws IOException
     *
     */
    @Override
    public void write(byte[] b) throws IOException {
      write(b, 0, b.length);
    }
  }

  private void redirectSystemStreams() {
    System.setOut(new PrintStream(new ColoredOutputStream(Color.black), true));
    System.setErr(new PrintStream(new ColoredOutputStream(Color.red), true));
  }

  /**
   * Method newProject
   *
   *
   */
  @Action
  public void newProject() {
    showChooser("New Project Folder");
  }

  /**
   * Method openProject
   *
   *
   */
  @Action
  public void openProject() {
    showChooser("Open Project Folder");
  }

  /**
   * Method jnlpUpdate
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "jnlpEnabled")
  public Task jnlpUpdate() {
    return new UpdateAppTask(getApplication());
  }

  /**
   * Method blame
   *
   *
   * @return
   *
   */
  @Action(enabledProperty = "singleRowSelected")
  public Task blame() {
    FileTableRow row = getSelectRows().get(0);

    return new AnnotateTask(svnApp, row);
  }

  /**
   * Method copy
   *
   *
   */
  @Action
  public void copy() {
    String          selection = messageTextPane.getSelectedText();
    StringSelection data      = new StringSelection(selection);
    Clipboard       clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    clipboard.setContents(data, data);
  }

  /**
   * Method clear
   *
   *
   */
  @Action()
  public void clear() {
    messageTextPane.setText("");
    System.gc();
  }
}

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

package org.sdrinovsky.sdsvn.tasks;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.jnlp.BasicService;
import javax.jnlp.DownloadService;
import javax.jnlp.DownloadService2;
import javax.jnlp.DownloadService2.ResourceSpec;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import javax.swing.JOptionPane;

import org.sdrinovsky.sdsvn.diff.DiffView;

/**
 *
 * @author sdrinovsky
 */
public class UpdateAppTask extends org.jdesktop.application.Task<Integer, Void> {
  private int          totalUpdates  = 0;
  private int          currentUpdate = 0;
  private BasicService bs;
  private String       loc;
  private URL          jnlp;

  /**
   * Constructor UpdateAppTask
   *
   *
   * @param app
   *
   */
  public UpdateAppTask(org.jdesktop.application.Application app) {
    super(app);
  }

  @Override
  protected Integer doInBackground() throws UnavailableServiceException, IOException {
    bs  = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
    loc = bs.getCodeBase().toString() + ".*";

    DownloadService2 ds2  = (DownloadService2)ServiceManager.lookup("javax.jnlp.DownloadService2");
    ResourceSpec     spec = new ResourceSpec(loc, null, ds2.ALL);

    setMessage("doing update check...");

    final List<URL> updates   = new ArrayList();
    ResourceSpec[]  resources = ds2.getCachedResources(spec);
    int             counter   = 1;

    totalUpdates = 100;

    for(ResourceSpec rs : resources) {
      setProgress(counter++, 0, resources.length);

      URL url = new URL(rs.getUrl());

      if(rs.getUrl().endsWith("jnlp")) {
        jnlp = url;
      }

      System.out.print("checking " + url + "...");

      if(rs.getLastModified() < url.openConnection().getLastModified()) {
        updates.add(url);

        totalUpdates += 100;

        System.err.println("needs update!");
      } else {
        System.out.println("up-to-date");
      }
    }

    if(totalUpdates > 0) {
      if(JOptionPane.OK_OPTION ==
            JOptionPane.showConfirmDialog(null, "An update is available\nDownload now?", "Install Updates",
                                          JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
        DownloadService ds = (DownloadService)ServiceManager.lookup("javax.jnlp.DownloadService");

        //        
        //        DownloadServiceListener dsl = new DownloadServiceListener() {
        //          @Override
        //          public void progress(URL url, String version, long readSoFar, long total, int overallPercent) {
        //            setProgress(Math.max(0, overallPercent) + currentUpdate, 0, totalUpdates);
        //          }
        //          @Override
        //          public void validating(java.net.URL url, java.lang.String version, long entry, long total,
        //                                 int overallPercent) {
        //            setMessage("validating " + url + "(" + overallPercent + "%)");
        //          }
        //          @Override
        //          public void upgradingArchive(java.net.URL url, java.lang.String version, int patchPercent,
        //                                       int overallPercent) {
        //            setMessage("upgrading " + url + "(" + overallPercent + "%)");
        //          }
        //          @Override
        //          public void downloadFailed(URL url, String string) {
        //            System.out.println("downloadFailed " + url + ": " + string);
        //          }
        //        };
        //try {
        //  ServiceLoader<DiffView> diffViewLoader = ServiceLoader.load(DiffView.class);
        //
        //  System.out.println("did we have a diff view? " + diffViewLoader.iterator().hasNext());
        //} catch(Throwable t) {
        //  t.printStackTrace();
        //}
        System.out.println("applying updates:");

        try {
          //http://localhost/webstart/lib/sdsvn-1.0-SNAPSHOT.jar
          ds.loadResource(new URL("http", "localhost", "/webstart/lib/JMeldDiffView-1.0-SNAPSHOT.jar"), null,
                          ds.getDefaultProgressWindow());
        } catch(Exception e) {
          e.printStackTrace();
        }

        //try {
        //  ServiceLoader<DiffView> diffViewLoader = ServiceLoader.load(DiffView.class);
        //  System.out.println("did we get a diff view? " + diffViewLoader.iterator().hasNext());
        //} catch(Throwable t) {
        //  t.printStackTrace();
        //}
        for(URL url : updates) {
          System.out.print("updating " + url + "...");
          ds.removeResource(url, null);
          ds.loadResource(url, null, ds.getDefaultProgressWindow());
          System.out.println("done");

          currentUpdate += 100;
        }
      } else {
        return -1;
      }
    }

    return updates.size();
  }

  @Override
  protected void failed(Throwable arg0) {
    JOptionPane.showMessageDialog(null, arg0.getMessage());
  }

  @Override
  protected void succeeded(Integer result) {
    if(result == 0) {
      JOptionPane.showMessageDialog(null, "Application is up-to-date");
    } else if(result > 0) {
      if(JOptionPane.YES_OPTION ==
            JOptionPane.showConfirmDialog(null, "Application updated, restart?", "Install Updates",
                                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
        bs.showDocument(jnlp);
        System.exit(0);
      }
    }
  }
}

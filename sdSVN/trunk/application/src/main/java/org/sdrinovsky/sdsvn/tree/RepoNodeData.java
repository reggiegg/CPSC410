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

package org.sdrinovsky.sdsvn.tree;

import java.util.Collection;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

/**
 * Class RepoNodeData
 *
 *
 * @author
 * @version $Revision$
 */
public class RepoNodeData {
  private SVNRepository repository;
  private RepoNodeData  parent;
  private SVNDirEntry   path;
  private boolean       recursive;
  private boolean       loading = false;
  private boolean       loaded  = false;

  /**
   * Constructor RepoNodeData
   *
   *
   * @param repository
   * @param recursive
   *
   */
  public RepoNodeData(SVNRepository repository, boolean recursive) {
    this.repository = repository;
    this.recursive  = recursive;
  }

  /**
   * Constructor RepoNodeData
   *
   *
   * @param repository
   * @param parent
   * @param path
   * @param recursive
   *
   */
  public RepoNodeData(SVNRepository repository, RepoNodeData parent, SVNDirEntry path, boolean recursive) {
    this.repository = repository;
    this.parent     = parent;
    this.path       = path;
    this.recursive  = recursive;
  }

  /**
   * Method getRepository
   *
   *
   * @return
   *
   */
  public SVNRepository getRepository() {
    return repository;
  }

  /**
   * Method getChildEntries
   *
   *
   * @param entries
   *
   * @throws SVNException
   *
   */
  public void getChildEntries(Collection<SVNDirEntry> entries) throws SVNException {
    repository.getDir(getPath(), -1, null, entries);
  }

  /**
   * Method getPath
   *
   *
   * @return
   *
   */
  public String getPath() {
    String string = "";

    if(parent != null) {
      string += parent.getPath() + "/";
    }

    if(path != null) {
      string += path.getRelativePath();
    }

    return string;
  }

  /**
   * Method getRepoPath
   *
   *
   * @return
   *
   */
  public String getRepoPath() {
    String string = "";

    if(parent != null) {
      string += parent.getRepoPath() + "/";
    }

    if(path != null) {
      string += path.getRelativePath();
    } else {
      string += repository.getLocation().toString();
    }

    return string;
  }

  /**
   * Method isFolder
   *
   *
   * @return
   *
   */
  public boolean isFolder() {
    return((path == null) || (path.getKind() == SVNNodeKind.DIR));
  }

  /**
   * Method startLoading
   *
   *
   */
  public void startLoading() {
    loaded  = true;
    loading = true;
  }

  /**
   * Method stopLoading
   *
   *
   */
  public void stopLoading() {
    loading = false;
  }

  /**
   * Method isLoaded
   *
   *
   * @return
   *
   */
  public boolean isLoaded() {
    return loaded;
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
    String text = null;

    if(path != null) {
      text = path.getRelativePath();
    } else {
      text = repository.getLocation().toString();
    }

    if(loading) {
      text += " (loading...)";
    }

    return text;
  }
}

package net.sourceforge.svnmonitor.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.svnmonitor.model.IRepoModel;
import net.sourceforge.svnmonitor.model.IRepositoryItem;
import net.sourceforge.svnmonitor.model.RepoFolder;
import net.sourceforge.svnmonitor.model.Repository;

/**
 * the default repo model impl
 */
public class RepoModel implements IRepoModel {
	private final List<IRepositoryItem> repos = new ArrayList<IRepositoryItem>();

	public RepoModel() {
		RepoFolder folder = new RepoFolder();
		folder.setName("testcat");

		Repository repo = new Repository();
		repo.setName("repo1");
		repo.setRepoUrl("http://asdf.us/svn");
		repo.setUserName("asdf");
		repo.setLastUpdate(new Date());
		repo.setUpdatePeriod(1);
		repo.setParent(folder);

		folder.getChildren().add(repo);
		repos.add(folder);
	}

	@Override
	public Collection<IRepositoryItem> getItems() {
		List<IRepositoryItem> copy = null;
		synchronized (repos) {
			copy = new ArrayList<IRepositoryItem>(repos);
		}
		return copy;
	}
}

package net.sourceforge.svnmonitor.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PlatformObject;

/**
 * Class that represents a Repository
 * 
 */
public class Repository extends PlatformObject implements IRepository, IAdaptable {
	private String name;
	private String repoUrl;
	private String userName;
	private Date lastUpdate;
	private Integer updatePeriod;
	private IRepositoryItem parent;

	@Override
	public Collection<IRepositoryItem> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IRepositoryItem getParent() {
		return parent;
	}

	public void setParent(IRepositoryItem parent) {
		this.parent = parent;
	}

	@Override
	public String getRepoUrl() {
		return repoUrl;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public Integer getUpdatePeriod() {
		return updatePeriod;
	}

	public void setUpdatePeriod(Integer updatePeriod) {
		this.updatePeriod = updatePeriod;
	}
}

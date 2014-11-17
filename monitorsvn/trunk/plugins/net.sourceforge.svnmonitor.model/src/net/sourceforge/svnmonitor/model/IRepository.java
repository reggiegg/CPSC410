package net.sourceforge.svnmonitor.model;

import java.util.Date;

/**
 * represents a repository
 * 
 */
public interface IRepository extends IRepositoryItem {

	/**
	 * @return the url of the repository to monitor
	 */
	String getRepoUrl();

	/**
	 * @return the username to use. pw?
	 */
	String getUserName();

	/**
	 * @return the timestamp of the last update
	 */
	Date getLastUpdate();

	/**
	 * @return the update Period in minutes
	 */
	Integer getUpdatePeriod();
}

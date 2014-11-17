package net.sourceforge.svnmonitor.model;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Repository or repository folder
 * 
 */
public interface IRepositoryItem extends IAdaptable {
	/**
	 * @return the displayable name of the repository or folder
	 */
	String getName();

	/**
	 * @return the children of this item
	 */
	Collection<IRepositoryItem> getChildren();

	/**
	 * @return the parent of this item or null;
	 */
	IRepositoryItem getParent();
}

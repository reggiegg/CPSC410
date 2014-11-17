package net.sourceforge.svnmonitor.workbench;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {
	private static String REPO_FOLDER_ID = "net.sourceforge.svnmonitor.REPOFOLDER";
	private static String LOG_FOLDER_ID = "net.sourceforge.svnmonitor.LOGFOLDER";
	private static String DETAIL_FOLDER_ID = "net.sourceforge.svnmonitor.DETAILFOLDER";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		IFolderLayout repoFolder = layout.createFolder(REPO_FOLDER_ID, IPageLayout.TOP, 0.5f, editorArea);
		IFolderLayout logFolder = layout.createFolder(LOG_FOLDER_ID, IPageLayout.RIGHT, 0.3f, REPO_FOLDER_ID);
		IFolderLayout detailFolder = layout.createFolder(DETAIL_FOLDER_ID, IPageLayout.BOTTOM, 1.0f, editorArea);

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		repoFolder.addView("net.sourceforge.svnmonitor.repoview.views.repoview");
		logFolder.addView("net.sourceforge.svnmonitor.logview.views.logview");
		detailFolder.addView("org.eclipse.pde.runtime.LogView");
		detailFolder.addView("net.sourceforge.svnmonitor.detailview.views.detailview");
	}
}

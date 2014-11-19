package libgdxplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;


/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class LibgdxNewWizard extends Wizard implements
		INewWizard {
	private LibgdxNewWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewWizard.
	 */
	public LibgdxNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new LibgdxNewWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		// final String containerName = "container";
		// final String fileName = "examples";
		//
		final String pName = page.getProjectName();
		final String pMainPackage = page.getProjectMainPackage();
		final String pMainClass = page.getProjectMainClass();
		final String pDestinationFolder = page.getProjectDestinationFolder();
		final boolean isCreateDesktopVersion = page.isCreateDesktopProject();
		
		System.out.println("name: " + pName);
		System.out.println("package: " + pMainPackage);
		System.out.println("class: " + pMainClass);
		System.out.println("folder: " + pDestinationFolder);
		System.out.println("create desktop: " + isCreateDesktopVersion);

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(pName, pMainPackage, pMainClass,
							pDestinationFolder, isCreateDesktopVersion, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String projectName, String projectMainPackage,
			String projectMainClass, String projectDestinationFolder,
			boolean isCreateDesktopVersion, IProgressMonitor monitor)
			throws CoreException {
		// create project
		System.out.println("do finissh");
		monitor.beginTask("Creating project " + projectName, 2);
		System.out.println("monitor begin task");
		
		NewProjectLibgdxCreation creation = new NewProjectLibgdxCreation(projectName, projectMainPackage, projectMainClass, projectDestinationFolder, isCreateDesktopVersion);
		
		creation.createMainFolder();
		String coreDescriptionFilePath = creation.createCoreProject();
		importProject(monitor, projectName, coreDescriptionFilePath);
		
		String androidDesciptionFilePath = creation.createAndroidProject();
		importProject(monitor, projectName + Constant.EXTENSION_ANDROID, androidDesciptionFilePath);
		
		if (isCreateDesktopVersion){
			String desktopDescriptionFilePath = creation.createDesktopProject();
			importProject(monitor, projectName + Constant.EXTENSION_DESKTOP, desktopDescriptionFilePath);
		}
		
		monitor.worked(1);
	}

	public void importProject(IProgressMonitor monitor, String projectName, String descriptionPath) throws CoreException {
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(descriptionPath));
			newProject.create(description, monitor);
			newProject.open(monitor);
			System.out.println("Created project: " + projectName + " with description file: " + descriptionPath);
		} else if (!newProject.isOpen()) {
			newProject.open(monitor);
		}
		
		System.out.println("Complete import project " + projectName);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
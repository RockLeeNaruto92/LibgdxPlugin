package libgdxplugin.wizards;

import java.io.File;

public class NewProjectLibgdxCreation {
	
	public String projectName = "Libgdx";
	public String projectMainPackage = "libgdx.plugin.example";
	public String projectMainClass = "LibgdxExample";
	public String projectDestinationFolder = "/home/superman/example";
	public boolean isCreateDesktopProject = true;
	
	
	public NewProjectLibgdxCreation(String projectName,
			String projectMainPackage, String projectMainClass,
			String projectDestinationFolder, boolean isCreateDesktopProject) {
		super();
		this.projectName = projectName;
		this.projectMainPackage = projectMainPackage;
		this.projectMainClass = projectMainClass;
		this.projectDestinationFolder = projectDestinationFolder;
		this.isCreateDesktopProject = isCreateDesktopProject;
	}

	public void create(){
		this.createMainFolder();
		this.createCoreProject();
		this.createAndroidProject();
		this.createDesktopProject();
	}
	
	/**
	 * 
	 * @return true: if complete creating main folder
	 * 		   false:else	
	 */
	public boolean createMainFolder(){
		String folderPath = projectDestinationFolder + "/" + projectName;
		File file = new File(folderPath);
		
		if (file.exists()){
			System.out.println("folder existed!  " + folderPath);
			return false;
		}
		
		file.mkdir();
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String createCoreProject(){
		String folderPath = projectDestinationFolder + "/" + projectName;
		CoreProjectCreation creation = new CoreProjectCreation(folderPath, projectName, projectMainClass, projectMainPackage);
		
		return creation.create();
	}
	
	public String createAndroidProject(){
		String folderPath = projectDestinationFolder + "/" + projectName;
		AndroidProjectCreation creation = new AndroidProjectCreation(folderPath, projectName, projectMainClass, projectMainPackage);

		return creation.create();
	}
	
	public String createDesktopProject(){
		String folderPath = projectDestinationFolder + "/" + projectName;
		DesktopProjectCreation creation = new DesktopProjectCreation(folderPath, projectName, projectMainClass, projectMainPackage);
		
		return creation.create();
	}
}

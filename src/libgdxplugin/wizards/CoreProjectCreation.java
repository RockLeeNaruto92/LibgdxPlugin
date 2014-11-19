package libgdxplugin.wizards;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class CoreProjectCreation {
	private String path;
	private String projectName;
	private String projectMainClass;
	private String projectMainPackage;
	private String srcFolder;
	
	// Constructor
	public CoreProjectCreation(String path, String projectName,
			String projectMainClass, String projectMainPackage) {
		super();
		this.path = path;
		this.projectName = projectName;
		this.projectMainClass = projectMainClass;
		this.projectMainPackage = projectMainPackage;
		this.srcFolder = "";
	}
	

	public String create(){
		createCoreProjectFolder();
		createSrcFolder();
		modifyProjectFile();
		modifyGwtFile();
		modifyClassPathFile();
		
		return path + "/" + projectName + "/" + Constant.PATTERN_PROJECT_FILE;
	}
	
	private void createCoreProjectFolder(){
		// copy pattern project
		String coreFolderPath = path + "/" + projectName;
		String patternCoreFolderPath = Constant.PATTERN_FOLDER + "/" + Constant.PATTERN_CORE_FOLDER;
		URL patternCoreFolderUrl = Platform.getBundle(Constant.BUNDLE_NAME).getEntry(patternCoreFolderPath);
		File coreFolder;
		File patternFolder;
		
		Utility.createFolder(coreFolderPath);
		coreFolder = new File(coreFolderPath);
		patternFolder = Utility.getPluginResourcesFile(patternCoreFolderPath);
		
		System.out.println("create core project folder: ");
		 
		try {
			FileUtils.copyDirectory(patternFolder, coreFolder);
		} catch (IOException e) {
			System.out.println("Not found pattern folder");
			e.printStackTrace();
		}
		
	}
	
	private void createSrcFolder(){
		// create structure of src folder
		String srcPath = createSrcStructure();
		// move and modify file java
		modifyJavaFile(srcPath);
	}
	
	/**
	 * Create src folder structure
	 * @return
	 */
	private String createSrcStructure(){
		String srcPath = path + "/" + projectName + "/src";
		String tempStr = projectMainPackage.replace(".", "-");
		String parts[] = tempStr.split("-");
		
		for (int i = 0	; i < parts.length; i++){
//			srcPath = srcPath + "/" + parts[i];
			srcFolder = srcFolder + parts[i];
			srcFolder += (i == parts.length - 1 ? "" : "/");
			srcPath = srcPath + "/" + parts[i] ;
			Utility.createFolder(srcPath);
		}
		
		System.out.println(srcFolder);
		System.out.println(srcPath);
		
		return srcPath;
	}
	
	/**
	 * Move pattern Java file and modify
	 * @param srcPath
	 */
	private void modifyJavaFile(String srcPath){
		// move java file to srcPath
		String javaFilePatternPath = path + "/" + projectName + "/" +  Constant.PATTERN_MAIN_CLASS_FILE;
		String destJavaFilePath = srcPath + "/" + projectMainClass + ".java";
		
		File srcFile = new File(javaFilePatternPath);
		File destFile = new File(destJavaFilePath);
		
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			System.out.println("Not found " + javaFilePatternPath + " or  " + destJavaFilePath);
		}
		
		srcFile.delete();
		// End move
		// begin modifying
		String content = null;
		try {
			content = FileUtils.readFileToString(destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Not found dest file");
//			e.printStackTrace();
		}
		
		String newContent = content.replaceAll(Constant.PATTERN_PROJECT_NAME, projectMainClass);
		newContent = newContent.replaceAll(Constant.PATTERN_MAIN_PACKAGE, projectMainPackage);
		
		try {
			FileUtils.writeStringToFile(destFile, newContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Not found dest file");
			e.printStackTrace();
		}
	}
	
	private void modifyProjectFile(){
		// modify file project
		String  projectFilePath = path + "/" + projectName + "/" + Constant.PATTERN_PROJECT_FILE;
		File file = new File(projectFilePath);
		String content = null;
		
		if (!file.exists()){
			System.out.println("Not found " + projectFilePath);
			return;
		}
		
		try {
			content = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = content.replaceAll(Constant.PATTERN_PROJECT_NAME, projectName);
		
		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void modifyGwtFile(){
		String gwtFilePath = path + "/" + projectName + "/" + Constant.PATTERN_GWT_XML_FILE;
		String destFilePath = path + "/" + projectName + "/src/" + projectName + ".gwt.xml"; 
		File srcFile = new File(gwtFilePath);
		File destFile = new File(destFilePath);
		
		if (!srcFile.exists()){
			return;
		}
		
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		srcFile.delete();
		
		String content = null;
		try {
			content = FileUtils.readFileToString(destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = content.replaceAll(Constant.PATTERN_SRC_FOLDER, srcFolder);
		try {
			FileUtils.writeStringToFile(destFile, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	private void modifyClassPathFile(){
		
	}

}

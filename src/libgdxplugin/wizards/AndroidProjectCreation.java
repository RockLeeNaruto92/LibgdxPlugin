package libgdxplugin.wizards;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public class AndroidProjectCreation {
	private String path;
	private String projectName;
	private String projectMainClass;
	private String projectMainPackage;
	private String srcFolder;
	
	public AndroidProjectCreation(String path, String projectName,
			String projectMainClass, String projectMainPackage) {
		super();
		this.path = path;
		this.projectName = projectName;
		this.projectMainClass = projectMainClass;
		this.projectMainPackage = projectMainPackage;
		this.srcFolder = "";
	}
	
	public String create(){
		createAndroidProjectFolder();
		createSrcFolder();
		modifyManifestFile();
		modifyProjectFile();
		modifyClasspathFile();
		
		return path + "/" + projectName + Constant.EXTENSION_ANDROID + "/" + Constant.PATTERN_PROJECT_FILE;
	}

	private void createAndroidProjectFolder() {
		// TODO Auto-generated method stub
		String androidFolderPath = path + "/" + projectName + Constant.EXTENSION_ANDROID;
		String patternAndroidFolderPath = Constant.PATTERN_FOLDER + "/" + Constant.PATTERN_ANDROID_FOLDER;
		File androidFolder;
		File patternFolder;
		
		Utility.createFolder(androidFolderPath);
		androidFolder = new File(androidFolderPath);
		patternFolder = Utility.getPluginResourcesFile(patternAndroidFolderPath);
		
		try {
			FileUtils.copyDirectory(patternFolder, androidFolder);
		} catch (IOException e) {
			System.out.println("Not found pattern folder");
//			e.printStackTrace();
		}
	}

	private void createSrcFolder() {
		// TODO Auto-generated method stub
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
		String srcPath = path + "/" + projectName + Constant.EXTENSION_ANDROID + "/src";
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
		String javaFilePatternPath = path + "/" + projectName + Constant.EXTENSION_ANDROID + "/" +  Constant.PATTERN_MAIN_ANDROID_CLASS_FILE;
		String destJavaFilePath = srcPath + "/" + Constant.PATTERN_MAIN_ANDROID_CLASS_FILE;
		
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

	private void modifyManifestFile() {
		// TODO Auto-generated method stub
		String manifestFilePath = path + "/" + projectName + Constant.EXTENSION_ANDROID + "/" + Constant.ANDROID_MANIFEST_XML_FILE;
		File file = new File(manifestFilePath);
		String content = null;
		
		if (!file.exists()){
			return;
		}
		
		try {
			content = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = content.replaceAll(Constant.PATTERN_MAIN_PACKAGE, projectMainPackage);
		
		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void modifyProjectFile() {
		// TODO Auto-generated method stub
		// modify file project
		String projectFilePath = path + "/" + projectName + Constant.EXTENSION_ANDROID + "/" + Constant.PATTERN_PROJECT_FILE;
		File file = new File(projectFilePath);
		String content = null;

		if (!file.exists()) {
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

	private void modifyClasspathFile() {
		// TODO Auto-generated method stub
		String classPathFilePath = path + "/" + projectName + Constant.EXTENSION_ANDROID + "/" + Constant.PATTERN_CLASS_PATH_FILE;
		File file = new File(classPathFilePath);
		String content = null;
		
		if (!file.exists()){
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
}

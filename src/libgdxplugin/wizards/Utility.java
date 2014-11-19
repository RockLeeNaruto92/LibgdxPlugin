package libgdxplugin.wizards;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;


public class Utility {
	public static boolean createFolder(String path){
		File file = new File(path);
		
		if (file.exists()){
			System.out.println(path + " existed!");
			return false;
		}
		
		file.mkdir();
		System.out.println("Create folder " + path);
		
		return true;
	}
	
	public static File getPluginResourcesFile(String path){
		URL fileUrl = Platform.getBundle(Constant.BUNDLE_NAME).getEntry(path);
		File file = null;
		
		try {
			file = new File(FileLocator.resolve(fileUrl).toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;	
	}
}

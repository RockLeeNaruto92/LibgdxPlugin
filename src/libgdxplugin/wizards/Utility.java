package libgdxplugin.wizards;
import java.io.File;


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
}

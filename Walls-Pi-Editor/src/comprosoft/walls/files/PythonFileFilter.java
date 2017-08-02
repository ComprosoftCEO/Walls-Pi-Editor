package comprosoft.walls.files;

import java.io.File;
import java.io.FileFilter;

/**
 * Filter Python files 
 */
public class PythonFileFilter implements FileFilter {

	//Handles all possible file types
	private FileExtensionFilter extensionFilter = new FileExtensionFilter("py");
	
	
	@Override
	public boolean accept(File pathname) {

		if (pathname.isHidden()) {return false;}
		
		return extensionFilter.accept(pathname, pathname.getName());
	}


}

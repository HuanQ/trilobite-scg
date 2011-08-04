package data;

import java.io.File;

public class Cleaner {
	
	static public final void cleanAll() {
		// Set up directory if needed
		File directory = new File( "resources/games/");
		File files[] = directory.listFiles();
		for(File f : files) {
			if(f.getName().equals(".svn"))
				continue;
			deleteRecursive(f);
		}
	}
	
	static private final void deleteRecursive(File path) {
		File[] c = path.listFiles();
        for(File file : c) {
            if(file.isDirectory()) {
            	deleteRecursive(file);
            }
            file.delete();
        }
        path.delete();
	}
	
}

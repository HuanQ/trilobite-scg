package data;

import java.io.File;

import managers.Level;

public class Cleaner {
	
	static public final void DeleteActor( int num ) {
		String path = "resources/games/" + Level.lvlname + "/";
		
		// Delete this actor;
		File file = new File( path + Integer.toString(num) + ".dat" );
		file.delete();
	}
	
	static public final void CleanAll() {
		// Set up directory if needed
		File directory = new File( "resources/games/");
		File files[] = directory.listFiles();
		for(File f : files) {
			if(f.getName().equals(".svn"))
				continue;
			doRecursiveDelete(f);
		}
	}
	
	static private final void doRecursiveDelete(File path) {
		File[] c = path.listFiles();
        for(File file : c) {
            if(file.isDirectory()) {
            	doRecursiveDelete(file);
            }
            file.delete();
        }
        path.delete();
	}
	
}

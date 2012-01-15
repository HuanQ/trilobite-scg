package data;

import java.io.File;

import managers.Constant;
import managers.Level;

public class Cleaner {
	
	static public final void DeleteActor( int num ) {
		String path = "resources/games/" + Level.lvlname + "/";
		
		// Delete this actor
		File file;
		
		file = new File( path + Integer.toString(num) + "." + Constant.GliderShip );
		file.delete();
		file = new File( path + Integer.toString(num) + "." + Constant.AgileShip );
		file.delete();
		file = new File( path + Integer.toString(num) + "." + Constant.TankShip );
		file.delete();
		file = new File( path + Integer.toString(num) + "." + Constant.DefendShip );
		file.delete();
	}
	
	static public final void CleanAll() {
		// Set up directory if needed
		File directory = new File("resources/games/");
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

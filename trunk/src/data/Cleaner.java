package data;

import java.io.File;

public class Cleaner {
	
	public Cleaner() {
		
	}
	
	public void cleanAll() {
		// Set up directory if needed
		File directory = new File( "resources/games/");
		File f[] = directory.listFiles();
		for(int i=0;i<f.length;++i) {
			if(f[i].getName().equals(".svn"))
				continue;
			deleteRecursive(f[i]);
		}
	}
	
	private void deleteRecursive(File path){
		File[] c = path.listFiles();
        //TODO: Cambiar tots els fors i posar-los aixi (es array, amb vector no es deu poder?)
        for (File file : c) {
            if (file.isDirectory()) {
            	deleteRecursive(file);
            }
            file.delete();
        }
        path.delete();
	}
	
}

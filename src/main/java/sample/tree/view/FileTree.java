package sample.tree.view;

import java.io.File;


public class FileTree {

//	private static void showTree(String dir, int level) {
//		StringBuilder sb = new StringBuilder();
//		for(int i=0; i<level; i++) {
//			sb.append("---");
//		}
//		File file = new File(dir);
//		if(file.isDirectory()) {
//			String[] dirs = file.list();
//			for(int i=0; i<dirs.length; i++) {
//				System.out.println(sb + file.getName());
//				showTree(dirs[i], level+1);
//			}			
//		}
//	}
//	
//	public static void main(String[] args) {
//		
//		showTree("d:/", 0);
//	}

//	public static void main(String[] args) {
//		File file = new File("d:/");
//		//String[] dirs = file.list();
//		File[] dirs = file.listFiles();
//		for(int i=0; i<dirs.length; i++) {
//			System.out.println(dirs[i].getName());
//		}
//	}
	
	private static void listFiles(File file, int level) {
		File[] files = file.listFiles();
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<level; i++) {
			sb.append("---");
		}
		
		for(int i=0; i<files.length; i++) {
			System.out.println(sb + files[i].getName());
			if(files[i].isDirectory()) {
				listFiles(files[i], level+1);
			}
		}
	}
	
	public static void main(String[] args) {
		File file = new File("D:\\CSS");
		System.out.println(file.getName());
		listFiles(file, 1);
		deleteAll(file);
	}
	
	// only delete file
	public static void deleteFile(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++) {
				deleteFile(files[i]);
			}
		} else {
			System.out.println(file.getName());
			file.delete();	
		}
	}
	
	// delete file and dir
	public static void deleteAll(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++) {
				deleteAll(files[i]);
			}
		}
		
		System.out.println(file.getName());
		file.delete();	
	}
}

















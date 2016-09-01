package sample.tree.view;

import java.io.File;

class ListFile {
	
	
	public static void listFile(File f, int levels) {
		File[] ff = f.listFiles();
		StringBuilder pre =new StringBuilder("");
		
		for(int level = 0; level<levels; level++)
			pre.append("----");
			
		for(int i=0; i<ff.length; i++) {
			if(levels == 0)
				System.out.println("\n");
				//append()�ǰ�pre��ɺͺ�����ӵĽ��  ��+��û�аѺ��������pre��
			System.out.println(pre+ff[i].getName());
			if(ff[i].isDirectory())								
				listFile(ff[i], levels+1);
		}
	}
	
	public static void main(String[] args) {
		
		File f = new File("D:"+File.separator+"share"+File.separator+"MyTest");
		listFile(f, 0);
	}
}
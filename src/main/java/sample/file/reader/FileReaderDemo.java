package sample.file.reader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;

public class FileReaderDemo {
	
	private static final String FILE = "test.properties";

	/**
	 * All of these search files from classpath.
	 *  
	 */
	@Test
	public void testLoadFileFromClasspathThenGetFilePath() {
		// search path is relative to the package in which this class is
		URL url1 = FileReaderDemo.class.getResource(FILE);
		// all below search path is relative to the classpath root
		URL url2 = FileReaderDemo.class.getClassLoader().getResource("sample/file/reader/" + FILE);
		URL url3 = ClassLoader.getSystemResource("sample/file/reader/" + FILE);
		URL url4 = Thread.currentThread().getContextClassLoader().getResource("sample/file/reader/" + FILE);
		System.out.println("***url1***: " + url1.getFile() + "\n" +
				"***url2***: " + url2.getFile() + "\n" +
				"***url3***: " + url3.getFile() + "\n" +
				"***url4***" + url4.getPath());
		
		
	}
	
	@Test
	public void testLoadFileFromClasspathAsStreamThenWrite2String() throws IOException {
		InputStream is = FileReaderDemo.class.getClassLoader().getResourceAsStream("sample/file/reader/" + FILE);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buff = new byte[256];
		int i = -1;
		int j = 0;
		while((i = is.read(buff, 0, 3)) != -1) {
			os.write(buff, 0, i);
			System.out.println((++j) + "--" + new String(buff));
		}
		System.out.println(new String(os.toByteArray()));
		
		os.close();
		is.close();
	}
	
	@Test
	public void testReadFileAsStreamThenWrite2File() throws IOException {
		InputStream is = FileReaderDemo.class.getClassLoader().getResourceAsStream("sample/file/reader/" + FILE);
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] buff = new byte[1024];
		
		OutputStream os = new FileOutputStream(System.getProperty("user.home") + 
				File.separator + "a.txt");
		BufferedOutputStream bos = new BufferedOutputStream(os);
		
		int i = -1;
		while((i = bis.read(buff)) != -1) {
			bos.write(buff, 0, i);
		}
		bos.close();
		bis.close();
		
	}
	
	@Test
	public void testConvertHex2Chinese() throws IOException {
		byte[] b = hexStringToBytes("f3B9D1D5F8D401060650431300350320");
		
		String s = new String(b, "gb2312");
		System.out.println(s);
		
	}
	
	private byte[] hexStringToBytes(String hexString) {   
		    if (hexString == null || hexString.equals("")) {   
		        return null;   
		    }   
		    hexString = hexString.toUpperCase();   
		    int length = hexString.length() / 2;   
		    char[] hexChars = hexString.toCharArray();   
		    byte[] d = new byte[length];   
		    for (int i = 0; i < length; i++) {   
		        int pos = i * 2;   
		        d[i] = (byte) ~(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		    }   
		    return d;   
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}  


}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Utility2 {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		String readFileName = "C:\\temp\\tomcat\\apache-tomcat-9.0.38.7z.0";
		
		for(int i=1; i<=16; i++) {
			String idxStr = "" + i;
			if(i < 10) {
				idxStr = "0" + i;
			}
			System.out.println(readFileName + idxStr);
			process(readFileName + idxStr);
		}
		
//		process("C:\\temp\\commons-dbutils-1.7.jar");


	}

	private static void process(String readFileName) throws FileNotFoundException, UnsupportedEncodingException {
		File f = new File(readFileName);
		FileInputStream fis = new FileInputStream(f);		
		byte byteArray[] = new byte[(int)f.length()];
		try {
			fis.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imageString = Base64.encodeBase64String(byteArray);
		
		File file = new File(readFileName + ".txt");
		FileOutputStream fos = new FileOutputStream(file);
		BufferedWriter buw = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));
		try {
			buw.write(imageString);
			buw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = new PrintWriter(readFileName + ".txt");
		out.println(imageString);
		out.close();
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package tmp.datacopy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class Base64Test {

	public static void main(String[] args) {
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(""));
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

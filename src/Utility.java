import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class Utility {

	public static void main(String[] args) {
		
		System.out.println("start");
		
		String filePath = "C:\\temp\\psftp.exe";
//		String filePath = "C:\\project\\eclipse-workspace\\Tip\\lib\\commons-io-2.8.0.jar";
		
		System.out.println(filePath);
		String encodedString = "";
		try {
			System.out.println(filePath);
			encodedString = convertFileToStr(filePath);
			System.out.println(encodedString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
//		String outputFileName = "";
//		convertStrToFile(encodedString, outputFileName);


	}

	private static String convertFileToStr(String filePath) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath ));
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}

	private static void convertStrToFile(String encodedString, String outputFileName) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		FileUtils.writeByteArrayToFile(new File(outputFileName), decodedBytes);
	}

}

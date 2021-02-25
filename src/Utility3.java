import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

public class Utility3 {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		String filePath = "D:/ifxjdbc.txt";
		 
        String str1 = readLineByLineJava8( filePath );
		System.out.println( str1 );
		
		String encodedImg = str1;
		byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
		Path destinationFile = Paths.get("c:/temp/output", "ifxjdbc.jar");
		try {
			Files.write(destinationFile, decodedImg);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	private static String readLineByLineJava8(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }

}

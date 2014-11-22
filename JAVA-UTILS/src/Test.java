import java.io.File;
import java.util.regex.Matcher;

/**
 * 
 */

/**
 * @author Vijay Shegokar
 *
 */
public class Test {

	public static void main(String[] args) {
		String basePath = "C:/Peerbuds/images/";
		System.out.println(File.separator);
		System.out.println(Matcher.quoteReplacement(basePath));
		System.out.println(basePath.replace("/", File.separator));
	}
	
}

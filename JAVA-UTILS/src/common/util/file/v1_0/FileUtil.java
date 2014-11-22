/**
 * 
 */
package common.util.file.v1_0;

import java.io.File;

/**
 * @author Vijay Shegokar
 * @version 1.0
 * @date 28-September-2014
 *
 */
public class FileUtil {
	
	/**
	 * This method delete the passed path file if existed in the system
	 * @param filePath {@link String} File path to be delete
	 * @return {@link Boolean} Returns true if found and deleted
	 */
	public static boolean delete(String filePath){
		if(filePath != null){
			try {
				File file = new File(filePath);
				if(file.exists()){
					file.delete();
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}

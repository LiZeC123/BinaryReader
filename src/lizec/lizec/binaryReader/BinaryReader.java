package lizec.lizec.binaryReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryReader {
	
	public static String readAsHex(File file) throws IOException{
		StringBuilder stringBuilder = new StringBuilder();
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		int b;
		int count = 0;
		int lineCount = 0;
		String formatString = "%-5d|";
		
		
		try {
			while ((b=fin.read()) != -1) {
				if(count == 0){
					stringBuilder.append(String.format(formatString, lineCount));
				}
				
				String tString = Integer.toHexString(b);
				if(tString.length()==1){
					tString = "0" + tString;
				}
				
				if(count % 2 ==1){
					tString += " ";
				}
				
				stringBuilder.append(tString);
				if(count == 15){
					stringBuilder.append(Character.toString('\n'));
					count = 0;
					lineCount++;
				}
				else{
					++count;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		fin.close();
		return stringBuilder.toString();
	}
	
	public static String readAsText(File file) throws IOException{
		StringBuilder stringBuilder = new StringBuilder();
		//带缓冲：1.95M/s
		//不带缓冲  0.23M/s
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		int b;
		int count = 0;
		try {
			while ((b=fin.read()) != -1) {
				if(b == 0){
					stringBuilder.append(" ");
				}
				else if(b < 32 || b > 127){
					stringBuilder.append(".");
				}
				else{
					stringBuilder.append(Character.toString((char)b));
				}
				
				if(count == 15){
					stringBuilder.append(Character.toString('\n'));
					count = 0;
				}
				else{
					++count;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		fin.close();
		return stringBuilder.toString();
	}
	
	/**
	 * @param 
	 * @throws IOException 
	 */
	public static void writeFileAsHex(String dataString,File file) throws IOException{
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		String[] lines = dataString.split("\\n");
		for(String line:lines){
			String[] words = line.split("[|]")[1].split(" ");
			for(String word:words){
				//System.out.println(word);
				int n = Integer.parseInt(word, 16);
				//先写入高字节，再写入低字节
				out.write(n >>> 8);
				out.write(n);
			}
		}
		out.flush();
		out.close();
	}
}

package bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class M3u8Bean {
	private URL url = null;
	private StringBuffer sbMain = new StringBuffer();
	
	public StringBuffer getSbMain() {
		return sbMain;
	}
	public void setSbMain(StringBuffer sbMain) {
		this.sbMain = sbMain;
	}
	public  URL getUrl() {
		return url;
	}
	public  void setUrl(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public  StringBuffer getM3u8File() throws IOException {
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();

		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				sb.append(inputLine);
				sb.append('\n'); //if you want the newline
				System.out.println(inputLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			in.close();
		}
		return sb;
	}
	public  void setOutputFile(StringBuffer sb) throws IOException{
		PrintWriter writer = null;
		BufferedWriter bwr = null;
		try {
			writer = new PrintWriter("src/main/resources/output.txt", "UTF-8");
			bwr = new BufferedWriter(writer);
	        //write contents of StringBuffer to a file
	        bwr.write(sb.toString());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
	        //flush the stream
	        bwr.flush();
	        //close the stream
	        bwr.close();
		}
	}

}

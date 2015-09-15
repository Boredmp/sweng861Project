package com.sweng.rest.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.rest.impl.SwengImpl;

public class M3u8Bean {
	private URL url = null;
	private StringBuffer sbMain = new StringBuffer();
	private static final Logger LOG = LoggerFactory.getLogger(M3u8Bean.class);
	private boolean mainFile = false;
	private List<URL> m3u8Urls = new ArrayList<URL>();
	private String parentURL = "";
	private boolean validURL = false;
	private List<StringBuffer> sbAllFiles = new ArrayList<StringBuffer>();
	
	public M3u8Bean(String input) throws IOException{
		setUrl(input);
		setStringBuffer();
		isMainFile(sbMain);
		setParentURL(input);
		setM3u8Urls(input);
	}
	public StringBuffer getSbMain() {
		return sbMain;
	}
	public void setSbMain(StringBuffer sbMain) {
		this.sbMain = sbMain;
	}
	public void setStringBuffer(){
        LOG.info("Set the file and validate", "");
	
		try {
			//mBean.setUrl("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
			//setUrl("file:///C:/Users/cbraam/Downloads/ipadipad-high.m3u8");
			//M3u8Bean.setUrl(url);
			this.sbMain = getM3u8File();
		} catch (IOException e) {
			LOG.error("getValidation() - IOException:" + e);
		}
	}
	
	public void isMainFile(StringBuffer sb){
		String[] lines = sb.toString().split("\\n");
		for(String s: lines){
			if(s.contains("m3u8")){
				this.mainFile = true;
				break;
			}
		}
	}
	public void setChildFiles(StringBuffer sb){		
		String[] lines = sb.toString().split("\\n");
		try {
			for(String s: lines){
				if(s.contains("m3u8")){
					this.m3u8Urls.add(new URL(this.parentURL + s));
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public void setParentURL(String input){
		int end = input.lastIndexOf("/");
		this.parentURL = input.substring(0, end) + "/";
	}
	public void setM3u8Urls(String url){
        try {
	        if(this.mainFile){
	        	setChildFiles(sbMain);
	        	setSbAllFiles();
	        }else{
	        	this.m3u8Urls.add(new URL(url));
	        	this.sbAllFiles.add(sbMain);
	        }
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  void setUrl(String url) {
		try {
			this.url = new URL(url);
			this.validURL = true;
		} catch (MalformedURLException e) {
			this.validURL = false;
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
	public  void setSbAllFiles() throws IOException {
		BufferedReader in = null;
		int index = 0;
		
		try {
			for(URL u: m3u8Urls){
				sbAllFiles.add(new StringBuffer());
				in = new BufferedReader(new InputStreamReader(u.openStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null){
					sbAllFiles.get(index).append(inputLine);
					sbAllFiles.get(index).append('\n'); //if you want the newline
					System.out.println(inputLine);
				}
				index++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			in.close();
		}
	}
//	public  void setOutputFile(StringBuffer sb) throws IOException{
//		PrintWriter writer = null;
//		BufferedWriter bwr = null;
//		try {
//			writer = new PrintWriter("src/main/resources/output.txt", "UTF-8");
//			bwr = new BufferedWriter(writer);
//	        //write contents of StringBuffer to a file
//	        bwr.write(sb.toString());
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//	        //flush the stream
//	        bwr.flush();
//	        //close the stream
//	        bwr.close();
//		}
//	}
	public URL getUrl() {
		return url;
	}
	public List<URL> getM3u8Urls() {
		return m3u8Urls;
	}
	public String getParentURL() {
		return parentURL;
	}
	public List<StringBuffer> getSbAllFiles() {
		return sbAllFiles;
	}

}

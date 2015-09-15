package com.sweng.rest.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.rest.bean.M3u8Bean;
import com.sweng.rest.bean.ValidationBean;

public class SwengImpl {
	private static final Logger LOG = LoggerFactory.getLogger(SwengImpl.class);
	private int oldSeq= 0;
	
	public String getValidatedXML(String input){
		String print = "";
		try {
			M3u8Bean mBean = new M3u8Bean(input);
			print = "<ValidationBean>";
			for(StringBuffer sb: mBean.getSbAllFiles()){
				print+=getAllValidatedLines(sb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print+="</ValidationBean>";
		return print;
		
	}

	
    public String getAllValidatedLines(StringBuffer sb) throws IOException{
    	List<ValidationBean> allValidatedLines = new ArrayList<ValidationBean>();
    	String print = "<File>";

		String[] lines = sb.toString().split("\\n");
		for(String s: lines){
			if(s.equalsIgnoreCase("#EXT-X-ENDLIST")) break;
			ValidationBean vb = new ValidationBean(s, oldSeq);
			allValidatedLines.add(vb);
			print += printXMLValidations(vb);
			if(!s.isEmpty()){
				this.oldSeq = vb.getSequenceNumber();
			}
		}
		print+="</File>";
		return print;
    }
    
    public String printXMLValidations(ValidationBean vb){
    	String print = "<Row>"
    			+ "<line>"+vb.getLine()+"</line>"
    			+ "<isHashTagOrBlankLine>"+vb.isHashTagOrBlankLine()+"</isHashTagOrBlankLine>"
    			+ "<isNotMultipleHashTags>"+vb.isNotMultipleHashTags()+"</isNotMultipleHashTags>"
    			+ "<isSequenceInOrder>"+vb.isSequenceInOrder(0)+"</isSequenceInOrder>"
    			+ "</Row>";
    	return print;
    }
}

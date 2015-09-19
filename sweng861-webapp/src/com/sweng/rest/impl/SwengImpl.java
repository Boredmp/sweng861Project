package com.sweng.rest.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.rest.bean.M3u8Bean;
import com.sweng.rest.bean.ValidationBean;
import com.sweng.rest.util.Constants;

public class SwengImpl {
	private static final Logger LOG = LoggerFactory.getLogger(SwengImpl.class);
	private int oldSeq= 0;
	private double targetDuration = 0;
	private int mediaSequence = 0;
	
	public int getMediaSequence() {
		return mediaSequence;
	}

	public void setMediaSequence(String line) {
		if(line.contains(Constants.mediaSeq)){
			String temp = line.replace(Constants.mediaSeq, "");
			this.mediaSequence = Integer.parseInt(temp);
		}
	}

	public double getTargetDuration() {
		return targetDuration;
	}

	public void setTargetDuration(String line) {
		if(line.contains(Constants.TARGETDURATION)){
			String[] temp = line.split(":");
			this.targetDuration = Double.parseDouble(temp[1]);
		}
	}
	
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
			if(s.equalsIgnoreCase(Constants.ENDLIST)) break;
			setTargetDuration(s);
			setMediaSequence(s);
			
			ValidationBean vb = new ValidationBean(s, this.oldSeq, this.mediaSequence, this.targetDuration);
			allValidatedLines.add(vb);
			print += printXMLValidations(vb);
			if(!s.isEmpty()){
				this.oldSeq = vb.getNewSeq();
			}
		}
		print+="</File>";
		return print;
    }
    
    public String printXMLValidations(ValidationBean vb){
    	String print = "<Row>"
    			+ "<line>"+vb.getLine()+"</line>"
    			+ "<isHashTagBlankLineOrTS>"+vb.isHashTagBlankLineOrTS()+"</isHashTagBlankLineOrTS>"
    			+ "<isNotMultipleHashTags>"+vb.isNotMultipleHashTags()+"</isNotMultipleHashTags>"
    			+ "<isSequenceInOrder>"+vb.isSequenceInOrder()+"</isSequenceInOrder>"
    			+ "<isTargerDurationSame>"+vb.isTargerDurationSame()+"</isTargerDurationSame>"
    			+ "</Row>";
    	return print;
    }
}

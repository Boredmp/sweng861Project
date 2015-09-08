package com.sweng.rest.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.rest.bean.ValidationBean;

public class SwengImpl {
	private static final Logger LOG = LoggerFactory.getLogger(SwengImpl.class);
	private int oldSeq= 0;
	
    public String getAllValidatedLines(StringBuffer sb) throws IOException{
    	List<ValidationBean> allValidatedLines = new ArrayList<ValidationBean>();
    	OutputStream os = null;
    	String print = "<ValidationBean>";

		String[] lines = sb.toString().split("\\n");
		for(String s: lines){
			ValidationBean vb = new ValidationBean(s, oldSeq);
			allValidatedLines.add(vb);
			os = System.out;
			printValidations(os, vb);
			print += printXMLValidations(vb);
			this.oldSeq = vb.getSequenceNumber();
		}
		print+="</ValidationBean>";
		return print;
    }
    public void printValidations(OutputStream os, ValidationBean vb){
    	PrintWriter pw = new PrintWriter(os);
    	pw.write("<ValidationBean>");
    	pw.write("    <line>"+vb.getLine()+"</line>");
    	pw.write("    <isHashTagOrBlankLine>"+vb.isHashTagOrBlankLine()+"</isHashTagOrBlankLine>");
    	pw.write("    <isNotMultipleHashTags>"+vb.isNotMultipleHashTags()+"</isNotMultipleHashTags>");
    	pw.write("    <isSequenceInOrder>"+vb.isSequenceInOrder(0)+"</isSequenceInOrder>");
    	pw.write("</ValidationBean>");
    }
    
    public String printXMLValidations(ValidationBean vb){
    	String print = "<line>"+vb.getLine()+"</line>"
    			+ "<isHashTagOrBlankLine>"+vb.isHashTagOrBlankLine()+"</isHashTagOrBlankLine>"
    			+ "<isNotMultipleHashTags>"+vb.isNotMultipleHashTags()+"</isNotMultipleHashTags>"
    			+ "<isSequenceInOrder>"+vb.isSequenceInOrder(0)+"</isSequenceInOrder>";
    	return print;
    }
}

package com.sweng.rest.bean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationBean {
	private String line = "";

	private boolean isSequenceInOrder;
	private boolean isHashTagOrBlankLine;
	private boolean isNotMultipleHashTags;
	
	public ValidationBean (String line, int oldSeq){
		this.line = line;
		this.isSequenceInOrder = isSequenceInOrder(oldSeq);
		this.isHashTagOrBlankLine = isHashTagOrBlankLine();
		this.isNotMultipleHashTags = isNotMultipleHashTags();
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public boolean isSequenceInOrder(int oldSeq) {
		boolean success = false;
		if(oldSeq < getSequenceNumber()){
			success = true;
		}
		
		return success;
	}
	public void setSequenceInOrder(boolean isSequenceInOrder) {
		this.isSequenceInOrder = isSequenceInOrder;
	}
	public boolean isHashTagOrBlankLine() {
		return (getFirstChar().equals("#") || getFirstChar().isEmpty()) ? true : false;
	}
	public void setHashTagOrBlankLine(boolean isHashTagOrBlankLine) {
		this.isHashTagOrBlankLine = isHashTagOrBlankLine;
	}
	public boolean isNotMultipleHashTags() {
		return !isMultipleOccurances("#");
	}
	public void setNotMultipleHashTags(boolean isNotMultipleHashTags) {
		this.isNotMultipleHashTags = isNotMultipleHashTags;
	}
	public String getFirstChar(){
		String first = line.substring(0,1);
		return first;
	}
	public boolean isMultipleOccurances(String findStr){
		boolean success = false;
		if((line.split(findStr, -1).length-1)>1){
			success = true;
		}
		return success;
	}

	public int getSequenceNumber(){
		final Pattern pattern = Pattern.compile("\\d+"); // the regex
		final Matcher matcher = pattern.matcher(line); // your string

		final ArrayList<Integer> ints = new ArrayList<Integer>(); // results

		while (matcher.find()) { // for each match
		    ints.add(Integer.parseInt(matcher.group())); // convert to int
		}
		return ints.get(0);
	}
}

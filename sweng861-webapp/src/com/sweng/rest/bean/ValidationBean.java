package com.sweng.rest.bean;

import com.sweng.rest.util.Constants;
import com.sweng.rest.util.Util;

public class ValidationBean {
	private String line = "";
	private int newSeq = 0;

	private boolean isSequenceInOrder = false;
	private boolean isHashTagBlankLineOrTS = false;
	private boolean isNotMultipleHashTags = false;
	private boolean isTargerDurationSame = false;
	
	public ValidationBean (String line, int oldSeq, int mediaSequence, double targetDuration){
		this.line = line;
		setSequenceInOrder(oldSeq, mediaSequence);
		setHashTagBlankLineOrTS();
		setNotMultipleHashTags();
		setTargetDurationSame(targetDuration);
	}
	public int getNewSeq() {
		return newSeq;
	}

	public boolean isSequenceInOrder() {
		return isSequenceInOrder;
	}

	public boolean isHashTagBlankLineOrTS() {
		return isHashTagBlankLineOrTS;
	}

	public boolean isNotMultipleHashTags() {
		return isNotMultipleHashTags;
	}

	public boolean isTargerDurationSame() {
		return isTargerDurationSame;
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}

	/*   The EXT-X-TARGETDURATION tag specifies the maximum Media Segment
   duration.  The EXTINF duration of each Media Segment in the Playlist
   file, when rounded to the nearest integer, MUST be less than or equal
   to the target duration; longer segments can trigger playback stalls
   or other errors.  It applies to the entire Playlist file.  Its format
   is:

   #EXT-X-TARGETDURATION:<s>

   where s is a decimal-integer indicating the target duration in
   seconds.  The EXT-X-TARGETDURATION tag is REQUIRED.**/
	public void setTargetDurationSame(double targetDuration){		
		if(targetDuration != 0 && line.contains(Constants.EXTINF)){
			if(targetDuration >= Util.getNumber(line)){
				this.isTargerDurationSame = true;
			}
		}else{
			this.isTargerDurationSame = true;
		}
	}
	
	/*The EXT-X-MEDIA-SEQUENCE tag indicates the Media Sequence Number of
	   the first Media Segment that appears in a Playlist file.  Its format
	   is:

	   #EXT-X-MEDIA-SEQUENCE:<number>

	   where number is a decimal-integer.

	   If the Media Playlist file does not contain an EXT-X-MEDIA-SEQUENCE
	   tag then the Media Sequence Number of the first Media Segment in the
	   Media Playlist SHALL be considered to be 0.  A client MUST NOT assume
	   that segments with the same Media Sequence Number in different Media
	   Playlists contain matching content - see Section 6.3.2.

	   A URI for a Media Segment is not required to contain its Media
	   Sequence Number.**/
	public void setSequenceInOrder(int oldSeq, int mediaSequence) {
		if(!line.isEmpty()
				&& line.contains(Constants.TS)
				&& mediaSequence != 0){
			this.newSeq = Util.getSequenceNumber(line);
			if(oldSeq <= newSeq){
				this.isSequenceInOrder = true;
			}
		}else{
			this.isSequenceInOrder = true;
		}
	}
	public void setHashTagBlankLineOrTS() {
		this.isHashTagBlankLineOrTS = (line.isEmpty()) ? true : false 
				|| Util.getFirstChar(line).equals("#") 
				|| line.contains(Constants.TS);
	}

	public void setNotMultipleHashTags() {
		this.isNotMultipleHashTags = !Util.isMultipleOccurances(line,"#");
	}

}

package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bean.M3u8Bean;
import bean.ValidationBean;


@Path("/")
public class RootResource {
	private static final Logger LOG = LoggerFactory.getLogger(RootResource.class);
	private int oldSeq= 0;
	
    @GET
    @Path("/file")
    @Produces("application/xml")
    public String getValidation(@PathParam("url") String url) throws IOException {
        LOG.info("Set the file and validate", url);
        M3u8Bean mBean = new M3u8Bean();
        StringBuffer sb = null;
	
		try {
			mBean.setUrl("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
			//M3u8Bean.setUrl(url);
			sb = mBean.getM3u8File();
			mBean.setSbMain(sb);
			
		} catch (IOException e) {
			LOG.error("getValidation() - IOException:" + e);
		}
        return getAllValidatedLines(sb);
    }
    
    
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

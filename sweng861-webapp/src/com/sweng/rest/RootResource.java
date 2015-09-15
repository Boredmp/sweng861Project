package com.sweng.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.rest.bean.M3u8Bean;
import com.sweng.rest.impl.SwengImpl;

@Path("/")
public class RootResource {
	private static final Logger LOG = LoggerFactory.getLogger(RootResource.class);
	
    @GET
    @Path("/file")
    @Produces("application/xml")
    public String getValidation() throws IOException {
        LOG.info("Set the file and validate", "");
        SwengImpl sImpl = new SwengImpl();
	
//		try {
			//mBean.setUrl("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
			//mBean.setUrl("file:///C:/Users/cbraam/Downloads/ipadipad-high.m3u8");
			//M3u8Bean.setUrl(url);
			//sb = mBean.getM3u8File();
			//mBean.setSbMain(sb);
			
//		} catch (IOException e) {
//			LOG.error("getValidation() - IOException:" + e);
//		}
        return sImpl.getValidatedXML("file:///C:/Users/cbraam/Downloads/ipad.m3u8");
    }
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "Hello Jersey";
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}

}

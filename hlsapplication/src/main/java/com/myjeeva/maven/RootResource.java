/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.myjeeva.maven;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This Java class with be hosted in the URI path defined by the @Path annotation. @Path annotations on the methods
 * of this class always refer to a path relative to the path defined at the class level.
 * <p/>
 * For example, with 'http://localhost:8181/cxf' as the default CXF servlet path and '/crm' as the JAX-RS server path,
 * this class will be hosted in 'http://localhost:8181/cxf/crm/customerservice'.  An @Path("/customers") annotation on
 * one of the methods would result in 'http://localhost:8181/cxf/crm/customerservice/customers'.
 */
@Path("/")
public class RootResource {

    private static final Logger LOG = LoggerFactory.getLogger(RootResource.class);

    long currentId = 123;
    Map<Long, Customer> customers = new HashMap<Long, Customer>();
    Map<Long, Order> orders = new HashMap<Long, Order>();
    private MessageContext jaxrsContext;

    public RootResource() {
        init();
    }

    /**
     * This method is mapped to an HTTP GET of 'http://localhost:8181/cxf/crm/customerservice/customers/{id}'.  The value for
     * {id} will be passed to this message as a parameter, using the @PathParam annotation.
     * <p/>
     * The method returns a Customer object - for creating the HTTP response, this object is marshaled into XML using JAXB.
     * <p/>
     * For example: surfing to 'http://localhost:8181/cxf/crm/customerservice/customers/123' will show you the information of
     * customer 123 in XML format.
     */
    @GET
    //@Path("/customers/{id}/")
    @Path("/file")
    @Produces("application/xml")
    public List<ValidationBean> getValidation(@PathParam("url") String url) {
        LOG.info("Set the file and validate", url);
        List<ValidationBean> allValidatedLines = null;
	
		try {
			M3u8Bean.setUrl("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
			//M3u8Bean.setUrl(url);
			StringBuffer sb = M3u8Bean.getM3u8File();
			M3u8Bean.setOutputFile(sb);
			
			allValidatedLines = getAllValidatedLines();
		} catch (IOException e) {
			LOG.error("getValidation() - IOException:" + e);
		}
        return allValidatedLines;
    }
    
    public List<ValidationBean> getAllValidatedLines() throws IOException{
    	List<ValidationBean> allValidatedLines = new ArrayList<ValidationBean>();
    	OutputStream os = null;
    	InputStream file = null;
    	BufferedReader br = null;
		try {
			file = new FileInputStream("src/main/resources/output.txt");

			InputStreamReader isr = new InputStreamReader(file,
					Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
	
			String line;
			while ((line = br.readLine()) != null) {
				ValidationBean vb = new ValidationBean(line, 0);
				allValidatedLines.add(vb);
				os = System.out;
				printValidations(os, vb);
			}
		} catch (IOException e) {
			LOG.error("getAllValidatedLines() - IOException:" + e);
		}finally{
			file.close();
			br.close();
		}
		return allValidatedLines;
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
    /**
     * Using HTTP PUT, we can can upload the XML representation of a customer object.  This operation will be mapped
     * to the method below and the XML representation will get unmarshaled into a real Customer object using JAXB.
     * <p/>
     * The method itself just updates the customer object in our local data map and afterwards uses the Reponse class to
     * build the appropriate HTTP response: either OK if the update succeeded (translates to HTTP Status 200/OK) or not
     * modified if the method failed to update a customer object (translates to HTTP Status 304/Not Modified).
     * <p/>
     * Note how this method is using the same @Path value as our next method - the HTTP method used will determine which
     * method is being invoked.
     */
    @PUT
    @Path("/customers/")
    @Consumes({"application/xml", "application/json" })
    @ApiOperation(value = "Update an existing Customer")
    @ApiResponses(value = {
                           @ApiResponse(code = 500, message = "Invalid ID supplied"),
                           @ApiResponse(code = 204, message = "Customer not found") 
                         })
    public Response updateCustomer(@ApiParam(value = "Customer object that needs to be updated", required = true) Customer customer) {
        LOG.info("Invoking updateCustomer, Customer name is: {}", customer.getName());
        Customer c = customers.get(customer.getId());
        Response r;
        if (c != null) {
            customers.put(customer.getId(), customer);
            r = Response.ok().build();
        } else {
            r = Response.notModified().build();
        }

        return r;
    }

    /**
     * Using HTTP POST, we can add a new customer to the system by uploading the XML representation for the customer.
     * This operation will be mapped to the method below and the XML representation will get unmarshaled into a real
     * Customer object.
     * <p/>
     * After the method has added the customer to the local data map, it will use the Response class to build the HTTP reponse,
     * sending back the inserted customer object together with a HTTP Status 200/OK.  This allows us to send back the
     * new id for the customer object to the client application along with any other data that might have been updated in
     * the process.
     * <p/>
     * Note how this method is using the same @Path value as our previous method - the HTTP method used will determine which
     * method is being invoked.
     */
    @POST
    @Path("/customers/")
    @Consumes({"application/xml", "application/json" })
    @ApiOperation(value = "Add a new Customer")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Invalid ID supplied"), })
    public Response addCustomer(@ApiParam(value = "Customer object that needs to be updated", required = true)
                                Customer customer) {
        LOG.info("Invoking addCustomer, Customer name is: {}", customer.getName());
        customer.setId(++currentId);
         
        customers.put(customer.getId(), customer);
        if (jaxrsContext != null && jaxrsContext.getHttpHeaders().getMediaType().getSubtype().equals("json")) {
            return Response.ok().type("application/json").entity(customer).build();
        } else {
            return Response.ok().type("application/xml").entity(customer).build();
        }
    }

    /**
     * This method is mapped to an HTTP DELETE of 'http://localhost:8181/cxf/crm/customerservice/customers/{id}'.  The value for
     * {id} will be passed to this message as a parameter, using the @PathParam annotation.
     * <p/>
     * The method uses the Response class to create the HTTP response: either HTTP Status 200/OK if the customer object was
     * successfully removed from the local data map or a HTTP Status 304/Not Modified if it failed to remove the object.
     */
    @DELETE
    @Path("/customers/{id}/")
    @ApiOperation(value = "Delete Customer")
    @ApiResponses(value = {
                           @ApiResponse(code = 500, message = "Invalid ID supplied"),
                           @ApiResponse(code = 204, message = "Customer not found") 
                         })
    public Response deleteCustomer(@ApiParam(value = "ID of Customer to delete", required = true) @PathParam("id") String id) {
        LOG.info("Invoking deleteCustomer, Customer id is: {}", id);
        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);

        Response r;
        if (c != null) {
            r = Response.ok().build();
            customers.remove(idNumber);
        } else {
            r = Response.notModified().build();
        }

        return r;
    }

    /**
     * This method is mapped to an HTTP GET of 'http://localhost:8181/cxf/crm/customerservice/orders/{id}'.  The value for
     * {id} will be passed to this message as a parameter, using the @PathParam annotation.
     * <p/>
     * The method returns an Order object - the class for that object includes a few more JAX-RS annotations, allowing it to
     * display one of these two outputs, depending on the actual URI path being used:
     * - display the order information itself in XML format
     * - display details about a product in the order in XML format in a path relative to the URI defined here
     */
    @Path("/orders/{orderId}/")
    public Order getOrder(@PathParam("orderId") String orderId) {
        LOG.info("Invoking getOrder, Order id is: {}", orderId);
        long idNumber = Long.parseLong(orderId);
        Order c = orders.get(idNumber);
        return c;
    }

    /**
     * The init method is used by the constructor to insert a Customer and Order object into the local data map
     * for testing purposes.
     */
    final void init() {
        Customer c = new Customer();
        c.setName("John");
        c.setId(123);
        customers.put(c.getId(), c);

        Order o = new Order();
        o.setDescription("order 223");
        o.setId(223);
        orders.put(o.getId(), o);
    }
    
    @Context
    public void setMessageContext(MessageContext messageContext) {
        this.jaxrsContext = messageContext;
    }
}

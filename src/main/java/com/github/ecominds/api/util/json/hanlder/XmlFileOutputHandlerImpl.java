/**
 * @author	: Rajiv Kumar
 * @project	: json-xml-parser
 * @since	: 1.0.0
 * @date	: 20-Mar-2018
 */

package com.github.ecominds.api.util.json.hanlder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class XmlFileOutputHandlerImpl extends AbstractOutputHandler{
	
	private final String xmlFilePath;
	private BufferedWriter writer;

	public XmlFileOutputHandlerImpl(String xmlFilePath){
		this.xmlFilePath = xmlFilePath;
	}

	protected void initResourceHandler()throws IOException{
		this.writer = new BufferedWriter(new FileWriter(xmlFilePath));
	}
	
	protected void releaseResourceHandler()throws IOException{
		writer.flush();
		writer.close();
	}

	/**
	 * @param value
	 */
	protected void print(String value)throws IOException{
		writer.write(value + "\n");
	}
}
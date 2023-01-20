/**
 * @author	: Rajiv Kumar
 * @project	: json-xml-parser
 * @since	: 1.0.0
 * @date	: 20-Mar-2018
 */

package com.github.ecominds.api.util.json.hanlder;

import java.io.IOException;

public interface IOutputHandler {
	String FORMAT_PREFIX = "\t";
	
	void init()throws IOException;
	void close()throws IOException;
	
	void writeStartField(String fieldName);
	void writeEndField(String fieldName);
	
	void writeFieldValue(String fieldName, String value);
}
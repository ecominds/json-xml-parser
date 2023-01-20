/**
 * @author	: Rajiv Kumar
 * @project	: json-xml-parser
 * @since	: 1.0.0
 * @date	: 20-Mar-2018
 */

package com.github.ecominds.api.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.ecominds.api.util.json.hanlder.IOutputHandler;

public final class JsonToXmlParser {
	private static Logger logger = Logger.getLogger(JsonToXmlParser.class);
	
	private Integer currentIndex;
	private Map<Integer, JsonArrayInfo> jsonArrMap;
	
	public JsonToXmlParser(){
		currentIndex = 0;
		jsonArrMap = new HashMap<Integer, JsonToXmlParser.JsonArrayInfo>();
	}
	
	public void parse(String jsonFilePath, IOutputHandler outputHandler)throws IOException{
		InputStream inputStream = null;
		try{
			inputStream = new BufferedInputStream(new FileInputStream(jsonFilePath));
			parse(inputStream, outputHandler);
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException ex) {
					logger.fatal("Exception in closing the connection", ex);
				}
			}
		}
	}
	
	public void parse(InputStream inputStream, IOutputHandler outputHandler)throws IOException{
		parseJson(new JsonFactory().createParser(inputStream), outputHandler);
	}
	
	/**
	 * @param createParser
	 * @param outputHandler
	 * @throws IOException 
	 */
	private void parseJson(JsonParser jsonParser, IOutputHandler outputHandler) throws IOException {
		JsonToken token = null;
		String fieldName = null;
		JsonArrayInfo jsonArr = null;
		
		outputHandler.init();
		
		while((token = jsonParser.nextToken()) != null){
			fieldName = jsonParser.getCurrentName();
			if(fieldName == null){
				continue;
			}
			
			switch(token){
				case START_ARRAY:{
					jsonArr = new JsonArrayInfo(fieldName);
					outputHandler.writeStartField(jsonArr.getArrayFieldName());
					
					currentIndex++;
					jsonArrMap.put(currentIndex, jsonArr);
					break;
				}
				case END_ARRAY:{
					outputHandler.writeEndField(jsonArr.getArrayFieldName());
					jsonArr.endArray();

					currentIndex--;
					jsonArr = jsonArrMap.get(currentIndex);
					break;
				}
				case START_OBJECT:{
					endJsonArr(outputHandler, jsonArr, fieldName, null);
					outputHandler.writeStartField(fieldName);
					break;
				}
				case END_OBJECT:{
					outputHandler.writeEndField(fieldName);
					break;
				}
				case FIELD_NAME:{
					break;
				}
				default:{
					// Other than above (for all values)
					String value = jsonParser.getText();
					if(endJsonArr(outputHandler, jsonArr, fieldName, value)){
						break;
					}
					outputHandler.writeFieldValue(fieldName, value);
				}
			}
		}
		outputHandler.close();
	}
	
	private boolean endJsonArr(IOutputHandler outputHandler, JsonArrayInfo jsonArr, 
			String fieldName, String value){
		if(jsonArr != null){
			if(jsonArr.contains(fieldName)){
				outputHandler.writeEndField(jsonArr.getArrayFieldName());
				outputHandler.writeStartField(jsonArr.getArrayFieldName());
				if(value != null){
					outputHandler.writeFieldValue(fieldName, value);
				}
				jsonArr.endArray(fieldName);
				return true;
			}
			jsonArr.addAttFieldName(fieldName);
		}
		return false;
	}

	private class JsonArrayInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		private final String fieldName;
		private final List<String> attrCol;
		
		public JsonArrayInfo(String arrayFieldName){
			this.fieldName = arrayFieldName;
			this.attrCol = new ArrayList<String>();
		}
		
		public String getArrayFieldName(){
			return fieldName;
		}
		
		public void endArray(){
			attrCol.clear();
		}
		
		public void endArray(String attFieldName){
			endArray();
			addAttFieldName(attFieldName);
		}

		public void addAttFieldName(String attFieldName) {
			attrCol.add(attFieldName);
		}
		
		public boolean contains(String attFieldName) {
			return attrCol.contains(attFieldName);
		}
	}
}
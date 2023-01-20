/**
 * @author	: Rajiv Kumar
 * @project	: json-xml-parser
 * @since	: 1.0.0
 * @date	: 20-Mar-2018
 */

package com.github.ecominds.api.util.json.hanlder;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.github.ecominds.api.util.AppConfigConst;

public abstract class AbstractOutputHandler implements IOutputHandler{
	protected final Logger logger;
	
	private int indentLevel;
	
	protected AbstractOutputHandler(){
		logger = Logger.getLogger(this.getClass());
		this.indentLevel = 0;
	}
	
	@Override
	public void init() throws IOException {
		initResourceHandler();
		if(AppConfigConst.isWriteXmlStart){
			print("<?xml version=\"1.0\" encoding=\"UAT-8\" ?>");
		}
		print("<" + AppConfigConst.rootNode + ">");
	}

	@Override
	public void close() throws IOException {
		print("</" + AppConfigConst.rootNode + ">");
		releaseResourceHandler();
	}
	
	@Override
	public final void writeStartField(String fieldName) {
		indentLevel++;
		printWithFormat("<" + fieldName + ">");
	}

	@Override
	public final void writeEndField(String fieldName) {
		printWithFormat("</" + fieldName + ">");
		indentLevel--;
	}

	@Override
	public final void writeFieldValue(String fieldName, String value) {
		String printVal = "<" + fieldName + ">" + value + "</" + fieldName + ">";
		if(AppConfigConst.isFormattingEnabled){
			printVal = FORMAT_PREFIX + printVal;
		}
		printWithFormat(printVal);
	}
	
	/**
	 * @param value
	 */
	private void printWithFormat(String value) {
		if(AppConfigConst.isFormattingEnabled){
			StringBuilder buf = new StringBuilder();
			for(int index=0;index<indentLevel;index++){
				buf.append(FORMAT_PREFIX);
			}
			value = buf.toString() + value;
		}
		try{
			print(value);
		}catch(IOException ex){
			logger.fatal("Exception in writing value : " + value, ex);
		}
	}
	
	protected abstract void initResourceHandler()throws IOException;
	protected abstract void releaseResourceHandler()throws IOException;
	protected abstract void print(String value)throws IOException;
}
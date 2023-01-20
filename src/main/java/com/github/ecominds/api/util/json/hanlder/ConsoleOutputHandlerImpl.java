/**
 * @author	: Rajiv Kumar
 * @project	: json-xml-parser
 * @since	: 1.0.0
 * @date	: 20-Mar-2018
 */

package com.github.ecominds.api.util.json.hanlder;

import java.io.IOException;

public class ConsoleOutputHandlerImpl extends AbstractOutputHandler{

	@Override
	protected void initResourceHandler() throws IOException {}

	@Override
	protected void releaseResourceHandler() throws IOException {}

	@Override
	protected void print(String value) throws IOException {
		System.out.println(value);
	}

}
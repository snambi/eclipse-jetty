package net.sourceforge.eclipsejetty.tomcat7;

import java.util.List;

import net.sourceforge.eclipsejetty.common.AbstractServerConfiguration;
import net.sourceforge.eclipsejetty.util.DOMBuilder;

public class Tomcat7ServerConfiguration extends AbstractServerConfiguration{

	@Override
	protected void buildThreadPool(DOMBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildHttpConfig(DOMBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<String> getJNDIItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildConnector(DOMBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getDefaultHandlerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildExtraOptions(DOMBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getDocType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getClassToConfigure() {
		// TODO Auto-generated method stub
		return null;
	}

}

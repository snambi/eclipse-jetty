package net.sourceforge.eclipsejetty.tomcat7;

import java.util.List;

import net.sourceforge.eclipsejetty.common.AbstractServerConfiguration;
import net.sourceforge.eclipsejetty.util.DOMBuilder;

public class Tomcat7ServerConfiguration extends AbstractServerConfiguration{

	@Override
	protected void buildThreadPool(DOMBuilder builder) {
		
	}

	@Override
	protected void buildHttpConfig(DOMBuilder builder) {
		
	}

	@Override
	protected List<String> getJNDIItems() {
		return null;
	}

	@Override
	protected void buildConnector(DOMBuilder builder) {
		
	}

	@Override
	protected String getDefaultHandlerClass() {
		return null;
	}

	@Override
	protected void buildExtraOptions(DOMBuilder builder) {
		System.out.println("buildextraOptions");
		System.out.println(builder);
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

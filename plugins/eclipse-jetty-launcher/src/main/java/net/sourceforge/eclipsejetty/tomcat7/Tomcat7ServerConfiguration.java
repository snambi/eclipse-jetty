package net.sourceforge.eclipsejetty.tomcat7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	/**
	 * <code>
     * <Context path="/mywebapp" docBase="/Users/theuser/mywebapp/src/main/webapp" >
			<Resources className="org.apache.naming.resources.VirtualDirContext"
				extraResourcePaths="/WEB-INF/classes=/Users/theuser/mywebapp/target/classes"/>
			<Loader className="org.apache.catalina.loader.VirtualWebappLoader"
				virtualClasspath="/Users/theuser/mywebapp/target/classes;/Users/theuser/mylib/target/classes;/Users/theuser/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar"
			/>
			<JarScanner scanAllDirectories="true" />
		</Context>
     * </code>
	 */
	@Override
    public DOMBuilder build()
    {
        DOMBuilder builder = new DOMBuilder();

        String contextPath = getDefaultContextPath();
        if( contextPath.trim().equals("/")){
        	contextPath = "";
        }
        if( contextPath.toLowerCase().trim().equals("/root")){
        	contextPath = "";
        }
        if( contextPath.toLowerCase().trim().equals("root")){
        	contextPath = "";
        }
        
        builder.begin("Context");
        builder.attribute("docBase", getDefaultWar()).attribute("path", contextPath);
        buildContent(builder);
        builder.end();

        return builder;
    }
	
    @Override
    protected void buildContent(DOMBuilder builder)
    {
    	builder.begin("Resources");
    	builder.attribute("className", "org.apache.naming.resources.VirtualDirContext");
    	builder.attribute("extraResourcePaths", getDefaultWar());
    	builder.end();
    	
    	builder.begin("Loader");
    	builder.attribute("className", "org.apache.catalina.loader.VirtualWebappLoader");
    	builder.attribute("virtualClasspath", link(getDefaultClasspath() ));
    	builder.end();
    }
    
	@Override
    public void write(File file) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);

        try
        {
            build().write(out);
        }
        finally
        {
            out.close();
        }
    }
}

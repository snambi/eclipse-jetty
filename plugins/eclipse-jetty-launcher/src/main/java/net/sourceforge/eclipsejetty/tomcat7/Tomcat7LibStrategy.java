package net.sourceforge.eclipsejetty.tomcat7;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

import net.sourceforge.eclipsejetty.jetty.DependencyBasedContainerLibStrategy;

import org.eclipse.core.runtime.CoreException;

// TODO:  refactor the class hierarchy to fit Tomcat and other containers.
public class Tomcat7LibStrategy extends DependencyBasedContainerLibStrategy {

	public Collection<File> find(File containerPath, boolean jspSupport,
			boolean jmxSupport, boolean jndiSupport, boolean ajpSupport)
			throws CoreException {
		
		Collection<String> dependencies = new LinkedHashSet<String>();

        addServerDependencies(dependencies);

        if (jspSupport)
        {
            addJSPDependencies(dependencies);
        }

        if (jmxSupport)
        {
            addJMXDependencies(dependencies);
        }

        if (jndiSupport)
        {
            addJNDIDependencies(dependencies);
        }

        if (ajpSupport)
        {
            addAJPDependencies(dependencies);
        }

        Collection<File> results = new LinkedHashSet<File>();

        resolveDependencies(results, containerPath, dependencies);

        return results;
	}

	@Override
	protected void addServerDependencies(Collection<String> dependencies) {
		dependencies.add("bin/bootstrap.jar");
		dependencies.add("bin/tomcat-juli.jar");
	}

	@Override
	protected void addJMXDependencies(Collection<String> dependencies) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addJNDIDependencies(Collection<String> dependencies) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addJSPDependencies(Collection<String> dependencies) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAJPDependencies(Collection<String> dependencies) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resolveDependencies(Collection<File> results, File path,
			Collection<String> dependencies) throws CoreException {
		
		for( String dependency: dependencies ){
			File d = new File(path, dependency );
			if( d.exists() ){
				results.add(d);
			}else{
				System.err.println("dependency " + dependency + " doesn't exist");
			}
		}
		
	}

}

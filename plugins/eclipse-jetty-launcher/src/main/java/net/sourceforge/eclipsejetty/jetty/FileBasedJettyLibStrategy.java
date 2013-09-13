// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package net.sourceforge.eclipsejetty.jetty;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.eclipsejetty.JettyPlugin;
import net.sourceforge.eclipsejetty.common.IJettyLibStrategy;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Abstract implementation of the {@link IJettyLibStrategy} for external Jetties. Assumes that the libraries are in the
 * lib folder
 * 
 * @author Manfred Hantschel
 */
public abstract class FileBasedJettyLibStrategy extends DependencyBasedJettyLibStrategy
{

    /* (non-Javadoc)
     * @see net.sourceforge.eclipsejetty.jetty.DependencyBasedJettyLibStrategy#resolveDependencies(java.util.Collection, java.util.Collection)
     */
    @Override
    protected void resolveDependencies(Collection<File> results, File path, Collection<String> dependencies)
        throws CoreException
    {
        final File libPath = new File(path, "lib");

        if (!libPath.exists() || !libPath.isDirectory())
        {
            throw new CoreException(new Status(IStatus.ERROR, JettyPlugin.PLUGIN_ID, "Could not find Jetty libs"));
        }

        Collection<String> resolvedDependencies = new HashSet<String>();

        crawlDependencies(results, libPath, dependencies, resolvedDependencies);

        dependencies.removeAll(resolvedDependencies);

        if (dependencies.size() > 0)
        {
            throw new CoreException(new Status(IStatus.ERROR, JettyPlugin.PLUGIN_ID,
                "Failed to resolve Jetty dependencies: " + dependencies));
        }
    }

    protected void crawlDependencies(Collection<File> results, File path, Collection<String> dependencies,
        Collection<String> resolvedDependencies)
    {
        for (File file : path.listFiles())
        {
            if (file.isDirectory())
            {
                if (isPathIncluded(file, dependencies))
                {
                    crawlDependencies(results, file, dependencies, resolvedDependencies);
                }
            }
            else if (isFileIncluded(file, dependencies, resolvedDependencies))
            {
                results.add(file);
            }
        }
    }

    protected boolean isPathIncluded(File path, Collection<String> dependencies)
    {
        return true;
    }

    protected boolean isFileIncluded(File file, Collection<String> dependencies, Collection<String> resolvedDependencies)
    {
        String path = file.getPath().replace('\\', '/');

        for (String dependency : dependencies)
        {
            if (path.matches(dependency))
            {
                resolvedDependencies.add(dependency);

                return true;
            }
        }

        return false;
    }
}

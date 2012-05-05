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
package net.sourceforge.eclipsejetty;

import java.io.File;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.eclipsejetty.jetty.JettyVersion;
import net.sourceforge.eclipsejetty.util.RegularMatcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;

/**
 * Some utilities
 * 
 * @author Christian K&ouml;berl
 * @author Manfred Hantschel
 */
public class JettyPluginUtils
{

    /**
     * Returns the Jetty version.
     * 
     * @param embedded true if embedded
     * @param jettyPath the path of the Jetty installation
     * @return the version, not AUTO
     * @throws IllegalArgumentException if the detection fails
     */
    public static JettyVersion detectJettyVersion(boolean embedded, final String jettyPath)
        throws IllegalArgumentException
    {
        if (embedded)
        {
            return JettyVersion.JETTY_EMBEDDED;
        }

        final File jettyLibDir = new File(jettyPath, "lib");

        if (!jettyLibDir.exists() || !jettyLibDir.isDirectory())
        {
            throw new IllegalArgumentException("Could not find Jetty libs");
        }

        for (File file : jettyLibDir.listFiles())
        {
            if (!file.isFile())
            {
                continue;
            }

            String name = file.getName();

            if ((name.startsWith("jetty-")) && (name.endsWith(".jar")))
            {
                // jetty-6.1.26.jar - Jetty 6
                // jetty-server-7.6.3.v20120416.jar - Jetty 7
                // jetty-server-8.1.3.v20120416.jar - Jetty 8

                if (name.contains("-6."))
                {
                    return JettyVersion.JETTY_6;
                }
                else if (name.contains("-7."))
                {
                    return JettyVersion.JETTY_7;
                }
                else if (name.contains("-8."))
                {
                    return JettyVersion.JETTY_8;
                }
            }
        }

        throw new IllegalArgumentException("Failed to detect Jetty version.");
    }

    public static List<RegularMatcher> extractPatterns(final List<RegularMatcher> list, final String... text)
        throws IllegalArgumentException
    {
        for (final String entry : text)
        {
            if (entry.trim().length() > 0)
            {
                try
                {
                    list.add(new RegularMatcher(entry));
                }
                catch (final PatternSyntaxException e)
                {
                    throw new IllegalArgumentException("Invalid pattern: " + entry + " (" + e.getMessage() + ")", e);
                }
            }
        }

        return list;
    }

    public static String link(String[] values)
    {
        StringBuilder result = new StringBuilder();

        if (values != null)
        {
            for (int i = 0; i < values.length; i += 1)
            {
                if (i > 0)
                {
                    result.append(File.pathSeparator);
                }

                result.append(values[i]);
            }
        }

        return result.toString();
    }

    public static String resolveVariables(String s)
    {
        try
        {
            s = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(s);
        }
        catch (CoreException e)
        {
            // ignore
        }

        return s;
    }

}

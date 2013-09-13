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
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.eclipsejetty.common.ContainerVersion;
import net.sourceforge.eclipsejetty.util.RegularMatcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;

/**
 * Some utilities
 * 
 * @author Christian K&ouml;berl
 * @author Manfred Hantschel
 */
public class JettyPluginUtils
{

    /**
     * A collator set to primary strength, which means 'a', 'A' and '&auml;' is the same
     */
    public static final Collator DICTIONARY_COLLATOR;

    public static final Comparator<String> DICTIONARY_COMPARATOR = new Comparator<String>()
    {

        public int compare(String left, String right)
        {
            return dictionaryCompare(left, right);
        }

    };

    static
    {
        DICTIONARY_COLLATOR = Collator.getInstance();

        DICTIONARY_COLLATOR.setStrength(Collator.PRIMARY);
        DICTIONARY_COLLATOR.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
    }

    public static boolean equals(final Object obj0, final Object obj1)
    {
        return ((obj0 == null) && (obj1 == null)) || ((obj0 != null) && (obj0.equals(obj1)));
    }

    /**
     * Compares the two objects. If one of the objects is null, it will always be greater than the other object. If both
     * objects are null, they are equal.
     * 
     * @param <TYPE> the type of the object
     * @param left the first object
     * @param right the second object
     * @return the result of the compare function
     */
    public static <TYPE extends Comparable<TYPE>> int compare(final TYPE left, final TYPE right)
    {
        if (left == null)
        {
            if (right != null)
            {
                return 1;
            }
        }
        else
        {
            if (right != null)
            {
                return left.compareTo(right);
            }

            return -1;
        }

        return 0;
    }

    /**
     * Compares the two objects. If one of the objects is null, it will always be greater than the other object. If both
     * objects are null, they are equal. Uses the comparator to compare the objects.
     * 
     * @param <TYPE> the type of the object
     * @param comparator the comparator to be used
     * @param left the first object
     * @param right the second object
     * @return the result of the compare function
     */
    public static <TYPE> int compare(final Comparator<TYPE> comparator, final TYPE left, final TYPE right)
    {
        if (left == null)
        {
            if (right != null)
            {
                return 1;
            }
        }
        else
        {
            if (right != null)
            {
                return comparator.compare(left, right);
            }

            return -1;
        }

        return 0;
    }

    /**
     * Compares the strings using a dictionary collator. If one of the objects is null, it will always be greater than
     * the other object. If both objects are null, they are equal.
     * 
     * @param left the first string
     * @param right the second string
     * @return the result of the compare function
     */
    public static int dictionaryCompare(final String left, final String right)
    {
        return compare(DICTIONARY_COLLATOR, left, right);
    }

    /**
     * Returns the Jetty version.
     * 
     * @param embedded true if embedded
     * @param jettyPath the path of the Jetty installation
     * @return the version, not AUTO
     * @throws IllegalArgumentException if the detection fails
     */
    public static ContainerVersion detectJettyVersion(boolean embedded, final String jettyPath)
        throws IllegalArgumentException
    {
        if (embedded)
        {
            return ContainerVersion.JETTY_EMBEDDED;
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
                    return ContainerVersion.JETTY_6;
                }
                else if (name.contains("-7."))
                {
                    return ContainerVersion.JETTY_7;
                }
                else if (name.contains("-8."))
                {
                    return ContainerVersion.JETTY_8;
                }
                else if (name.contains("-9."))
                {
                    return ContainerVersion.JETTY_9;
                }
            }
        }

        throw new IllegalArgumentException("Failed to detect Jetty version.");
    }
    
    public static ContainerVersion detectTomcatVersion( boolean embedded , final String tomcatPath ){
    	// TODO: properly detect the version
    	return ContainerVersion.TOMCAT_7;
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
                    list.add(new RegularMatcher(entry.trim()));
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
                    // result.append(File.pathSeparator); // it seems, Jetty was built for Windows
                    result.append(";");
                }

                result.append(values[i]);
            }
        }

        return result.toString();
    }

    public static String resolveVariables(String s)
    {
        if (s == null)
        {
            return null;
        }

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

    public static String[] toLocationArray(Collection<IRuntimeClasspathEntry> classpathEntries)
    {
        return toLocationArray(classpathEntries.toArray(new IRuntimeClasspathEntry[classpathEntries.size()]));
    }

    public static String[] toLocationArray(IRuntimeClasspathEntry... classpathEntries)
    {
        Collection<String> results = toLocationCollection(classpathEntries);

        return results.toArray(new String[results.size()]);
    }

    public static Collection<String> toLocationCollection(Collection<IRuntimeClasspathEntry> classpathEntries)
    {
        return toLocationCollection(classpathEntries.toArray(new IRuntimeClasspathEntry[classpathEntries.size()]));
    }

    public static Collection<String> toLocationCollection(IRuntimeClasspathEntry... classpathEntries)
    {
        Set<String> results = new LinkedHashSet<String>();

        for (IRuntimeClasspathEntry entry : classpathEntries)
        {
            String location = toLocation(entry);

            if (location != null)
            {
                results.add(location);
            }
        }

        return results;
    }

    public static String toLocation(IRuntimeClasspathEntry entry)
    {
        String location = entry.getLocation();

        if (location == null)
        {
            return null;
        }

        return location.replace('\\', '/');
    }

    public static String getMavenScope(IRuntimeClasspathEntry entry)
    {
        IClasspathEntry classpathEntry = entry.getClasspathEntry();

        if (classpathEntry == null)
        {
            return "";
        }

        IClasspathAttribute[] extraAttributes = classpathEntry.getExtraAttributes();

        for (IClasspathAttribute extraAttribute : extraAttributes)
        {
            if ("maven.scope".equals(extraAttribute.getName()))
            {
                return extraAttribute.getValue();
            }
        }

        return "";
    }

}

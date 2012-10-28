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
package net.sourceforge.eclipsejetty.jetty6;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.eclipsejetty.jetty.AbstractServerConfiguration;
import net.sourceforge.eclipsejetty.util.DOMBuilder;

/**
 * Configuration for Jetty 6
 * 
 * @author Manfred Hantschel
 */
public class Jetty6ServerConfiguration extends AbstractServerConfiguration
{

    public Jetty6ServerConfiguration()
    {
        super();
    }

    @Override
    protected String getDocType()
    {
        // <!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://jetty.mortbay.org/configure.dtd">
        return null;
    }

    @Override
    protected void buildThreadPool(DOMBuilder builder)
    {
        builder.begin("Set").attribute("name", "ThreadPool");

        builder.begin("New").attribute("class", "org.mortbay.thread.QueuedThreadPool");
        builder.element("Set", "name", "minThreads", 2);
        builder.element("Set", "name", "maxThreads", 10);
        builder.element("Set", "name", "lowThreads", 4);
        builder.end();

        builder.end();
    }

    @Override
    protected void buildHttpConfig(DOMBuilder builder)
    {
        // nothing to do
    }

    @Override
    protected List<String> getJNDIItems()
    {
        return Arrays.asList("org.mortbay.jetty.webapp.WebInfConfiguration",
            "org.mortbay.jetty.plus.webapp.EnvConfiguration", "org.mortbay.jetty.plus.webapp.Configuration",
            "org.mortbay.jetty.webapp.JettyWebXmlConfiguration", "org.mortbay.jetty.webapp.TagLibConfiguration");
    }

    @Override
    protected String getClassToConfigure()
    {
        return "org.mortbay.jetty.Server";
    }

    @Override
    protected void buildConnector(DOMBuilder builder)
    {
        if (getPort() != null)
        {
            builder.begin("Call").attribute("name", "addConnector");
            builder.begin("Arg");
            builder.begin("New").attribute("class", "org.mortbay.jetty.nio.SelectChannelConnector");
            builder.element("Set", "name", "port", getPort());
            builder.element("Set", "name", "maxIdleTime", 30000);
            builder.element("Set", "name", "Acceptors", 2);
            builder.element("Set", "name", "statsOn", false);
            builder.end();
            builder.end();
            builder.end();
        }
    }

    @Override
    protected String getDefaultHandlerClass()
    {
        return "org.mortbay.jetty.webapp.WebAppContext";
    }

    @Override
    protected void buildExtraOptions(DOMBuilder builder)
    {
        builder.element("Set", "name", "stopAtShutdown", true);
        builder.element("Set", "name", "sendServerVersion", true);
        builder.element("Set", "name", "sendDateHeader", true);
        builder.element("Set", "name", "gracefulShutdown", 1000);
    }

}

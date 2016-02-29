/*
 * Copyright 2016 Blue Lotus Software, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluelotussoftware.tomcat.embedded;

import com.bluelotussoftware.servlet.HelloServlet;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * The class controls a configured Apache Tomcat instance using SSL/TLS.
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */
public class TomcatEmbeddedRunner {

    protected Tomcat tomcat;
    /*
     * The keyAlias, keystorePass, and keystoreFile attributes need to be changed, if you add a different certificate 
     * to the project with new values.
     */
    private final String keyAlias = "tomcat";
    private final String keystorePass = "changeit";
    private final Path keystoreFile = FileSystems.getDefault().getPath("src", "main", "resources", "keystore.jks");

    /**
     * <p>
     * Starts the configured Apache Tomcat instance on port 8443, e.g.
     * <a href="https://localhost:8443/hello">https://localhost:8443/hello</a>
     * or
     * <a href="https://localhost:8443/web/hello">https://localhost:8443/web/hello</a>
     * for Servlet 3.0.
     * </p>
     *
     * @throws LifecycleException if the application can not be started.
     */
    public void start() throws LifecycleException {

        Connector httpsConnector = new Connector();
        httpsConnector.setPort(8443);
        httpsConnector.setSecure(true);
        httpsConnector.setScheme("https");
        httpsConnector.setAttribute("keyAlias", keyAlias);
        httpsConnector.setAttribute("keystorePass", keystorePass);
        httpsConnector.setAttribute("keystoreFile", keystoreFile.toFile().getAbsolutePath());
        httpsConnector.setAttribute("clientAuth", "false");
        httpsConnector.setAttribute("sslProtocol", "TLS");
        httpsConnector.setAttribute("SSLEnabled", true);

        tomcat = new Tomcat();
        tomcat.setBaseDir("target/tomcat");
        tomcat.setPort(8443);

        Service service = tomcat.getService();
        service.addConnector(httpsConnector);
        tomcat.setConnector(httpsConnector);

        Connector[] connectors = tomcat.getService().findConnectors();
        for (Connector connector : connectors) {
            System.out.println("Protocol Handler: " + connector.getProtocolHandlerClassName() + " port: " + connector.getPort());
        }

        // Servlet API v >= 2.5 
        addHelloServlet();

        // Servlet API v.3.0+
        addHelloWebApplication();

        tomcat.start();
        tomcat.getServer().await();
    }

    /**
     * Stops the configured Apache Tomcat instance.
     *
     * @throws LifecycleException if the service can not be stopped, or an issue
     * occurs during shutdown.
     */
    public void stop() throws LifecycleException {
        if (tomcat != null) {
            tomcat.stop();
        }
    }

    /**
     * Add a Servlet to the server.
     */
    private void addHelloServlet() {
        Path basePath = FileSystems.getDefault().getPath("target");
        Context rootCtx = tomcat.addContext("", basePath.toFile().getAbsolutePath());
        Tomcat.addServlet(rootCtx, "Hello", new HelloServlet());
        rootCtx.addServletMapping("/hello", "Hello");
    }

    /**
     * Add a Servlet 3.0 application to the server.
     */
    private void addHelloWebApplication() {
        try {
            //Add Servlet 3.0
            StandardContext ctx = (StandardContext) tomcat.addWebapp("/web",
                    new File("src/main/webapp").getAbsolutePath());

            // Setting the parent class loader is required, otherwise ClassNotFoundExceptions are likely.
            ctx.setParentClassLoader(TomcatEmbeddedRunner.class.getClassLoader());

            WebResourceRoot resources = new StandardRoot(ctx);
            Path classes = FileSystems.getDefault().getPath("target", "classes");
            WebResourceSet resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", classes.toFile().getAbsolutePath(), "/");
            resources.addPreResources(resourceSet);
            ctx.setResources(resources);
        } catch (ServletException ex) {
            ex.printStackTrace(System.err);
        }
    }
}

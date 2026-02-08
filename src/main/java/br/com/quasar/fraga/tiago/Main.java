package br.com.quasar.fraga.tiago;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        // Inicializa CDI com Weld
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        ResteasyCdiExtension cdiExtension = new ResteasyCdiExtension();
        // Registra o extension no Weld (para que seja ativado no bootstrap)
        weld.extensions(cdiExtension);
        weld.addExtension(cdiExtension);

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute("resteasy.cdi.container", container);
        context.setAttribute("resteasy.cdi.extension", cdiExtension);
        context.setContextPath("/");
        context.setAttribute("resteasy.cdi.extension", new ResteasyCdiExtension());
        context.setAttribute("org.jboss.weld.environment.container.instance", container); // Integra CDI com Jetty

        // Configura recursos estáticos (index.html)
        URL webappUrl = Main.class.getClassLoader().getResource("webapp");
        if (webappUrl == null) {
            throw new RuntimeException("Diretório 'webapp' não encontrado no classpath");
        }

        ResourceFactory resourceFactory = ResourceFactory.of(context);
        String resourceBase = webappUrl.toExternalForm();
        Resource baseResource = resourceFactory.newResource(resourceBase);

        context.setBaseResource(baseResource);

        // DefaultServlet para arquivos estáticos em /
        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase", resourceBase);
        defaultServlet.setInitParameter("dirAllowed", "false");
        defaultServlet.setInitParameter("welcomeServlets", "true");
        context.addServlet(defaultServlet, "/");

        ServletHolder resteasyHolder = new ServletHolder("resteasy", HttpServletDispatcher.class);
        resteasyHolder.setInitParameter("jakarta.ws.rs.Application", "br.com.quasar.fraga.tiago.MyApplication");
        resteasyHolder.setInitParameter("resteasy.resources", "br.com.quasar.fraga.tiago.RestResource");  // Força registro explícito do resource
        resteasyHolder.setInitParameter("resteasy.scan.resources", "true");  // Ativa scan adicional se necessário
        resteasyHolder.setInitOrder(1);
        //context.addServlet(resteasyHolder, "/jeeboot/*");
        context.addServlet(resteasyHolder, "/*");

        server.setHandler(context);

        System.out.println("Servidor Jetty iniciado na porta 8080 com CDI e JAX-RS");
        System.out.println(" - Raiz estática: " + resourceBase);
        System.out.println(" - REST: /jeeboot/rest");
        server.start();
        server.join();

        // Shutdown CDI ao parar
        weld.shutdown();
    }
}
package br.com.quasar.fraga.tiago;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Configura o resource base para o diretório webapp dentro do classpath/JAR
        URL webappUrl = Main.class.getClassLoader().getResource("webapp");
        if (webappUrl == null) {
            throw new RuntimeException("Diretório 'webapp' não encontrado no classpath (src/main/resources/webapp)");
        }

        // Importante: usa o path completo do diretório (sem o arquivo específico)
        String resourceBase = webappUrl.toExternalForm();
        context.setResourceBase(resourceBase);

        // Adiciona o DefaultServlet mapeado para servir arquivos estáticos (inclui index.html automático)
        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase", resourceBase); // redundante mas reforça
        defaultServlet.setInitParameter("dirAllowed", "false");        // não lista diretórios
        defaultServlet.setInitParameter("welcomeServlets", "true");    // tenta index.html etc.
        context.addServlet(defaultServlet, "/");                       // mapeia para raiz

        // Adiciona seu servlet REST (tem precedência sobre o DefaultServlet)
        ServletHolder restServlet = new ServletHolder(new RestServlet());
        context.addServlet(restServlet, "/rest");

        server.setHandler(context);

        System.out.println("Servidor Jetty iniciado na porta 8080");
        System.out.println(" - Raiz estática: " + resourceBase);
        server.start();
        server.join();
    }
}
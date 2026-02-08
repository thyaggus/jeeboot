package br.com.quasar.fraga.tiago;

import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(org.jboss.resteasy.cdi.ResteasyCdiExtension.class);
        classes.add(RestResource.class);
        // Adicione aqui outros resources ou providers se tiver
        return classes;
    }
}
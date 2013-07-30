package pt.ist.bennu.vaadin;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

@HandlesTypes({ EmbeddedComponent.class })
public class EmbeddedComponentInitializer implements ServletContainerInitializer {

    private static Logger LOG = LoggerFactory.getLogger(EmbeddedComponentInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException {
        if (classes != null) {
            for (Class<?> type : classes) {
                EmbeddedComponent annotation = type.getAnnotation(EmbeddedComponent.class);
                if (annotation != null) {
                    LOG.info("Got and EmbeddedComponent annotation: " + showAnnotation(annotation));
                    Class<? extends EmbeddedComponentContainer> embeddedComponentClass =
                            (Class<? extends EmbeddedComponentContainer>) type;
                    for (String path : annotation.path()) {
                        try {
                            LOG.info("Register page : " + embeddedComponentClass.getName());
                            EmbeddedApplication.addPage(embeddedComponentClass);
                        } catch (PatternSyntaxException e) {
                            throw new Error("Error interpreting pattern: " + path, e);
                        }
                    }
                }
            }
        }
    }

    private String showAnnotation(EmbeddedComponent annotation) {
        StringBuilder s = new StringBuilder();
        final String paths = Arrays.toString(annotation.path());
        s.append("paths: ");
        s.append(paths);
        final String args = Arrays.toString(annotation.args());
        s.append("args:");
        s.append(args);
        return s.toString();
    }

}

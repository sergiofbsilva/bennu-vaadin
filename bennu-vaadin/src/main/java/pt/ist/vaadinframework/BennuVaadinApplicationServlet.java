package pt.ist.vaadinframework;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.VirtualHostAwarePortalConfiguration;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class BennuVaadinApplicationServlet extends ApplicationServlet {

    private String vaadinHtml;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            StringWriter s = new StringWriter();
            IOUtils.copy(getServletContext().getResourceAsStream("/embedded/vaadin.html"), s);
            vaadinHtml = s.toString();
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void writeAjaxPage(HttpServletRequest request, HttpServletResponse response, Window window, Application application)
            throws IOException, MalformedURLException, ServletException {
        final BufferedWriter page = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
        page.write(apply("{{theme}}", getTheme()));
        page.close();
    }

    private String getTheme() {
        VirtualHostAwarePortalConfiguration.ensure();
        return Bennu.getInstance().getConfiguration().getTheme();
    }

    private String apply(String target, String replacement) {
        return vaadinHtml.replace(target, replacement);
    }

}

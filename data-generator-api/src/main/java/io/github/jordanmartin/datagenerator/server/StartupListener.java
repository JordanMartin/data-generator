package io.github.jordanmartin.datagenerator.server;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class StartupListener {

    @ConfigProperty(name = "quarkus.http.port")
    int httpPort;

    @ConfigProperty(name = "open-browser", defaultValue = "true")
    boolean openBrowser;

    void onStart(@Observes StartupEvent ev) {
        if ("prod".equals(ProfileManager.getActiveProfile()) && openBrowser) {
            openBrowserAsyncWhenPortIsSet();
        }
    }

    private void openBrowserAsyncWhenPortIsSet() {
        new Thread(() -> {
            if (waitPortIsSet()) {
                openInBrowser();
            }
        }).start();
    }

    private boolean waitPortIsSet() {
        int poolTime = 500;
        int maxTime = 5000;
        for (int duration = 0; duration < maxTime; duration += poolTime) {
            String httpPort = Optional.ofNullable(System.getProperty("quarkus.http.port"))
                    .orElse("0");
            this.httpPort = Integer.parseInt(httpPort);
            if (this.httpPort != 0) {
                return true;
            }
            try {
                Thread.sleep(poolTime);
            } catch (InterruptedException ignored) {
            }
        }
        return false;
    }

    private void openInBrowser() {
        String url = "http://localhost:" + httpPort;
        log.info("Serveur démarré: {}", url);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
            // There is no default browser
            return;
        }
        try {
            desktop.browse(new URI(url));
            log.info("Ouverture automatique du navagateur. Utilisez l'option -Dopen-browser=false pour le désactiver");
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Failed opening the default browser to show the URL (" + url + ").", e);
        }
    }

}
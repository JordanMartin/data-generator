package io.github.datagenerator.server;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
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

        createTrayIcon();
    }

    private void createTrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/tray-icon.png"));
            } catch (Exception ignored) {
            }
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open in browser");
            openItem.addActionListener(actionEvent -> {
                openInBrowser(false);
            });
            popup.add(openItem);
            MenuItem exitItem = new MenuItem("Shutdown server");
            exitItem.addActionListener(actionEvent -> {
                System.exit(0);
            });
            popup.add(exitItem);
            TrayIcon trayIcon = new TrayIcon(image, "Date generator ", popup);
            trayIcon.addActionListener(e -> {
                openInBrowser(false);
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException ignored) {
            }
        }
    }

    private void openBrowserAsyncWhenPortIsSet() {
        new Thread(() -> {
            if (waitPortIsSet()) {
                openInBrowser(true);
            }
        }).start();
    }

    private boolean waitPortIsSet() {
        int poolTime = 500;
        int maxTime = 5000;
        if (this.httpPort > 0) {
            return true;
        }
        for (int duration = 0; duration < maxTime; duration += poolTime) {
            String httpPort = Optional.ofNullable(System.getProperty("quarkus.http.port"))
                    .orElse("0");
            this.httpPort = Integer.parseInt(httpPort);
            if (this.httpPort > 0) {
                return true;
            }
            try {
                Thread.sleep(poolTime);
            } catch (InterruptedException ignored) {
            }
        }
        return false;
    }

    private void openInBrowser(boolean openAuto) {
        String url = "http://localhost:" + httpPort;
        log.info("Server started: {}", url);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
            // There is no default browser
            return;
        }
        try {
            desktop.browse(new URI(url));
            if (openAuto) {
                log.info("Browser automatically opened. Use option -Dopen-browser=false to disable");
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Failed opening the default browser to show the URL (" + url + ").", e);
        }
    }
}

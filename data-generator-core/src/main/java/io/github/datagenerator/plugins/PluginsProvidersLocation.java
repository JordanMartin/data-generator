package io.github.datagenerator.plugins;

/**
 * The class is only use to locate the destination package of the plugins
 */
public class PluginsProvidersLocation {

    private PluginsProvidersLocation() {
    }

    public static String getPackage() {
        return PluginsProvidersLocation.class.getPackageName();
    }

}

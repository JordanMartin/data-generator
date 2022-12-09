package io.github.datagenerator.domain.providers;

/**
 * The class is only use to locate the destination package of the core providers
 */
public class CoreProvidersLocation {

    private CoreProvidersLocation() {}

    public static String getPackage() {
        return CoreProvidersLocation.class.getPackageName();
    }
}

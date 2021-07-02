/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera;

import org.senera.senera.BuildConfig;

/**
 *
 * @author user
 */
public final class Version {

    private static final String VERSION = BuildConfig.VERSION_NAME;
    private static final String GROUPID = "${project.groupId}";
    private static final String ARTIFACTID = "${project.artifactId}";
    private static final String REVISION = "${buildNumber}";

    public static String getVersion() {
        return VERSION+"-ANDROID";
    }
}


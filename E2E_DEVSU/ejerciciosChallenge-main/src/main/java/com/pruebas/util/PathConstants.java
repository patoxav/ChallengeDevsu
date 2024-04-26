package com.pruebas.util;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import java.io.File;

public class PathConstants {
    private PathConstants() {
    }

    private static final EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();

    private static final String RESOURCES_PATH = System.getProperty("user.dir")
            + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;

    private static final String FEATURE_PATH = RESOURCES_PATH + "features" + File.separator;

    private static final String DATA_PATH = RESOURCES_PATH + "data" + File.separator;

    public static String resourcesPath() {
        if (variables.getProperty("resources.path") != null)
            return variables.getProperty("resources.path");
        return RESOURCES_PATH;
    }

    public static String featurePath() {
        if (variables.getProperty("features.path") != null)
            return variables.getProperty("features.path");
        return FEATURE_PATH;
    }

    public static String dataPath() {
        if (variables.getProperty("data.path") != null)
            return variables.getProperty("data.path");
        return DATA_PATH;
    }

    public static String validatePath(String path) {
        String[] separators = {"/", "\\", "-"};
        for (String separator : separators) {
            if (path.contains(separator)) {
                path = path.replace(separator, File.separator);
                break;
            }
        }
        return path;
    }
}

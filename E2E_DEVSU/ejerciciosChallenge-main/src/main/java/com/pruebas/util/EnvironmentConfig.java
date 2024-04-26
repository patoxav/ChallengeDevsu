package com.pruebas.util;

import net.thucydides.core.util.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

public class EnvironmentConfig {

    private static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public String getVariable(String variable) {
        String value = System.getenv(variable);//obtener desde variables de entorno
        if (value == null || value.isEmpty()) {
            value = System.getProperty(variable);//obtener desde variables del sistema
            if (value == null || value.isEmpty()) {
                value = environmentVariables.getProperty(variable);//obtener desde variables de sistema y serenity.properties
            }
        }
        return value == null ? "" : value;
    }

}
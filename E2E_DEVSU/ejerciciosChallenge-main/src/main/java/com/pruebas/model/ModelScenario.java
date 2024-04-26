package com.pruebas.model;

import io.cucumber.java.Scenario;

public class ModelScenario {

    private static Scenario scenario;

    private ModelScenario() {
        throw new IllegalStateException("Utility class");
    }

    public static Scenario getScenario() {
        return scenario;
    }

    public static void setScenario(Scenario scenario) {
        ModelScenario.scenario = scenario;
    }
}

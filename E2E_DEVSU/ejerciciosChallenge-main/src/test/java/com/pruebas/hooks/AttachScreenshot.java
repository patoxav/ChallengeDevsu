package com.pruebas.hooks;

import com.pruebas.util.AttachScreenshotToScenario;
import com.pruebas.util.PropertiesReader;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttachScreenshot extends AttachScreenshotToScenario {

    static PropertiesReader readProperties = new PropertiesReader();

    @After("not @Database and not @API")
    @AfterStep("not @manual and not @Database and not @API")
    public void attachScreenshotJsonReportForScenario(Scenario scenario) {
        boolean isManualScenario = false;

        try {
            String[] tagsScenario = scenario.getSourceTagNames().toArray(new String[0]);
            for (String lineTag : tagsScenario) {
                if (lineTag.trim().equalsIgnoreCase("@manual")) {
                    isManualScenario = true;
                    break;
                }
            }
            if (isManualScenario) {
                String addEvidenceOn = readProperties.getPropiedad("add.evidence.cucumber.on");
                if (addEvidenceOn.trim().equalsIgnoreCase("failed")) {
                    if (scenario.isFailed()) {
                        addScreenshotManualTest(scenario);
                    }
                } else {
                    addScreenshotManualTest(scenario);
                }
            } else {
                if (scenario.isFailed()) {
                    addScreenshot(scenario);
                }
            }
        } catch (Exception e) {
            log.warn("ERROR: al adjuntar imagen/evidencia al reporte JSON generado por cucumber:" + e.getMessage());
        }

    }
}
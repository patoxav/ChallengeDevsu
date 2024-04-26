package com.pruebas.util;

import lombok.extern.slf4j.Slf4j;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class GenerateUnifiedReport {
    private GenerateUnifiedReport() {
    }

    public static void generateReport(String reportsOutputPath, String jsonResumePath, String nameJsonReport)
            throws IOException {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(reportsOutputPath), new String[] { "json" }, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        JSONArray reportJson = new JSONArray();
        jsonFiles.forEach(file -> {
            JSONArray jsonContent = getReportJsonByFile(file.getAbsolutePath());
            if (jsonContent != null) {
                jsonContent.forEach(content -> reportJson.add(content));
            }
            jsonPaths.add(file.getAbsolutePath());
        });

        if (validatePathReport(jsonResumePath)) {
            Files.write(Paths.get(jsonResumePath + "/" + nameJsonReport), reportJson.toJSONString().getBytes());
            Configuration config = new Configuration(new File("build"), "Automation");
            ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
            reportBuilder.generateReports();
        } else {
            log.error("No se pudo crear el path" + jsonResumePath + " para el reporte.");
        }
    }

    public static JSONArray getReportJsonByFile(String filePath) {
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {

            log.error("ERROR en getReportJsonByFile al leer " + filePath + "\n" + e);
        }
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonObject = null;
        try {
            jsonObject = (JSONArray) jsonParser.parse(reader);
        } catch (ParseException e) {

            log.error("ERROR en getReportJsonByFile al Parsear los datos " + reader + "\n" + e);
        }
        assert jsonObject != null;
        if (jsonObject.size() != 0) {
            return jsonObject;
        }
        return null;
    }

    public static boolean validatePathReport(String jsonResumePath) {
        File pathFolderReport = new File(jsonResumePath);
        boolean existFolderReport = Files.exists(Paths.get(jsonResumePath));
        if (jsonResumePath.contains("cucumber")) {
            String pathCucumber = jsonResumePath.substring(0, 24);
            File pathFolderCucmber = new File(pathCucumber);
            boolean existFolderCucumber = Files.exists(Paths.get(pathCucumber));
            if (!existFolderCucumber) {
                pathFolderCucmber.mkdir();
            }
        }
        if (!existFolderReport) {
            existFolderReport = pathFolderReport.mkdir();
        }
        return existFolderReport;
    }
}

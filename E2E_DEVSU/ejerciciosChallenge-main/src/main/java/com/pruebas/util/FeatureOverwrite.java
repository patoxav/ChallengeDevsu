package com.pruebas.util;

import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.util.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class FeatureOverwrite {
    static Logger logger = Logger.getLogger(FeatureOverwrite.class.getName());
    static String msgError = "ERROR: ";
    static PropertiesReader readProperties = new PropertiesReader();
    private static final EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final Map<String, List<String>> currentFeatures = new HashMap<>();
    private static List<String> featuresList = new ArrayList<>();
    private static boolean externalDataProcessIsEmpty;

    private FeatureOverwrite() {
    }

    public static void overwriteFeatureFileAdd(final String featurePath) throws IOException {
        addExternalDataToFeature(featurePath);
    }

    private static void addExternalDataToFeature(final String featurePath) throws IOException {
        File featureFile = new File(featurePath);
        List<String> featureWithExternalData;
        if (featurePath.toLowerCase().contains("manual.feature")) {
            featureWithExternalData = impSetPaneOrCsvDataToFeature(featureFile);
        } else {
            featureWithExternalData = impSetFileDataToFeature(featureFile, featureFile.getName());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8)) {
            for (final String writeLine : featureWithExternalData) {
                writer.write(writeLine);
                writer.write("\n");
            }
        }
    }

    private static void externalDataProcess(String data, List<String> fileData) {
        String filePath = getValidFilePath(data);
        List<Map<String, String>> externalData = getDataFromFile(
                PathConstants.dataPath() + PathConstants.validatePath(filePath));
        if (!externalData.isEmpty()) {
            externalDataProcessIsEmpty = false;
            Collection<String> headers = externalData.get(0).keySet();
            fileData.add(getGherkinExample(headers));
            for (int rowNumber = 0; rowNumber < externalData.size() - 1; rowNumber++) {
                Collection<String> rowValues = externalData.get(rowNumber).values();
                String example = getGherkinExample(rowValues);
                if (!"".equals(example))
                    fileData.add(example);
            }
        } else {
            externalDataProcessIsEmpty = true;
        }
    }

    private static List<String> impSetFileDataToFeature(final File featureFile, String featureName) throws IOException {
        final List<String> fileData = new ArrayList<>();
        try (BufferedReader buffReader = Files.newBufferedReader(
                Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8)) {
            String data;
            List<String> previousData = new ArrayList<>();
            List<String> staticDataExample = new ArrayList<>();
            boolean exampleData = false;
            while ((data = buffReader.readLine()) != null) {
                previousData.add(data);
                if (data.trim().contains("@externaldata")) {
                    externalDataProcess(data, fileData);
                    exampleData = false;
                    data = validateExternaldataProcess(data);
                } else if ((data.trim().startsWith("@") || data.trim().startsWith("Scenario")) && exampleData) {
                    exampleData = false;
                    fileData.addAll(staticDataExample);
                }else {
                    log.debug("- No cumple condiciones -");
                }
                if (!exampleData) {
                    fileData.add(data);
                } else {
                    staticDataExample.add(data);
                }
                if (data.contains("Examples")) {
                    staticDataExample.clear();
                    exampleData = true;
                }
            }
            if (exampleData && !staticDataExample.isEmpty()) {
                fileData.addAll(staticDataExample);
                staticDataExample.clear();
            }
            currentFeatures.put(featureName, previousData);
        }
        return fileData;
    }

    private static String validateExternaldataProcess(String data) {
        if (!externalDataProcessIsEmpty) {
            StringBuilder externalString = new StringBuilder("#").append(data);
            data = externalString.toString();
        }
        return data;
    }

    private static String getValidFilePath(String data) {
        return data.substring(StringUtils.ordinalIndexOf(data, "@", 2) + 1)
                .replace("|", "")
                .trim();
    }

    private static List<Map<String, String>> getDataFromFile(String filePath) {
        if (isCSV(filePath)) {
            return CSVReader.getData(filePath);
        } else {
            return ExcelReader.getData(filePath);
        }
    }

    private static boolean isCSV(String filePath) {
        return filePath.toLowerCase().endsWith(".csv");
    }

    private static String getGherkinExample(Collection<String> examplesFields) {
        String example = "";
        for (String field : examplesFields) {
            if (!"".equals(field.trim())) {
                example = String.format("%s|%s", example, field);
            }
        }
        if ("".equals(example))
            return "";
        return example + "|";
    }

    public static void overwriteFeatureFileRemove(final String featurePath) throws IOException {
        removeExternalDataToFeature(featurePath);
    }

    private static void removeExternalDataToFeature(final String featurePath) throws IOException {
        File featureFile = new File(featurePath);

        final List<String> featureWithExternalData;

        if (featurePath.contains("Manual.feature")) {
            featureWithExternalData = impRemovePaneDataToFeature(featureFile);
        } else {
            featureWithExternalData = currentFeatures.get(featureFile.getName());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8)) {
            for (final String writeLine : featureWithExternalData) {
                writer.write(writeLine);
                writer.write("\n");
            }
        }
    }

    private static List<String> impSetPaneOrCsvDataToFeature(final File featureFile) throws IOException {
        final List<String> fileData = new ArrayList<>();
        try (BufferedReader buffReader = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()),
                StandardCharsets.UTF_8);
             BufferedReader buffReaderScenario = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()),
                     StandardCharsets.UTF_8)) {
            String data;
            String externalDataSt;
            final List<String> scenarios = new ArrayList<>();
            String nameScenario;
            int numScenario = 0;
            String azureOrLocalExecution = variables.getProperty("execute");
            if (azureOrLocalExecution == null || azureOrLocalExecution.isEmpty()) {
                azureOrLocalExecution = readProperties.getPropiedad("azure.or.local.execution");
            }
            while ((nameScenario = buffReaderScenario.readLine()) != null) {
                if (nameScenario.trim().contains("Scenario:")) {
                    scenarios.add(nameScenario);
                }
            }
            while ((data = buffReader.readLine()) != null) {
                boolean foundHashTag = data.trim().contains("@manual") && !(data.trim().contains("@manual-result:"));
                if (foundHashTag) {
                    if (azureOrLocalExecution.equalsIgnoreCase("azure")) {
                        String dataPassedOrFailed = featureFile.getName().replace(".feature", ".csv");
                        dataPassedOrFailed = PathConstants.dataPath() + "manualtest" + File.separator + dataPassedOrFailed;
                        //externalDataSt = ManualReadFeature.setPassedOrFailedFromCSV(numScenario, dataPassedOrFailed);
                    } else {
                       // externalDataSt = ManualReadFeature.setPassedOrFailedFromPane(featureFile.getName(), scenarios.get(numScenario), numScenario);
                    }
                    numScenario++;
                   // StringBuilder externalString = new StringBuilder(data).append(" ").append(externalDataSt.trim());
                   // data = externalString.toString();
                }
                fileData.add(data);
            }
        }
        return fileData;
    }

    private static List<String> impRemovePaneDataToFeature(final File featureFile) throws IOException {
        final List<String> fileData = new ArrayList<>();
        BufferedReader buffReader = null;
        try {
            buffReader = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8);
            String data;
            while ((data = buffReader.readLine()) != null) {
                if (data.trim().contains("@manual-result:") || data.trim().contains("#EstadoScenarioNoDefinido")) {
                    data = data.replace(" @manual-result:passed", "");
                    data = data.replace(" @manual-result:failed", "");
                    data = data.replace(" #EstadoScenarioNoDefinido", "");
                    fileData.add(data);
                    continue;
                }
                fileData.add(data);
            }
        } finally {
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, msgError, e);
                }
            }
        }
        return fileData;
    }

    public static List<String> listFilesByFolder(String featureName, final File folder) {
        final String ALL_FEATURES = "todos";
        if (featureName.equalsIgnoreCase(ALL_FEATURES)) {
            for (final File fileOrFolder : Objects.requireNonNull(folder.listFiles())) {
                if (fileOrFolder.isDirectory()) {
                    listFilesByFolder(featureName, fileOrFolder);
                } else {
                    featuresList.add(fileOrFolder.getAbsolutePath());
                }
            }
        } else {
            featuresList = List.of(featureName.split(";"));
        }
        return new ArrayList<>(featuresList);
    }

    public static void clearListFilesByFolder() {
        featuresList.clear();
    }

}
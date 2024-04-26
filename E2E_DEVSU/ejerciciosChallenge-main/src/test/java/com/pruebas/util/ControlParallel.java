package com.pruebas.util;

import com.pruebas.exceptions.ExcInvalidArgument;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Slf4j
public class ControlParallel {
    private ControlParallel() {
    }

    private static final String RUNNER_EJECUTANDOSE = "RunnerEjecutandose";
    private static final String PARALLEL_CONTROL_PROPERTIES = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "properties" + File.separator + "parallelcontrol.properties";

    public static void setOrRemoveExecution(String addOrDeleteRunner) {
        File propertiesFile = new File(PARALLEL_CONTROL_PROPERTIES);
        List<String> propertiesModified = addOrDeleteExecutioInProperties(propertiesFile, addOrDeleteRunner);
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(propertiesFile.getAbsolutePath()), StandardCharsets.UTF_8);
            for (final String writeline : propertiesModified) {
                writer.write(writeline);
                writer.write("\n");
            }
        } catch (IOException e) {
            log.error("Error al escribir en properties parallelcontrol.properties " + e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("Error al cerrar BufferedWriter ," + e.getMessage(), e);
                }
            }
        }
    }

    private static List<String> addOrDeleteExecutioInProperties(final File propertiesFile, String addOrDelete) {
        final List<String> fileData = new ArrayList<>();
        BufferedReader buffReader = null;
        try {
            buffReader = Files.newBufferedReader(Paths.get(propertiesFile.getAbsolutePath()), StandardCharsets.UTF_8);
            String data;
            if (addOrDelete.equals("add")) {
                while ((data = buffReader.readLine()) != null) {
                    fileData.add(data);
                }
                fileData.add(RUNNER_EJECUTANDOSE);
            } else {
                int cont = 0;
                while ((data = buffReader.readLine()) != null) {
                    if (data.contains(RUNNER_EJECUTANDOSE)) {
                        cont++;
                    }
                    if (cont != 1) {
                        fileData.add(data);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error al leer properties parallelcontrol.properties " + e.getMessage(), e);
        } finally {
            closeBufferedReader(buffReader);
        }
        return fileData;
    }

    public static boolean validateExecution(String stage) {
        boolean go = false;
        File featureFile = new File(PARALLEL_CONTROL_PROPERTIES);
        BufferedReader buffReader = null;
        try {
            buffReader = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8);
            String data;
            int cont = 0;
            while ((data = buffReader.readLine()) != null) {
                if (data.contains(RUNNER_EJECUTANDOSE)) {
                    cont++;
                }
            }
            if (stage.trim().equalsIgnoreCase("Runner1") || stage.trim().equalsIgnoreCase("Runner2")) {
                if (cont == 1) {
                    go = true;
                }
            } else if (stage.trim().equalsIgnoreCase("Runner3")) {
                if (cont == 2) {
                    go = true;
                }
            } else if (stage.trim().equalsIgnoreCase("Runner4")) {
                if (cont == 3) {
                    go = true;
                }
            } else {
                throw new ExcInvalidArgument("Argumento '" + stage + "' no admitido en el metodo");
            }
        } catch (IOException e) {
            log.error("Error al leer properties parallelcontrol.properties " + e.getMessage(), e);
        } finally {
            closeBufferedReader(buffReader);
        }
        return go;
    }

    private static void closeBufferedReader(BufferedReader buffReader) {
        if (buffReader != null) {
            try {
                buffReader.close();
            } catch (IOException e) {
                log.error("Error al cerrar BufferedReader ," + e.getMessage(), e);
            }
        }
    }

    public static void startRunner(final String runner) {
        await().atMost(30, SECONDS).until(featuresOverwritten(runner));
        ControlParallel.setOrRemoveExecution("add");
        log.info("Inicia " + runner);
    }

    public static void endsRunner(final String runner) {
        ControlParallel.setOrRemoveExecution("delete");
        log.info("Termina " + runner);
    }

    public static Callable<Boolean> featuresOverwritten(final String runner) {
        return () -> ControlParallel.validateExecution(runner);
    }
}

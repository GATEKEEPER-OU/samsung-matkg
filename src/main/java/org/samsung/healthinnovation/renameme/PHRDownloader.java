package org.samsung.healthinnovation.renameme;

import org.apache.commons.io.FileUtils;
import org.commons.EmailUtils;
import org.commons.OutputUtils;
import org.commons.ResourceUtils;
import org.fhir.FHIRClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class PHRDownloader {

  static final String FHIR_ENDPOINT_CONFIG = "fhir-endpoint.properties";
  static final String OUTPUT_DIR = "datasets/data-phr";

  public static void main(String[] args) {
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);

    Properties fhirConfig = loadConfiguration(FHIR_ENDPOINT_CONFIG);
    try (FHIRClient fhirClient = FHIRClient.connect(fhirConfig)) {
//      JSONArray results = fhirClient.getObservations("1984-01-01", "1984-01-07");
      JSONArray results = fhirClient.getObservations("2022-01-02", "2022-02-02");

      for (int i=0; i < results.length(); ++i) {
        JSONObject result = results.getJSONObject(i);

        String userId = result.getString("user_id");
        String filename = EmailUtils.getUsername(userId);
        File outputFile = new File(OUTPUT_DIR,  filename + ".fhir.json" );
        FileUtils.writeStringToFile(outputFile, result.getString("value"), StandardCharsets.UTF_8); // @todo move to OutputUtils
      }

    } catch (IOException e) {
      // @todo Message
//      LOGGER.error(e.getMessage());
      e.printStackTrace(); // DEBUG
    }
  }

  private static Properties loadConfiguration(String path) {
    Properties config = new Properties();
    try {
      InputStream configStream = ResourceUtils.getResourceAsStream(path);
      config.load(configStream);

    } catch (IOException e) {
      // @todo improve message and use LOGGER
      System.out.println(" >>>> " + path + " not set");
      System.out.println(e);
    }
    return config;
  }

}
package org.samsung.healthinnovation.fetcher;

import org.commons.EmailUtils;
import org.commons.OutputUtils;
import org.commons.PropertiesUtils;
import org.fhir.FHIRClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class SHDownloader {

  static final String KGWORKFLOW_CONFIG = "kgworkflow.properties";
  static final Logger LOGGER = LoggerFactory.getLogger(SHDownloader.class); // TODO

  public static void main(String[] args) throws IOException {
    Properties config = PropertiesUtils.loadConfiguration(KGWORKFLOW_CONFIG);

    Properties fetchConfig = PropertiesUtils.getSubset(config, "phr.fetch");
    String destDirPath = fetchConfig.getProperty("destdir");
    File outputDir = new File(destDirPath);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);

    Properties endpointConfig = PropertiesUtils.getSubset(fetchConfig, "endpoint");
    try (FHIRClient fhirClient = FHIRClient.connect(endpointConfig)) {

      // Empty results
//      JSONArray results = fhirClient.getObservations("1984-01-01", "1984-01-07");

      // With results
//      JSONArray results = fhirClient.getObservations("2022-01-02", "2022-02-02");
//      String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
      JSONArray results = fhirClient.getObservations("20200301", "20220901");

      for (int i=0; i < results.length(); ++i) {
        JSONObject result = results.getJSONObject(i);

        String userId = result.getString("user_id");
        String outputFilename = EmailUtils.getUsername(userId);
        File outputFile = new File(outputDir,  outputFilename + ".fhir.json" );
        OutputUtils.save(outputFile, result.getString("value"));
      }

    } catch (IOException e) {
      // @todo Message
//      LOGGER.error(e.getMessage());
      e.printStackTrace(); // DEBUG
    }
  }

}
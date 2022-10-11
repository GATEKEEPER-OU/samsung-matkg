package org.samsung.healthinnovation.fetcher;

import org.commons.EmailUtils;
import org.commons.OutputUtils;
import org.commons.PropertiesUtils;
import org.fhir.FHIRClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class PHRDownloader {

  static final String FHIR_ENDPOINT_CONFIG = "phr-endpoint.properties";
  static final String OUTPUT_DIR = "datasets/data-phr";

  public static void main(String[] args) {
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);

    Properties fhirConfig = PropertiesUtils.loadConfiguration(FHIR_ENDPOINT_CONFIG);
    try (FHIRClient fhirClient = FHIRClient.connect(fhirConfig)) {

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
        File outputFile = new File(OUTPUT_DIR,  outputFilename + ".fhir.json" );
        OutputUtils.save(outputFile, result.getString("value"));
      }

    } catch (IOException e) {
      // @todo Message
//      LOGGER.error(e.getMessage());
      e.printStackTrace(); // DEBUG
    }
  }

}
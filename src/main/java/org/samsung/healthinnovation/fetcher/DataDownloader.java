package org.samsung.healthinnovation.fetcher;

import org.commons.EmailUtils;
import org.commons.OutputUtils;
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
public class DataDownloader {

  static final Logger LOGGER = LoggerFactory.getLogger(DataDownloader.class);

  /**
   * TODO
   * */
  public static void fetch(Properties endpointConfig, File outputDir, String from, String to) throws IOException {
    try (FHIRClient fhirClient = FHIRClient.connect(endpointConfig)) {
      JSONArray results = fhirClient.getObservations(from, to);
      for (int i=0; i < results.length(); ++i) {
        JSONObject result = results.getJSONObject(i);
        String userId = result.getString("user_id");
        String outputFilename = EmailUtils.getUsername(userId);
        File outputFile = new File(outputDir,  outputFilename + ".json" );
//      File outputFile = new File(outputDir,  outputFilename + ".fhir.json" );
        OutputUtils.save(outputFile, result.getString("value"));
      }
    }
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private DataDownloader() {
  }

}

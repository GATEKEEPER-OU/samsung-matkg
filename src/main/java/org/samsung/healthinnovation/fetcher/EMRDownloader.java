package org.samsung.healthinnovation.fetcher;

import org.commons.EmailUtils;
import org.commons.OutputUtils;
import org.commons.PropertiesUtils;
import org.fhir.FHIRClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class EMRDownloader {

  static final String FHIR_ENDPOINT_CONFIG = "fhir-endpoint.properties";
  static final String OUTPUT_DIR = "datasets/data-emr";

  public static void main(String[] args) {
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);

    // TODO
  }

}
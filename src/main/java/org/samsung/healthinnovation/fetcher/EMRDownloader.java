package org.samsung.healthinnovation.fetcher;

import org.commons.OutputUtils;

import java.io.File;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class EMRDownloader {

  static final String FHIR_ENDPOINT_CONFIG = "phr-endpoint.properties";
  static final String OUTPUT_DIR = "datasets/data-emr";

  public static void main(String[] args) {
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);

    // TODO
  }

}
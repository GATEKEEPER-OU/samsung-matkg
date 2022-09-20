package org.samsung.healthinnovation.triplestoremanager;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.commons.PropertiesUtils;
import org.commons.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * @todo
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class TriplestoreUploader {

  static final String TRIPLESTORE_ENDPOINT_CONFIG = "triplestore-endpoint.properties";

  static final String TRIPLESTORE_URL_TEMPLATE = "http://__ENDPOINT__/blazegraph/sparql";

  static final String TRIPLESTORE_DIR = "output/kg-phr";

  static final Logger LOGGER = LoggerFactory.getLogger(TriplestoreUploader.class);

  public static void main(String[] args) {
    Properties triplestoreConfig = PropertiesUtils.loadConfiguration(TRIPLESTORE_ENDPOINT_CONFIG);

    String endpoint = triplestoreConfig.getProperty("triplestore_host");
    String postUrl = TRIPLESTORE_URL_TEMPLATE.replace("__ENDPOINT__", endpoint);

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      String[] exts = {"nt"};
      File triplestoreDir = new File(TRIPLESTORE_DIR);
      Iterator<File> datasets = FileUtils.iterateFiles(triplestoreDir, exts, false);
      while (datasets.hasNext()) {
        File data = datasets.next();
        LOGGER.info("Uploading: {}", data.getName());
        RestUtils.makePost(httpClient, postUrl, data);
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
//      e.printStackTrace(); // DEBUG
    }
  }

}

package org.samsung.healthinnovation.triplestoremanager;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

  static final String TRIPLESTORE_URL_TEMPLATE = "http://__ENDPOINT__/blazegraph/sparql";

  static final Logger LOGGER = LoggerFactory.getLogger(TriplestoreUploader.class);

  /**
   * TODO
   * */
  public static void store(File sourceDir, String[] exts, Properties triplestoreConfig) throws IOException {
    String endpoint = triplestoreConfig.getProperty("host");
    String postUrl = TRIPLESTORE_URL_TEMPLATE.replace("__ENDPOINT__", endpoint);
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      Iterator<File> datasets = FileUtils.iterateFiles(sourceDir, exts, false);
      while (datasets.hasNext()) {
        File data = datasets.next();
        LOGGER.info("Uploading: {}", data.getName());
        RestUtils.makePost(httpClient, postUrl, data);
      }
    }
  }

}
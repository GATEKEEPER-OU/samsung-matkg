package org.samsung.healthinnovation.knowledgegraphworkflow;

import org.commons.OutputUtils;
import org.commons.PropertiesUtils;
import org.ou.gatekeeper.fhir.adapters.CSSAdapter;
import org.ou.gatekeeper.fhir.adapters.SHAdapter;
import org.ou.gatekeeper.fhir.adapters.FHIRAdapter;
import org.samsung.healthinnovation.competencyquerychecker.QueryChecker;
import org.samsung.healthinnovation.fetcher.DataDownloader;
import org.samsung.healthinnovation.structuredkgconstruction.KGConstruction;
import org.samsung.healthinnovation.triplestoremanager.TriplestoreUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class KGWorkflow {

  static final Logger LOGGER = LoggerFactory.getLogger(KGWorkflow.class);

  /**
   * TODO desc
   * */
  public static void run(Properties config) throws IOException {
    String triplestoreEnabledProp = config.getProperty("triplestore.enable");
    boolean triplestoreEnabled = Boolean.parseBoolean(triplestoreEnabledProp);

    String competencyQueriesEnabledProp = config.getProperty("competencyqueries.enable");
    boolean competencyQueriesEnabled = Boolean.parseBoolean(competencyQueriesEnabledProp);

    //
    // Samsung Health stages
    //
    String shEnabledProp = config.getProperty("sh.enable");
    boolean shEnabled = Boolean.parseBoolean(shEnabledProp);
    if (shEnabled) {
      Properties shConfig = PropertiesUtils.getSubset(config, "sh");
      fetchStage(shConfig);
      constructStage(shConfig, SHAdapter.create());

      if (triplestoreEnabled) {
        String shKgDirPath = config.getProperty("sh.kg.destdir");
        File shKgDir = new File(shKgDirPath);
        storeStage(config, shKgDir);
      }

      if (competencyQueriesEnabled) {
        queryCheckStage(shConfig);
      }
    }

    //
    // CSS stages
    //
    String cssEnabledProp = config.getProperty("css.enable");
    boolean cssEnabled = Boolean.parseBoolean(cssEnabledProp);
    if (cssEnabled) {
      Properties cssConfig = PropertiesUtils.getSubset(config, "css");
//      fetchStage(shConfig); // TODO wait for the EMR endpoint implementation
      // Temporary implementation reading from local files
      cssConfig.setProperty("fetch.destdir", "datasets/data-css"); // WORKAROUND
      // -------------------------------------------------
      constructStage(cssConfig, CSSAdapter.create());

      if (triplestoreEnabled) {
        String cssKgDirPath = config.getProperty("css.kg.destdir");
        File cssKgDir = new File(cssKgDirPath);
        storeStage(config, cssKgDir);
      }

      if (competencyQueriesEnabled) {
        queryCheckStage(cssConfig);
      }
    }
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private KGWorkflow() {
  }

  /**
   * TODO desc
   * */
  private static File prepare(String path) {
    File dir = new File(path);
    dir.mkdirs();
    OutputUtils.clean(dir);
    return dir;
  }

  /**
   * TODO desc
   * */
  private static void fetchStage(Properties config) throws IOException {
    Properties endpointProps = PropertiesUtils.getSubset(config, "fetch.endpoint");
    String destDirPath = config.getProperty("fetch.destdir");
    String  rangeStart = config.getProperty("fetch.range.start");
    String    rangeEnd = config.getProperty("fetch.range.end");
    File dataDir = prepare(destDirPath);
    DataDownloader.fetch(endpointProps, dataDir, rangeStart, rangeEnd);
  }

  /**
   * TODO desc
   * */
  private static void constructStage(Properties config, FHIRAdapter adapter) throws IOException {
    String dataDirPath = config.getProperty("fetch.destdir");
    File dataDir = new File(dataDirPath);
    String kgDirPath = config.getProperty("kg.destdir");
    File kgDir = prepare(kgDirPath);
    KGConstruction.construct(dataDir, kgDir, adapter);
  }

  /**
   * TODO desc
   * */
  private static void storeStage(Properties config, File sourceDir) throws IOException {
    String[] exts = { "json" };
    Properties triplestoreEndpointProps = PropertiesUtils.getSubset(config, "triplestore.endpoint");
    TriplestoreUploader.store(sourceDir, exts, triplestoreEndpointProps);
  }

  /**
   * TODO desc
   * */
  private static void queryCheckStage(Properties config) {
    String queryDirPath = "queries"; // TODO parametrize
    String sourceDirPath = config.getProperty("kg.destdir");
    File sourceDir = new File(sourceDirPath);
    String resultsDirPath = config.getProperty("competencyqueries.destdir");
    File resultsDir = new File(resultsDirPath);
    QueryChecker.check(queryDirPath, sourceDir, resultsDir);
  }

}
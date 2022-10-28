package org.samsung.healthinnovation.competencyquerychecker;

import org.commons.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.oxfordsemantic.commons.RDFoxUtils;
import tech.oxfordsemantic.jrdfox.client.ConnectionFactory;
import tech.oxfordsemantic.jrdfox.client.DataStoreConnection;
import tech.oxfordsemantic.jrdfox.client.ServerConnection;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class QueryChecker {

  static final String DATASTORE_NAME = "GK-DataStore";
  static final String ONTOLOGY_NAME = "ontologies/HeLiFit-OWL-Functional-Syntax.owl";
  static final String[] QUERY_FILE_EXTS = { "txt" };

  static final Logger LOGGER = LoggerFactory.getLogger(QueryChecker.class);

  public static void check(String queryDirPath, File sourceDir, File resultsDir) {
    String role = "temp";
    String password = "EMPTY";
    RDFoxUtils.startLocalServer(role, password);

    try (ServerConnection serverConnection = ConnectionFactory.newServerConnection("rdfox:local", role, password)) {
      serverConnection.setNumberOfThreads(2);
      serverConnection.createDataStore(DATASTORE_NAME, Collections.emptyMap());
      try (DataStoreConnection dataStoreConnection = serverConnection.newDataStoreConnection(DATASTORE_NAME)) {
        // upload RDF graph into rdfox
        RDFoxUtils.importData(dataStoreConnection, sourceDir);
        RDFoxUtils.importOntology(dataStoreConnection, ONTOLOGY_NAME);

        // run competency queries
        List<File> queries = RDFoxUtils.listOfQueries(queryDirPath, QUERY_FILE_EXTS);
        for (File queryFile : queries) {
          // @todo refactory this
          String queryName = queryFile.getName().substring(0, queryFile.getName().length()-3);
          // --------------------
//          System.out.println(" queryName >>>>" + queryName); //DEBUG

          File queryOutputFile = new File(resultsDir, queryName + "nt");
//          String userId = "<https://opensource.samsung.com/projects/helifit/id/user1%40saxony.gatekeeper.com>"; // @todo take it from file content
          String queryTemplate = ResourceUtils.readFileToString(queryFile);
//          System.out.println("queryTemplate >>>>> " + queryTemplate); // DEBUG
//          String query = queryTemplate.replace("__ID__", userId);
          String query = queryTemplate;
          RDFoxUtils.saveQueryResults(dataStoreConnection, query, queryOutputFile);
        }
      }
    } catch (JRDFoxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
  }


  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private QueryChecker() {
  }

}

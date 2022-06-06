package org.samsung.healthinnovation.helifitcq;

import org.commons.OutputUtils;
import org.commons.ResourceUtils;
import tech.oxfordsemantic.commons.RDFoxUtils;
import tech.oxfordsemantic.jrdfox.client.*;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class EMRCompetencyQueries {

  static final String OUTPUT_MAPPING_DIR = "output/kg-emr";
  static final String RESULT_DIR = "output/queries";

  static final String DATASTORE_NAME = "GK-Puglia-DataStore";
  static final String ONTOLOGY_NAME = "HeLiFit-OWL-Functional-Syntax_v1.5.2.owl";
  static final String QUERIES_PATH = "queries";
  static final String[] QUERY_FILE_EXTS = { "txt" };

  public static void main(String[] args) {
    File rdfOutputFile = new File(OUTPUT_MAPPING_DIR + "/output-356.nt");

    File queryOutputDir = new File(RESULT_DIR);
    queryOutputDir.mkdir();
    OutputUtils.clean(queryOutputDir);

    // connect to rdfox
    try (ServerConnection serverConnection = ConnectionFactory.newServerConnection("rdfox:local", "", "")) {
      serverConnection.setNumberOfThreads(2);
      serverConnection.createDataStore(DATASTORE_NAME, Collections.emptyMap());
      try (DataStoreConnection dataStoreConnection = serverConnection.newDataStoreConnection(DATASTORE_NAME)) {
        // upload RDF graph into rdfox
        RDFoxUtils.importData(dataStoreConnection, rdfOutputFile);
        RDFoxUtils.importOntology(dataStoreConnection, ONTOLOGY_NAME);

        // run competency queries
        List<File> queries = RDFoxUtils.listOfQueries(QUERIES_PATH, QUERY_FILE_EXTS);
        for (File queryFile : queries) {
          String queryName = queryFile.getName().substring(0, queryFile.getName().toString().length()-3);
//          System.out.println(queryName); //DEBUG

          File queryOutputFile = new File(RESULT_DIR + "/" + queryName + "nt");
//          String userId = "<https://opensource.samsung.com/projects/helifit/id/user1%40saxony.gatekeeper.com>"; // @todo take it from file content
          String queryTemplate = ResourceUtils.readFileToString(queryFile);
//          System.out.println("queryTemplate >>>>> " + queryTemplate); // DEBUG
//          String query = queryTemplate.replace("__ID__", userId);
          String query = queryTemplate;
//          RDFoxUtils.printQueryResults(dataStoreConnection, query); // DEBUG
          RDFoxUtils.saveQueryResults(dataStoreConnection, query, queryOutputFile);
        }
      }
    } catch (JRDFoxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
  }

}
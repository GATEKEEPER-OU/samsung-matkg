package org.samsung.healthinnovation.helifitcq;

import org.commons.ResourceUtils;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.fhir.adapters.EMRAdapter;
import org.ou.gatekeeper.fhir.adapters.FHIRAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;
import tech.oxfordsemantic.commons.RDFoxUtils;
import tech.oxfordsemantic.jrdfox.client.ConnectionFactory;
import tech.oxfordsemantic.jrdfox.client.DataStoreConnection;
import tech.oxfordsemantic.jrdfox.client.ServerConnection;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class CompetencyQueries {

  static final String DATASETS_DIR = "datasets/export-20220221";
  static final String OUTPUT_DIR = "output/emr";

  static final String DATASTORE_NAME = "GK-Puglia-DataStore";
  static final String ONTOLOGY_NAME = "HeLiFit-RDF_v1.5.0.owl";
  static final String QUERIES_PATH = "queries";
  static final String[] QUERY_FILE_EXTS = { "txt" };

  public static void main(String[] args) {
    // check if rdfox.jar exists in lib

    File datasetFile = new File(DATASETS_DIR + "/356.json");
    File rdfOutputFile = new File(OUTPUT_DIR + "/output-356.nt");

    // generate RDF graph
    FHIRAdapter converter = EMRAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);
    RDFizer.trasform(datasetFile, converter, mapping, rdfOutputFile);

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
          String query = ResourceUtils.readFileToString(queryFile);

//          System.out.println(">>>>> " + query); // DEBUG

        }
      }

    } catch (JRDFoxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
  }

}
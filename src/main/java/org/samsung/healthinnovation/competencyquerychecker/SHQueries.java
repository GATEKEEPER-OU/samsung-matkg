package org.samsung.healthinnovation.competencyquerychecker;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.commons.OutputUtils;
import org.commons.ResourceUtils;
import tech.oxfordsemantic.commons.RDFoxUtils;
import tech.oxfordsemantic.jrdfox.Prefixes;
import tech.oxfordsemantic.jrdfox.client.ConnectionFactory;
import tech.oxfordsemantic.jrdfox.client.DataStoreConnection;
import tech.oxfordsemantic.jrdfox.client.ServerConnection;
import tech.oxfordsemantic.jrdfox.client.UpdateType;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class SHQueries {
    static final String RDF_DATA_DIR = "datasets/shsampledata/server-ntriples";
    static final String RESULT_DIR = "output/shdata/server-ntriples";

    static final String DATASTORE_NAME = "GK-Puglia-DataStore";
    static final String ONTOLOGY_NAME = "ontologies/HeLiFit-OWL-Functional-Syntax.owl";
    static final String QUERIES_PATH = "shqueries/specific";
    static final String[] QUERY_FILE_EXTS = {"txt"};

    static void RetrieveDataWithQueries() {
        File rdfDataDir = new File(RDF_DATA_DIR);
        File[] listOfFiles = rdfDataDir.listFiles();
        File queryOutputDir = new File(RESULT_DIR);
        queryOutputDir.mkdirs();
        OutputUtils.clean(queryOutputDir);
        String role = "temp";
        String password = "EMPTY";
        RDFoxUtils.startLocalServer(role, password);
        File rdfFile = null;

        // connect to rdfox
        try (ServerConnection serverConnection = ConnectionFactory.newServerConnection("rdfox:local",
                role, password)) {
            serverConnection.setNumberOfThreads(2);

            serverConnection.createDataStore(DATASTORE_NAME, Collections.emptyMap());

            try (DataStoreConnection dataStoreConnection = serverConnection.newDataStoreConnection(DATASTORE_NAME)) {

                // traverse the files in this directory
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        rdfFile = listOfFiles[i];

                        RDFoxUtils.importData(dataStoreConnection, rdfFile);
                        RDFoxUtils.importOntology(dataStoreConnection, ONTOLOGY_NAME);

                        // run competency queries
                        List<File> queries = RDFoxUtils.listOfQueries(QUERIES_PATH, QUERY_FILE_EXTS);
                        for (File queryFile : queries) {
                            String queryName = queryFile.getName().substring(0, queryFile.getName().length() - 3);
                            // --------------------
                            String outputFilePrefix = listOfFiles[i].getName().substring(0,
                                    listOfFiles[i].getName().length() - 3);
                            File queryOutputFile = new File(RESULT_DIR, outputFilePrefix + "-"
                                    + queryName + "nt");
                            String queryTemplate = ResourceUtils.readFileToString(queryFile);
                            String query = queryTemplate;
                            RDFoxUtils.saveQueryResults(dataStoreConnection, query, queryOutputFile);
                        }
                        dataStoreConnection.clear();
                    }
                }
            }
        } catch (JRDFoxException e) {
            // @todo Message
            e.printStackTrace(); // DEBUG
        }
    }

    static void RetrieveDataWithOneQuery(String queryName) {
        File rdfDataDir = new File(RDF_DATA_DIR);
        File[] listOfFiles = rdfDataDir.listFiles();
        File queryOutputDir = new File(RESULT_DIR);
        queryOutputDir.mkdir();
        OutputUtils.clean(queryOutputDir);
        String role = "temp";
        String password = "EMPTY";
        RDFoxUtils.startLocalServer(role, password);
        File rdfFile = null;

        // connect to rdfox
        try (ServerConnection serverConnection = ConnectionFactory.newServerConnection("rdfox:local",
                role, password)) {
            serverConnection.setNumberOfThreads(2);

            serverConnection.createDataStore(DATASTORE_NAME, Collections.emptyMap());

            try (DataStoreConnection dataStoreConnection = serverConnection.newDataStoreConnection(DATASTORE_NAME)) {

                // traverse the files in this directory
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        rdfFile = listOfFiles[i];

                        RDFoxUtils.importData(dataStoreConnection, rdfFile);
                        RDFoxUtils.importOntology(dataStoreConnection, ONTOLOGY_NAME);

                        // run competency queries
                        String outputFilePrefix = listOfFiles[i].getName().substring(0,
                                listOfFiles[i].getName().length() - 3);
                        File queryOutputFile = new File(RESULT_DIR, outputFilePrefix + "-"
                                + queryName.substring(0, queryName.length() - 3) + "nt");
                        String queryFullName = QUERIES_PATH + "/" + queryName;
                        File queryFile = RDFoxUtils.getQueryFile(queryFullName);
                        String query = ResourceUtils.readFileToString(queryFile);
                        RDFoxUtils.saveQueryResults(dataStoreConnection, query, queryOutputFile);

                        dataStoreConnection.clear();
                    }
                }
            }
        } catch (JRDFoxException e) {
            // @todo Message
            e.printStackTrace(); // DEBUG
        }
    }

    public static void main(String[] args) throws IOException, JRDFoxException {
        RetrieveDataWithQueries();
//        RetrieveDataWithOneQuery("HeartRate_stream_data.sparql.txt");
    }


}

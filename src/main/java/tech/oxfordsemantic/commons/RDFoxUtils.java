package tech.oxfordsemantic.commons;

import org.commons.ResourceUtils;
import tech.oxfordsemantic.jrdfox.Prefixes;
import tech.oxfordsemantic.jrdfox.client.ConnectionFactory;
import tech.oxfordsemantic.jrdfox.client.DataStoreConnection;
import tech.oxfordsemantic.jrdfox.client.ServerConnection;
import tech.oxfordsemantic.jrdfox.client.UpdateType;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class RDFoxUtils {

  /**
   * @todo
   * */
  public static void importOntology(DataStoreConnection dataStoreConnection, String ontologyName) {
    File onotologyFile = null;
    try {
      URL ontologyResource = RDFoxUtils.class.getClassLoader().getResource(ontologyName);
      onotologyFile = new File(ontologyResource.toURI());

    } catch (URISyntaxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
    importData(dataStoreConnection, onotologyFile, Prefixes.s_defaultPrefixes);
  }

  /**
   * @todo
   * */
  public static void importData(DataStoreConnection dataStoreConnection, File data) {
    importData(dataStoreConnection, data, Prefixes.s_emptyPrefixes);
  }

  /**
   * @todo
   * */
  public static void importData(DataStoreConnection dataStoreConnection, File data, Prefixes prefixes) {
    try (InputStream inputStream = new FileInputStream(data)) {
      dataStoreConnection.importData(UpdateType.ADDITION, prefixes, inputStream);

    } catch (FileNotFoundException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG

    } catch (IOException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG

    } catch (JRDFoxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
  }

  /**
   * @todo
   * */
  public static List<File> listOfQueries(String path, String[] exts) {
    Collection<File> parts = ResourceUtils.getResourceFiles(path, exts);
    List<File> list = new ArrayList<>(parts);
    list.sort(Comparator.comparing(File::getName));
    return list;
  }

}
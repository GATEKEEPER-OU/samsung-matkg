package tech.oxfordsemantic.commons;

import org.apache.commons.io.FileUtils;
import org.commons.ResourceUtils;
import tech.oxfordsemantic.jrdfox.Prefixes;
import tech.oxfordsemantic.jrdfox.client.*;
import tech.oxfordsemantic.jrdfox.exceptions.JRDFoxException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
    Prefixes prefixes = new Prefixes();
    prefixes.declareStandardPrefixes();
    importData(dataStoreConnection, onotologyFile, prefixes);
  }

  /**
   * @todo
   * */
  public static void importData(DataStoreConnection dataStoreConnection, File data) {
    // @todo think about a refactory ?
    if (data.isDirectory()) {
      File[] files = data.listFiles();
      for (File file: files) {
        importData(dataStoreConnection, file, Prefixes.s_emptyPrefixes);
      }
    } else {
      importData(dataStoreConnection, data, Prefixes.s_emptyPrefixes);
    }
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
  public static void saveQueryResults(DataStoreConnection dataStoreConnection, String query, File output) {
    Prefixes prefixes = new Prefixes();
    prefixes.declareStandardPrefixes();
    try (Cursor cursor = dataStoreConnection.createCursor(null, prefixes, query, Collections.emptyMap())) {
      int arity = cursor.getArity();
      for (long multiplicity = cursor.open(); multiplicity != 0; multiplicity = cursor.advance()) {
        StringBuilder row = new StringBuilder();
        for (int termIndex = 0; termIndex < arity; ++termIndex) {
//          row.insert(0, " ");
//          ResourceValue resource = cursor.getResourceValue(termIndex);
//          row.insert(0, resource.toString(prefixes));
          row.append(" ");
          ResourceValue resource = cursor.getResourceValue(termIndex);
          row.append(resource.toString(prefixes));
        }
        row.append(".\n");
//        System.out.print("DEBUG >>>> " + row); // DEBUG
        FileUtils.writeStringToFile(output, row.toString(), Charset.defaultCharset(), true);
      }

    } catch (JRDFoxException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG

    } catch (IOException e) {
      // @todo Message
      e.printStackTrace(); // DEBUG
    }
  }

  /**
   * @todo
   * */
  public static void printQueryResults(DataStoreConnection dataStoreConnection, String query) {
    Prefixes prefixes = new Prefixes();
    prefixes.declareStandardPrefixes();
    try (Cursor cursor = dataStoreConnection.createCursor(null, prefixes, query, Collections.emptyMap())) {

      int numberOfRows = 0;
      System.out.println();
      System.out.println("=======================================================================================");
      int arity = cursor.getArity();
      // We iterate trough the result tuples.
      for (long multiplicity = cursor.open(); multiplicity != 0; multiplicity = cursor.advance()) {
        // We iterate trough the terms of each tuple.
        for (int termIndex = 0; termIndex < arity; ++termIndex) {
          if (termIndex != 0)
            System.out.print(" ");
          ResourceValue resource = cursor.getResourceValue(termIndex);
          System.out.print(resource.toString(prefixes));
        }
        System.out.print(" * ");
        System.out.print(multiplicity);
        System.out.println();
        ++numberOfRows;
      }
      System.out.println("---------------------------------------------------------------------------------------");
      System.out.println("  The number of rows returned: " + numberOfRows);
      System.out.println("=======================================================================================");
      System.out.println();

    } catch (JRDFoxException e) {
      throw new RuntimeException(e);
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
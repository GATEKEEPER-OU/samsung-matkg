package org.samsung.healthinnovation.structuredkgconstruction;

import org.apache.commons.io.FileUtils;
import org.commons.FilenameUtils;
import org.json.JSONObject;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.adapters.DataAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class KGConstruction {

  /**
   * TODO desc
   * */
  public static void construct(File sourceDir, File destDir, DataAdapter fhirAdapter) {
    String[] exts = {"json"};
    String outputExt = "nt";
    Iterator<File> datasets = FileUtils.iterateFiles(sourceDir, exts, false);
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);
    while (datasets.hasNext()) {
      File dataset = datasets.next();
      String trimmedDatasetName = FilenameUtils.trim2LvlExtension(dataset.getName());
      String outputFilename = "output-" + FilenameUtils
        .changeExtension(trimmedDatasetName, outputExt);
      File output = new File(destDir, outputFilename);
      RDFizer.transform(dataset, output, fhirAdapter, mapping);
    }
  }

  public static String construct(JSONObject rawData, String destinationFolder, DataAdapter fhirAdapter) throws IOException {
    String outputPath = "output/test-data";
    String fileName = UUID.randomUUID().toString();
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_");
    String dateStr = dateFormat.format(date);

    String finalFolderPath = outputPath + "/" + destinationFolder;
    String filePath = finalFolderPath + "/" + dateStr + "_" + fileName + ".json";
    String outputFilename =  dateStr + "_" + "output-" + fileName + ".turtle";
    File output = new File(finalFolderPath + "/" + outputFilename);

    Files.createDirectories(Path.of(finalFolderPath));

    try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
      out.write(rawData.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    File dataset = new File(filePath);
    RMLMapping mapping = HelifitMapping.create(OutputFormat.TURTLE);
    RDFizer.trasform(dataset, fhirAdapter, mapping, output, false);

    String content = new String(Files.readAllBytes(Paths.get(finalFolderPath + "/" + outputFilename)));
    return content;
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private KGConstruction() {
  }

}
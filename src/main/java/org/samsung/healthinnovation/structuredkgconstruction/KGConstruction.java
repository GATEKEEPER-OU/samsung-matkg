package org.samsung.healthinnovation.structuredkgconstruction;

import org.apache.commons.io.FileUtils;
import org.commons.FilenameUtils;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.adapters.DataAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;

import java.io.File;
import java.util.Iterator;

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

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private KGConstruction() {
  }

}
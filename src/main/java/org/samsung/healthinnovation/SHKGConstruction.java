package org.samsung.healthinnovation;

import org.apache.commons.io.FileUtils;
import org.commons.FilenameUtils;
import org.commons.OutputUtils;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.adapters.DataAdapter;
import org.ou.gatekeeper.adapters.sh.SHAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;

/**
 * @todo
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class SHKGConstruction {

//  static final String DATASETS_DIR = "datasets/phr";
  static final String DATASETS_DIR = "datasets/data-sh2";
  static final String OUTPUT_DIR = "output/kg-sh2";

  static final Logger LOGGER = LoggerFactory.getLogger(SHKGConstruction.class);

  public static void main(String[] args) {
    // @todo NoSuchFileException: datasets/data-sh.. dataset missing
    File datasetsDir = new File(DATASETS_DIR);
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdirs();
    OutputUtils.clean(outputDir);
    String outputExt = "nt";

    String[] exts = {"json"};
    Iterator<File> datasets = FileUtils.iterateFiles(datasetsDir, exts, true);

    DataAdapter converter = SHAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

    while (datasets.hasNext()) {
      File dataset = datasets.next();
      LOGGER.info(">>> " + dataset.getAbsolutePath());
      String trimmedDatasetName = FilenameUtils.trim2LvlExtension(dataset.getName());
      Long timestamp = System.nanoTime();
      String outputFilename = "output-" + timestamp + "-" + FilenameUtils
        .changeExtension(trimmedDatasetName, outputExt);
      File output = new File(outputDir, outputFilename);
      RDFizer.transform(dataset, output, converter, mapping);
    }
  }

}
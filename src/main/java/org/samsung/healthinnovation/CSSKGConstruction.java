package org.samsung.healthinnovation;

import org.apache.commons.io.FileUtils;
import org.commons.FilenameUtils;
import org.commons.OutputUtils;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.adapters.DataAdapter;
import org.ou.gatekeeper.adapters.css.CSSAdapter;
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
public class CSSKGConstruction {

  static final String DATASETS_DIR = "datasets/emr";
  static final String OUTPUT_DIR = "output/kg-css";

  static final Logger LOGGER = LoggerFactory.getLogger(CSSKGConstruction.class);

  public static void main(String[] args) {
    // @todo NoSuchFileException: datasets/data-css dataset missing
    File datasetsDir = new File(DATASETS_DIR);
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdirs();
    OutputUtils.clean(outputDir);
    String outputExt = "nt";

    String[] exts = {"json"};
    Iterator<File> datasets = FileUtils.iterateFiles(datasetsDir, exts, true);

    DataAdapter converter = CSSAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

    while (datasets.hasNext()) {
      File dataset = datasets.next();
      String trimmedDatasetName = FilenameUtils.trim2LvlExtension(dataset.getName());
      String outputFilename = "output-" + FilenameUtils
        .changeExtension(trimmedDatasetName, outputExt);
      File output = new File(outputDir, outputFilename);
      RDFizer.transform(dataset, output, converter, mapping);
    }

  }

}
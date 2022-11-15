package org.samsung.healthinnovation.structuredkgconstruction;

import org.apache.commons.io.FileUtils;
import org.commons.OutputUtils;
import org.ou.gatekeeper.fhir.adapters.EMRAdapter;
import org.ou.gatekeeper.fhir.adapters.FHIRAdapter;
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
public class EMRKGConstruction {

  static final String DATASETS_DIR = "datasets/data-emr";
  static final String OUTPUT_DIR = "output/kg-emr";

  static final Logger LOGGER = LoggerFactory.getLogger(EMRKGConstruction.class);

  public static void main(String[] args) {
    // @todo NoSuchFileException: datasets/data-emr dataset missing
    File datasetsDir = new File(DATASETS_DIR);
    File outputDir = new File(OUTPUT_DIR);
    outputDir.mkdir();
    OutputUtils.clean(outputDir);
    String outputExt = "nt";

    String[] exts = {"json"};
    Iterator<File> datasets = FileUtils.iterateFiles(datasetsDir, exts, false);

    FHIRAdapter converter = EMRAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

//    RDFizer.trasform(datasets, converter, mapping, outputDir, outputExt); // TODO fix
  }

}
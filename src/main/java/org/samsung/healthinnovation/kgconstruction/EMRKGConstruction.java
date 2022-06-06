package org.samsung.healthinnovation.kgconstruction;

import org.apache.commons.io.FileUtils;
import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.fhir.adapters.EMRAdapter;
import org.ou.gatekeeper.fhir.adapters.FHIRAdapter;
import org.ou.gatekeeper.fhir.adapters.PHRAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;
import java.io.File;
import java.util.Iterator;

/**
 * @todo
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class EMRKGConstruction {


  static final String DATASETS_DIR = "datasets/data-emr";
  static final String RESULT_DIR = "output/kg-emr";

  public static void main(String[] args) {
    File datasetsDir = new File(DATASETS_DIR);
    String[] exts = {"json"};
    Iterator<File> datasets = FileUtils.iterateFiles(datasetsDir, exts, false);
    File outputFolder = new File(RESULT_DIR);
    outputFolder.mkdir();
    String outputExt = "nt";

    FHIRAdapter converter = EMRAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

    RDFizer.trasform(datasets, converter, mapping, outputFolder, outputExt);
  }

}
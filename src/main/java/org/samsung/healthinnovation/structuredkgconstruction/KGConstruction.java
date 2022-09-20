package org.samsung.healthinnovation.structuredkgconstruction;

import org.ou.gatekeeper.RDFizer;
import org.ou.gatekeeper.fhir.adapters.EMRAdapter;
import org.ou.gatekeeper.fhir.adapters.FHIRAdapter;
import org.ou.gatekeeper.fhir.adapters.PHRAdapter;
import org.ou.gatekeeper.rdf.enums.OutputFormat;
import org.ou.gatekeeper.rdf.mappings.HelifitMapping;
import org.ou.gatekeeper.rdf.mappings.RMLMapping;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class KGConstruction {

  /**
   * TODO desk
   * */
  public static void emr() {

    FHIRAdapter converter = EMRAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

//    RDFizer.trasform(datasetFile, converter, mapping, outputFile);
  }

  /**
   * TODO desk
   * */
  public static void phr() {
    FHIRAdapter converter = PHRAdapter.create();
    RMLMapping mapping = HelifitMapping.create(OutputFormat.NTRIPLES);

//    RDFizer.trasform(datasetFile, converter, mapping, outputFile);
  }

  /**
   * TODO desk
   * */
  private KGConstruction() {
  }

}

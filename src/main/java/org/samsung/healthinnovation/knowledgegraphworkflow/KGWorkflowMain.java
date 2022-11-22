package org.samsung.healthinnovation.knowledgegraphworkflow;

import org.commons.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class KGWorkflowMain {

  static final String KGWORKFLOW_CONFIG = "kgworkflow.properties";

  public static void main(String[] args) throws IOException {
    Properties config = PropertiesUtils.loadConfiguration(KGWORKFLOW_CONFIG);
    KGWorkflow.run(config);
  }

}

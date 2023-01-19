# MatKG

MatKG is a tool for building materialized health knowldge graph from structured and semistructured data, including PHR data from Samsung Health and EMR from T2D lab test.  
The current version V1.1.1 of MatKG is based on:
1. HeLiFit ontology V1.6.5
2. Competecy Query set V1.6.5
3. RDFizer V1.1.1
4. Data PHR: Samsung Health & Data EMR: T2D Lab test.


# Pre-requisites
1. JAVA - Install from [java link](https://www.java.com/en/download/). MatKG is developed using using JDK-17.0.2.
2. RDFox - dowload from [rdfox link](https://www.oxfordsemantic.tech/downloads), make sure you have the license is installed.
3. RDFizer - dowload rdfizer-v1.1.1.jar from [rdfizer](https://github.ecodesamsung.com/Health-Innovation/rdfizer/releases/tag/v1.1.1)
4. [ToReview] Data - download the health data from [data-emr]() & [data-phr](). Please contact us for the data.

# Clone
1. with HTTPS, git clone https://github.ecodesamsung.com/Health-Innovation/matkg.git
2. with SSH, git clone git@github.ecodesamsung.com:Health-Innovation/matkg.git
3. Open the project in your IDE. We used IDEAJ which is higly recommendate for compatibility issues and the instruction as follow.
4. Configure IDEAJ to make the project running

## Configure RDFox
1. Download RDFox 6.0 from https://www.oxfordsemantic.tech/downloads
2. Create "rdfox" folder under matkg/lib
3. Copy the files JRDFox.jar, libRDFox.dll, libRDFox.lib, libRDFox-static.lib under rdfizer/lib/rdfox
4. Copy RDFox licence matkg/lib
5. Go to File --> Project Structure --> Libraries --> click on "+" --> Java --> select matkg/lib/rdfox/JRDFox.java --> APPLY.

## Configure RDFizer
1. RDFizer - dowload rdfizer-v1.1.1.jar from [rdfizer](https://github.ecodesamsung.com/Health-Innovation/rdfizer/releases/tag/v1.1.1)
2. Copy the files rdfizer-v1.1.1.jar under rdfizer/lib/
3. Go to File --> Project Structure --> Libraries --> click on "+" --> Java --> select rdfizer/lib/rdfizer-v1.1.1.jar --> APPLY.

## Build and Executor from IDEs
1. Build Project with "Build --> Build Project"
2. Copy the EMR data under the  "dataset/emr" folder of the project;
3. Copy the PHR "dataset/phr" folder of the project;
4. For EMR data, use matkg\src\main\java\org\samsung\healthinnovation\CSSKGConstruction.java
5. For PHR data, use matkg\src\main\java\org\samsung\healthinnovation\SHKGConstruction.java

## Troubleshotting
### Maven Dependencies not found
1. File --> Settings --> "Build, Execution. Deployment" --> Build Tools --> Maven --> Repositories --> Update the hhtps://repo.maven.apache.org/maven2
2. From the pom.xml file, right click --> Maven --> Reload Project


## To Review
4.2 Add the JAR libraries: File --> Project Structure --> Libraries --> New Project Library (+ button) --> add JRDFox.jar, rdfizer-v1.1.1.jar in libs
4.3 Set Java: File --> Setting --> Build, Execution, Deployment --> Build Tool --> Maven --> Runner --> JRE
5. Add data-emr and data-phr under the folder dataset of MatKG
6. Build the KG by running PHRKGConstruction.java and EMRKGConstruction.java. See the result in output/kg-phr and output/kg-emr
7. Run all the competency queries with ALLCompetencyQueries.java over kg-phr + kg-emr
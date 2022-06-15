# MatKG

MatKG is a tool for building materialized health knowldge graph from structured and semistructured data, including PHR data from Samsung Health and EMR from T2D lab test.  
The current version V1.0 of MatKG is based on: 
1. HeLiFit ontology V1.5.0
2. Competecy Query set V1.5.0
3. RDFizer V1.0.0
4. Data PHR: Samsung Health & Data EMR: T2D Lab test.   


# Pre-requisites
1. JAVA - Install from [java link](https://www.java.com/en/download/). MatKG is developed using using JDK-17.0.2.
2. RDFox - dowload from [rdfox link](https://www.oxfordsemantic.tech/downloads), make sure you have the license is installed.
3. RDFizer - dowload rdfizer-v1.0.0.jar from [rdfizer](https://github.ecodesamsung.com/Health-Innovation/rdfizer/releases/tag/v1.0.0)
4. Data - download the health data from [data-emr]() & [data-phr]()   

# Steps to set-up
1. with HTTPS, git clone https://github.ecodesamsung.com/Health-Innovation/matkg.git
2. with SSH, git clone git@github.ecodesamsung.com:Health-Innovation/matkg.git
3. Open the project in your IDE. We used IDEAJ which is higly recommendate for compatibility issues and the instruction as follow.
4. Configure IDEAJ to make the project running: 
- Add the JAR libraries: File --> Project Structure --> Libraries --> New Project Library (+ button) --> add JRDFox.jar, rdfizer-v1.0.0.jar in libs
- Set Java: File --> Setting --> Build, Execution, Deployment --> Build Tool --> Maven --> Runner --> JRE
5. Add data-emr and data-phr under the folder dataset of MatKG
6. Build the KG by running PHRKGConstruction.java and EMRKGConstruction.java. See the result in output/kg-phr and output/kg-emr 
7. Run all the competency queries with ALLCompetencyQueries.java over kg-phr + kg-emr

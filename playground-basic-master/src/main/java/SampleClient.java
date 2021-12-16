import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
    	System.out.println("running started");
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();
  
       
        List<Patient> patientList = new ArrayList<Patient>(); //create a list storing patients' information
        for(int i = 0; i < response.getEntry().size(); i++) {
        	patientList.add((Patient)response.getEntry().get(i).getResource());
        }
        
        sortPatients(patientList);
        printPatient(patientList);
        System.out.println("exit");
   
    }
    //BASIC TASKS
    public static void sortPatients(List<Patient> patients) {  //Sorts a list of `Patient` by Given Name
    	System.out.println("sorting patients");
    	Collections.sort(patients, new Comparator<Patient>() {  //Compares patient elements by the given name field
    	    public int compare(Patient p1, Patient p2) {
    	        return p1.getName().get(0).getGiven().get(0).toString().toUpperCase().compareTo(p2.getName().get(0).getGiven().get(0).toString().toUpperCase());
    	    }
    	   });
    }
    
    public static void printPatient(List<Patient> patients) { //output a list of `Patient` to the console
    	System.out.println("printing patients");
    	 for(Patient p : patients) {
         	System.out.println(p.getName().get(0).getGiven().get(0).toString() + " " + p.getName().get(0).getFamily().toString());
         	
         }
    }

}

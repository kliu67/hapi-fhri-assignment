import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new myClientInterceptor(false));
        //responseTime for each search
        int totalResponseTime = 0;
        //total number of runs completed
        int numberOfRuns = 0; 
        //counter for number of loops to run
        int loopCounter = Integer.valueOf(theArgs[0]);
        //path of .txt file
        String path = theArgs[1].toString();
        //toggles cache-control for the httprequest
        boolean cacheOff = false;
        //open a file containing a list of family names
        while(loopCounter > 0) {
        	System.out.println("Loop " + loopCounter);
        	System.out.println("Searching...");
        	try {
        		//opens a .txt file containing family names
            	Scanner scanner = new Scanner(new File(path));
            	//repeats the search n number of times, n = loopCounter
    			while (scanner.hasNextLine()) {
    				//list to holder the search result
    	            
    	            
    				List<Patient> pList = new ArrayList<Patient>();
    				
    				//if this is a 3rd run, turn off cache
    				if(loopCounter == 1) {
    					cacheOff = true;
    				}
    				//if this is not a 3rd run, turn on cache
    				else {
    					cacheOff = false;
    				}
    				
    				Bundle re = searchPatient(scanner.nextLine(), client, cacheOff);
    				//sums the response time and number of search requests
    				totalResponseTime += ((myClientInterceptor)client.getInterceptorService().getAllRegisteredInterceptors().get(0)).gettime();
    				numberOfRuns++;
    				
    				//uncomment below to store the search result in an arralist, perform sort, and print the patient names to console
    				for(int i = 0; i < re.getEntry().size(); i++) {
    					pList.add((Patient)re.getEntry().get(i).getResource());
    				}
    				sortPatient(pList);
    				printPatient(pList);
    				
    			}
    			scanner.close();
    			
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
        	
        	//outputs running results
        	System.out.println("cache turnedd off ?: " + cacheOff);
            System.out.println("total time is " + totalResponseTime);
            System.out.println("numberofruns is " + numberOfRuns);
            System.out.println("average time is " + avgTime(totalResponseTime, numberOfRuns));
            loopCounter--;
        }

    }
    
    
    public static Bundle searchPatient (String familyName, IGenericClient client, boolean cacheControl) {
    	return client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value(familyName))
                .cacheControl(new CacheControlDirective().setNoCache(cacheControl))
                .returnBundle(Bundle.class)
                .execute();
    	
    }
    
    public static void sortPatient(List<Patient> patients) {  //Sorts a list of `Patient` by Given Name
    	Collections.sort(patients, new Comparator<Patient>() {  //Compares patient elements by the given name field
    	    public int compare(Patient p1, Patient p2) {
    	        return p1.getName().get(0).getGiven().get(0).toString().toUpperCase().compareTo(p2.getName().get(0).getGiven().get(0).toString().toUpperCase());
    	    }
    	   });
    }
    
    public static void printPatient(List<Patient> patients) { //output a list of `Patient` to the console
    	 for(Patient p : patients) {
         	System.out.println(p.getName().get(0).getGiven().get(0).toString() + " " + p.getName().get(0).getFamily().toString());
         	
         }
    }
    
    public static int avgTime(int totalTime, int numberOfRuns) { //get the average response time of all searches in one iteration
    	return totalTime / numberOfRuns;
    }

}

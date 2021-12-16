# hapi-fhri-assignment


**How to run: Due to external dependencies, I was not able to compile the program in command line. All successful compilation was done in an IDE(Eclipse).
**


***The following information is to run the program that performs the basic tasks.***
The program takes no parameters

example:  java SampleClient

The program connects to the FHIR server and performs a search of patients with family name 'SMITH'. Output of the search is stored in a list for optimal sorting. Patients are sorted by given name alphabetically, ignoring case.


***The following information is to run the program that performs the intermediate tasks.
The program takes two parameters: 
  1. number of loops (int)
  2. file location (String)

example: java SampleClient 3 FamilyName.txt

The program will run a number of times equal to the number of loops indicated by a parameter. In each run, the program opens a .txt file containing a list of 20 family names. For each family name, a search request is sent. For each request, the response time is recorded and summed into a variable. At the end of each run, the average running time is obtained by dividing the total response time by the number of search requests. 

For all the runs done, cache is turned on until the last run. Cache control is toggled in the follow code:

                _client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value(familyName))
                .cacheControl(new CacheControlDirective().setNoCache(cacheControl))
                .returnBundle(Bundle.class)
                .execute();_
                
 Due to time constraints, unit test cases are not yet implement. I Will implement them later.






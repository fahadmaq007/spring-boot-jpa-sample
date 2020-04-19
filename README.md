# spring-boot-jpa-sample
Sample spring boot application demonstrating the generic usage of JPA APIs &amp; to be able to query easily.

How to Run?
***********
Before you run the application have a DB up & running & provide the details accordingly in the application.yml (src /resource folder)

<code>
 spring:
  datasource:
    url: jdbc:postgresql://YOURHOST:5432/spring-jpa
    username: postgres
    password: postgres
</code>

Run ./run.sh - A spring boot application will be hosted on http://localhost:8881

Upload Sample Data:
******************
The persons.json file is present in test folder resources, you can use the following API to store the data.

POST: <code>http://localhost:8881/persons</code>

BODY: The file content - JSON array

How to Query?
*************
List persons: 
GET Method: 
1. <code>http://localhost:8881/persons?age=30</code>
2. <code>http://localhost:8881/persons?age:lt=30</code>
3. <code>http://localhost:8881/persons?name:like=Dunlap</code>
4. <code>http://localhost:8881/persons?dob:btw=millis1,millis2</code> # Time in Millis

POST Method: 
1. <code>http://localhost:8881/persons/json?sort=age&page=0&size=20</code>
   
   Body: <code>{"filters":[{"field":"age","value":30}]}</code> # The Operation EQ is default
2. <code>http://localhost:8881/persons/json?sort=age&page=0&size=20</code>
   
   Body: <code>{"filters":[{"field":"age","op":"LT","value":30}]}</code>

For more details, please refer the API details below.

API Details:
***********

It has a <code>com.maqs.springboot.sample.repository.SpecificationBuilder</code> that translates the <code>com.maqs.springboot.sample.dto.SearchCriteria</code> DTO object & prepares a collection of Predicates. 

For. eg. Create a search criteria on Person entity to filter by age who are less than 30. The JSON below would fetch the persons whose age is less than 30.

<code>
 SearchCriteria searchCriteria = new SearchCriteria();
 
 SearchCriteria.Filter age30 = new SearchCriteria.Filter(field, SearchCriteria.Operation.LT, value);
 searchCriteria.addFilter(age30);
</code> 

The above criteria is equivalent to the following JSON:

<code>{"filters":[{"field":"age","op":"LT","value":30}]}</code>

Add New Entity:
***************
Add a new Managed Entity class and use the generic SpecificationBuilder to perform the similar operations illustrated above.

For eg. Employee Entity - List the employees who are under Department#1

GET Method: 
<code>http://localhost:8881/employees?dept=1</code>


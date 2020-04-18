# spring-boot-jpa-sample
Sample spring boot application demonstrating the generic usage of JPA APIs &amp; to be able to query easily.

How to Run?
***********
Run ./run.sh - A spring boot application will be hosted on http://localhost:8881

How to Query?
*************
List persons: 
1. GET Method: <code>http://localhost:8881/persons?age:lt=30</code>

2. POST Method: 
   URL : <code>http://localhost:8881/persons?sort=age&page=0&size=20</code>
   Body: <code>{"filters":[{"field":"age","op":"LT","value":30}]}</code>

API Details:
***********

It has a <code>com.maqs.springboot.sample.repository.SpecificationBuilder</code> that translates the <code>com.maqs.springboot.sample.dto.SearchCriteria</code> DTO object & prepares a collection of Predicates. 

For. eg. Create a search criteria on Person entity to filter by age who are less than 30. The JSON below would fetch the persons whose age is less than 30.

<code>{"filters":[{"field":"age","op":"LT","value":30}]}</code>


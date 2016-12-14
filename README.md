# introsde-2016-assignment-1

## TASK

This first assignment is divided in two main parts.
The first part is based on the third laboratory session. In particular, it was required to work with XPath, a query language that uses path expressions to navigate the XML and queries information inside them. This first part is moreover divided in four points.

For the first point it is necessary to use XPath to implement method **getWeight(personID)** and **getHeight(personID)**, which return weight (or height) of a person with that ID. Instead, make a function **printAll()** that prints all people in the **database.xml** with detail.

The **database.xml** file contains 20 person and has the following structure:


```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<people>
	<person id="1">
        	<firstname>George R. R.</firstname>
        	<lastname>Martin</lastname>
        	<birthdate>1984-09-20T18:00:00.000+02:00</birthdate>
        	<healthprofile>
            		<lastupdate>2014-09-20T18:00:00.000+02:00</lastupdate>
            		<weight>90</weight>
            		<height>1.70</height>
            		<bmi>31.14</bmi>
        	</healthprofile>
    	</person>
    ....   
</people>   
```

The third point concerns a function **printHealthProfile(personID)** that accepts an ID as parameter and prints the HealthProfile of the person with that ID. Finally, it is needed another function **printPersonFromWeight(weight, operator)** which accepts a weight and an operator (=, > or <) as parameters and prints people that fulfill that condition.

On the other hand, the second part of the assignment is based on the forth laboratory session. This second part is about JAXB which allows to map XML to Java Classes. In particular, the first step concerns the creation of an **person.xsd** file (XML schema) from the XML document **database.xml**. Through this XSD file is possible to automatically generate four classes thanks to the Java compiler XJC. The classes, which will be generated in **peoplestore.generated** package, are **HealthprofileType.java**, **ObjectFactory.java**, **PeopleType.java** and **PersonType.java**.

These classes are necessary to do the marshalling to XML and the unmarshalling from XML. The **XMLMarshaller.java** class handles the marshalling to XML, printing the content and saving it to **people.xml** file. The **XMLUnMarshaller.java** class handles the unmarshalling from **people.xml**.

Moreover, they can be used also for marshalling to JSON thanks to **MOXy** library. Marshalling to **people.json** is done by the **JSONMarshaller.java** class.

After completing these two parts of the assignment, it is necessary create a target in the **build.xml** file **execute.evaluation** which:

1. runs **printAll()**
2. runs **printHealthProfile(personID)** with ID = 5
3. runs **printPersonFromWeight(weight, operator)** with weight > 90
4. runs **XMLMarshaller**, it creates 3 persons using java and marshals them to XML, printing the content and saving to **people.xml** file
5. runs **XMLUnMarshaller**, un-marshaling from **people.xml**
6. runs **JSONMarshaller**, it creates 3 persons using java and marshals them to JSON, printing the content and saving to **people.json** file.

## CODE
### XPathAdvance
The first part of the assignment has been implemented in the **XPathAdvance.java** class, which is based on the XPathTestAdvance seen in laboratory. Besides loading the **database.xml** file, the **XPathAdvance** class contains **getWeight(personID)**, **getHeight(personID)**, **printAll()**, **printHealthProfile(personID)** and **printPersonFromWeight(weight, operator)** methods. These methods work in almost the same way.

For example, this is the **printPersonFromWeight(weight, operator)** method:
```java
public void printPersonFromWeight(int weight, String operator) throws XPathExpressionException {
		XPathExpression expr = xpath
				.compile("/people/person/healthprofile[weight" + operator + "" + weight + "]/parent::person");
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println("People with weight " + operator + " " + weight + "Kg:");
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getTextContent());
		}
}
  ```
The method evaluates the compiled XPath expression in database.xml and returns the result in a list of node. Later, it prints the result.

For the second part of the assign three different classes were used: **XMLMarshaller**, **XMLUnMarshaller** and **JSONMarshaller**. To work correctly, these classes need the classes generated from **people.xsd** using XJC.

### XMLMarshaller
The **XMLMarshaller** instantiates a new JAXB Context. After that, it creates a new marshaller using the JAXB Context and set its properties.
```java
// Create a JaxBContext
JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
// Create the Marshaller Object using the JaxB Context
Marshaller marshaller = jaxbContext.createMarshaller();
// Set it to true if you need the XML output to formatted
marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
```
  
Now, people can be created through the generated classes using java.
```java
ObjectFactory factory = new ObjectFactory();

PeopleType people = factory.createPeopleType();

PersonType person = factory.createPersonType();
List<PersonType> personList = people.getPerson();
			
XMLGregorianCalendar randomBirthdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(randomBirthdate());
person.setBirthdate(randomBirthdate);
person.setFirstname("Daniele");
person.setId(1);
person.setLastname("Dellagiacoma");
HealthprofileType healthprofile = factory.createHealthprofileType();
			
GregorianCalendar c = new GregorianCalendar();
c.setTime(new Date());
XMLGregorianCalendar lastupdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
healthprofile.setLastupdate(lastupdate);
healthprofile.setHeight(1.80);
healthprofile.setWeight(70);
healthprofile.setBmi(healthprofile.getWeight()/(Math.pow(healthprofile.getHeight(), 2)));

person.setHealthprofile(healthprofile);

personList.add(person);
  ```
  
Finally, a JAXB Element of type People is created and used to print the content and save it to **people.xml** file.

```java
JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
// Marshal the People object to XML and print the output to console
marshaller.marshal(peopleElement, System.out);
// Marshal the People object to XML and write the output to the people.xml file
marshaller.marshal(peopleElement, new FileOutputStream(xmlDocument));
  ```
### JSONMarshaller

The **JSONMarshaller** is the same as **XMLMarshaller**, except that it uses **MOXy** library to format the otput object as a json. It do that setting the **MarshallerProperties** in this way:

```java
// Set the Marshaller media type to JSON
marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
// Set it to true if you need to include the JSON root element in the JSON output
marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
  ```
  
For this reason is necessary to enable **MOXy** at the beginning

```java
//enables MOXy
System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
  ```

### XMLUnMarshaller

On the other hand, the **XMLUnMarshaller** create an JAXB context and the UnMarshaller object using the context. After that, it uses **people.xsd** schema to validate subsequent unmarshal operations. 

```java
// Create a JaxBContext
JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
// Create the Unmarshaller Object using the JaxB Context
Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
Schema schema = schemaFactory.newSchema(new File("people.xsd"));
unMarshaller.setSchema(schema);
  ```
  
  Now, it can use a People element to get the value of each person in the file **people.xml**.
  
 ```java
 JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller.unmarshal(xmlDocument);

//Create root object type from the JAXBElement object using its getValue() method
PeopleType people = peopleElement.getValue();
   ``` 
  
Finally, it prints all the person in **people.xml** and their information.


## DEPLOYMENT

First of all, it is necessary to run the following command on terminal window in the folder where the **build.xml** file is:
```sh
ant compile
```

This instruction will download **ivy.jar** into **ivy** folder, and all libraries that are specified in **ivi.xml** fil will be downloaded into lib folder. Moreover, the target generate creates the folder **bookstore/generated** with four classes in it (**HealthprofileType**, **ObjectFactory**, **PeopleType** and **PersonType**).

After that, the following command can be used to run all the instructions described above.
```sh
ant execute.evaluation
```

The **build.xml** file has been implemented modifying the build.xml used during the forth laboratory. 



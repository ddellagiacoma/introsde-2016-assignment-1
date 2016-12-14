package peoplestore;

import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import peoplestore.generated.*;


public class XMLUnMarshaller {  	

	public void unMarshall(File xmlDocument) throws Exception {
		
			// Create a JaxBContext
			JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
			// Create the Unmarshaller Object using the JaxB Context
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File("people.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller.unmarshal(xmlDocument);

			//Create root object type from the JAXBElement object using its getValue() method
			PeopleType people = peopleElement.getValue();

			List<PersonType> personList = people.getPerson();
			//Use appropriate getters of root object to get for each Person
			//its attributes and children
			for (int i = 0; i < personList.size(); i++) {

				PersonType person = (PersonType) personList.get(i);
				System.out.println("ID "+person.getId());
				System.out.println("Firstname "+person.getFirstname());
				System.out.println("Lastname "+person.getLastname());
				System.out.println("Birthday "+person.getBirthdate());
				
				HealthprofileType healthprofile=person.getHealthprofile();
				System.out.println("Lastupdate "+healthprofile.getLastupdate());
				System.out.println("Weight "+healthprofile.getWeight());
				System.out.println("Height "+healthprofile.getHeight());
				System.out.println("BMI "+healthprofile.getBmi());		
			}
	}
	
	public static void main(String[] args) throws Exception {
		File xmlDocument = new File("people.xml");
		XMLUnMarshaller jaxbUnmarshaller = new XMLUnMarshaller();
		jaxbUnmarshaller.unMarshall(xmlDocument);
    }	
	
	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}
	}
}
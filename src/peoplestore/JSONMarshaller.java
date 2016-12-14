package peoplestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import peoplestore.generated.*;


public class JSONMarshaller {

	//prints and generate the people.json document
	public void generateJSONDocument(File jsonDocument) throws Exception {

		//enables MOXy
		System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");

		// Create a JaxBContext
		JAXBContext jc = JAXBContext.newInstance("peoplestore.generated");
		// Create the Marshaller Object using the JaxB Context
		Marshaller marshaller = jc.createMarshaller();
		// Set the Marshaller media type to JSON
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		// Set it to true if you need to include the JSON root element in the JSON output
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
		// Set it to true if you need the JSON output to formatted
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		ObjectFactory factory = new ObjectFactory();

		PeopleType people = factory.createPeopleType();

		// create first person
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
		healthprofile.setBmi(healthprofile.getWeight() / (Math.pow(healthprofile.getHeight(), 2)));

		person.setHealthprofile(healthprofile);

		personList.add(person);

		//create second person
		person = factory.createPersonType();
		personList = people.getPerson();

		randomBirthdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(randomBirthdate());
		person.setBirthdate(randomBirthdate);
		person.setFirstname("Pinco");
		person.setId(2);
		person.setLastname("Pallo");
		healthprofile = factory.createHealthprofileType();

		c = new GregorianCalendar();
		c.setTime(new Date());
		lastupdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		healthprofile.setLastupdate(lastupdate);
		healthprofile.setHeight(1.80);
		healthprofile.setWeight(70);
		healthprofile.setBmi(healthprofile.getWeight() / (Math.pow(healthprofile.getHeight(), 2)));

		person.setHealthprofile(healthprofile);

		personList.add(person);
		
		//create third person
		person = factory.createPersonType();
		personList = people.getPerson();

		randomBirthdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(randomBirthdate());
		person.setBirthdate(randomBirthdate);
		person.setFirstname("Albert");
		person.setId(3);
		person.setLastname("Farm");
		healthprofile = factory.createHealthprofileType();

		c = new GregorianCalendar();
		c.setTime(new Date());
		lastupdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		healthprofile.setLastupdate(lastupdate);
		healthprofile.setHeight(1.80);
		healthprofile.setWeight(70);
		healthprofile.setBmi(healthprofile.getWeight() / (Math.pow(healthprofile.getHeight(), 2)));

		person.setHealthprofile(healthprofile);

		personList.add(person);
		JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
		// Marshal the People object to JSON and print the output to console
		marshaller.marshal(peopleElement, System.out);
		// Marshal the People object to JSON and write the output to the people.json file
		marshaller.marshal(peopleElement, new FileOutputStream(jsonDocument));
	}

	public static void main(String[] args) throws Exception {
		String jsonDocument = "people.json";
		JSONMarshaller jaxbMarshaller = new JSONMarshaller();
		jaxbMarshaller.generateJSONDocument(new File(jsonDocument));
	}

	//returns a random birthdate
	private static GregorianCalendar randomBirthdate() {
		GregorianCalendar gc = new GregorianCalendar();
		int year = randBetween(1940, 2000);
		gc.set(Calendar.YEAR, year);
		int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
		gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return gc;
	}

	//returns a random day of the year
	private static int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}
}
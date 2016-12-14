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

import peoplestore.generated.*;


public class XMLMarshaller {

	//prints and generate the people.xml document
	public void generateXMLDocument(File xmlDocument) throws Exception {
		
			// Create a JaxBContext
			JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
			// Create the Marshaller Object using the JaxB Context
			Marshaller marshaller = jaxbContext.createMarshaller();
			// Set it to true if you need the XML output to formatted
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			
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
			healthprofile.setBmi(healthprofile.getWeight()/(Math.pow(healthprofile.getHeight(), 2)));

			person.setHealthprofile(healthprofile);

			personList.add(person);
			
			// create second person
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
			healthprofile.setHeight(1.75);
			healthprofile.setWeight(64);
			healthprofile.setBmi(healthprofile.getWeight()/(Math.pow(healthprofile.getHeight(), 2)));

			person.setHealthprofile(healthprofile);

			personList.add(person);
			
			// create third person
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
			healthprofile.setHeight(1.92);
			healthprofile.setWeight(87);
			healthprofile.setBmi(healthprofile.getWeight()/(Math.pow(healthprofile.getHeight(), 2)));

			person.setHealthprofile(healthprofile);

			personList.add(person);

			JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
			// Marshal the People object to XML and print the output to console
			marshaller.marshal(peopleElement, System.out);
			// Marshal the People object to XML and write the output to the people.xml file
			marshaller.marshal(peopleElement, new FileOutputStream(xmlDocument));
	}

	public static void main(String[] args) throws Exception {
		String xmlDocument = "people.xml";
		XMLMarshaller jaxbMarshaller = new XMLMarshaller();
		jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
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
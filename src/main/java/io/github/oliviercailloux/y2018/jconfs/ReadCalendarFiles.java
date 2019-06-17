package io.github.oliviercailloux.y2018.jconfs;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.Iterator;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

/**
 * This class allows to read and iCalelndar file and creates a conference object
 * from a parsed iCalendar file
 * 
 * @author berkani-mustapha
 *
 */
public class ReadCalendarFiles {

	/**
	 * Parse an iCalendar object with ical4j API function took from source :
	 * https://www.programcreek.com/java-api-examples/?api=net.fortuna.ical4j.model.property.Method
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws ParserException
	 */
	public static void ReadCalendarFile(String filePath) throws IOException, ParserException {

		try (FileInputStream fin1 = new FileInputStream(filePath)) {

			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(fin1);

			// Iterating over the calendar component
			for (Iterator<CalendarComponent> i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = i.next();
				System.out.println("Component [" + component.getName() + "]");
				// Iterating over the component property
				for (Iterator<Property> j = component.getProperties().iterator(); j.hasNext();) {
					Property property = j.next();
					System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
				}
			}
		}
	}

	/**
	 * generate a localDate from string parametre This function is needed since the
	 * ical4j property DTSTART is a string so we have to convert it into a localDate
	 * format
	 * 
	 * @param date
	 * @return LocalDate
	 */
	public static LocalDate stringToLocalDate(String date) {
		return LocalDate.parse(date);
	}

	/**
	 * Creates conference from ics file, function inspired by function
	 * readCalendarFile
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @throws ParserException
	 * @throws ParseException
	 * @throws ValidationException
	 */

	public static Conference createConference(String filepath) throws IOException, ParserException, ParseException {

		Conference conf = null;
		try (FileInputStream fin2 = new FileInputStream(filepath)) {

			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(fin2);
			Component confCompo = calendar.getComponent("CONFERENCE");

			// the url is the primary key of a conference
			URL confURL = new URL(confCompo.getProperty("URL").getValue());
			conf = new Conference(confURL);

			// add the others attributes
			conf.setTitle(confCompo.getProperty("SUMMARY").getValue());
			conf.setCountry(confCompo.getProperty("COUNTRY").getValue());
			conf.setFeeRegistration(Double.parseDouble(confCompo.getProperty("FEE").getValue()));
			conf.setStartDate(confCompo.getProperty("DTSTART").getValue());
			conf.setEndDate(confCompo.getProperty("DTEND").getValue());
			conf.setCity(confCompo.getProperty("CITY").getValue());
		}
		return conf;

	}
}

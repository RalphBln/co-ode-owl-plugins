/**
 * 
 */
package uk.ac.manchester.mae.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.coode.oae.utils.ParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;

import uk.ac.manchester.mae.parser.ArithmeticsParser;
import uk.ac.manchester.mae.parser.ParseException;

/**
 * @author Luigi Iannone
 * 
 */
public class Tester extends TestCase {
	private static final String expressionFilePathPropertyName = "expressionFilePath";
	private File tests;
	Set<String> lines = new HashSet<String>();

	@Override
	protected void setUp() throws Exception {
		tests = new File(System
				.getProperty(expressionFilePathPropertyName));
		lines = loadLines(new FileReader(tests));
		super.setUp();
	}

	public void testParser() {
		for (String string : lines) {
			ParserFactory.initParser(string, OWLManager
					.createOWLOntologyManager());
			System.out.println(string);
			try {
				ArithmeticsParser.Start();
			} catch (ParseException e) {
				System.out.println(string);
				System.out.println(e);
				fail();
			}
		}
	}

	/**
	 * Load a {@link Set} of String elements each containing a line from the
	 * input {@link FileReader}. <b>Notice: Lines MUST not contain whitesapces</b>
	 * 
	 * @param reader
	 * @return a {@link Set} of {@link String} elements each containing a
	 *         non-empty line.
	 * @throws IOException
	 */
	public Set<String> loadLines(FileReader reader) throws IOException {
		Set<String> toReturn = new HashSet<String>();
		BufferedReader aBufferedReader = new BufferedReader(reader);
		String aLine = null;
		while ((aLine = aBufferedReader.readLine()) != null) {
			aLine = aLine.trim();
			if (!aLine.matches("\\s*")) {
				toReturn.add(aLine);
			}
		}
		return toReturn;
	}
}

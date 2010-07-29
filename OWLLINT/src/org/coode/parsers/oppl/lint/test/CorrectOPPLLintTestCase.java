package org.coode.parsers.oppl.lint.test;

import java.net.URI;

import junit.framework.TestCase;

import org.coode.oppl.lint.OPPLLintParser;
import org.coode.oppl.lint.OPPLLintScript;
import org.coode.oppl.lint.ParserFactory;
import org.coode.parsers.test.JUnitTestErrorListener;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

public class CorrectOPPLLintTestCase extends TestCase {
	private static final JUnitTestErrorListener ERROR_LISTENER = new JUnitTestErrorListener();

	public void testTransitivePropertyHierarchy() throws Exception {
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = ontologyManager.createOntology(URI.create("http://oppl2.sourceforge.net/owllint/transitivePropertyHierachyTest.owl"));
		String script = "Transitivity in property hierarchies lint; ?x:OBJECTPROPERTY, ?y:OBJECTPROPERTY SELECT ?y subPropertyOf ?x, Transitive ?y, Transitive ?x  WHERE ?x!= ?y BEGIN REMOVE Transitive ?y END; RETURN ?y; Lint that corrects the undesirable situation in which a transitive object property is sub-property of another transitive object property";
		ParserFactory factory = new ParserFactory(ontology, ontologyManager);
		OPPLLintParser parser = factory.build(CorrectOPPLLintTestCase.ERROR_LISTENER);
		OPPLLintScript parsed = parser.parse(script);
		assertNotNull(parsed);
		System.out.println(parsed);
	}
}

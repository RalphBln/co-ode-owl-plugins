/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.manchester.cs.owl.lint.examples;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.lint.LintException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;

import uk.ac.manchester.cs.owl.lint.LintManagerFactory;
import uk.ac.manchester.cs.owl.lint.commons.Match;
import uk.ac.manchester.cs.owl.lint.commons.OntologyWiseLintPattern;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Feb 13, 2008
 */
public final class SingleSubClassLintPattern extends OntologyWiseLintPattern<OWLClass> {
	/**
	 * @return the set of {@link OWLClass} that have just one <b>asserted</b>
	 *         subclass in the input {@link OWLOntology}
	 * @throws LintException
	 * @see org.semanticweb.owlapi.lint.LintPattern#matches(org.semanticweb.owl.model.OWLOntology)
	 */
	@Override
	public Set<Match<OWLClass>> matches(OWLOntology ontology) throws LintException {
		Set<Match<OWLClass>> toReturn = new HashSet<Match<OWLClass>>();
		try {
			OWLReasoner reasoner = LintManagerFactory.getInstance().getReasoner();
			if (reasoner == null) {
				for (OWLClass cls : ontology.getClassesInSignature()) {
                    Collection<OWLClassExpression> subClasses = EntitySearcher
                            .getSubClasses(cls, ontology);
					int count = 0;
					Iterator<OWLClassExpression> it = subClasses.iterator();
					while (count <= 1 && it.hasNext()) {
						OWLClassExpression anSubClassOWLDescription = it.next();
						if (!anSubClassOWLDescription.isAnonymous()) {
							count++;
						}
					}
					if (count == 1) {
						toReturn.add(new Match<OWLClass>(cls, ontology,
								"The class has only got one sub-class"));
					}
				}
			} else {
				// Subclasses are equivalence classes rather than OWLClass
				// therefore
				// they are presented in sets
				for (OWLClass cls : ontology.getClassesInSignature()) {
					NodeSet<OWLClass> subClasses = reasoner.getSubClasses(cls, true);
					Set<OWLClass> flattened = subClasses.getFlattened();
					flattened.remove(ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing());
					if (flattened.size() == 1) {
						toReturn.add(new Match<OWLClass>(cls, ontology,
								"The class has only got one sub-class"));
					}
				}
			}
		} catch (OWLRuntimeException e) {
			throw new LintException(e);
		}
		return toReturn;
	}

	@Override
    public boolean isInferenceRequired() {
		return true;
	}
}

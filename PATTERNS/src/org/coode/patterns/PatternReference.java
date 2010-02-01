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
package org.coode.patterns;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.coode.oppl.variablemansyntax.Variable;
import org.semanticweb.owl.expression.OWLEntityChecker;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLConstantAnnotation;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.NamespaceUtil;

/**
 * @author Luigi Iannone
 * 
 *         Jun 25, 2008
 */
public class PatternReference {
	private OWLOntologyManager ontologyManger;
	private String patternName;
	private PatternConstraintSystem patternConstraintSystem;
	private boolean resolvable;
	private Set<String> visited = new HashSet<String>();
	private List<String>[] arguments;
	private PatternOPPLScript extractedPattern;

	public PatternReference(String patternName,
			PatternConstraintSystem constraintSystem,
			OWLOntologyManager ontologyManager, List<String>... args)
			throws PatternException {
		this.patternName = patternName;
		this.patternConstraintSystem = constraintSystem;
		this.ontologyManger = ontologyManager;
		this.init(args);
	}

	public PatternReference(String patternName,
			PatternConstraintSystem constraintSystem,
			OWLOntologyManager ontologyManager, Set<String> visitedPatterns,
			List<String>... args) throws PatternException {
		this.patternName = patternName;
		this.patternConstraintSystem = constraintSystem;
		this.ontologyManger = ontologyManager;
		this.visited = visitedPatterns;
		this.init(args);
	}

	protected void init(List<String>... args) throws PatternException {
		Set<OWLOntology> ontologies = this.ontologyManger.getOntologies();
		Iterator<OWLOntology> ontologyIterator = ontologies.iterator();
		boolean found = false;
		OWLOntology anOntology;
		this.extractedPattern = null;
		// first check whether a pattern with that name is present
		while (!found && ontologyIterator.hasNext()) {
			anOntology = ontologyIterator.next();
			Set<OWLOntologyAnnotationAxiom> ontologyAnnotationAxioms = anOntology
					.getOntologyAnnotationAxioms();
			Iterator<OWLOntologyAnnotationAxiom> it = ontologyAnnotationAxioms
					.iterator();
			OWLOntologyAnnotationAxiom anOntologyAnnotationAxiom;
			while (!found && it.hasNext()) {
				anOntologyAnnotationAxiom = it.next();
				OWLAnnotation<? extends OWLObject> annotation = anOntologyAnnotationAxiom
						.getAnnotation();
				if (!this.hasBeenVisited(annotation.getAnnotationURI())) {
					PatternExtractor patternExtractor = this.patternConstraintSystem
							.getPatternModelFactory().getPatternExtractor(
									this.getVisitedAnnotations());
					this.extractedPattern = annotation.accept(patternExtractor);
					found = this.extractedPattern != null
							&& this.extractedPattern.getName().equals(
									this.patternName);
				}
			}
		}
		if (!found) {
			throw new PatternReferenceNotFoundException(this.patternName);
		}
		// Now check its variable compatibility
		this.checkCompatibility(args);
		this.resolvable = this.isResolvable(args);
		// List<Object> replacements = this.computeReplacements(args);
		// this.resolution = this.extractedPattern
		// .getDefinitorialPortion(replacements);
		this.arguments = args;
	}

	/**
	 * @param extractedPattern
	 * @param args
	 * @return
	 */
	protected List<List<Object>> computeReplacements(List<String>... args) {
		List<List<Object>> replacements = new ArrayList<List<Object>>();
		for (List<String> iThAssignments : args) {
			List<Object> iThReplacement = new ArrayList<Object>();
			for (String iThAssignment : iThAssignments) {
				Variable v = this.patternConstraintSystem
						.getVariable(iThAssignment);
				if (v != null) {
					iThReplacement.add(v);
				} else {
					iThReplacement.add(this.parse(iThAssignment));
				}
			}
			replacements.add(iThReplacement);
		}
		return replacements;
	}

	private OWLObject parse(String string) {
		OWLEntityChecker entityChecker = this.patternConstraintSystem
				.getPatternModelFactory().getOPPLParser().getOPPLFactory()
				.getOWLEntityChecker();
		OWLObject toReturn = entityChecker.getOWLClass(string);
		if (toReturn == null) {
			toReturn = entityChecker.getOWLDataProperty(string);
		}
		if (toReturn == null) {
			toReturn = entityChecker.getOWLDataType(string);
		}
		if (toReturn == null) {
			toReturn = entityChecker.getOWLIndividual(string);
		}
		if (toReturn == null) {
			toReturn = entityChecker.getOWLObjectProperty(string);
		}
		return toReturn;
	}

	/**
	 * @param args
	 */
	protected boolean isResolvable(List<String>... args) {
		boolean isResolvable = true;
		for (int i = 0; i < args.length && isResolvable; i++) {
			List<String> iThArgumentAssignments = args[i];
			Iterator<String> it = iThArgumentAssignments.iterator();
			String anIthAssignment;
			while (it.hasNext() && isResolvable) {
				anIthAssignment = it.next();
				Variable variable = this.patternConstraintSystem
						.getVariable(anIthAssignment);
				if (variable != null) {
					isResolvable = false;
				}
			}
		}
		return isResolvable;
	}

	/**
	 * @param extractedPattern
	 * @param args
	 * @throws PatternException
	 * @throws IncompatibleArgumentException
	 * @throws InvalidNumebrOfArgumentException
	 */
	protected void checkCompatibility(List<String>... args)
			throws PatternException, IncompatibleArgumentException,
			InvalidNumebrOfArgumentException {
		boolean compatible = true;
		List<Variable> variables = this.extractedPattern.getInputVariables();
		if (variables.size() == args.length) {
			for (int i = 0; i < args.length; i++) {
				List<String> iThArgAssignements = args[i];
				for (String anIthAssignment : iThArgAssignements) {
					Variable variable = variables.get(i);
					if (this.patternConstraintSystem
							.getVariable(anIthAssignment) != null) {
						Variable argVariable = this.patternConstraintSystem
								.getVariable(anIthAssignment);
						compatible = argVariable.getType().equals(
								variable.getType());
					} else {
						OWLObject arg = this.parse(anIthAssignment);
						if (arg != null) {
							compatible = variable.getType().isCompatibleWith(
									arg);
							// if (arg instanceof OWLEntity) {
							// compatible = variable.getType()
							// .isCompatibleWith(arg);
							// } else {
							// compatible = variable.getType()
							// .isCompatibleWith(arg);
							// }
						} else {
							compatible = false;
							throw new PatternException("Illegal argument "
									+ iThArgAssignements);
						}
					}
					if (!compatible) {
						throw new IncompatibleArgumentException(
								anIthAssignment, variable);
					}
				}
			}
		} else {
			throw new InvalidNumebrOfArgumentException(this.patternName,
					args.length, variables.size());
		}
	}

	/**
	 * @return the resolvable
	 */
	public boolean isResolvable() {
		return this.resolvable;
	}

	/**
	 * @return the resolution
	 * @throws PatternException
	 */
	public String getResolutionString() throws PatternException {
		List<List<Object>> replacements = this
				.computeReplacements(this.arguments);
		return this.extractedPattern
				.getDefinitorialPortionStrings(replacements);
	}

	/**
	 * @return the resolution
	 * @throws PatternException
	 */
	public List<OWLObject> getResolution() throws PatternException {
		List<List<Object>> replacements = this
				.computeReplacements(this.arguments);
		return this.extractedPattern.getDefinitorialPortions(replacements);
	}

	protected boolean hasBeenVisited(URI annotationURI) {
		boolean found = false;
		// TODO performance wise: have visited contain the complete uris or
		// check that the uri starts with the namespace before creating the
		// namespaceutil object
		NamespaceUtil nsUtil = new NamespaceUtil();
		String[] split = nsUtil.split(annotationURI.toString(), null);
		if (split != null && split.length == 2
				&& split[0].equals(PatternModel.NAMESPACE)) {
			found = this.visited.contains(split[1]);
		}
		return found;
	}

	protected Set<OWLConstantAnnotation> getVisitedAnnotations() {
		Set<OWLConstantAnnotation> toReturn = new HashSet<OWLConstantAnnotation>();
		for (String visitedPatternName : this.visited) {
			Iterator<OWLOntology> it = this.ontologyManger.getOntologies()
					.iterator();
			boolean found = false;
			OWLOntology ontology;
			while (!found && it.hasNext()) {
				ontology = it.next();
				Iterator<OWLOntologyAnnotationAxiom> annotationIterator = ontology
						.getOntologyAnnotationAxioms().iterator();
				OWLOntologyAnnotationAxiom anOntologyAnnotationAxiom = null;
				while (!found && annotationIterator.hasNext()) {
					anOntologyAnnotationAxiom = annotationIterator.next();
					found = anOntologyAnnotationAxiom.getAnnotation()
							.getAnnotationURI().equals(
									URI.create(PatternModel.NAMESPACE
											+ visitedPatternName));
					if (found) {
						toReturn
								.add((OWLConstantAnnotation) anOntologyAnnotationAxiom
										.getAnnotation());
					}
				}
			}
		}
		return toReturn;
	}

	public InstantiatedPatternModel getInstantiation() throws PatternException {
		if (this.isResolvable()) {
			InstantiatedPatternModel toReturn = null;
			if (this.extractedPattern instanceof InstantiatedPatternModel) {
				toReturn = (InstantiatedPatternModel) this.extractedPattern;
			} else {
				PatternModel p = (PatternModel) this.extractedPattern;
				toReturn = p.getPatternModelFactory()
						.createInstantiatedPatternModel(p);
			}
			List<List<Object>> replacements = this
					.computeReplacements(this.arguments);
			int i = 0;
			for (Variable variable : toReturn.getInputVariables()) {
				List<Object> variableReplacements = replacements.get(i);
				for (Object object : variableReplacements) {
					if (object instanceof OWLObject) {
						toReturn.instantiate(variable, (OWLObject) object);
					}
				}
				i++;
			}
			return toReturn;
		} else {
			throw new NonResovableArgumentsException(this.patternName,
					this.arguments);
		}
	}

	/**
	 * @return the extractedPattern
	 */
	public PatternOPPLScript getExtractedPattern() {
		return this.extractedPattern;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("$");
		buffer.append(this.patternName);
		buffer.append('(');
		boolean firstArgument = true;
		for (List<String> ithArgList : this.arguments) {
			String comma = firstArgument ? "" : ", ";
			buffer.append(comma);
			firstArgument = false;
			if (ithArgList.size() > 1) {
				buffer.append('{');
				boolean firstSubArg = true;
				for (String string : ithArgList) {
					comma = firstSubArg ? "" : ", ";
					firstSubArg = false;
					buffer.append(comma);
					buffer.append(string);
				}
				buffer.append('}');
			} else if (ithArgList.size() == 1) {
				buffer.append(ithArgList.get(0));
			}
		}
		buffer.append(')');
		return buffer.toString().trim();
	}

	public String getPatternName() {
		return this.patternName;
	}

	/**
	 * @return the arguments
	 */
	public List<String>[] getArguments() {
		return this.arguments;
	}

	/**
	 * @return the patternConstraintSystem
	 */
	public PatternConstraintSystem getConstraintSystem() {
		return this.patternConstraintSystem;
	}

	/**
	 * @return the ontologyManger
	 */
	public OWLOntologyManager getOntologyManger() {
		return this.ontologyManger;
	}
}

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
package org.coode.oppl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.oppl.syntax.OPPLParser;
import org.coode.oppl.variablemansyntax.ConstraintSystem;
import org.semanticweb.owl.model.OWLAxiom;

import uk.ac.manchester.cs.owl.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;

/**
 * @author Luigi Iannone
 * 
 */
public class OPPLQueryImpl implements OPPLQuery {
	private final List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();
	private final List<OWLAxiom> assertedAxioms = new ArrayList<OWLAxiom>();
	private final Set<AbstractConstraint> constraints = new HashSet<AbstractConstraint>();
	private final ConstraintSystem constraintSystem;

	/**
	 * @param constraintSystem
	 */
	public OPPLQueryImpl(ConstraintSystem constraintSystem) {
		this.constraintSystem = constraintSystem;
	}

	/**
	 * 
	 * @see org.coode.oppl.OPPLQuery#addAssertedAxiom(org.semanticweb.owl.model.OWLAxiom)
	 */
	public void addAssertedAxiom(OWLAxiom axiom) {
		this.assertedAxioms.add(axiom);
	}

	/**
	 * @see org.coode.oppl.OPPLQuery#addAxiom(org.semanticweb.owl.model.OWLAxiom)
	 */
	public void addAxiom(OWLAxiom axiom) {
		this.axioms.add(axiom);
	}

	/**
	 * @see org.coode.oppl.OPPLQuery#addConstraint(org.coode.oppl.InequalityConstraint)
	 */
	public void addConstraint(AbstractConstraint constraint) {
		this.constraints.add(constraint);
	}

	/**
	 * @return the axioms
	 */
	public List<OWLAxiom> getAxioms() {
		return this.axioms;
	}

	/**
	 * @return the assertedAxioms
	 */
	public List<OWLAxiom> getAssertedAxioms() {
		return this.assertedAxioms;
	}

	public List<AbstractConstraint> getConstraints() {
		return new ArrayList<AbstractConstraint>(this.constraints);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("SELECT ");
		boolean first = true;
		for (OWLAxiom axiom : this.getAssertedAxioms()) {
			String commaString = first ? "ASSERTED " : "ASSERTED, ";
			StringWriter writer = new StringWriter();
			ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(
					writer);
			renderer.setShortFormProvider(new SimpleVariableShortFormProvider(
					this.constraintSystem));
			first = false;
			buffer.append(commaString);
			axiom.accept(renderer);
			buffer.append(writer.toString());
		}
		for (OWLAxiom axiom : this.getAxioms()) {
			String commaString = first ? "" : ", ";
			StringWriter writer = new StringWriter();
			ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(
					writer);
			renderer.setShortFormProvider(new SimpleVariableShortFormProvider(
					this.constraintSystem));
			first = false;
			buffer.append(commaString);
			axiom.accept(renderer);
			buffer.append(writer.toString());
		}
		if (this.getConstraints().size() > 0) {
			buffer.append(" WHERE ");
			first = true;
			for (AbstractConstraint c : this.getConstraints()) {
				String commaString = first ? "" : ", ";
				buffer.append(commaString);
				buffer.append(c.toString());
			}
		}
		return buffer.toString();
	}

	public String render() {
		StringBuffer buffer = new StringBuffer("SELECT ");
		boolean first = true;
		for (OWLAxiom axiom : this.getAssertedAxioms()) {
			String commaString = first ? "ASSERTED " : "ASSERTED, ";
			// StringWriter writer = new StringWriter();
			// ManchesterOWLSyntaxObjectRenderer renderer = new
			// ManchesterOWLSyntaxObjectRenderer(
			// writer);
			ManchesterSyntaxRenderer renderer = OPPLParser.getOPPLFactory()
					.getManchesterSyntaxRenderer(this.constraintSystem);
			// renderer.setShortFormProvider(new
			// SimpleVariableShortFormProvider(
			// this.getConstraintSystem()));
			buffer.append(commaString);
			if (!first) {
				buffer.append("\n");
			}
			first = false;
			axiom.accept(renderer);
			buffer.append(renderer.toString());
			buffer.append("\n");
		}
		for (OWLAxiom axiom : this.getAxioms()) {
			String commaString = first ? "" : ", ";
			// StringWriter writer = new StringWriter();
			// ManchesterOWLSyntaxObjectRenderer renderer = OPPLParser
			// .getOPPLFactory().getOWLObjectRenderer(writer);
			ManchesterSyntaxRenderer renderer = OPPLParser.getOPPLFactory()
					.getManchesterSyntaxRenderer(this.constraintSystem);
			buffer.append(commaString);
			if (!first) {
				buffer.append("\n");
			}
			first = false;
			axiom.accept(renderer);
			buffer.append(renderer.toString());
		}
		if (this.getConstraints().size() > 0) {
			buffer.append("\nWHERE ");
			first = true;
			for (AbstractConstraint c : this.getConstraints()) {
				String commaString = first ? "" : ", ";
				buffer.append(commaString);
				buffer.append(c.render());
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

	/**
	 * @return the constraintSystem
	 */
	public ConstraintSystem getConstraintSystem() {
		return this.constraintSystem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (this.assertedAxioms == null ? 0 : this.assertedAxioms
						.hashCode());
		result = prime * result
				+ (this.axioms == null ? 0 : this.axioms.hashCode());
		result = prime * result
				+ (this.constraints == null ? 0 : this.constraints.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		OPPLQueryImpl other = (OPPLQueryImpl) obj;
		if (this.assertedAxioms == null) {
			if (other.assertedAxioms != null) {
				return false;
			}
		} else if (!this.assertedAxioms.equals(other.assertedAxioms)) {
			return false;
		}
		if (this.axioms == null) {
			if (other.axioms != null) {
				return false;
			}
		} else if (!this.axioms.equals(other.axioms)) {
			return false;
		}
		if (this.constraints == null) {
			if (other.constraints != null) {
				return false;
			}
		} else if (!this.constraints.equals(other.constraints)) {
			return false;
		}
		return true;
	}
}
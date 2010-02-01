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
import java.util.List;

import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.oppl.utils.ParserFactory;
import org.coode.oppl.variablemansyntax.ConstraintSystem;
import org.coode.oppl.variablemansyntax.Variable;
import org.coode.oppl.variablemansyntax.VariableScope;
import org.coode.oppl.visitors.InputVariableCollector;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;

import uk.ac.manchester.cs.owl.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;

/**
 * @author Luigi Iannone
 * 
 */
public class OPPLScriptImpl implements OPPLScript {
	private final ConstraintSystem constraintSystem;
	private final List<Variable> variables = new ArrayList<Variable>();
	private final OPPLQuery query;
	private final List<OWLAxiomChange> actions;

	/**
	 * @param constraintSystem
	 * @param query
	 * @param actions
	 */
	public OPPLScriptImpl(ConstraintSystem constraintSystem,
			List<Variable> variables, OPPLQuery query,
			List<OWLAxiomChange> actions) {
		this.constraintSystem = constraintSystem;
		this.variables.addAll(variables);
		this.query = query;
		this.actions = actions;
	}

	/**
	 * @see org.coode.oppl.OPPLScript#getConstraintSystem()
	 */
	public ConstraintSystem getConstraintSystem() {
		return this.constraintSystem;
	}

	/**
	 * @see org.coode.oppl.OPPLScript#getInputVariables()
	 */
	public List<Variable> getInputVariables() {
		InputVariableCollector visitor = new InputVariableCollector(
				new ArrayList<Variable>());
		for (Variable variable : this.getVariables()) {
			variable.accept(visitor);
		}
		return visitor.getCollectedVariables();
	}

	/**
	 * @see org.coode.oppl.OPPLScript#getVariables()
	 */
	public List<Variable> getVariables() {
		return new ArrayList<Variable>(this.variables);
	}

	/**
	 * @return the query
	 */
	public OPPLQuery getQuery() {
		return this.query;
	}

	/**
	 * @return the actions
	 */
	public List<OWLAxiomChange> getActions() {
		return this.actions;
	}

	public void accept(OPPLScriptVisitor visitor) {
		for (Variable v : this.getVariables()) {
			visitor.visit(v);
		}
		visitor.visit(this.getQuery());
		visitor.visitActions(this.getActions());
	}

	public <P> P accept(OPPLScriptVisitorEx<P> visitor) {
		P p = null;
		for (Variable v : this.getVariables()) {
			p = visitor.visit(v, p);
		}
		p = visitor.visit(this.getQuery(), p);
		p = visitor.visitActions(this.getActions(), p);
		return p;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Variable v : this.getVariables()) {
			String commaString = first ? "" : ", ";
			first = false;
			buffer.append(commaString);
			buffer.append(v.toString());
			VariableScope variableScope = v.getVariableScope();
			if (variableScope != null) {
				buffer.append('[');
				buffer.append(variableScope.getDirection().toString());
				StringWriter writer = new StringWriter();
				buffer.append(' ');
				ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(
						writer);
				renderer
						.setShortFormProvider(new SimpleVariableShortFormProvider(
								this.constraintSystem));
				variableScope.getScopingObject().accept(renderer);
				buffer.append(writer.toString());
				buffer.append(']');
			}
		}
		OPPLQuery opplQuery = this.getQuery();
		if (this.query != null) {
			buffer.append(opplQuery.toString());
		}
		if (this.getActions().size() > 0) {
			buffer.append(" BEGIN ");
			first = true;
			for (OWLAxiomChange action : this.getActions()) {
				String commaString = first ? "" : ", ";
				StringWriter writer = new StringWriter();
				ManchesterOWLSyntaxObjectRenderer renderer = new ManchesterOWLSyntaxObjectRenderer(
						writer);
				renderer
						.setShortFormProvider(new SimpleVariableShortFormProvider(
								this.constraintSystem));
				String actionString = action instanceof AddAxiom ? "ADD "
						: "REMOVE ";
				first = false;
				buffer.append(commaString);
				buffer.append(actionString);
				action.getAxiom().accept(renderer);
				buffer.append(writer.toString());
			}
			buffer.append(" END;");
		}
		return buffer.toString();
	}

	public String render() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Variable v : this.getVariables()) {
			String commaString = first ? "" : ",\n ";
			buffer.append(commaString);
			first = false;
			buffer.append(v.toString());
			VariableScope variableScope = v.getVariableScope();
			if (variableScope != null) {
				buffer.append('[');
				buffer.append(variableScope.getDirection().toString());
				buffer.append(' ');
				ManchesterSyntaxRenderer renderer = ParserFactory.getInstance()
						.getOPPLFactory().getManchesterSyntaxRenderer(
								this.constraintSystem);
				variableScope.getScopingObject().accept(renderer);
				buffer.append(renderer.toString());
				buffer.append(']');
			}
		}
		OPPLQuery opplQuery = this.getQuery();
		buffer.append('\n');
		if (this.query != null) {
			buffer.append(opplQuery.render());
		}
		if (this.getActions().size() > 0) {
			buffer.append("BEGIN\n ");
			first = true;
			for (OWLAxiomChange action : this.getActions()) {
				String commaString = first ? "" : ",\n ";
				String actionString = action instanceof AddAxiom ? "\tADD "
						: "\tREMOVE ";
				ManchesterSyntaxRenderer renderer = ParserFactory.getInstance()
						.getOPPLFactory().getManchesterSyntaxRenderer(
								this.constraintSystem);
				buffer.append(commaString);
				first = false;
				buffer.append(actionString);
				action.getAxiom().accept(renderer);
				buffer.append(renderer.toString());
			}
			buffer.append("\nEND;");
		}
		return buffer.toString();
	}

	public void addVariable(Variable variable) {
		this.variables.add(variable);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (this.actions == null ? 0 : this.actions.hashCode());
		result = prime * result
				+ (this.query == null ? 0 : this.query.hashCode());
		result = prime * result
				+ (this.variables == null ? 0 : this.variables.hashCode());
		return result;
	}

	/**
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
		OPPLScriptImpl other = (OPPLScriptImpl) obj;
		if (this.actions == null) {
			if (other.actions != null) {
				return false;
			}
		} else if (!this.actions.equals(other.actions)) {
			return false;
		}
		if (this.query == null) {
			if (other.query != null) {
				return false;
			}
		} else if (!this.query.equals(other.query)) {
			return false;
		}
		if (this.variables == null) {
			if (other.variables != null) {
				return false;
			}
		} else if (!this.variables.equals(other.variables)) {
			return false;
		}
		return true;
	}
}

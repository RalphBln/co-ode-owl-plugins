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
package org.coode.oppl.variablemansyntax.generated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.variablemansyntax.Variable;
import org.coode.oppl.variablemansyntax.bindingtree.BindingNode;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObject;

/**
 * @author Luigi Iannone
 * 
 */
public class VariableIndexGeneratedValue implements
		SingleValueGeneratedValue<String> {
	private final RegExpGenerated variable;
	private final RegExpGeneratedValue value;
	private final int index;
	private Collection<OWLEntity> entities;

	/**
	 * @param variable
	 * @param attribute
	 */
	public VariableIndexGeneratedValue(RegExpGenerated variable, int i,
			Collection<OWLObject> e) {
		this.variable = variable;
		this.value = variable.getValue();
		this.index = i;
		this.entities = new HashSet<OWLEntity>();
		for (OWLObject o : e) {
			if (o instanceof OWLEntity) {
				this.entities.add((OWLEntity) o);
			}
		}
	}

	@Override
	public String toString() {
		return this.variable.getName() + ".GROUPS(" + this.index + ")";
	}

	/**
	 * @return the attribute
	 */
	public final int getIndex() {
		return this.index;
	}

	/**
	 * @return the variable
	 */
	public final Variable getVariable() {
		return this.variable;
	}

	public List<String> computePossibleValues() {
		Set<String> set = new HashSet<String>();
		for (OWLEntity entity : this.entities) {
			set.addAll(entity.accept(this.value));
		}
		return new ArrayList<String>(set);
	}

	public String getGeneratedValue(BindingNode node) {
		if (!this.value.isMatched()) {
			this.value.getGeneratedValue(node);
		}
		OWLObject o = node.getAssignmentValue(this.variable);
		if (o == null || !(o instanceof OWLEntity)) {
			return null;
		}
		if (this.entities.contains(o)) {
			List<String> l = ((OWLEntity) o).accept(this.value);
			if (l.size() > this.index) {
				return l.get(this.index);
			}
		}
		return null;
	}

	public void accept(SingleValueGeneratedValueVisitor visitor) {
		visitor.vistVariableIndexGeneratedValue(this);
	}

	public <O> O accept(SingleValueGeneratedValueVisitorEx<O> visitor) {
		return visitor.vistVariableIndexGeneratedValue(this);
	}
}

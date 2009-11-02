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
package org.coode.oppl.protege.ui;

import org.protege.editor.core.ui.list.MListItem;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;

/**
 * @author Luigi Iannone
 * 
 */
public class ActionListItem implements MListItem {
	private final OWLAxiomChange axiomChange;
	private final boolean isDeleteable;
	private final boolean isEditable;

	/**
	 * @param axiomChange
	 */
	public ActionListItem(OWLAxiomChange axiomChange, boolean isEditable,
			boolean isDeleteable) {
		this.axiomChange = axiomChange;
		this.isDeleteable = isDeleteable;
		this.isEditable = isEditable;
	}

	public String getTooltip() {
		String addOrRemove = this.axiomChange instanceof AddAxiom ? "ADD "
				: "REMOVE ";
		String toFrom = this.axiomChange instanceof AddAxiom ? " to "
				: " from ";
		return addOrRemove + " the axiom" + toFrom
				+ this.axiomChange.getOntology().getURI().toString();
	}

	/**
	 * @see org.protege.editor.core.ui.list.MListItem#handleDelete()
	 */
	public boolean handleDelete() {
		return true;
	}

	/**
	 * @see org.protege.editor.core.ui.list.MListItem#handleEdit()
	 */
	public void handleEdit() {
	}

	/**
	 * @see org.protege.editor.core.ui.list.MListItem#isDeleteable()
	 */
	public boolean isDeleteable() {
		return this.isDeleteable;
	}

	/**
	 * @see org.protege.editor.core.ui.list.MListItem#isEditable()
	 */
	public boolean isEditable() {
		return this.isEditable;
	}

	/**
	 * @return the axiomChange
	 */
	public OWLAxiomChange getAxiomChange() {
		return this.axiomChange;
	}
}

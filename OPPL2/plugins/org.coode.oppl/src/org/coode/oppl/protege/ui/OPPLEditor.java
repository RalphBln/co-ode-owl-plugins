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

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.coode.oppl.OPPLScript;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Tabbed pane containing two editors for OPPL, namely the text based
 * OPPLTextEditor and OPPLBuilder.
 * 
 * @author Luigi Iannone
 * 
 */
public class OPPLEditor extends JTabbedPane implements VerifiedInputEditor,
		ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3481576138019196471L;
	private final Set<InputVerificationStatusChangedListener> listeners = new HashSet<InputVerificationStatusChangedListener>();
	private final OWLEditorKit owlEditorKit;
	private final OPPLBuilder opplBuilder;
	private final OPPLTextEditor opplTextEditor;
	private OPPLScript opplScript;

	/**
	 * @return the opplScript
	 */
	public OPPLScript getOPPLScript() {
		return this.opplScript;
	}

	/**
	 * @param opplScript
	 *            the opplScript to set
	 */
	public void setOPPLScript(OPPLScript opplScript) {
		this.opplBuilder.setOPPLScript(opplScript);
		this.fireStateChanged();
	}

	/**
	 * @return the owlEditorKit
	 */
	public OWLEditorKit getOwlEditorKit() {
		return this.owlEditorKit;
	}

	public OPPLEditor(OWLEditorKit owlEditor) {
		this.owlEditorKit = owlEditor;
		this.opplBuilder = new OPPLBuilder(this.getOwlEditorKit());
		this.opplTextEditor = new OPPLTextEditor(this.getOwlEditorKit());
		this.opplBuilder
				.addStatusChangedListener(new InputVerificationStatusChangedListener() {
					public void verifiedStatusChanged(boolean newState) {
						OPPLEditor.this.opplScript = null;
						if (newState) {
							OPPLEditor.this.opplScript = OPPLEditor.this.opplBuilder
									.getOPPLScript();
						}
						OPPLEditor.this.handleChange();
					}
				});
		this.opplTextEditor
				.addStatusChangedListener(new InputVerificationStatusChangedListener() {
					public void verifiedStatusChanged(boolean newState) {
						OPPLEditor.this.opplScript = null;
						if (newState) {
							OPPLEditor.this.opplScript = OPPLEditor.this.opplTextEditor
									.getOPPLScript();
						}
						OPPLEditor.this.handleChange();
					}
				});
		this.addChangeListener(this);
		this.initGUI();
	}

	private void initGUI() {
		this.add(this.opplBuilder);
		this.add(this.opplTextEditor);
	}

	protected void handleChange() {
		boolean newStatus = this.getOPPLScript() != null;
		this.notifyListeners(newStatus);
	}

	/**
	 * @throws NullPointerException
	 *             if the input is {@code null}.
	 * @see org.protege.editor.core.ui.util.VerifiedInputEditor#addStatusChangedListener
	 *      (org.protege.editor.core.ui.util.InputVerificationStatusChangedListener)
	 */
	public void addStatusChangedListener(
			InputVerificationStatusChangedListener listener) {
		if (listener == null) {
			throw new NullPointerException("The listener canno be null");
		}
		this.listeners.add(listener);
	}

	/**
	 * @see org.protege.editor.core.ui.util.VerifiedInputEditor#
	 *      removeStatusChangedListener
	 *      (org.protege.editor.core.ui.util.InputVerificationStatusChangedListener)
	 */
	public void removeStatusChangedListener(
			InputVerificationStatusChangedListener listener) {
		this.listeners.remove(listener);
	}

	private void notifyListeners(boolean newStatus) {
		for (InputVerificationStatusChangedListener l : this.listeners) {
			l.verifiedStatusChanged(newStatus);
		}
	}

	/**
	 * Clears both internal editors.
	 */
	public void clear() {
		this.opplBuilder.clear();
		this.opplTextEditor.clear();
	}

	public void stateChanged(ChangeEvent changeEvent) {
		Component selectedComponent = this.getSelectedComponent();
		if (selectedComponent.equals(this.opplBuilder)
				&& this.opplScript != null) {
			this.opplBuilder.setOPPLScript(this.opplScript);
		} else if (selectedComponent.equals(this.opplTextEditor)
				&& this.opplScript != null) {
			this.opplTextEditor.setOPPLScript(this.opplScript);
		}
	}
}

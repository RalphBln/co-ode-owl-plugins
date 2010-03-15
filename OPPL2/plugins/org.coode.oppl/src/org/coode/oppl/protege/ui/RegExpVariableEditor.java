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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.coode.oppl.exceptions.InvalidVariableNameException;
import org.coode.oppl.syntax.ParseException;
import org.coode.oppl.syntax.TokenMgrError;
import org.coode.oppl.utils.ParserFactory;
import org.coode.oppl.utils.ProtegeParserFactory;
import org.coode.oppl.variablemansyntax.ConstraintSystem;
import org.coode.oppl.variablemansyntax.Variable;
import org.coode.oppl.variablemansyntax.VariableType;
import org.coode.oppl.variablemansyntax.generated.RegExpGenerated;
import org.coode.oppl.variablemansyntax.generated.SingleValueGeneratedVariable;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.description.OWLExpressionParserException;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionChecker;
import org.semanticweb.owl.model.OWLException;

/**
 * @author Luigi Iannone
 * 
 */
public class RegExpVariableEditor extends AbstractVariableEditor {
	private final class VariableOWLExpressionChecker implements
			OWLExpressionChecker<RegExpGenerated> {
		public VariableOWLExpressionChecker() {
		}

		private RegExpGenerated lastEditedObject = null;

		public void check(String text) throws OWLExpressionParserException {
			this.lastEditedObject = null;
			ProtegeParserFactory.initParser(text,
					RegExpVariableEditor.this.owlEditorKit.getModelManager(),
					null);
			try {
				String variableName = RegExpVariableEditor.this.variableNameExpressionEditor
						.createObject();
				Object selectedValue = RegExpVariableEditor.this.jRadioButtonTypeMap
						.get(RegExpVariableEditor.this.findSelectedButton());
				if (selectedValue instanceof VariableType) {
					RegExpGenerated variableDefinition = (RegExpGenerated) ParserFactory
							.getInstance().opplFunction(variableName,
									(VariableType) selectedValue,
									RegExpVariableEditor.this.constraintSystem);
					this.lastEditedObject = variableDefinition;
				} else {
					this.lastEditedObject = null;
					throw new OWLExpressionParserException(new Exception(
							"Undefined variable type"));
				}
			} catch (ParseException e) {
				this.lastEditedObject = null;
				throw new OWLExpressionParserException(e);
			} catch (TokenMgrError e) {
				this.lastEditedObject = null;
				throw new OWLExpressionParserException(e);
			} catch (OWLException e) {
				this.lastEditedObject = null;
				throw new OWLExpressionParserException(e);
			}
		}

		public RegExpGenerated createObject(String text)
				throws OWLExpressionParserException {
			this.check(text);
			return this.lastEditedObject;
		}
	}

	private class ChangeTypeActionListener implements ActionListener {
		public ChangeTypeActionListener() {
		}

		@SuppressWarnings("unused")
		public void actionPerformed(ActionEvent e) {
			RegExpVariableEditor.this.handleChange();
		}
	}

	private static final long serialVersionUID = 8899160597858126563L;
	protected final OWLEditorKit owlEditorKit;
	protected final ConstraintSystem constraintSystem;
	protected final Map<JRadioButton, VariableType> jRadioButtonTypeMap = new HashMap<JRadioButton, VariableType>();
	private final Map<VariableType, JRadioButton> typeJRadioButonMap = new HashMap<VariableType, JRadioButton>();
	private final ExpressionEditor<RegExpGenerated> opplFunctionEditor;

	public RegExpVariableEditor(OWLEditorKit owlEditorKit,
			ConstraintSystem constraintSystem) {
		this.setLayout(new BorderLayout());
		this.owlEditorKit = owlEditorKit;
		this.constraintSystem = constraintSystem;
		this.variableNameExpressionEditor = new ExpressionEditor<String>(
				owlEditorKit, new OWLExpressionChecker<String>() {
					private String variableName;

					public void check(String text)
							throws OWLExpressionParserException {
						this.variableName = null;
						if (text.matches("(\\?)?(\\w)+")) {
							this.variableName = text.startsWith("?") ? text
									: "?" + text;
						} else {
							throw new OWLExpressionParserException(
									new InvalidVariableNameException(text));
						}
					}

					public String createObject(String text)
							throws OWLExpressionParserException {
						this.check(text);
						return this.variableName;
					}
				});
		JPanel variableNamePanel = new JPanel(new BorderLayout());
		variableNamePanel.setBorder(ComponentFactory
				.createTitledBorder("Variable name:"));
		this.variableNameExpressionEditor
				.addStatusChangedListener(new InputVerificationStatusChangedListener() {
					public void verifiedStatusChanged(boolean newState) {
						if (newState) {
							RegExpVariableEditor.this.handleChange();
						}
					}
				});
		variableNamePanel.add(this.variableNameExpressionEditor);
		this.add(variableNamePanel, BorderLayout.NORTH);
		this.variableTypeButtonGroup = new ButtonGroup();
		JPanel variableTypePanel = new JPanel(new GridLayout(0, VariableType
				.values().length));
		for (VariableType variableType : VariableType.values()) {
			JRadioButton typeRadioButton = new JRadioButton(variableType.name());
			typeRadioButton.addActionListener(new ChangeTypeActionListener());
			this.variableTypeButtonGroup.add(typeRadioButton);
			variableTypePanel.add(typeRadioButton);
			this.jRadioButtonTypeMap.put(typeRadioButton, variableType);
			this.typeJRadioButonMap.put(variableType, typeRadioButton);
		}
		this.typeJRadioButonMap.get(VariableType.values()[0]).setSelected(true);
		JPanel scopeBorderPanel = new JPanel(new BorderLayout());
		scopeBorderPanel.setBorder(ComponentFactory
				.createTitledBorder("Variable Scope"));
		JPanel variableTypeAndScopePanel = new JPanel(new BorderLayout());
		variableTypeAndScopePanel.add(scopeBorderPanel, BorderLayout.NORTH);
		variableTypeAndScopePanel.add(variableTypePanel, BorderLayout.CENTER);
		variableTypeAndScopePanel.setBorder(ComponentFactory
				.createTitledBorder("Variable Type"));
		this.add(variableTypeAndScopePanel, BorderLayout.CENTER);
		this.opplFunctionEditor = new ExpressionEditor<RegExpGenerated>(
				this.owlEditorKit, new VariableOWLExpressionChecker());
		this.opplFunctionEditor
				.addStatusChangedListener(new InputVerificationStatusChangedListener() {
					@SuppressWarnings("unused")
					public void verifiedStatusChanged(boolean newState) {
						RegExpVariableEditor.this.handleChange();
					}
				});
		JPanel opplFunctionEditorPanel = new JPanel(new BorderLayout());
		opplFunctionEditorPanel.setBorder(ComponentFactory
				.createTitledBorder("OPPL Function: "));
		opplFunctionEditorPanel.add(ComponentFactory
				.createScrollPane(this.opplFunctionEditor));
		this.add(opplFunctionEditorPanel, BorderLayout.SOUTH);
	}

	protected void handleChange() {
		if (this.check()) {
			try {
				this.variableNameExpressionEditor.createObject();
				if (this.variable != null) {
					this.constraintSystem.removeVariable(this.variable);
				}
				this.variable = this.opplFunctionEditor.createObject();
				this.constraintSystem.importVariable(this.variable);
				this.notifyListeners();
			} catch (OWLExpressionParserException e) {
				this.notifyListeners();
				throw new RuntimeException(e);
			} catch (OWLException e) {
				this.notifyListeners();
				throw new RuntimeException(e);
			}
		} else {
			this.notifyListeners();
		}
	}

	@Override
	protected boolean check() {
		try {
			this.variableNameExpressionEditor.createObject();
			this.opplFunctionEditor.createObject();
			return true;
		} catch (OWLExpressionParserException e) {
			return false;
		} catch (OWLException e) {
			return false;
		}
	}

	@Override
	public void setVariable(RegExpGenerated v) {
		this.clear();
		this.variableNameExpressionEditor.setText(v.getName());
		this.typeJRadioButonMap.get(v.getType()).setSelected(true);
		this.opplFunctionEditor.setText(v.getOPPLFunction());
	}

	@Override
	public void setVariable(Variable variable) {
		if (variable instanceof RegExpGenerated) {
			this.setVariable((RegExpGenerated) variable);
		}
		throw new RuntimeException(
				"Regular InputVariables not allowed on a RegExpVariableEditor!");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setVariable(SingleValueGeneratedVariable<?> variable) {
		throw new RuntimeException(
				"Regular GeneratedVariables not allowed on a RegExpVariableEditor!");
	}
}

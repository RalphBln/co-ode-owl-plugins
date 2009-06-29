package org.coode.patterns.ui;

import org.coode.patterns.PatternModel;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.OWLEquivalentClassesAxiomFrameSectionRow;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLOntology;

public class PatternOWLEquivalentClassesAxiomFrameSectionRow extends
		OWLEquivalentClassesAxiomFrameSectionRow {
	private final PatternModel generatingPatternModel;

	/**
	 * @param owlEditorKit
	 * @param section
	 * @param ontology
	 * @param rootObject
	 * @param axiom
	 */
	public PatternOWLEquivalentClassesAxiomFrameSectionRow(
			OWLEditorKit owlEditorKit,
			PatternOWLEquivalentClassesAxiomFrameSection section,
			OWLOntology ontology, OWLClass rootObject,
			OWLEquivalentClassesAxiom axiom, PatternModel generatingPatternModel) {
		super(owlEditorKit, section, ontology, rootObject, axiom);
		this.generatingPatternModel = generatingPatternModel;
	}

	@Override
	public boolean isDeleteable() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	/**
	 * @return the generatingPatternModel
	 */
	public final PatternModel getGeneratingPatternModel() {
		return this.generatingPatternModel;
	}
}

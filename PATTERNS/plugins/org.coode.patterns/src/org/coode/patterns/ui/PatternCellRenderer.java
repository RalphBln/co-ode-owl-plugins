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
package org.coode.patterns.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.coode.oppl.utils.ArgCheck;
import org.coode.patterns.AbstractPatternModelFactory;
import org.coode.patterns.PatternExtractor;
import org.coode.patterns.PatternModel;
import org.coode.patterns.PatternOPPLScript;
import org.coode.patterns.utils.Utils;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAnnotationAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLSubClassAxiom;

/**
 * @author Luigi Iannone
 * 
 *         Jun 19, 2008
 */
public class PatternCellRenderer implements ListCellRenderer {
	private OWLEditorKit owlEditorKit;
	private final OWLCellRenderer owlCellRenderer;
	private AbstractPatternModelFactory factory;

	public PatternCellRenderer(OWLEditorKit owlEditorKit,
			AbstractPatternModelFactory f) {
		ArgCheck.checkNullArgument("The OWL Editor Kit", owlEditorKit);
		this.owlEditorKit = owlEditorKit;
		this.factory = f;
		this.owlCellRenderer = new OWLCellRenderer(this.owlEditorKit);
		this.owlCellRenderer.setHighlightKeywords(true);
		this.owlCellRenderer.setWrap(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JPanel toReturn = new JPanel(new FlowLayout());
		PatternExtractor patternExtractor = this.factory.getPatternExtractor();
		if (value instanceof PatternClassFrameSectionRow) {
			JLabel valueRendering = new JLabel();
			OWLAnnotationAxiom<? extends OWLObject> annotationAxAnnotation = ((PatternClassFrameSectionRow) value)
					.getAxiom();
			OWLAnnotation<?> annotation = annotationAxAnnotation
					.getAnnotation();
			PatternOPPLScript patternModel = annotation
					.accept(patternExtractor);
			if (patternModel != null) {
				valueRendering.setText(patternModel.getRendering());
			} else {
				valueRendering.setText(value.toString());
			}
			toReturn.add(valueRendering);
		} else if (value instanceof PatternOntologyFrameSectionRow) {
			PatternOntologyFrameSectionRow row = (PatternOntologyFrameSectionRow) value;
			OWLAnnotationAxiom<? extends OWLObject> annotationAxAnnotation = row
					.getAxiom();
			OWLAnnotation<?> annotation = annotationAxAnnotation
					.getAnnotation();
			PatternModel patternModel = (PatternModel) annotation
					.accept(patternExtractor);
			if (patternModel != null) {
				toReturn.add(new JLabel(patternModel.getRendering()));
			} else {
				toReturn.add(new JLabel(value.toString()));
			}
		} else if (value instanceof PatternOWLEquivalentClassesAxiomFrameSectionRow) {
			OWLEquivalentClassesAxiom annotationAxiom = ((PatternOWLEquivalentClassesAxiomFrameSectionRow) value)
					.getAxiom();
			Set<OWLAxiomAnnotationAxiom> annotationAxioms = annotationAxiom
					.getAnnotationAxioms(this.owlEditorKit.getModelManager()
							.getActiveOntology());
			if (Utils.isPatternGenerated(annotationAxioms)) {
				Set<OWLDescription> descriptions = new HashSet<OWLDescription>(
						annotationAxiom.getDescriptions());
				descriptions
						.remove(((PatternOWLEquivalentClassesAxiomFrameSectionRow) value)
								.getRoot());
				return this.owlCellRenderer.getListCellRendererComponent(list,
						descriptions.iterator().next(), index, isSelected,
						cellHasFocus);
			}
		} else if (value instanceof PatternOWLSubClassAxiomFrameSectionRow) {
			OWLSubClassAxiom annotationAxiom = ((PatternOWLSubClassAxiomFrameSectionRow) value)
					.getAxiom();
			Set<OWLAxiomAnnotationAxiom> annotationAxioms = annotationAxiom
					.getAnnotationAxioms(this.owlEditorKit.getModelManager()
							.getActiveOntology());
			if (Utils.isPatternGenerated(annotationAxioms)) {
				return this.owlCellRenderer.getListCellRendererComponent(list,
						annotationAxiom.getSuperClass(), index, isSelected,
						cellHasFocus);
			}
		}
		return toReturn;
	}
}

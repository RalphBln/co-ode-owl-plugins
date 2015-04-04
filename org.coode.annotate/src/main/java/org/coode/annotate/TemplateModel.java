package org.coode.annotate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.coode.annotate.prefs.AnnotationTemplateDescriptor;
import org.coode.annotate.prefs.AnnotationTemplatePrefs;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;

/*
* Copyright (C) 2007, University of Manchester
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

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 16, 2007<br><br>
 */
public class TemplateModel {

    private final java.util.List<TemplateRow> cList = new ArrayList<>();

    private final AnnotationComponentComparator comparator = new AnnotationComponentComparator();

    private final Set<TemplateModelListener> listeners = new HashSet<>();

    private final OWLModelManager mngr;

    private OWLAnnotationSubject subject;

    private OWLOntologyChangeListener ontChangeListener = new OWLOntologyChangeListener(){
        @Override
        public void ontologiesChanged(List<? extends OWLOntologyChange> list) {
            handleOntologyChanges(list);
        }
    };

    private ChangeListener layoutChangeListener = new ChangeListener(){
        @Override
        public void stateChanged(ChangeEvent event) {
            refresh();
        }
    };


    public TemplateModel(OWLModelManager mngr) {
        this.mngr = mngr;

        mngr.addOntologyChangeListener(ontChangeListener);

        AnnotationTemplatePrefs.getInstance().addChangeListener(layoutChangeListener);
    }


    public EditorType getComponentType(OWLAnnotationProperty property){
        return getDefaultDescriptor().getEditor(property);
    }


    public Set<OWLAnnotationAssertionAxiom> getAnnotations(OWLAnnotationSubject annotationSubject) {
        Set<OWLAnnotationAssertionAxiom> annotations = new HashSet<>();
        for (OWLOntology ont : mngr.getActiveOntologies()){
            annotations.addAll(ont.getAnnotationAssertionAxioms(annotationSubject));
        }
        return annotations;
    }


    public Set<OWLOntology> getOntologiesContainingAnnotation(TemplateRow templateRow) {
        Set<OWLOntology> onts = new HashSet<>();
        OWLAxiom ax = templateRow.getAxiom();
        if (ax != null) {
        	for (OWLOntology ont : mngr.getActiveOntologies()){
        		if (ont.containsAxiom(ax)){
        			onts.add(ont);
        		}
        	}
        }
        return onts;
    }

    public OWLModelManager getOWLModelManager() {
        return mngr;
    }

    public OWLAnnotationSubject getSubject() {
        return subject;
    }

    protected void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        for (OWLOntologyChange change : changes){
            if (change.isAxiomChange() &&
                change.getAxiom().isOfType(AxiomType.ANNOTATION_ASSERTION) &&
                ((OWLAnnotationAssertionAxiom)change.getAxiom()).getSubject().equals(subject)){
                refresh();
                return;
            }
        }
    }

    protected void refresh(){
        setSubject(subject);
    }

    public void setSubject(OWLAnnotationSubject annotationSubject) {
        subject = annotationSubject;
        cList.clear();

        if (annotationSubject != null){
            final List<OWLAnnotationProperty> properties = getDefaultDescriptor().getProperties();

            Set<OWLAnnotationAssertionAxiom> annots = getAnnotations(annotationSubject);
            Set<OWLAnnotationProperty> usedProperties = new HashSet<>();
            for (OWLAnnotationAssertionAxiom annot : annots){
                final OWLAnnotationProperty property = annot.getAnnotation().getProperty();
                if (properties.contains(property)){
                    usedProperties.add(property);
                    cList.add(new TemplateRow(annot, this));
                }
            }

            for (OWLAnnotationProperty property : properties){
                if (!usedProperties.contains(property)){
                    cList.add(new TemplateRow(annotationSubject, property, this));
                }
            }

            Collections.sort(cList, comparator);
        }

        notifyStructureChanged();
    }

    public List<TemplateRow> getRows() {
        return Collections.unmodifiableList(cList);
    }

    public void addRow(OWLAnnotationProperty property) {
        cList.add(new TemplateRow(subject, property, this));
        Collections.sort(cList, comparator);
        notifyStructureChanged();
    }

    public void removeRow(TemplateRow c) {
        c.setValue(null);
        cList.remove(c);
        notifyStructureChanged();
    }

    private void notifyStructureChanged() {
        for (TemplateModelListener l : listeners){
            l.modelStructureChanged();
        }
    }

    public void addModelListener(TemplateModelListener l){
        listeners.add(l);
    }

    public void removeModelListener(TemplateModelListener l){
        listeners.remove(l);
    }

    /**
     * Called by the rows when they make changes
     * (so that the table can manage itself without having to refresh completely)
     * This implementation disables the ontology change listeners temporarily
     *
     * @param changes the set of changes to apply
     */
    public void requestApplyChanges(List<OWLOntologyChange> changes) {
        if (!changes.isEmpty()){
            mngr.removeOntologyChangeListener(ontChangeListener);
            mngr.applyChanges(changes);
            mngr.addOntologyChangeListener(ontChangeListener);
        }
    }


    public void dispose() {
        mngr.removeOntologyChangeListener(ontChangeListener);
        AnnotationTemplatePrefs.getInstance().removeChangeListener(layoutChangeListener);        
        listeners.clear();
    }


    protected AnnotationTemplateDescriptor getDefaultDescriptor() {
        return AnnotationTemplatePrefs.getInstance().getDefaultDescriptor(mngr.getOWLDataFactory());
    }


    class AnnotationComponentComparator implements Comparator<TemplateRow> {

        @Override
        public int compare(TemplateRow c1, TemplateRow c2) {
            OWLAnnotationProperty uri1 = c1.getProperty();
            OWLAnnotationProperty uri2 = c2.getProperty();
            for (OWLAnnotationProperty uri : getDefaultDescriptor().getProperties()){
                if (uri.equals(uri1)){
                    return -1;
                }
                if (uri.equals(uri2)){
                    return 1;
                }
            }
            return 0;
        }
    }
}

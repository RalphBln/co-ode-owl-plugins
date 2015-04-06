package org.coode.puntools.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.protege.editor.core.ui.view.View;
import org.protege.editor.core.ui.view.ViewsPane;
import org.protege.editor.core.ui.view.ViewsPaneMemento;
import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.protege.editor.owl.ui.view.individual.AbstractOWLIndividualViewComponent;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 21, 2009<br><br>
 */
public class PunIndividualView extends AbstractOWLSelectionViewComponent {
    private static final long serialVersionUID = 1L;
    private ViewsPane individualViewsPane;

    private JScrollPane scroller;

    private JComponent addNewPane;

    private static final String IND_PANEL_ID = "Pun";
    private static final String NEW_PANEL_ID = "Create new";
    private static final String NONE_PANEL_ID = "None";

    private CardLayout cardLayout;


    @Override
    public void initialiseView() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        URL indURL = getClass().getResource("/selected-entity-view-individual-panel.xml");

        individualViewsPane = new ViewsPane(getOWLWorkspace(),
                                            new ViewsPaneMemento(indURL,
                                                                 "org.protege.editor.owl.ui.view.selectedentityview.individuals",
                                                                 false));

        for (View v : individualViewsPane.getViews()){
            v.setSyncronizing(false);
            v.setPinned(false);
        }

        scroller = new JScrollPane(individualViewsPane);

        addNewPane = new JPanel(new BorderLayout());
        addNewPane.add(new JButton(new AbstractAction("Add individual pun"){
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent event) {
                handleAddPun();
            }
        }), BorderLayout.NORTH);


        add(scroller, IND_PANEL_ID);
        add(addNewPane, NEW_PANEL_ID);
        add(new JPanel(), NONE_PANEL_ID);
    }


    protected void handleAddPun() {
        OWLEntity currentEntity = getCurrentEntity();
        if (currentEntity != null && !currentEntity.isOWLNamedIndividual()){
            final OWLOntology ont = getOWLModelManager().getActiveOntology();
            final OWLDataFactory df = getOWLModelManager().getOWLDataFactory();
            OWLNamedIndividual ind = df.getOWLNamedIndividual(currentEntity.getIRI());
            getOWLModelManager().applyChange(new AddAxiom(ont, df.getOWLDeclarationAxiom(ind)));
            updateViewContentAndHeader();
        }
    }


    private OWLEntity getCurrentEntity() {
        return getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
    }


    private void changeView(String view){
        cardLayout.show(this, view);
    }

    @Override
    public void disposeView() {
        individualViewsPane.dispose();
    }


    @Override
    protected OWLObject updateView() {
        OWLNamedIndividual pun = null;
        OWLEntity currentEntity = getCurrentEntity();
        if (currentEntity == null || currentEntity.isOWLNamedIndividual()){
            changeView(NONE_PANEL_ID);
        }
        else{
            IRI iri = currentEntity.getIRI();
            for (OWLOntology ont : getOWLModelManager().getActiveOntologies()){
                if (ont.containsIndividualInSignature(iri, Imports.EXCLUDED)) {
                    pun = getOWLModelManager().getOWLDataFactory().getOWLNamedIndividual(iri);
                    break;
                }
            }
            refresh(pun);
        }
        return pun;
    }


    private void refresh(OWLNamedIndividual owlIndividual) {
        if (owlIndividual == null){
            changeView(NEW_PANEL_ID);
        }
        else{
            for (View v : individualViewsPane.getViews()){
                if (v.getViewComponent() instanceof AbstractOWLIndividualViewComponent){
                    ((AbstractOWLIndividualViewComponent)v.getViewComponent()).updateView(owlIndividual);
                }
            }
            changeView(IND_PANEL_ID);
        }
    }


    @Override
    protected boolean isOWLClassView() {
        return true;
    }


    @Override
    protected boolean isOWLObjectPropertyView() {
        return true;
    }


    @Override
    protected boolean isOWLDataPropertyView() {
        return true;
    }


    @Override
    protected boolean isOWLIndividualView() {
        return true;
    }


    @Override
    protected boolean isOWLDatatypeView() {
        return true;
    }
}

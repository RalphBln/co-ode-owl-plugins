package org.coode.taxonomy;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.view.cls.AbstractOWLClassViewComponent;
import org.semanticweb.owlapi.model.OWLClass;

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
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jul 10, 2007<br><br>
 * <p/>
 * 
 * Shows a tab indented tree of descendants for the selected class
 */
public class TabbedHierarchyView extends AbstractOWLClassViewComponent {
    private static final long serialVersionUID = 1L;
    private JTextArea namesComponent;

    // convenience class for querying the asserted subsumption hierarchy directly
    private OWLObjectHierarchyProvider<OWLClass> hp;

    // create the GUI
    @Override
    public void initialiseClassView() {
        setLayout(new BorderLayout(6, 6));
        // in our implementation, just create a simple text area in a scrollpane
        namesComponent = new JTextArea();
        namesComponent.setTabSize(2);
        add(new JScrollPane(namesComponent), BorderLayout.CENTER);
    }

    // called automatically when the global selection changes
    @Override
    protected OWLClass updateView(OWLClass selectedClass) {
        namesComponent.setText("");
        if (selectedClass != null){
            hp = getOWLModelManager().getOWLHierarchyManager().getOWLClassHierarchyProvider();
            render(selectedClass, 0);
        }
        return selectedClass;
    }

    // render the class and recursively all of its subclasses
    private void render(OWLClass selectedClass, int indent) {
        for (int i=0; i<indent; i++){
            namesComponent.append("\t");
        }
        namesComponent.append(getOWLModelManager().getRendering(selectedClass));
        namesComponent.append("\n");
        // the hierarchy provider gets subclasses for us
        for (OWLClass sub: hp.getChildren(selectedClass)){
            render(sub, indent+1);
        }
    }

    // remove any listeners and perform tidyup (none required in this case)
    @Override
    public void disposeView() {
    }
}

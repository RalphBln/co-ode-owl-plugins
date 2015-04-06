package org.coode.search.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.coode.search.ResultsTreeCellRenderer;
import org.coode.search.ui.FlatButton;
import org.protege.editor.owl.ui.tree.OWLLinkedObjectTree;
import org.protege.editor.owl.ui.view.AbstractActiveOntologyViewComponent;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Mar 18, 2008<br><br>
 */
public class SearchAnnotationsView extends AbstractActiveOntologyViewComponent implements ResultsView {
    private static final long serialVersionUID = 1L;

    protected List<AnnotationFilterUI> filters = new ArrayList<>();

    private OWLLinkedObjectTree resultsTree;

    protected JComponent filterPanel;

    protected Map<AnnotationFilterUI, Set<OWLAnnotationAssertionAxiom>> resultsMap = new HashMap<>();

    private ResultsTreeCellRenderer cellRenderer;

    @Override
    protected void initialiseOntologyView() {
        setLayout(new BorderLayout());

        resultsTree = new OWLLinkedObjectTree(getOWLEditorKit());
        resultsTree.setDrawNodeSeperators(true);
        resultsTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("results (0)")));

        cellRenderer = new ResultsTreeCellRenderer(getOWLEditorKit());
        resultsTree.setCellRenderer(cellRenderer);

        add(new JScrollPane(resultsTree), BorderLayout.CENTER);

        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
        add(filterPanel, BorderLayout.NORTH);

        addFilter(0);
    }


    @Override
    protected void disposeOntologyView() {
        for (AnnotationFilterUI filter : filters){
            filter.dispose();
        }
    }


    @Override
    protected synchronized void updateView(OWLOntology activeOntology) {
        for (AnnotationFilterUI filter : filters){
            filter.startSearch();
        }
    }


    protected void addFilter(int index) {
        final AnnotationFilterUI filter = new AnnotationFilterUI(this, getOWLEditorKit());
        final JComponent filterHolder = new JPanel();
        filterHolder.setLayout(new BoxLayout(filterHolder, BoxLayout.LINE_AXIS));
        filterHolder.add(filter);
        filterHolder.add(new FlatButton(new AbstractAction("-"){
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent event) {
                if (filters.size() > 1){
                    filters.remove(filter);
                    filterPanel.remove(filterHolder);
                    filterPanel.revalidate();
                    resultsMap.remove(filter);
                    filter.dispose();
                    refresh();
                }
            }
        }));
        filterHolder.add(new FlatButton(new AbstractAction("+"){
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent event) {
                // add after the current one
                addFilter(filters.indexOf(filter)+1);
            }
        }));

        filterPanel.add(filterHolder);
        filters.add(index, filter);

        // bump the earlier ones down
        for (int i=index+1; i<filters.size(); i++){
            final Container moveFilterHolder = filters.get(i).getParent();
            filterPanel.remove(moveFilterHolder);
            filterPanel.add(moveFilterHolder);
        }

        validate();
    }


    @Override
    public void resultsChanged(AnnotationFilterUI filterUI) {
        resultsMap.put(filterUI, filterUI.getResults());
        refresh();
    }


    protected void refresh() {
        List<Set<OWLAnnotationAssertionAxiom>> results = new ArrayList<>();
        cellRenderer.clearSearches();
        for (AnnotationFilterUI filter : filters){
            final String search = filter.getSearchText();
            cellRenderer.addSearch(search);
            results.add(resultsMap.get(filter));
        }
        resultsTree.setModel(new ResultsTreeModel(results, getOWLModelManager()));

        final int rootCount = resultsTree.getRowCount();
        if (rootCount > 0){
            resultsTree.setSelectionRow(0);
            for (int i=Math.min(rootCount, 20); i>=0; i--){
                resultsTree.expandRow(i);
            }
        }
    }
}

package org.coode.matrix.ui.view;

import org.coode.matrix.model.api.TreeMatrixModel;
import org.coode.matrix.model.parser.OWLObjectListParser;
import org.coode.matrix.ui.component.MatrixTreeTable;
import org.coode.matrix.ui.renderer.OWLObjectTreeTableCellRenderer;
import org.coode.matrix.ui.renderer.OWLObjectsRenderer;
import org.coode.matrix.ui.renderer.OWLObjectListRenderer;
import org.coode.matrix.ui.action.AddAnnotationAction;
import org.coode.matrix.ui.action.RemoveColumnAction;
import org.coode.matrix.ui.editor.OWLObjectListEditor;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.OWLEntityComparator;
import org.protege.editor.owl.ui.tree.OWLObjectTreeNode;
import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.*;

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
 * Date: Jul 3, 2007<br><br>
 */
public abstract class AbstractTreeMatrixView<R extends OWLEntity> extends AbstractOWLSelectionViewComponent {

    private OWLObjectHierarchyProvider<R> hierarchy;

    private OWLObjectTreeTableCellRenderer<R> tree;

    private MatrixTreeTable<R> table;

    private TreeMatrixModel<R> model;

    private OWLEntityComparator<R> comparator;

    private OWLObjectListRenderer objectListRen;

    private TableCellEditor objectListEditor;
    
    private OWLObjectListParser parser;

    private TreeSelectionListener selectionlistener = new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            if (!isPinned()) {
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    R owlObject = ((OWLObjectTreeNode<R>) path.getLastPathComponent()).getOWLObject();
                    setSelectedEntity(owlObject);
                }
                else {
                    // Update from OWL selection model
                    updateViewContentAndHeader();
                }
            }
        }
    };

    // @@TODO ensure no columns remain when the referenced entity is removed from the ontology
    public final void initialiseView() throws Exception {

        setLayout(new BorderLayout(6, 6));

        hierarchy = getHierarchyProvider();

        tree = new OWLObjectTreeTableCellRenderer<R>(getOWLEditorKit(), hierarchy, getOWLEntityComparator());

        model = createMatrixModel(tree);

        table = new MyMatrixTreeTable(model, getOWLModelManager());

        JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);

        createDefaultCellRendererAndEditor();

        tree.addTreeSelectionListener(selectionlistener);

        addAction(new AddAnnotationAction(getOWLEditorKit(), model), "A", "A");
        addAction(new RemoveColumnAction(getOWLEditorKit(), model, table), "B", "A");

        initialiseMatrixView();
    }

    protected Comparator<R> getOWLEntityComparator() {
        if (comparator == null){
            comparator = new OWLEntityComparator<R>(getOWLModelManager()){

                public int compare(R o1, R o2) {
                    if (o1 instanceof OWLIndividual && o2 instanceof OWLClass){
                        return -1;
                    }
                    else if (o2 instanceof OWLIndividual && o1 instanceof OWLClass){
                        return 1;
                    }
                    return super.compare(o1, o2);
                }
            };
        }
        return comparator;
    }

    protected abstract OWLObjectHierarchyProvider<R> getHierarchyProvider();

    protected abstract TreeMatrixModel<R> createMatrixModel(OWLObjectTreeTableCellRenderer<R> tree);

    protected TableCellRenderer getCellRendererForColumn(Object columnObject) {
        return objectListRen;
    }

    protected TableCellEditor getCellEditor(Object columnObject, R rowObject) {
        return objectListEditor;
    }

    protected abstract void initialiseMatrixView() throws Exception;

    protected final TreeMatrixModel<R> getMatrixModel() {
        return model;
    }

    protected final JTable getTable() {
        return table;
    }

    public void disposeView() {
        tree.removeTreeSelectionListener(selectionlistener);
        selectionlistener = null;
    }

    protected R updateView() {
        R selectedOWLObject = getSelectedOWLEntity();
        if (selectedOWLObject == null) {
            return null;
        }
        R treeSelCls = tree.getSelectedOWLObject();
        if (treeSelCls != null) {
            if (selectedOWLObject.equals(treeSelCls)) {
                return selectedOWLObject;
            }
        }
        tree.setSelectedOWLObject(selectedOWLObject);
        TreePath treePath = tree.getSelectionPath();
        if (treePath != null) {
            tree.scrollPathToVisible(treePath);
            return ((OWLObjectTreeNode<R>) treePath.getLastPathComponent()).getOWLObject();
        }
        return selectedOWLObject;
    }

    private R getSelectedOWLEntity() {
        if (isOWLClassView()) {
            return (R) getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
        }
        else if (isOWLObjectPropertyView()) {
            return (R) getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getLastSelectedObjectProperty();
        }
        else if (isOWLDataPropertyView()) {
            return (R) getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getLastSelectedDataProperty();
        }
        else if (isOWLIndividualView()) {
            return (R) getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getLastSelectedIndividual();
        }
        else {
            return (R) getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
        }
    }

    private void createDefaultCellRendererAndEditor() {
        OWLObjectsRenderer objRen = new OWLObjectsRenderer(getOWLModelManager());

        parser = new OWLObjectListParser(getOWLModelManager());

        objectListEditor = new OWLObjectListEditor(getOWLEditorKit(), objRen, parser);
        objectListRen = new OWLObjectListRenderer(objRen);
    }

    /**
     *
     * @param type - one of "CLASS, OBJPROP, DATAPROP, INDIVIDUAL, DATATYPE" constants from OWLObjectListParser
     */
    protected final void setEditorType(int type) {
        parser.setTypes(Collections.singleton(type));
    }

    /**
     * Table that asks for the renderers and editors for a given column from the view
     */
    private class MyMatrixTreeTable extends MatrixTreeTable<R> {

        public MyMatrixTreeTable(TreeMatrixModel<R> model, OWLModelManager mngr) {
            super(model, mngr);
        }

        public TableCellRenderer getCellRenderer(int row, int col) {
            TableCellRenderer ren = null;
            if (col > 0) {
                Object colObj = model.getObjectForColumn(col);
                ren = getCellRendererForColumn(colObj);
            }
            if (ren == null) {
                ren = super.getCellRenderer(row, col);
            }
            return ren;
        }

        public TableCellEditor getCellEditor(int row, int col) {
            TableCellEditor editor = null;
            if (col > 0) {
                Object colObj = model.getObjectForColumn(col);
                R rowObj = model.getOWLObjectForRow(row);
                editor = AbstractTreeMatrixView.this.getCellEditor(colObj, rowObj);
            }
            if (editor == null) {
                editor = super.getCellEditor(row, col);
            }
            return editor;
        }
    }
}
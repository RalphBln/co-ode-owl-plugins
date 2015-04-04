package org.coode.annotate.prefs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
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
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.coode.annotate.EditorType;
import org.protege.editor.core.ui.util.Icons;
import org.protege.editor.core.ui.util.UIUtil;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 1, 2008<br><br>
 */
public class AnnotationTemplatePrefsPanel extends OWLPreferencesPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private JToolBar toolbar;

    private MyTableModel model;

    private boolean dirty = false;

    private Action addAction = new AbstractAction("Add Annotation", OWLIcons.getIcon("property.annotation.add.png")){
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleAddAnnotation();
        }
    };

    private Action removeAction = new AbstractAction("Remove Annotation", OWLIcons.getIcon("property.annotation.remove.png")){
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleRemoveAnnotation();
        }
    };

    private Action upAction = new AbstractAction("Move Up", Icons.getIcon("object.move_up.gif")){
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleMoveUp();
        }
    };

    private Action downAction = new AbstractAction("Move Down", Icons.getIcon("object.move_down.gif")){
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleMoveDown();
        }

    };

    private Action importAction = new AbstractAction("Import from file", Icons.getIcon("project.open.gif")){
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleImport();
        }

    };


    private Action exportAction = new AbstractAction("Export to file", Icons.getIcon("project.save.gif")){
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleExport();
        }

    };

    protected AnnotationTemplateDescriptor descriptor;


    @Override
    public void applyChanges() {
        if (dirty){
            AnnotationTemplatePrefs.getInstance().setDefaultDescriptor(descriptor);
            dirty = false;
        }
    }


    @Override
    public void initialise() {

        setLayout(new BorderLayout());

        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        addToolbarAction(addAction);
        addToolbarAction(removeAction);
        toolbar.addSeparator(new Dimension(6, 6));
        addToolbarAction(upAction);
        addToolbarAction(downAction);
        toolbar.addSeparator(new Dimension(6, 6));
        addToolbarAction(importAction);
        addToolbarAction(exportAction);

        add(toolbar, BorderLayout.NORTH);

        // create a copy of the default descriptor
        final OWLDataFactory df = getOWLModelManager().getOWLDataFactory();
        descriptor = new AnnotationTemplateDescriptor(AnnotationTemplatePrefs.getInstance().getDefaultDescriptor(df));
        model = new MyTableModel(descriptor);

        table = new JTable(model);
        table.setRowHeight(table.getRowHeight() + 5);
        table.setShowVerticalLines(false);

        setupRenderersAndEditors();

        final JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);
    }


    private void setupRenderersAndEditors() {
        table.setDefaultRenderer(OWLAnnotationProperty.class, new OWLCellRenderer(getOWLEditorKit()));

        JComboBox comboBox = new JComboBox(EditorType.values());
        table.setDefaultEditor(EditorType.class, new DefaultCellEditor(comboBox));
    }


    private void addToolbarAction(Action action) {
        JButton button = new JButton(action);
        button.setToolTipText((String)action.getValue(Action.NAME));
        button.setText(null);
        button.setBorder(new EmptyBorder(4, 4, 4, 4));
        toolbar.add(button);
    }


    @Override
    public void dispose() {
        // do nothing
    }

    protected void handleAddAnnotation() {
        OWLAnnotationProperty property = new UIHelper(getOWLEditorKit()).pickAnnotationProperty();
        if (property != null){
            Object[] rowData = new Object[]{property, EditorType.text};
            model.addRow(rowData);
            table.getSelectionModel().setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1);
            dirty = true;
        }
    }


    protected void handleRemoveAnnotation() {
        final int row = table.getSelectedRow();
        if (row != -1){
            model.removeRow(row);
            if (row < model.getRowCount()){
                table.getSelectionModel().setSelectionInterval(row, row);
            }
            else if (row-1 > 0){
                table.getSelectionModel().setSelectionInterval(row-1, row-1);
            }
            dirty = true;
        }
    }

    protected void handleMoveUp() {
        final int row = table.getSelectedRow();
        if (row > 0){
            model.moveRow(row, row, row-1);
            table.getSelectionModel().setSelectionInterval(row-1, row-1);
            dirty = true;
        }
    }

    protected void handleMoveDown() {
        final int row = table.getSelectedRow();
        if (row < model.getRowCount()-1){
            model.moveRow(row, row, row+1);
            table.getSelectionModel().setSelectionInterval(row+1, row+1);
            dirty = true;
        }
    }


    protected void handleImport() {
        JFrame f = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, getParent());
        if (f == null) {
            f = new JFrame();
        }

        File importFile = UIUtil.openFile(f, "Import a template file for your annotations", Collections.EMPTY_SET);
        if (importFile != null){
            try (FileInputStream inStream = new FileInputStream(importFile)) {
                descriptor = new AnnotationTemplateDescriptor(inStream, getOWLModelManager().getOWLDataFactory());
                model = new MyTableModel(descriptor);
                table.setModel(model);
                dirty = true;
                inStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected void handleExport() {
        JFrame f = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, getParent());
        if (f == null) {
            f = new JFrame();
        }

        File exportFile = UIUtil.saveFile(f, "Export a template file for your annotations", Collections.EMPTY_SET);
        if (exportFile != null){
            try (
                FileOutputStream out = new FileOutputStream(exportFile);
                PrintStream outStream = new PrintStream(out);){
                descriptor.export(outStream);
                outStream.flush();
                outStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class MyTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 1L;
        protected MyTableModel(AnnotationTemplateDescriptor descriptor) {
            addColumn("Annotation property");
            addColumn("Editor type");

            for (OWLAnnotationProperty property : descriptor.getProperties()){
                Object[] rowData = new Object[]{property, descriptor.getEditor(property)};
                super.addRow(rowData);
            }
        }


        @Override
        public void setValueAt(Object o, int row, int col) {
            super.setValueAt(o, row, col);
            OWLAnnotationProperty property = descriptor.getProperties().get(row);
            if (col == 0){
                descriptor.changeProperty(property, (OWLAnnotationProperty)o);
            }
            else if (col == 1){
                descriptor.setEditor(property, (EditorType)o);
            }
        }


        @Override
        public void moveRow(int start, int end, int to) {
            super.moveRow(start, end, to);
            descriptor.move(start, end, to);
        }


        @Override
        public void removeRow(int row) {
            super.removeRow(row);
            OWLAnnotationProperty property = descriptor.getProperties().get(row);
            descriptor.remove(property);
        }


        @Override
        public void addRow(Object[] rowData) {
            super.addRow(rowData);
            descriptor.addRow((OWLAnnotationProperty)rowData[0], (EditorType)rowData[1]);
        }


        @Override
        public Class<?> getColumnClass(int col) {
            if (col == 0){
                return OWLAnnotationProperty.class;
            }
            else{
                return EditorType.class;
            }
        }
    }
}

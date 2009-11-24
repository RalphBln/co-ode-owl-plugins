/**
 * 
 */
package org.coode.oppl.protege.ui.rendering;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLObject;

/**
 * @author Luigi Iannone
 * 
 */
public class InstantiationTableCellRenderer implements TableCellRenderer {
	private final OWLCellRenderer owlCellRenderer;
	private final OWLEditorKit owlEditorKit;

	public InstantiationTableCellRenderer(OWLEditorKit owlEditorKit) {
		if (owlEditorKit == null) {
			throw new NullPointerException("The OWL editor kit cannot be null");
		}
		this.owlEditorKit = owlEditorKit;
		this.owlCellRenderer = new OWLCellRenderer(owlEditorKit);
	}

	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 *      .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component toReturn = new DefaultTableCellRenderer()
				.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
		if (value instanceof OWLObject) {
			toReturn = this.owlCellRenderer.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
		}
		return toReturn;
	}

	/**
	 * @return the owlEditorKit
	 */
	protected OWLEditorKit getOWLEditorKit() {
		return this.owlEditorKit;
	}
}

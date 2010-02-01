package org.coode.patterns.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.table.TableModel;

import org.coode.patterns.locality.LocalityChecker;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLOntology;

public class LocalityCheckerActionListener extends LocalityChecker implements
		ActionListener {
	private static final String SAFETY_ANALYSIS_BREAKDOWN = "Safety analysis breakdown";
	public final static int DIMENSION = 18;
	private final JButton resultButton;
	private OWLOntology activeOntology;
	private OWLEntity not;

	public LocalityCheckerActionListener(final OWLEditorKit kit,
			Set<OWLEntity> signature, JButton resultButton) {
		super(kit.getOWLModelManager().getOWLOntologyManager(), kit
				.getOWLModelManager().getReasoner(), signature);
		this.activeOntology = kit.getOWLModelManager().getActiveOntology();
		this.resultButton = resultButton;
		this.resultButton.setIcon(this.generateIcon(Color.gray));
		this.resultButton.setToolTipText("Check not executed yet");
		this.resultButton.setEnabled(false);
		this.not = kit.getOWLModelManager().getOWLDataFactory().getOWLNothing();
	}

	public Icon generateIcon(final Color color) {
		return new Icon() {
			/**
			 * Draw the icon at the specified location. Icon implementations may
			 * use the Component argument to get properties useful for painting,
			 * e.g. the foreground or background color.
			 */
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Color oldColor = g.getColor();
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(color);
				g2.fillOval(x + 2, y + 2, DIMENSION - 4, DIMENSION - 4);
				g2.setColor(Color.LIGHT_GRAY);
				g2.drawOval(x + 2, y + 2, DIMENSION - 4, DIMENSION - 4);
				g2.setColor(oldColor);
			}

			/**
			 * Returns the icon's width.
			 * 
			 * @return an int specifying the fixed width of the icon.
			 */
			public int getIconWidth() {
				return DIMENSION;
			}

			/**
			 * Returns the icon's height.
			 * 
			 * @return an int specifying the fixed height of the icon.
			 */
			public int getIconHeight() {
				return DIMENSION;
			}
		};
	}

	public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
		this.resultButton.setEnabled(true);
		this.resultButton.setToolTipText(SAFETY_ANALYSIS_BREAKDOWN);
		if (this.ipm != null) {
			if (!this.isLocal(this.activeOntology)) {
				this.resultButton.setIcon(this.generateIcon(Color.red));
			} else {
				this.resultButton.setIcon(this.generateIcon(Color.green));
			}
		}
	}

	public TableModel print() {
		return new LocResultTableModel(this, this.not);
	}
}

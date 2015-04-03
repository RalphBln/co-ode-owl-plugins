package org.coode.oae.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.coode.oae.ui.VariableListModel.VariableListItem;
import org.coode.oae.ui.utils.StretchingPanelsFactory;
import org.protege.editor.core.ui.list.MList;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.core.ui.list.MListSectionHeader;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.JOptionPaneEx;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import uk.ac.manchester.mae.evaluation.BindingModel;
import uk.ac.manchester.mae.evaluation.PropertyChainCell;
import uk.ac.manchester.mae.evaluation.PropertyChainModel;

public class BindingEditor_modalwindows extends JPanel implements
		ActionListener, VerifiedInputEditor {
	private static final long serialVersionUID = -2534565329159746844L;

	protected final class TwoAddButtonsMList extends MList {
		private static final long serialVersionUID = 7034517538254357642L;

		@Override
		@SuppressWarnings("unchecked")
		protected void handleDelete() {
			BindingEditor_modalwindows.this.propertyChainCells
					.remove(((VariableListItem<PropertyChainCell>) getSelectedValue())
							.getItem());
			handlePropertyChainsUpdate();
		}

		@Override
		protected List<MListButton> getSectionButtons(MListSectionHeader header) {
			List<MListButton> buttons = new ArrayList<MListButton>();
			if (header.canAdd()) {
				buttons.add(BindingEditor_modalwindows.this.addObjectProperty);
				buttons.add(BindingEditor_modalwindows.this.addDataProperty);
			}
			return buttons;
		}
	}

	private static class MyButton extends MListButton implements ImageObserver {
		private Image image;

		MyButton(String name, Color color, Image i, ActionListener l) {
			super(name, color);
			this.image = i;
			setActionListener(l);
		}

		@Override
		public void paintButtonContent(Graphics2D g) {
			Rectangle r = getBounds();
			g.drawImage(this.image, r.x, r.y, r.width, r.height, this);
		}

		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			return false;
		}
	}

	protected transient MListButton addObjectProperty = new MyButton(
			"Add Object Property", Color.BLUE,
			GraphicalEditorConstants.objectPropertyIcon.getImage(), this);
	protected transient MListButton addDataProperty = new MyButton(
			"Add Datatype Property", Color.GREEN,
			GraphicalEditorConstants.dataPropertyIcon.getImage(), this);
	private BindingModel editedBindingModel;
	private OWLEditorKit kit;
	private JTextField identifier = ComponentFactory.createTextField();
	private final TwoAddButtonsMList propertychainsView = new TwoAddButtonsMList();
	protected final List<PropertyChainCell> propertyChainCells = new ArrayList<PropertyChainCell>();
	protected final VariableListModel<PropertyChainCell> propertychainsModel = new VariableListModel<PropertyChainCell>(
			this.propertyChainCells, "Property chain elements", true);
	private ErrorReport errorsLabel = new ErrorReport();
	private Set<InputVerificationStatusChangedListener> listeners = new HashSet<InputVerificationStatusChangedListener>();
	private ObjectPropertySelector obp;
	private DataPropertySelector dp;

	public boolean isCorrect() {
		if (this.propertyChainCells.size() == 0) {
			return false;
		}
		boolean correct = true;
		for (int i = 0; i < this.propertyChainCells.size() - 1; i++) {
			if (!(this.propertyChainCells.get(i).getProperty() instanceof OWLObjectProperty)) {
				correct = false;
			}
		}
		if (this.propertyChainCells.get(this.propertyChainCells.size() - 1)
				.getProperty() instanceof OWLObjectProperty) {
			correct = false;
		}
		return correct;
	}

	/**
	 * @param k
	 *            the {@link OWLEditorKit} to use
	 * @param model
	 *            the {@link BindingModel} to use; must not be null but can be
	 *            empty
	 */
	public BindingEditor_modalwindows(OWLEditorKit k) {
		super(new BorderLayout());
		this.kit = k;
		this.obp = new ObjectPropertySelector(k);
		this.dp = new DataPropertySelector(k);
		this.propertychainsView.setModel(this.propertychainsModel);
		this.propertychainsView
				.setCellRenderer(new RenderableObjectCellRenderer(this.kit));
		this.propertychainsView
				.setPreferredSize(GraphicalEditorConstants.LIST_PREFERRED_SIZE);
		init();
	}

	public void setBindingModel(BindingModel model) {
		this.editedBindingModel = model;
		this.propertyChainCells.clear();
		handlePropertyChainsUpdate();
		if (this.editedBindingModel != null) {
			this.identifier.setText(this.editedBindingModel.getIdentifier());
			PropertyChainModel pcm = this.editedBindingModel
					.getPropertyChainModel();
			while (pcm != null) {
				this.propertyChainCells.add(pcm.getCell());
				pcm = pcm.getChild();
			}
			handlePropertyChainsUpdate();
		} else {
			this.identifier.setText("");
		}
	}

	protected void handlePropertyChainsUpdate() {
		this.propertychainsModel.init();
		boolean correct = isCorrect();
		this.errorsLabel.clearReport();
		if (this.identifier.getText().length() == 0) {
			this.errorsLabel.addReport("An identifier must be specified");
		}
		if (!isCorrect()) {
			this.errorsLabel
					.addReport("The property chain must be made of object properties and end with a datatype property");
		}
		for (InputVerificationStatusChangedListener i : this.listeners) {
			i.verifiedStatusChanged(correct);
		}
	}

	public BindingModel getBindingModel() {
		int ret = showDialog("Create Bindings", this);
		if (ret == JOptionPane.OK_OPTION) {
			this.editedBindingModel = new BindingModel(this.identifier
					.getText(), new PropertyChainModel(this.propertyChainCells));
			return this.editedBindingModel;
		} else {
			return null;
		}
	}

	public void init() {
		this.add(this.identifier, BorderLayout.NORTH);
		this.add(this.propertychainsView, BorderLayout.CENTER);
		this.add(StretchingPanelsFactory.getStretchyPanelWithBorder(
				this.errorsLabel, "Error report"), BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.addObjectProperty)) {
			int ret = showDialog("Select Object Property and Facet", this.obp);
			if (ret == JOptionPane.OK_OPTION) {
				PropertyChainCell p = this.obp.getCell();
				if (p != null) {
					this.propertyChainCells.add(p);
					handlePropertyChainsUpdate();
				}
			}
			this.obp.clear();
		}
		if (e.getSource().equals(this.addDataProperty)) {
			int ret = showDialog("Select Datatype Property", this.dp);
			if (ret == JOptionPane.OK_OPTION) {
				PropertyChainCell p = this.dp.getCell();
				if (p != null) {
					this.propertyChainCells.add(p);
					handlePropertyChainsUpdate();
				}
				this.dp.clear();
			}
		}
	}

	public int showDialog(String title, JComponent component) {
		return JOptionPaneEx.showValidatingConfirmDialog(getParent(), title,
				component, JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null);
	}

	public void addStatusChangedListener(
			InputVerificationStatusChangedListener listener) {
		this.listeners.add(listener);
	}

	public void removeStatusChangedListener(
			InputVerificationStatusChangedListener listener) {
		this.listeners.remove(listener);
	}
}

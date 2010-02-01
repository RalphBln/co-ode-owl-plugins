package org.coode.patterns.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.coode.oppl.variablemansyntax.Variable;
import org.coode.oppl.variablemansyntax.bindingtree.BindingNode;
import org.coode.patterns.locality.LocalityChecker;
import org.semanticweb.owl.model.OWLEntity;

public class LocResultTableModel implements TableModel {
	private static enum values {
		PRESENT("X", 'X'), ABSENT(" ", ' ');
		private char v;
		private String val;

		values(String val, char v) {
			this.val = val;
			this.v = v;
		}

		public static String getValue(char v) {
			if (v == PRESENT.v) {
				return PRESENT.val;
			}
			if (v == ABSENT.v) {
				return ABSENT.val;
			}
			throw new RuntimeException("Unknown value: " + v);
		}
	}

	private final String[][] dataArray;
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	public String[][] getDataArray() {
		return this.dataArray;
	}

	public LocResultTableModel(LocalityChecker checker, OWLEntity not) {
		List<Variable> toassign = new ArrayList<Variable>(checker.getToAssign());
		List<Boolean> bindingsLocality = checker.getExploredNodesLocality();
		List<BindingNode> bindings = checker.getExploredNodes();
		this.dataArray = new String[bindings.size() + 1][toassign.size() + 1];
		this.dataArray[0][0] = "Safe";
		for (int i = 0; i < toassign.size(); i++) {
			this.dataArray[0][i + 1] = toassign.get(i).getName();
		}
		if (bindings.size() == bindingsLocality.size()) {
			for (int i = 0; i < bindings.size(); i++) {
				this.setValueAt(bindingsLocality.get(i), i, 0);
				BindingNode node = bindings.get(i);
				for (int j = 0; j < toassign.size(); j++) {
					Variable v = toassign.get(j);
					if (!not.equals(node.getAssignmentValue(v))) {
						this.setValueAt(values.PRESENT.val, i, j + 1);
					} else {
						this.setValueAt(values.ABSENT.val, i, j + 1);
					}
				}
			}
			this.sort();
		}
	}

	private void sort() {
		List<String> trues = new ArrayList<String>();
		List<String> falses = new ArrayList<String>();
		for (int i = 1; i < this.dataArray.length; i++) {
			StringBuilder b = new StringBuilder();
			for (int j = 1; j < this.dataArray[i].length; j++) {
				b.append(this.dataArray[i][j]);
			}
			String record = b.toString();
			if (Boolean.parseBoolean(this.dataArray[i][0])) {
				trues.add(record);
			} else {
				falses.add(record);
			}
		}
		Collections.sort(trues);
		Collections.sort(falses);
		for (int i = 0; i < trues.size(); i++) {
			String s = trues.get(i);
			this.dataArray[i + 1][0] = Boolean.TRUE.toString();
			for (int j = 1; j < this.dataArray[i + 1].length; j++) {
				this.dataArray[i + 1][j] = values.getValue(s.charAt(j - 1));
			}
		}
		for (int i = 0; i < falses.size(); i++) {
			String s = falses.get(i);
			this.dataArray[i + 1 + trues.size()][0] = Boolean.FALSE.toString();
			for (int j = 1; j < this.dataArray[i + 1 + trues.size()].length; j++) {
				this.dataArray[i + 1 + trues.size()][j] = values.getValue(s
						.charAt(j - 1));
			}
		}
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.dataArray[rowIndex + 1][columnIndex] = aValue.toString();
	}

	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(l);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.dataArray[rowIndex + 1][columnIndex];
	}

	public int getRowCount() {
		return this.dataArray.length - 1;
	}

	public String getColumnName(int columnIndex) {
		return this.dataArray[0][columnIndex];
	}

	public int getColumnCount() {
		return this.dataArray[0].length;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
	}
}

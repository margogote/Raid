package Models;

import javax.swing.table.AbstractTableModel;

/**
 * Modele de tableau sur lequel on se base dans les interfaces
 * 
 * @author margaux
 */
public class TabModel extends AbstractTableModel {

	private Object[][] data;
	private String[] title;

	/** Constructeur */
	public TabModel(Object[][] data, String[] title) {
		this.data = data;
		this.title = title;
	}

	/**
	 * Retourne le titre de la colonne à l'indice spécifié
	 * 
	 * @return this.title[col]
	 */
	public String getColumnName(int col) {
		return this.title[col];
	}

	/**
	 * Retourne le nombre de colonnes
	 * 
	 * @return this.title.length
	 */
	public int getColumnCount() {
		return this.title.length;
	}

	/**
	 * Retourne le nombre de lignes
	 * 
	 * @return this.data.length
	 */
	public int getRowCount() {
		return this.data.length;
	}

	/**
	 * Retourne la valeur à l'emplacement spécifié
	 * 
	 * @return this.data[row][col]
	 */
	public Object getValueAt(int row, int col) {
		return this.data[row][col];
	}

	/**
	 * Retourner l'id que l'on veut
	 * 
	 * @return this.data[row][2]
	 */
	public Object getId(int row) {
		return this.data[row][2];
	}

	/** Définit la valeur à l'emplacement spécifié */
	public void setValueAt(Object value, int row, int col) {
		this.data[row][col] = value;
	}

	/**
	 * Retourne la classe de la donnée de la colonne
	 * 
	 * @return this.data[0][col].getClass()
	 */
	public Class getColumnClass(int col) {
		// On retourne le type de la cellule à la colonne demandée
		// On se moque de la ligne puisque les données sont les mêmes
		// On choisit donc la première ligne
		return this.data[0][col].getClass();
	}

	/**
	 * permet de rendre éditable un cellule
	 * 
	 * @return true
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}
}

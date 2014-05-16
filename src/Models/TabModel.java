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
	 * Retourne le titre de la colonne � l'indice sp�cifi�
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
	 * Retourne la valeur � l'emplacement sp�cifi�
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

	/** D�finit la valeur � l'emplacement sp�cifi� */
	public void setValueAt(Object value, int row, int col) {
		this.data[row][col] = value;
	}

	/**
	 * Retourne la classe de la donn�e de la colonne
	 * 
	 * @return this.data[0][col].getClass()
	 */
	public Class getColumnClass(int col) {
		// On retourne le type de la cellule � la colonne demand�e
		// On se moque de la ligne puisque les donn�es sont les m�mes
		// On choisit donc la premi�re ligne
		return this.data[0][col].getClass();
	}

	/**
	 * permet de rendre �ditable un cellule
	 * 
	 * @return true
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}
}

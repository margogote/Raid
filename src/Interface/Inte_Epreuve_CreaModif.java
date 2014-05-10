package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Models.TabModel;

public class Inte_Epreuve_CreaModif extends JFrame{

	JFrame thePanel = new JFrame();

	/*   Champs  */
	private JLabel nomL = new JLabel("Nom");
	private JTextField nomT = new JTextField("");

	private JLabel typeL = new JLabel("Type");
	private JComboBox<Object> typeC = new JComboBox<Object>();

	private JLabel dateL = new JLabel("Date de début");
	private JTextField dateT = new JTextField("..... date ....");

	private JLabel dureeL = new JLabel("Durée");
	private JTextField dureeT = new JTextField("..... durée ....");
	
	private JLabel difficL = new JLabel("Difficulté");
	private String[] difficulte = { "CHOISIR", "Aventure", "Expert" };
	private JComboBox<Object> difficC = new JComboBox<Object>(difficulte);
	
	/* Boutons */
	private JButton modif = new JButton("Modifier");
	private JButton supp = new JButton("Supprimer");
	private JButton creer = new JButton("Créer");
	
	private JButton oK = new JButton("Valider");
	private JButton annuler = new JButton("Annuler");
	
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons
	private JPanel panBoutCreer = new JPanel();
	private JPanel panBoutSupp = new JPanel();
	private JPanel panBoutModif = new JPanel();

	private JPanel panTitre =new JPanel();
	
	/* Tableau */
	private TabModel tabModel;
	private JTable tableau;
	private Object[][] data = {};
	private String title[] = { "", "idBalise", "Type", "Valeur" };

	private int idc;
	private JPanel panel;
	
	public Inte_Epreuve_CreaModif(int idC, JPanel pan) {
		thePanel = this;
		idc = idC;
		panel=pan;

		thePanel.setTitle("Raidzultats");
		thePanel.setSize(800, 600);
		thePanel.setLocationRelativeTo(null);
		thePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thePanel.setLayout(new BorderLayout()); // Pour les placements

		oK.setPreferredSize(new Dimension(100, 30));
		annuler.setPreferredSize(new Dimension(100, 30));
		
		JPanel btnP = new JPanel();
		btnP.add(oK);
		btnP.add(annuler);

		JPanel labelP = new JPanel();
		//labelP.setPreferredSize(new Dimension(450, 225));
		labelP.setLayout(new GridLayout(6, 2));
		labelP.add(nomL);
		labelP.add(nomT);
		labelP.add(typeL);
		labelP.add(typeC);
		labelP.add(dateL);
		labelP.add(dateT);
		labelP.add(dureeL);
		labelP.add(dureeT);
		labelP.add(difficL);
		labelP.add(difficC);
		
		JPanel megaP = new JPanel();
		megaP.setBorder(BorderFactory
				.createTitledBorder("Créer/modifier votre épreuve"));
		megaP.add(labelP);
		
		modif.setPreferredSize(new Dimension(100, 30));
		creer.setPreferredSize(new Dimension(100, 30));
		supp.setPreferredSize(new Dimension(100, 30));
		
		panBoutCreer.add(creer);
		panBoutSupp.add(supp);
		panBoutModif.add(modif);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		panBoutonsListe.add(panBoutCreer);
		panBoutonsListe.add(panBoutModif);
		panBoutonsListe.add(panBoutSupp);

		// Nous ajoutons notre tableau à notre contentPane dans un scroll
		// Sinon les titres des colonnes ne s'afficheront pas !
		tabModel = new TabModel(data, title);
		tableau = new JTable(tabModel);
		tableau.setRowHeight(30);
		JScrollPane jScroll = new JScrollPane(tableau);
		jScroll.setPreferredSize(new Dimension(400, 400));
		
		panTitre.setBorder(BorderFactory.createTitledBorder("Ici vous pouvez gérer les balises de l'epreuve"));
		//panTitre.setPreferredSize(new Dimension(350, 450));
		panTitre.add(panBoutonsListe);
		panTitre.add(jScroll);
		
		JPanel gigaP = new JPanel();
		gigaP.add(megaP);
		gigaP.add(panTitre);
		
		JPanel ultraP = new JPanel();
		ultraP.setLayout(new BoxLayout(ultraP, BoxLayout.PAGE_AXIS));
		ultraP.add(gigaP);
		ultraP.add(btnP);
		
		JPanel panPan = new JPanel();
		panPan.add(ultraP);

		thePanel.add(panPan);

		thePanel.setVisible(true);
		

		EcouteurOK ecoutOK = new EcouteurOK();
		oK.addActionListener(ecoutOK);

		EcouteurQ ecoutQ = new EcouteurQ();
		annuler.addActionListener(ecoutQ);
	}
	
	public class EcouteurOK implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

		}
	}

	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			thePanel.dispose();
		}
	}
}

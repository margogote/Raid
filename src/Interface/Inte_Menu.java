package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Interface.Inte_Accueil.EcouteurQ;

/**
 * Onglet menu : Permet de changer de competition, de quitter l'application et d'avoir des informations supplémentaires
 * 
 * @author Margaux
 * 
 */
public class Inte_Menu extends JPanel {

	private JFrame jFrameContenantLOnglet;

	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */

	private JButton retour = new JButton("Changer de compétition");
	private JButton quitter = new JButton("Quitter");
	private JButton aPropos = new JButton("A propos");

	/**
     * Classe principale
     * permet de gérer l'interface du panel
     */
	public Inte_Menu(JFrame jFrameContenantLOnglet) {

		this.jFrameContenantLOnglet = jFrameContenantLOnglet;

		retour.setPreferredSize(new Dimension(200, 30));
		quitter.setPreferredSize(new Dimension(200, 30));
		aPropos.setPreferredSize(new Dimension(200, 30));

		JPanel panBoutRetour = new JPanel();
		JPanel panBoutQuit = new JPanel();
		JPanel panBoutAP = new JPanel();

		panBoutRetour.add(retour);
		panBoutQuit.add(quitter);
		panBoutAP.add(aPropos);

		panBoutonsListe.setLayout(new BoxLayout(panBoutonsListe,
				BoxLayout.PAGE_AXIS));
		//panBoutonsListe.setPreferredSize(new Dimension(200,150));
		panBoutonsListe.add(panBoutRetour);
		panBoutonsListe.add(panBoutQuit);
		panBoutonsListe.add(panBoutAP);

		//panBalise.setLayout(new BoxLayout(panBalise, BoxLayout.PAGE_AXIS));

		//panMega.setLayout(new BorderLayout());
		//panBalise.add(panBoutonsListe , BorderLayout.CENTER );

		//panBalise.setLayout(new BoxLayout(panBalise, BoxLayout.Y_AXIS));
		
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//this.setLayout(new BorderLayout());
		//this.add(Box.createVerticalGlue(), BorderLayout.NORTH);
		this.add(panBoutonsListe /*, BorderLayout.CENTER */);
		//this.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

		EcouteurQ ecoutQ = new EcouteurQ();
		quitter.addActionListener(ecoutQ);

		EcouteurRet ecoutRet = new EcouteurRet();
		retour.addActionListener(ecoutRet);
		
		EcouteurAP ecoutAP = new EcouteurAP();
		aPropos.addActionListener(ecoutAP);

	}

	/**
     * Permet de gérer les clics du type "Quitter"
     * Ferme la fenêtre
     */
	public class EcouteurQ implements ActionListener { // Action du quitter

		public void actionPerformed(ActionEvent arg0) {

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment quitter?", "Attention",
					JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				// fen2.dispose();
				// System.exit(0); ----- ! Attention quite le programme pas la
				// fenetre ------
				jFrameContenantLOnglet.dispose();

			}
		}
	}

	/**
     * Permet de gérer les clics du type "Changer de competition".
     */
	public class EcouteurRet implements ActionListener { // Action du retour

		public void actionPerformed(ActionEvent arg0) {

			int rep = 0;
			rep = JOptionPane.showConfirmDialog(null,
					"Voulez vous vraiment quitter?", "Attention",
					JOptionPane.YES_NO_OPTION);

			if (rep == 0) {
				Inte_Accueil accueil = new Inte_Accueil();
				jFrameContenantLOnglet.dispose();

			}
		}
	}

	/**
     * Permet de gérer les clics du type "A Propos".
     */
	public class EcouteurAP implements ActionListener { // Action du Apropos

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane
					.showMessageDialog(
							null,
							"L'application Raidzultat a été développée par Margaux Simon & Philippe Smessaert",
							"Tadaaa!", JOptionPane.INFORMATION_MESSAGE);

		}
	}

}

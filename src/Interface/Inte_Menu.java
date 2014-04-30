package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Interface.Inte_Accueil.EcouteurQ;

public class Inte_Menu extends JPanel {

	private JFrame jFrameContenantLOnglet;

	/* Panels */
	private JPanel panMega = new JPanel(); // Panel qui contient tous
	private JPanel panBalise = new JPanel(); // Panel du champ de recherche
	private JPanel panBoutonsListe = new JPanel(); // Panel des bouttons

	/* Boutons */

	private JButton retour = new JButton("Changer de comp�tition");
	private JButton quitter = new JButton("Quitter");
	private JButton aPropos = new JButton("A propos");

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
		panBoutonsListe.add(panBoutRetour);
		panBoutonsListe.add(panBoutQuit);
		panBoutonsListe.add(panBoutAP);

		panBalise.setLayout(new BoxLayout(panBalise, BoxLayout.PAGE_AXIS));
		panBalise.setLayout(new BorderLayout());
		panBalise.add(panBoutonsListe, BorderLayout.CENTER);

		// panMega.setLayout(new BorderLayout());
		panMega.add(panBalise /* , BorderLayout.CENTER */);

		this.setLayout(new BorderLayout());
		this.add(panMega /* , BorderLayout.CENTER */);

		EcouteurQ ecoutQ = new EcouteurQ();
		quitter.addActionListener(ecoutQ);

		EcouteurRet ecoutRet = new EcouteurRet();
		retour.addActionListener(ecoutRet);
		
		EcouteurAP ecoutAP = new EcouteurAP();
		aPropos.addActionListener(ecoutAP);

	}

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

	public class EcouteurAP implements ActionListener { // Action du retour

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane
					.showMessageDialog(
							null,
							"L'application Raidzultat a �t� d�velopp�e par Margaux Simon & Philippe Smessaert",
							"Tadaaa!", JOptionPane.INFORMATION_MESSAGE);

		}
	}

}

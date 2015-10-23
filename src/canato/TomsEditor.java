package canato;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
** Adding a comment to the class
*/
public class TomsEditor extends JFrame implements ActionListener {

	public static final String OPEN = "open";
	public static final String EXIT = "exit";
	public static final String FONT = "font";
	public static final String COLOR = "color";

	private JTextArea textArea;
	private String[] fontNames;
	private JFileChooser fileChooser;

	public TomsEditor() {

		// Ladda in lista med fontnamn så detta bara behöver göras en gång
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		fontNames = ge.getAvailableFontFamilyNames();

		// Skapa en file selecion dialog här som vi senare använder för att
		// öppna
		// filer. Fördelen att inte skapa den på nytt varje gång är att den
		// behåller sin position
		fileChooser = new JFileChooser();

		// Skapa menu bar
		JMenuBar menubar = new JMenuBar();

		// File menu and its menu items
		JMenu file = new JMenu("Arkiv");
		menubar.add(file);

		JMenuItem mi = new JMenuItem("Öppna...");
		mi.setActionCommand(OPEN);
		mi.addActionListener(this);
		file.add(mi);

		mi = new JMenuItem("Avsluta");
		mi.setActionCommand(EXIT);
		mi.addActionListener(this);
		file.add(mi);

		// Edit menu and its menu items
		JMenu edit = new JMenu("Inställningar");
		menubar.add(edit);

		mi = new JMenuItem("Sätt Färg...");
		mi.setActionCommand(COLOR);
		mi.addActionListener(this);
		edit.add(mi);

		mi = new JMenuItem("Sätt Typsnitt...");
		mi.setActionCommand(FONT);
		mi.addActionListener(this);
		edit.add(mi);

		// Text area
		textArea = new JTextArea(20, 40);
		textArea.setFont(new Font("Arial", Font.PLAIN, 14));
		JScrollPane sp = new JScrollPane(textArea);
		add(sp, BorderLayout.CENTER);

		// Frame settings
		setJMenuBar(menubar);
		setTitle("Min Editor");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	// - Action rutin för knappar
	// -----------------------------------------------
	public void actionPerformed(ActionEvent event) {
		String ac = event.getActionCommand();
		switch (ac) {
		case OPEN:
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				String fileContents = loadFile(fileChooser.getSelectedFile());
				textArea.setText(fileContents);
			}

			break;
		case EXIT:
			System.exit(0);
			break;
		case COLOR:
			Color newColor = JColorChooser.showDialog(this,
					"Välj Förgrundsfärg", textArea.getForeground());
			if (newColor != null)
				textArea.setForeground(newColor);
			break;
		case FONT:
			String fontName = (String) JOptionPane.showInputDialog(this,
					"Systemfonter", "Välj Font", JOptionPane.PLAIN_MESSAGE,
					null, fontNames, textArea.getFont().getFontName());
			if (fontName != null) {
				textArea.setFont(new Font(fontName, Font.PLAIN, 14));
			}
			break;
		}
	}

	// - Laddar in en fil i textarean
	// ---------------------------------------------
	private String loadFile(File file) {

		try {
			FileReader in = new FileReader(file);
			BufferedReader brd = new BufferedReader(in);
			StringBuffer fileBuffer = new StringBuffer();
			String line = null;

			while ((line = brd.readLine()) != null) {
				fileBuffer.append(line);
				fileBuffer.append(System.getProperty("line.separator"));
			}

			in.close();
			return fileBuffer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	// - Main
	// ---------------------------------------------------------------------
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TomsEditor ex = new TomsEditor();
				ex.setVisible(true);
			}
		});
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BAbout {

	public BAbout() {
		// Constructor
	}

	public void getAbout() {
		String name = "Bitterli's Media Player in Java";
		String version = "Version " + version();
		String changes = "Changelog:"
				+ "\n(Mar 02 2020) Added codec listing tool."
				+ "\n(Mar 01 2020) Added audio track switch option."
				+ "\n(Mar 01 2020) Added single button for play and pause."
				+ "\n(Mar 01 2020) Added subtitle controls."
				+ "\n(Feb 29 2020) Updated main class info."
				+ "\n(Feb 28 2020) Added playlist functionality."
				+ "\n(Feb 18 2020) Added fullscreen media controls."
				+ "\n(Feb 12 2020) Added Fullscreen Function"
				+ "\n(Feb 11 2020) Added ability to save Equalizers to text file."
				+ "\n(Feb 07 2020) Added library default presets."
				+ "\n(Feb 03 2020) Added mute button and volume slider."
				+ "\n(Feb 03 2020) Finishing touches to Video effects panel."
				+ "\n(Jan 30 2020) Added meta information for audio files."
				+ "\n(Jan 28 2020) Fixed media looping."
				+ "\n(Jan 16 2020) Removed References to online logo information."
				+ "\n(Jan 13 2020) Created a git repository and github cloud."
				+ "\n(Jan 07 2020) Removed all default library-based players, entirely VLCJ"
				+ "\n(Jan 06 2020) Added base equalization to videos."
				+ "\n(Dec 20 2019) Adding special features to VideoPlayer."
				+ "\n(Dec 20 2019) Added slider functionality (scaling is proper)."
				+ "\n(Dec 18 2019) Fixed menu bar issues."
				+ "\n(Dec 17 2019) Cleaned up video GUI, added slider."
				+ "\n(Dec 14 2019) Finished midi player."
				+ "\n(Dec 10 2019) Added open operation between midi and sample classes."
				+ "\n(Dec 02 2019) Added midi player class GUI."
				+ "\n(Nov 22 2019) Added midi player class base.";
		String copyright = "(C) 2020 bitterli.us";

		try {
			JFrame about = new JFrame("About");
			JPanel titleArea = new JPanel(new BorderLayout());

			Image logoImg = ImageIO.read(new File("img\\\\logoTapes.png"));
			logoImg = logoImg.getScaledInstance(64, 64, 0);
			ImageIcon logo = new ImageIcon(logoImg);
			JLabel imageLabel = new JLabel(logo);
			imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			titleArea.add(imageLabel, BorderLayout.LINE_START);

			JPanel textTitle = new JPanel(new GridLayout(2, 1));
			textTitle.add(new JLabel(name));
			textTitle.add(new JLabel(version));

			titleArea.add(textTitle);

			JPanel changeLog = new JPanel();
			JTextArea textArea = new JTextArea(20, 40);
			JScrollPane scrollableTextArea = new JScrollPane(textArea);
			scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			textArea.setText(changes);

			changeLog.add(scrollableTextArea);

			JPanel footer = new JPanel(new BorderLayout());
			footer.add(new JLabel(copyright), BorderLayout.LINE_START);

			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					about.dispose();
				}
			});

			footer.add(close, BorderLayout.LINE_END);

			footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			about.setLayout(new BorderLayout());
			about.add(titleArea, BorderLayout.PAGE_START);
			about.add(changeLog, BorderLayout.CENTER);
			about.add(footer, BorderLayout.PAGE_END);
			about.setSize(new Dimension(500, 515));
			about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			about.setBackground(Color.LIGHT_GRAY);
			about.setVisible(true);
		} catch (IOException e) {

		}
	}
	
	public String version() {
		return "B_0.9.3 - Options update";
	}
}

/* "main" class
 * Handles player runtime, sets up file reading.
 * 
 * this class handles the graphical interface of the audio player.
 * and handles selecting and controls of the audio file.
 * 
 */

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class gui {

	private File file;

	JFrame frame = new JFrame("Media Player");

	public gui() {
		// constructor
	}

	@SuppressWarnings("deprecation")
	public void start() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		JFrame welcome = new JFrame("Loading... - Audio Player");
		welcome.setResizable(false);
		JPanel welcomeComponents = new JPanel();
		Image logo = ImageIO.read(new File("namelogo.png"));
		logo = logo.getScaledInstance(700, 145, 0);
		welcomeComponents.add(new JLabel(new ImageIcon(logo)));
		String text = "<html><center><p><br><br>Bitterli's Media Player<br>Version " + new BAbout().version()
				+ "<br><br>Loading...</p></center></html>";
		welcomeComponents.add(new JLabel(String.format(text)));

		welcome.add(welcomeComponents);
		welcome.setSize(new Dimension(800, 300));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		welcome.setLocation(dim.width / 2 - welcome.getSize().width / 2, dim.height / 2 - welcome.getSize().height / 2);
		welcome.setVisible(true);

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		SwingUtilities.invokeLater(() -> {
			try {
				Media vlcPlayer = new Media();
				vlcPlayer.getVideo(dim, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		welcome.dispose();
	}
}

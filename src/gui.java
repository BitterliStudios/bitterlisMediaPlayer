
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
import java.net.URL;

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

	private void getFile() {
		JFileChooser j = new JFileChooser();
		j.setFileFilter(new FileNameExtensionFilter("Media Files",
				//audio formats
				"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob", "ape", "au", "awb",
				"caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp", "mod", "mp1", "mp2", "mp3",
				"mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra", "rmi", "s3m", "sid", "spx", "tak", "thd",
				"tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm",
				//video formats
				"3g2", "3gp", "3gp2", "3gpp", "amv", "asf", "avi", "bik", "bin", "divx", "drc", "dv", "evo", "f4v", "flv", "gvi",
				"gxf", "iso", "m1v", "m2t", "m2ts", "m2v", "m4v", "mkv", "mov", "mp2", "mp2v", "mp4", "mp4v", "mpe", "mpeg", "mpeg1",
				"mpeg2", "mpeg4", "mpg", "mpv2", "mts", "mtv", "mxf", "mxg", "nsv", "nuv", "ogg", "ogm", "ogv", "ogx", "ps", "rec",
				"rm", "rmvb", "rpl", "thp", "tod", "ts", "tts", "txd", "vob", "vro", "webm", "wm", "wmv", "wtv", "xesc"
				));
		j.showOpenDialog(null);
		file = j.getSelectedFile();
	}

	private String fileType() {
		String fileName = file.getName();
		String fileType = "";
		for (int i = fileName.length() - 3; i < fileName.length(); i++) {
			fileType += fileName.substring(i, i + 1);
		}
		return fileType;
	}

	public void start() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		JFrame welcome = new JFrame("Loading... - Audio Player");
		welcome.setResizable(false);
		JPanel welcomeComponents = new JPanel();
		Image logo = ImageIO.read(new File("namelogo.png"));
		logo = logo.getScaledInstance(700, 145, 0);
		welcomeComponents.add(new JLabel(new ImageIcon(logo)));
		String text = "<html><center><p><br><br>Bitterli's Media Player<br>in Java using vlcj.<br><br>Loading...</p></center></html>";
		welcomeComponents.add(new JLabel(String.format(text)));

		welcome.add(welcomeComponents);
		welcome.setSize(new Dimension(800, 300));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		welcome.setLocation(dim.width / 2 - welcome.getSize().width / 2, dim.height / 2 - welcome.getSize().height / 2);
		welcome.setVisible(true);

		getFile();
		if (file == null) {
			file = new File("test1.mp3");
		}
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		SwingUtilities.invokeLater(() -> {
			Media vlcPlayer = new Media(file);
			try {
				vlcPlayer.getVideo(dim);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		welcome.dispose();
	}
}

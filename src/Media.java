
/**
 * Media.class
 * The meat of the program.
 * Invoked from SwingUtilities in gui class.
 * Object oriented call.
 */

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.player.Equalizer;
import uk.co.caprica.vlcj.player.MediaMetaData;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Media {

	private boolean loop;
	private EmbeddedMediaPlayer mediaPlayer;
	private File file;
	private boolean stopped;
	private Equalizer eq;
	private MediaPlayerFactory mediaPlayerFactory;
	private Canvas c;

	@SuppressWarnings("unused")

	public Media(File f) {
		file = f;
		equalizer();
	}

	private void equalizer() {
		eq = new Equalizer(10);
		for (int i = 0; i < 10; i++) {
			eq.setAmp(i, 0);
		}

	}

	public boolean isAudio() {
		boolean isAudio = false;
		String[] audio = { "3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob",
				"ape", "au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp",
				"mod", "mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra",
				"rmi", "s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm" };
		String fileName = file.getName();
		String fileType = "";
		for (int i = fileName.length() - 3; i < fileName.length(); i++) {
			fileType += fileName.substring(i, i + 1);
		}
		for (int i = 0; i < audio.length; i++) {
			if (fileType.equals(audio[i])) {
				isAudio = true;
			}
		}
		return isAudio;
	}

	public void getVideo(Dimension dim) throws IOException {
		loop = false;
		stopped = false;
		@SuppressWarnings("unused")
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		JFrame frame = new JFrame("" + file.getName() + " - Media Player");
		mediaPlayerFactory = new MediaPlayerFactory();

		JMenuBar main = new JMenuBar();
		WrapLayout layout = new WrapLayout(WrapLayout.LEFT, 0, 0);
		main.setLayout(layout);
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getFile();
				mediaPlayer.stop();
				mediaPlayer.playMedia(file.getPath());
			}

			@SuppressWarnings("unused")
			// I will use this later on, not just now.
			private String fileType() {
				String fileName = file.getName();
				String fileType = "";
				for (int i = fileName.length() - 3; i < fileName.length(); i++) {
					fileType += fileName.substring(i, i + 1);
				}
				return fileType;
			}

			private void getFile() {
				JFileChooser j = new JFileChooser();
				j.setFileFilter(new FileNameExtensionFilter("Media Files",
						// audio formats
						"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob",
						"ape", "au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka",
						"mlp", "mod", "mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus",
						"qcp", "ra", "rmi", "s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma",
						"wv", "xa", "xm",
						// video formats
						"3g2", "3gp", "3gp2", "3gpp", "amv", "asf", "avi", "bik", "bin", "divx", "drc", "dv", "evo",
						"f4v", "flv", "gvi", "gxf", "iso", "m1v", "m2t", "m2ts", "m2v", "m4v", "mkv", "mov", "mp2",
						"mp2v", "mp4", "mp4v", "mpe", "mpeg", "mpeg1", "mpeg2", "mpeg4", "mpg", "mpv2", "mts", "mtv",
						"mxf", "mxg", "nsv", "nuv", "ogg", "ogm", "ogv", "ogx", "ps", "rec", "rm", "rmvb", "rpl", "thp",
						"tod", "ts", "tts", "txd", "vob", "vro", "webm", "wm", "wmv", "wtv", "xesc"));
				j.showOpenDialog(null);
				file = j.getSelectedFile();
			}

		});
		fileMenu.add(open);

		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
				frame.dispose();
			}

		});
		fileMenu.add(close);

		main.add(fileMenu);

		JMenu helpMenu = new JMenu("Help");

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				BAbout about = new BAbout();
				about.getAbout();

			}

		});

		JMenu videoSettings = new JMenu("Video");
		JMenuItem videoEffects = new JMenuItem("Effects");
		videoEffects.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				mediaPlayer.setAdjustVideo(true);

				JPanel veP = new JPanel(new GridLayout(0, 5));

				JPanel bP = new JPanel(new BorderLayout());
				bP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel bL = new JLabel("Brightness");
				JLabel bT = new JLabel("Currently: " + (int) (mediaPlayer.getBrightness() * 100));
				int bInt = (int) ((double) mediaPlayer.getBrightness() * 100);
				JSlider bS = new JSlider(JSlider.VERTICAL, 0, 200, bInt);
				bS.setMajorTickSpacing(10);
				bS.setPaintTicks(true);
				bS.setPaintLabels(true);
				ChangeListener bC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float bF = (float) theSlider.getValue() / 100;
							mediaPlayer.setBrightness(bF);
							bT.setText("Currently: " + (int) (mediaPlayer.getBrightness() * 100));
						}
					}
				};
				bS.addChangeListener(bC);
				bP.add(bL, BorderLayout.PAGE_START);
				bP.add(bS, BorderLayout.CENTER);
				bP.add(bT, BorderLayout.PAGE_END);
				veP.add(bP);
				
				JPanel cP = new JPanel(new BorderLayout());
				cP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel cL = new JLabel("Contrast");
				JLabel cT = new JLabel("Currently: " + (int) (mediaPlayer.getBrightness() * 100));
				int cInt = (int) ((double) mediaPlayer.getContrast() * 100);
				JSlider cS = new JSlider(JSlider.VERTICAL, 0, 200, cInt);
				cS.setMajorTickSpacing(10);
				cS.setPaintTicks(true);
				cS.setPaintLabels(true);
				ChangeListener cC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float cF = (float) theSlider.getValue() / 100;
							mediaPlayer.setContrast(cF);
							cT.setText("Currently: " + (int) (mediaPlayer.getContrast() * 100));
						}
					}
				};
				cS.addChangeListener(cC);
				cP.add(cL, BorderLayout.PAGE_START);
				cP.add(cS, BorderLayout.CENTER);
				cP.add(cT, BorderLayout.PAGE_END);
				veP.add(cP);
				
				JPanel hP = new JPanel(new BorderLayout());
				hP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel hL = new JLabel("Hue");
				JLabel hT = new JLabel("Currently: " + (int) (mediaPlayer.getHue()) + "°");
				int hInt = (int) (mediaPlayer.getHue());
				JSlider hS = new JSlider(JSlider.VERTICAL, 0, 360, hInt);
				hS.setMajorTickSpacing(45);
				hS.setPaintTicks(true);
				hS.setPaintLabels(true);
				ChangeListener hC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							int hF = theSlider.getValue();
							mediaPlayer.setHue(hF);
							hT.setText("Currently: " + (int) (mediaPlayer.getHue()));
						}
					}
				};
				hS.addChangeListener(hC);
				hP.add(hL, BorderLayout.PAGE_START);
				hP.add(hS, BorderLayout.CENTER);
				hP.add(hT, BorderLayout.PAGE_END);
				veP.add(hP);

				JFrame veF = new JFrame("Video Effects");
				veP.setBorder(new EmptyBorder(10, 10, 10, 10));
				veF.add(veP);
				veF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				veF.setSize(new Dimension(600, 400));
				//veF.setResizable(false);
				veF.setVisible(true);
			}

		});
		videoSettings.add(videoEffects);
		main.add(videoSettings);

		JMenu audioSettings = new JMenu("Audio");

		JMenuItem audioInfo = new JMenuItem("Track Info");
		audioInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isAudio()) {
					MediaMetaData meta = mediaPlayer.getMediaMeta().asMediaMetaData();

					JFrame i = new JFrame("Track Info");
					JPanel bigPanel = new JPanel();

					JPanel albumArtPanel = new JPanel();
					BufferedImage artB = mediaPlayer.getMediaMeta().getArtwork();
					Image art = artB;
					art = art.getScaledInstance(87, 87, 0);
					JLabel albumArt = new JLabel(new ImageIcon(art));
					albumArtPanel.add(albumArt);

					JPanel albumInfo = new JPanel(new GridLayout(10, 1));

					JLabel titleL = new JLabel("Title: ");
					String titleT = "N/A";
					if (!String.valueOf(meta.getTitle()).equals("null")) {
						titleT = meta.getTitle();
					}
					JTextField title = new JTextField(titleT, 10);
					title.setEditable(false); // Will enable when I add meta information updating.
					albumInfo.add(titleL);
					albumInfo.add(title);

					JLabel albumNameL = new JLabel("Album Title: ");
					String albumNameT = "N/A";
					if (!String.valueOf(meta.getAlbum()).equals("null")) {
						albumNameT = meta.getAlbum();
					}
					JTextField albumName = new JTextField(albumNameT, 10);
					albumName.setEditable(false); // Will enable when I add meta information updating.
					albumInfo.add(albumNameL);
					albumInfo.add(albumName);

					JLabel albumArtistL = new JLabel("Album Artist: ");
					String albumArtistT = "N/A";
					if (!String.valueOf(meta.getAlbumArtist()).equals("null")) {
						albumArtistT = meta.getAlbumArtist();
					}
					JTextField albumArtist = new JTextField(albumArtistT, 10);
					albumArtist.setEditable(false); // Will enable when I add meta information updating.
					albumInfo.add(albumArtistL);
					albumInfo.add(albumArtist);

					JLabel trackNumL = new JLabel("Track Number / Total Number of Tracks: ");
					String trackNumT = "N/A";
					if (String.valueOf(meta.getTrackNumber()).equals("null")
							&& !String.valueOf(meta.getTrackTotal()).equals("null")) {
						trackNumT = "N/A / " + meta.getTrackTotal();
					} else if (String.valueOf(meta.getTrackTotal()).equals("null")
							&& !(String.valueOf(meta.getTrackNumber()).equals("null"))) {
						trackNumT = meta.getTrackNumber() + " / N/A";
					} else if (!(String.valueOf(meta.getTrackNumber()).equals("null")
							&& String.valueOf(meta.getTrackTotal()).equals("null"))) {
						trackNumT = "" + meta.getTrackNumber() + "/" + meta.getTrackTotal();
					}
					JTextField trackNum = new JTextField(trackNumT, 10);
					trackNum.setEditable(false); // Will enable when I add meta information updating.
					albumInfo.add(trackNumL);
					albumInfo.add(trackNum);

					JLabel diskL = new JLabel("Disk Number / Total Number of Disks");
					String diskT = "N/A";
					if (String.valueOf(meta.getDiscNumber()).equals("null")
							&& !String.valueOf(meta.getDiscTotal()).equals("null")) {
						diskT = "N/A / " + meta.getDiscTotal();
					} else if (String.valueOf(meta.getDiscTotal()).equals("null")
							&& !String.valueOf(meta.getDiscNumber()).equals("null")) {
						diskT = meta.getDiscNumber() + "/ N/A";
					} else if (!(String.valueOf(meta.getDiscTotal()).equals("null")
							&& String.valueOf(meta.getDiscTotal()).equals("null"))) {
						diskT = "" + meta.getDiscNumber() + "/" + meta.getDiscTotal();
					}
					JTextField disk = new JTextField(diskT, 10);
					disk.setEditable(false); // Will enable when I add meta information updating.
					albumInfo.add(diskL);
					albumInfo.add(disk);

					bigPanel.add(albumArtPanel);
					bigPanel.add(albumInfo);

					i.add(bigPanel);

					i.setSize(300, 400);
					i.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					i.setLocation(dim.width / 2 - i.getSize().width / 2, dim.height / 2 - i.getSize().height / 2);
					i.setResizable(false);
					i.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "This is not an audio format.");
				}
			}

		});

		JMenuItem mute = new JMenuItem("Mute");
		mute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.setAdjustVideo(true);
				if (!mediaPlayer.isMute()) {
					mediaPlayer.mute(true);
					mute.setText("Unmute");
				} else {
					mediaPlayer.mute(false);
					mute.setText("Mute");
				}
				mediaPlayer.setAdjustVideo(false);
			}

		});

		JMenuItem equalizer = new JMenuItem("Equalizer");
		equalizer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Float[] values = new Float[11];
				values[0] = mediaPlayer.getEqualizer().getPreamp();
				for (int i = 1; i < 11; i++) {
					if (mediaPlayer.getEqualizer().getAmp(i - 1) != 0.0f) {
						values[i] = mediaPlayer.getEqualizer().getAmp(i - 1);
					} else {
						values[i] = 0.0f;
					}
				}

				JPanel eqpanel = new JPanel(new GridLayout(0, 11));

				JPanel preampPanel = new JPanel(new BorderLayout());
				preampPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel preampLabel = new JLabel("Preamp");
				JLabel preampText = new JLabel("" + values[0] + "dB");
				JSlider preamp = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[0]));
				preamp.setMajorTickSpacing(2);
				preamp.setPaintTicks(true);
				preamp.setPaintLabels(true);
				ChangeListener preampC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[0] = (new Float(theSlider.getValue()));
							preampText.setText("" + values[0] + "dB");
							mediaPlayer.getEqualizer().setPreamp(values[0]);
						}
					}
				};
				preamp.addChangeListener(preampC);
				preampPanel.add(preampLabel, BorderLayout.PAGE_START);
				preampPanel.add(preamp, BorderLayout.CENTER);
				preampPanel.add(preampText, BorderLayout.PAGE_END);
				eqpanel.add(preampPanel);

				JPanel eq1Panel = new JPanel(new BorderLayout());
				eq1Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq1Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(0) + "");
				JLabel eq1Text = new JLabel("" + values[1] + "dB");
				JSlider eq1 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[1]));
				eq1.setMajorTickSpacing(2);
				eq1.setPaintTicks(true);
				eq1.setPaintLabels(true);
				ChangeListener eq1C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[1] = (new Float(theSlider.getValue()));
							eq1Text.setText("" + values[1] + "dB");
							mediaPlayer.getEqualizer().setAmp(0, values[1]);
						}
					}
				};
				eq1.addChangeListener(eq1C);
				eq1Panel.add(eq1Label, BorderLayout.PAGE_START);
				eq1Panel.add(eq1, BorderLayout.CENTER);
				eq1Panel.add(eq1Text, BorderLayout.PAGE_END);
				eqpanel.add(eq1Panel);

				JPanel eq2Panel = new JPanel(new BorderLayout());
				eq2Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq2Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(1) + "");
				JLabel eq2Text = new JLabel("" + values[1] + "dB");
				JSlider eq2 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[2]));
				eq2.setMajorTickSpacing(2);
				eq2.setPaintTicks(true);
				eq2.setPaintLabels(true);
				ChangeListener eq2C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[2] = (new Float(theSlider.getValue()));
							eq2Text.setText("" + values[2] + "dB");
							mediaPlayer.getEqualizer().setAmp(1, values[2]);
						}
					}
				};
				eq2.addChangeListener(eq2C);
				eq2Panel.add(eq2Label, BorderLayout.PAGE_START);
				eq2Panel.add(eq2, BorderLayout.CENTER);
				eq2Panel.add(eq2Text, BorderLayout.PAGE_END);
				eqpanel.add(eq2Panel);

				JPanel eq3Panel = new JPanel(new BorderLayout());
				eq3Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq3Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(2) + "");
				JLabel eq3Text = new JLabel("" + values[3] + "dB");
				JSlider eq3 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[3]));
				eq3.setMajorTickSpacing(2);
				eq3.setPaintTicks(true);
				eq3.setPaintLabels(true);
				ChangeListener eq3C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[3] = (new Float(theSlider.getValue()));
							eq3Text.setText("" + values[3] + "dB");
							mediaPlayer.getEqualizer().setAmp(2, values[3]);
						}
					}
				};
				eq3.addChangeListener(eq3C);
				eq3Panel.add(eq3Label, BorderLayout.PAGE_START);
				eq3Panel.add(eq3, BorderLayout.CENTER);
				eq3Panel.add(eq3Text, BorderLayout.PAGE_END);
				eqpanel.add(eq3Panel);

				JPanel eq4Panel = new JPanel(new BorderLayout());
				eq4Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq4Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(3) + "");
				JLabel eq4Text = new JLabel("" + values[4] + "dB");
				JSlider eq4 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[4]));
				eq4.setMajorTickSpacing(2);
				eq4.setPaintTicks(true);
				eq4.setPaintLabels(true);
				ChangeListener eq4C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[4] = (new Float(theSlider.getValue()));
							eq4Text.setText("" + values[4] + "dB");
							mediaPlayer.getEqualizer().setAmp(3, values[4]);
						}
					}
				};
				eq4.addChangeListener(eq4C);
				eq4Panel.add(eq4Label, BorderLayout.PAGE_START);
				eq4Panel.add(eq4, BorderLayout.CENTER);
				eq4Panel.add(eq4Text, BorderLayout.PAGE_END);
				eqpanel.add(eq4Panel);

				JPanel eq5Panel = new JPanel(new BorderLayout());
				eq5Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq5Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(4) + "");
				JLabel eq5Text = new JLabel("" + values[5] + "dB");
				JSlider eq5 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[5]));
				eq5.setMajorTickSpacing(2);
				eq5.setPaintTicks(true);
				eq5.setPaintLabels(true);
				ChangeListener eq5C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[5] = (new Float(theSlider.getValue()));
							eq5Text.setText("" + values[5] + "dB");
							mediaPlayer.getEqualizer().setAmp(4, values[5]);
						}
					}
				};
				eq5.addChangeListener(eq5C);
				eq5Panel.add(eq5Label, BorderLayout.PAGE_START);
				eq5Panel.add(eq5, BorderLayout.CENTER);
				eq5Panel.add(eq5Text, BorderLayout.PAGE_END);
				eqpanel.add(eq5Panel);

				JPanel eq6Panel = new JPanel(new BorderLayout());
				eq6Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq6Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(5) + "");
				JLabel eq6Text = new JLabel("" + values[6] + "dB");
				JSlider eq6 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[6]));
				eq6.setMajorTickSpacing(2);
				eq6.setPaintTicks(true);
				eq6.setPaintLabels(true);
				ChangeListener eq6C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[6] = (new Float(theSlider.getValue()));
							eq6Text.setText("" + values[6] + "dB");
							mediaPlayer.getEqualizer().setAmp(5, values[6]);
						}
					}
				};
				eq6.addChangeListener(eq6C);
				eq6Panel.add(eq6Label, BorderLayout.PAGE_START);
				eq6Panel.add(eq6, BorderLayout.CENTER);
				eq6Panel.add(eq6Text, BorderLayout.PAGE_END);
				eqpanel.add(eq6Panel);

				JPanel eq7Panel = new JPanel(new BorderLayout());
				eq7Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq7Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(6) + "");
				JLabel eq7Text = new JLabel("" + values[7] + "dB");
				JSlider eq7 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[7]));
				eq7.setMajorTickSpacing(2);
				eq7.setPaintTicks(true);
				eq7.setPaintLabels(true);
				ChangeListener eq7C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[7] = (new Float(theSlider.getValue()));
							eq7Text.setText("" + values[7] + "dB");
							mediaPlayer.getEqualizer().setAmp(6, values[7]);
						}
					}
				};
				eq7.addChangeListener(eq7C);
				eq7Panel.add(eq7Label, BorderLayout.PAGE_START);
				eq7Panel.add(eq7, BorderLayout.CENTER);
				eq7Panel.add(eq7Text, BorderLayout.PAGE_END);
				eqpanel.add(eq7Panel);

				JPanel eq8Panel = new JPanel(new BorderLayout());
				eq8Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq8Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(7) + "");
				JLabel eq8Text = new JLabel("" + values[8] + "dB");
				JSlider eq8 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[8]));
				eq8.setMajorTickSpacing(2);
				eq8.setPaintTicks(true);
				eq8.setPaintLabels(true);
				ChangeListener eq8C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[8] = (new Float(theSlider.getValue()));
							eq8Text.setText("" + values[8] + "dB");
							mediaPlayer.getEqualizer().setAmp(7, values[8]);
						}
					}
				};
				eq8.addChangeListener(eq8C);
				eq8Panel.add(eq8Label, BorderLayout.PAGE_START);
				eq8Panel.add(eq8, BorderLayout.CENTER);
				eq8Panel.add(eq8Text, BorderLayout.PAGE_END);
				eqpanel.add(eq8Panel);

				JPanel eq9Panel = new JPanel(new BorderLayout());
				eq9Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq9Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(8) + "");
				JLabel eq9Text = new JLabel("" + values[9] + "dB");
				JSlider eq9 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[9]));
				eq9.setMajorTickSpacing(2);
				eq9.setPaintTicks(true);
				eq9.setPaintLabels(true);
				ChangeListener eq9C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[9] = (new Float(theSlider.getValue()));
							eq9Text.setText("" + values[9] + "dB");
							mediaPlayer.getEqualizer().setAmp(8, values[9]);
						}
					}
				};
				eq9.addChangeListener(eq9C);
				eq9Panel.add(eq9Label, BorderLayout.PAGE_START);
				eq9Panel.add(eq9, BorderLayout.CENTER);
				eq9Panel.add(eq9Text, BorderLayout.PAGE_END);
				eqpanel.add(eq9Panel);

				JPanel eq10Panel = new JPanel(new BorderLayout());
				eq10Panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel eq10Label = new JLabel("" + mediaPlayerFactory.getEqualizerBandFrequencies().get(9) + "");
				JLabel eq10Text = new JLabel("" + values[10] + "dB");
				JSlider eq10 = new JSlider(JSlider.VERTICAL, -12, 12, (int) Math.round(values[10]));
				eq10.setMajorTickSpacing(2);
				eq10.setPaintTicks(true);
				eq10.setPaintLabels(true);
				ChangeListener eq10C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							values[10] = (new Float(theSlider.getValue()));
							eq10Text.setText("" + values[10] + "dB");
							mediaPlayer.getEqualizer().setAmp(9, values[10]);
						}
					}
				};
				eq10.addChangeListener(eq10C);
				eq10Panel.add(eq10Label, BorderLayout.PAGE_START);
				eq10Panel.add(eq10, BorderLayout.CENTER);
				eq10Panel.add(eq10Text, BorderLayout.PAGE_END);
				eqpanel.add(eq10Panel);

				JFrame frameEQ = new JFrame("Equalizer");
				eqpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
				frameEQ.add(eqpanel);
				frameEQ.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameEQ.setSize(new Dimension(800, 400));
				frameEQ.setResizable(false);
				frameEQ.setVisible(true);

			}

		});

		audioSettings.add(mute);
		audioSettings.add(equalizer);
		audioSettings.add(audioInfo);

		main.add(audioSettings);

		helpMenu.add(about);
		main.add(helpMenu);

		frame.setJMenuBar(main);

		c = new Canvas();
		JPanel p = new JPanel();
		c.setBounds(100, 500, 1050, 500);
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);
		p.setBounds(100, 50, 1050, 600);
		frame.add(p, BorderLayout.NORTH);
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

		JPanel p0 = new JPanel();

		JLabel time = new JLabel("0:00 / 0:00");

		JSlider slider = new JSlider(0, 100, 0);
		slider.setPaintLabels(true);

		slider.setPreferredSize(new Dimension(600, 30));
		slider.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				double mousePos = e.getX() - 5;
				float jumpTo = (float) (mousePos / 590);
				mediaPlayer.setPosition(jumpTo);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});

		ActionListener taskPerformed = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int getSliderValue = (int) (mediaPlayer.getPosition() * 100);
				slider.setValue(getSliderValue);

				String length = "";
				int seconds = (int) (mediaPlayer.getLength() / 1000);
				int minutes = 0;
				if (seconds >= 60) {
					minutes = seconds / 60;
					seconds %= 60;
				}
				if (seconds < 10) {
					length = "" + minutes + ":0" + seconds;
				} else {
					length = "" + minutes + ":" + seconds;
				}

				String position = "";
				seconds = (int) (mediaPlayer.getTime() / 1000);
				minutes = 0;
				if (seconds >= 60) {
					minutes = seconds / 60;
					seconds %= 60;
				}
				if (seconds < 10) {
					position = "" + minutes + ":0" + seconds;
				} else {
					position = "" + minutes + ":" + seconds;
				}

				time.setText("" + position + " / " + length);
			}
		};
		Timer t = new Timer(100, taskPerformed);
		t.start();
		p0.add(time);
		p0.add(slider);

		p0.setBounds(100, 900, 105, 200);
		frame.add(p0, BorderLayout.CENTER);

		JPanel p1 = new JPanel();

		p1.setBounds(100, 900, 105, 200);
		frame.add(p1, BorderLayout.SOUTH);
		JButton playbutton = new JButton("Play");
		Image playimg = null;
		try {
			playimg = ImageIO.read(new File("Play.gif"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		playbutton.setIcon(new ImageIcon(playimg));
		playbutton.setBounds(50, 50, 150, 100);
		p1.add(playbutton);

		JButton pausebutton = new JButton("Pause");
		Image pauseimg = null;
		try {
			pauseimg = ImageIO.read(new File("Pause.png"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pausebutton.setIcon(new ImageIcon(pauseimg));
		pausebutton.setBounds(80, 50, 150, 100);
		p1.add(pausebutton);

		JButton stopbutton = new JButton("Stop");
		Image stopimg = null;
		try {
			stopimg = ImageIO.read(new File("Stop.gif"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stopbutton.setIcon(new ImageIcon(stopimg));
		stopbutton.setBounds(80, 50, 150, 100);
		p1.add(stopbutton);

		JButton loopbutton = new JButton("Loop");
		Image loopimg = null;
		try {
			loopimg = ImageIO.read(new File("Loop.png"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		loopimg = loopimg.getScaledInstance(15, 15, 0);
		loopbutton.setIcon(new ImageIcon(loopimg));
		loopbutton.setBounds(80, 50, 150, 100);
		p1.add(loopbutton);

		frame.setSize(904, 645);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
		mediaPlayer.playMedia(file.getPath());
		mediaPlayer.setEqualizer(eq);
		mediaPlayer.getEqualizer().setPreamp(0);
		c.setBackground(Color.black);

		// Main logo (shown when stopped, no album art on audio).
		// The album art is displayed over the logo.
		// The timers are to delay the placement of each object.
		// The main logo to ensure it's drawn over the video object.
		// The album art to ensure the album art has loaded in (null pointer errors)

		ActionListener drawLogo = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					BufferedImage before = ImageIO.read(new File("logoTapes.png"));
					int w = before.getWidth();
					int h = before.getHeight();
					BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale(0.25, 0.25);
					AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					after = scaleOp.filter(before, after);
					int imgWidth = (after.getWidth()) / 8;
					int imgHeight = (after.getHeight()) / 8;

					c.getGraphics().drawImage(after, 444 - imgWidth, 250 - imgHeight, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Timer logoTimer = new Timer(100, drawLogo);
		logoTimer.setRepeats(false);
		logoTimer.start();

		if (isAudio()) {
			ActionListener drawArt = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					BufferedImage before = mediaPlayer.getMediaMeta().getArtwork();
					int w = before.getWidth();
					int h = before.getHeight();
					BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale(0.5, 0.5);
					AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					after = scaleOp.filter(before, after);
					int imgWidth = (after.getWidth()) / 4;
					int imgHeight = (after.getHeight()) / 4;

					c.getGraphics().drawImage(after, 444 - imgWidth, 250 - imgHeight, null);
				}
			};
			Timer artTimer = new Timer(1000, drawArt);
			artTimer.setRepeats(false);
			artTimer.start();
		}

		ActionListener stopAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.pause();
			}
		};
		Timer stop = new Timer(100, stopAction);

		pausebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.pause();
			}
		});
		playbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (stopped) {
					stopped = false;
					stop.stop();
					mediaPlayer.play();
				} else {
					mediaPlayer.play();
				}
			}
		});
		stopbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.stop();
				stop.start();
				stopped = true;
			}
		});
		loopbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!loop) {
					if (!stopped) {
						loopbutton.setBackground(Color.GRAY);
						loop = true;
						mediaPlayer.setRepeat(true);
					} else {
						mediaPlayer.play();
						stopped = false;
						loopbutton.setBackground(Color.GRAY);
						loop = true;
						mediaPlayer.setRepeat(true);
					}
				} else {
					loopbutton.setBackground(Color.LIGHT_GRAY);
					loop = false;
					mediaPlayer.setRepeat(false);
				}
			}
		});
	}

	public void stop() {
		mediaPlayer.stop();
		mediaPlayer.pause();
	}

}
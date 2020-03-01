
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.player.Equalizer;
import uk.co.caprica.vlcj.player.MediaMetaData;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

public class Media {

	private EmbeddedMediaPlayer mediaPlayer;
	private boolean stopped;
	private Equalizer eq;
	private MediaPlayerFactory mediaPlayerFactory;

	private Image playpauseimg;

	private LinkedList<File> list = new LinkedList<File>();
	private int listP = 0;
	private boolean pLoop;
	private boolean sLoop;

	private Canvas c;
	private Object[] videoEffectValues = new Object[6];
	private boolean videoEffectToggle;
	private boolean albumArt = false;

	private eqPresetManager presets;

	private Dimension pSize;
	private Dimension cSize;

	private JFrame frame;

	private JButton loopbutton;

	public Media() {
		pLoop = false;
		sLoop = false;
		stopped = false;
		mediaPlayerFactory = new MediaPlayerFactory();
		openFile();
		equalizer();
	}

	private void openFile() { // first open method
		JFileChooser j = new JFileChooser();
		j.setMultiSelectionEnabled(true);
		j.setFileFilter(new FileNameExtensionFilter("Media Files",
				// audio formats
				"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob", "ape",
				"au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp", "mod",
				"mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra", "rmi",
				"s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm",
				// video formats
				"3g2", "3gp", "3gp2", "3gpp", "amv", "asf", "avi", "bik", "bin", "divx", "drc", "dv", "evo", "f4v",
				"flv", "gvi", "gxf", "iso", "m1v", "m2t", "m2ts", "m2v", "m4v", "mkv", "mov", "mp2", "mp2v", "mp4",
				"mp4v", "mpe", "mpeg", "mpeg1", "mpeg2", "mpeg4", "mpg", "mpv2", "mts", "mtv", "mxf", "mxg", "nsv",
				"nuv", "ogg", "ogm", "ogv", "ogx", "ps", "rec", "rm", "rmvb", "rpl", "thp", "tod", "ts", "tts", "txd",
				"vob", "vro", "webm", "wm", "wmv", "wtv", "xesc"));
		j.showOpenDialog(null);
		File[] theList = j.getSelectedFiles();
		for (int i = 0; i < theList.length; i++) {
			list.add(theList[i]);
		}
	}

	private void equalizer() {
		eq = new Equalizer(10);
		for (int i = 0; i < 10; i++) {
			eq.setAmp(i, 0);
		}

	}

	private boolean isAudio() {

		boolean isAudio = false;
		String[] audio = { "3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob",
				"ape", "au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp",
				"mod", "mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra",
				"rmi", "s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm" };
		String fileName = list.get(listP).getPath();
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

	public void getVideo(Dimension dim, boolean demo) throws IOException {
		String title = "" + list.get(listP).getName() + " - Media Player";
		if (demo) {
			title = "Demonstration Version - Media Player";
		}
		frame = new JFrame(title);

		JMenuBar main = new JMenuBar();
		WrapLayout layout = new WrapLayout(WrapLayout.LEFT, 0, 0);
		main.setLayout(layout);
		JMenu fileMenu = new JMenu("Media");
		JMenuItem openFile = new JMenuItem("Open from file (CTRL+O)");
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSingle();
				listP++;
				mediaPlayer.stop();
				mediaPlayer.playMedia(list.get(listP).getPath());
			}

		});
		fileMenu.add(openFile);

		JMenuItem openMultiFiles = new JMenuItem("Open multiple files (CTRL+SHIFT+O)");
		openMultiFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openMultiple(false);
				mediaPlayer.stop();
				mediaPlayer.playMedia(list.get(listP).getPath());
			}
		});
		fileMenu.add(openMultiFiles);

		JMenuItem close = new JMenuItem("Quit (CTRL+Q)");
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

				JPanel veP = new JPanel(new GridLayout(0, 5));

				JPanel bP = new JPanel(new BorderLayout());
				bP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel bL = new JLabel("Brightness");
				JLabel bT = new JLabel("Currently: " + (int) (mediaPlayer.getBrightness() * 10));
				int bInt = (int) ((double) mediaPlayer.getBrightness() * 10);
				JSlider bS = new JSlider(JSlider.VERTICAL, 0, 20, bInt);
				bS.setMajorTickSpacing(1);
				bS.setPaintTicks(true);
				bS.setPaintLabels(true);
				ChangeListener bC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float bF = (float) theSlider.getValue() / 10;
							mediaPlayer.setBrightness(bF);
							bT.setText("Currently: " + (int) (mediaPlayer.getBrightness() * 10));
							videoEffectValues[0] = bF;
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
				JLabel cT = new JLabel("Currently: " + (int) (mediaPlayer.getBrightness() * 10));
				int cInt = (int) ((double) mediaPlayer.getContrast() * 10);
				JSlider cS = new JSlider(JSlider.VERTICAL, 0, 20, cInt);
				cS.setMajorTickSpacing(1);
				cS.setPaintTicks(true);
				cS.setPaintLabels(true);
				ChangeListener cC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float cF = (float) theSlider.getValue() / 10;
							mediaPlayer.setContrast(cF);
							cT.setText("Currently: " + (int) (mediaPlayer.getContrast() * 10));
							videoEffectValues[1] = cF;
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
				JSlider hS = new JSlider(JSlider.VERTICAL, -180, 180, hInt);
				hS.setMajorTickSpacing(45);
				hS.setPaintTicks(true);
				hS.setPaintLabels(true);
				ChangeListener hC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							int hF = theSlider.getValue() + 180;
							mediaPlayer.setHue(hF);
							hT.setText("Currently: " + (int) (mediaPlayer.getHue()) + "°");
							videoEffectValues[2] = hF;
						}
					}
				};
				hS.addChangeListener(hC);
				hP.add(hL, BorderLayout.PAGE_START);
				hP.add(hS, BorderLayout.CENTER);
				hP.add(hT, BorderLayout.PAGE_END);
				veP.add(hP);

				JPanel sP = new JPanel(new BorderLayout());
				sP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel sL = new JLabel("Saturation");
				JLabel sT = new JLabel("Currently: " + (int) (mediaPlayer.getSaturation() * 10));
				int sInt = (int) (mediaPlayer.getSaturation() * 10);
				JSlider sS = new JSlider(JSlider.VERTICAL, 0, 30, sInt);
				sS.setMajorTickSpacing(2);
				sS.setPaintTicks(true);
				sS.setPaintLabels(true);
				ChangeListener sC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float sF = (float) (theSlider.getValue() / 10.0);
							mediaPlayer.setSaturation(sF);
							sT.setText("Currently: " + (int) (mediaPlayer.getSaturation() * 10));
							videoEffectValues[3] = sF;
						}
					}
				};
				sS.addChangeListener(sC);
				sP.add(sL, BorderLayout.PAGE_START);
				sP.add(sS, BorderLayout.CENTER);
				sP.add(sT, BorderLayout.PAGE_END);
				veP.add(sP);

				// gamma is not working properly

				JPanel gP = new JPanel(new BorderLayout());
				gP.setBorder(new EmptyBorder(10, 10, 10, 10));
				JLabel gL = new JLabel("Gamma");
				JLabel gT = new JLabel("Currently: " + (int) (mediaPlayer.getGamma() * 100));
				int gInt = (int) (mediaPlayer.getGamma() * 100);
				JSlider gS = new JSlider(JSlider.VERTICAL, 0, 1000, gInt);
				gS.setMajorTickSpacing(10);
				gS.setPaintTicks(true);
				gS.setPaintLabels(true);
				ChangeListener gC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float gF = (float) (theSlider.getValue() / 100.0);
							mediaPlayer.setGamma(gF);
							gT.setText("Currently: " + (int) (mediaPlayer.getGamma() * 100));
							videoEffectValues[4] = gF;
						}
					}
				};
				gS.addChangeListener(gC);
				gP.add(gL, BorderLayout.PAGE_START);
				gP.add(gS, BorderLayout.CENTER);
				gP.add(gT, BorderLayout.PAGE_END);
				// veP.add(gP);

				JPanel veT = new JPanel();
				JCheckBox toggle = new JCheckBox("Enable");
				toggle.setSelected(videoEffectToggle);
				toggle.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == 1) {
							mediaPlayer.setAdjustVideo(true);
							videoEffectToggle = true;
						} else {
							mediaPlayer.setAdjustVideo(false);
							videoEffectToggle = false;
						}
					}
				});
				veT.add(toggle);
				veT.setSize(600, 20);

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.weightx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;

				JFrame veF = new JFrame("Video Effects");
				veF.setLayout(new GridBagLayout());
				veP.setBorder(new EmptyBorder(10, 10, 10, 10));
				veF.add(veT, gbc);
				gbc.weighty = 2;
				veF.add(veP, gbc);

				veF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				veF.setSize(new Dimension(600, 350));
				veF.setResizable(false);
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
					try {
						BufferedImage artB = mediaPlayer.getMediaMeta().getArtwork();
						Image art = artB;
						art = art.getScaledInstance(87, 87, 0);
						JLabel albumArt = new JLabel(new ImageIcon(art));
						albumArtPanel.add(albumArt);
					} catch (NullPointerException f) {
						try {
							BufferedImage artB = ImageIO.read(new File("img\\logoTapes.png"));
							Image art = artB;
							art = art.getScaledInstance(87, 87, 0);
							JLabel albumArt = new JLabel(new ImageIcon(art));
							albumArtPanel.add(albumArt);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

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

			private JSlider preamp;
			private JSlider eq1;
			private JSlider eq2;
			private JSlider eq3;
			private JSlider eq4;
			private JSlider eq5;
			private JSlider eq6;
			private JSlider eq7;
			private JSlider eq8;
			private JSlider eq9;
			private JSlider eq10;

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
				preamp = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[0]));
				preamp.setMajorTickSpacing(4);
				preamp.setPaintTicks(true);
				preamp.setPaintLabels(true);
				ChangeListener preampC = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[0] = LibVlcConst.MIN_GAIN;
							} else {
								values[0] = newValue;
							}
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
				eq1 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[1]));
				eq1.setMajorTickSpacing(4);
				eq1.setPaintTicks(true);
				eq1.setPaintLabels(true);
				ChangeListener eq1C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[1] = LibVlcConst.MIN_GAIN;
							} else {
								values[1] = newValue;
							}
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
				eq2 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[2]));
				eq2.setMajorTickSpacing(4);
				eq2.setPaintTicks(true);
				eq2.setPaintLabels(true);
				ChangeListener eq2C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[2] = LibVlcConst.MIN_GAIN;
							} else {
								values[2] = newValue;
							}
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
				eq3 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[3]));
				eq3.setMajorTickSpacing(4);
				eq3.setPaintTicks(true);
				eq3.setPaintLabels(true);
				ChangeListener eq3C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[3] = LibVlcConst.MIN_GAIN;
							} else {
								values[3] = newValue;
							}
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
				eq4 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[4]));
				eq4.setMajorTickSpacing(4);
				eq4.setPaintTicks(true);
				eq4.setPaintLabels(true);
				ChangeListener eq4C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[4] = LibVlcConst.MIN_GAIN;
							} else {
								values[4] = newValue;
							}
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
				eq5 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[5]));
				eq5.setMajorTickSpacing(4);
				eq5.setPaintTicks(true);
				eq5.setPaintLabels(true);
				ChangeListener eq5C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[5] = LibVlcConst.MIN_GAIN;
							} else {
								values[5] = newValue;
							}
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
				eq6 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[6]));
				eq6.setMajorTickSpacing(4);
				eq6.setPaintTicks(true);
				eq6.setPaintLabels(true);
				ChangeListener eq6C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[6] = LibVlcConst.MIN_GAIN;
							} else {
								values[6] = newValue;
							}
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
				eq7 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[7]));
				eq7.setMajorTickSpacing(4);
				eq7.setPaintTicks(true);
				eq7.setPaintLabels(true);
				ChangeListener eq7C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[7] = LibVlcConst.MIN_GAIN;
							} else {
								values[7] = newValue;
							}
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
				eq8 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[8]));
				eq8.setMajorTickSpacing(4);
				eq8.setPaintTicks(true);
				eq8.setPaintLabels(true);
				ChangeListener eq8C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[8] = LibVlcConst.MIN_GAIN;
							} else {
								values[8] = newValue;
							}
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
				eq9 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[9]));
				eq9.setMajorTickSpacing(4);
				eq9.setPaintTicks(true);
				eq9.setPaintLabels(true);
				ChangeListener eq9C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[9] = LibVlcConst.MIN_GAIN;
							} else {
								values[9] = newValue;
							}
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
				eq10 = new JSlider(JSlider.VERTICAL, -20, 12, (int) Math.round(values[10]));
				eq10.setMajorTickSpacing(4);
				eq10.setPaintTicks(true);
				eq10.setPaintLabels(true);
				ChangeListener eq10C = new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							float newValue = (new Float(theSlider.getValue()));
							if (newValue == -20f) {
								values[10] = LibVlcConst.MIN_GAIN;
							} else {
								values[10] = newValue;
							}
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
				eqpanel.setBorder(new EmptyBorder(10, 10, 10, 10));

				JPanel top = new JPanel();

				presets = new eqPresetManager(mediaPlayerFactory.getAllPresetEqualizers());
				String[] names = presets.getAllNames();
				JComboBox<String> presetSel = new JComboBox<>(names);
				presetSel.setSelectedItem("Flat");

				presetSel.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String eqName = (String) ((JComboBox<String>) arg0.getSource()).getSelectedItem();
						Equalizer set = presets.getEqualizer(eqName);

						mediaPlayer.setEqualizer(set);
						values[0] = set.getPreamp();
						for (int i = 1; i < 11; i++) {
							if (mediaPlayer.getEqualizer().getAmp(i - 1) != 0.0f) {
								values[i] = set.getAmp(i - 1);
							} else {
								values[i] = 0.0f;
							}
						}

						preamp.setValue((int) Math.round(values[0]));
						eq1.setValue((int) Math.round(values[1]));
						eq2.setValue((int) Math.round(values[2]));
						eq3.setValue((int) Math.round(values[3]));
						eq4.setValue((int) Math.round(values[4]));
						eq5.setValue((int) Math.round(values[5]));
						eq6.setValue((int) Math.round(values[6]));
						eq7.setValue((int) Math.round(values[7]));
						eq8.setValue((int) Math.round(values[8]));
						eq9.setValue((int) Math.round(values[9]));
						eq10.setValue((int) Math.round(values[10]));
						presetSel.setSelectedItem(eqName);
					}

				});
				top.add(presetSel);

				JButton savePreset = new JButton("Save Preset");
				savePreset.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String name = JOptionPane.showInputDialog(null, "Enter Preset Name: ");
						presets.addPreset(name, values);
						presetSel.addItem(name);
					}

				});
				top.add(savePreset);

				top.setSize(600, 20);

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.weightx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;

				JFrame frameEQ = new JFrame("Equalizer");
				frameEQ.setLayout(new GridBagLayout());
				frameEQ.add(top, gbc);
				gbc.weighty = 2;
				frameEQ.add(eqpanel);
				frameEQ.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameEQ.setSize(new Dimension(800, 400));
				frameEQ.setResizable(false);
				frameEQ.setVisible(true);

			}

		});
		JMenu audioTrack = new JMenu("Audio Track");
		Timer delayAudioTrack = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (TrackDescription td : mediaPlayer.getAudioDescriptions()) {
					JMenuItem track = new JMenuItem(td.description());
					track.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mediaPlayer.setAudioTrack(td.id());
						}
					});
					audioTrack.add(track);
				}
			}
		});
		delayAudioTrack.setRepeats(false);
		delayAudioTrack.start();

		audioSettings.add(audioTrack);
		audioSettings.add(mute);
		audioSettings.add(equalizer);
		audioSettings.add(audioInfo);

		JMenu lMenu = new JMenu("Playlist");
		JMenuItem plLoop = new JMenuItem("Enable playlist looping");
		plLoop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!pLoop) {
						sLoop = false;
						pLoop = true;
						Image loopimg = ImageIO.read(new File("img\\loopAll.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
						plLoop.setText("Disable playlist looping");
						pLoop = true;
					} else {
						Image loopimg = ImageIO.read(new File("img\\Loop.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
						plLoop.setText("Enable playlist looping");
						pLoop = false;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		JMenuItem siLoop = new JMenuItem("Enable Single Track looping");
		siLoop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!mediaPlayer.getRepeat()) {
						siLoop.setText("Disable Single Track looping");
						pLoop = false;
						sLoop = true;
						Image loopimg = ImageIO.read(new File("img\\loopS.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
						mediaPlayer.setRepeat(true);
					} else {
						siLoop.setText("Enable Single Track looping");
						mediaPlayer.setRepeat(false);
						sLoop = false;
						Image loopimg = ImageIO.read(new File("img\\Loop.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
						siLoop.setText("Enable playlist looping");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		JMenuItem topAdd = new JMenuItem("Add tracks to top");
		topAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMultiple(true);
			}
		});
		JMenuItem endAdd = new JMenuItem("Add tracks to end");
		endAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMultiple(false);
			}
		});
		JMenuItem topRemove = new JMenuItem("Remove first track");
		topRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeFirst();
			}
		});
		JMenuItem endRemove = new JMenuItem("Remove last track");
		endRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeLast();
			}
		});
		JMenuItem removeSel = new JMenuItem("Remove a track");
		removeSel.addActionListener(new ActionListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void actionPerformed(ActionEvent e) {
				String[] tracks = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					tracks[i] = list.get(i).getName();
				}
				JComboBox comboBox = new JComboBox(tracks);
				comboBox.setSelectedIndex(0);
				JOptionPane.showMessageDialog(null, comboBox, "Select a track to remove", JOptionPane.QUESTION_MESSAGE);
				String selected = (String) comboBox.getSelectedItem();

				File selectedFile = null;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getName().equals(selected)) {
						selectedFile = list.get(i);
					}
				}
				list.removeFirstOccurrence(selectedFile);
			}
		});
		JMenuItem playlistPanel = new JMenuItem("Panel");
		playlistPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame listF = new JFrame("Playlist");
				JPanel table = new JPanel(new BorderLayout());
				JLabel tLabel1 = new JLabel("Playlist:");
				tLabel1.setSize(300, 50);
				table.add(tLabel1, BorderLayout.PAGE_START);
				String[] header = { "Number", "Track" };
				Object[][] tracks = new Object[list.size()][2];
				for (int i = 0; i < list.size(); i++) {
					tracks[i][0] = i + 1;
					tracks[i][1] = list.get(i).getName();
				}
				JTable pTable = new JTable(tracks, header);
				table.add(pTable, BorderLayout.CENTER);
				JLabel tLabel2 = new JLabel("Currently playing: " + list.get(listP).getName());
				tLabel2.setSize(300, 50);
				table.add(tLabel2, BorderLayout.PAGE_END);

				JPanel buttons = new JPanel(new GridLayout());
				try {
					JButton pLoopButton = new JButton();
					Image loopimg = ImageIO.read(new File("img\\Loop.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					pLoopButton.setIcon(new ImageIcon(loopimg));
					pLoopButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								if (!pLoop && !sLoop) { // playlist loop
									pLoop = true;
									Image loopimg = ImageIO.read(new File("img\\loopAll.png"));
									loopimg = loopimg.getScaledInstance(18, 18, 0);
									loopbutton.setIcon(new ImageIcon(loopimg));
									pLoopButton.setIcon(new ImageIcon(loopimg));
								} else if (pLoop && !sLoop) { // single loop
									pLoop = false;
									sLoop = true;
									Image loopimg = ImageIO.read(new File("img\\loopS.png"));
									loopimg = loopimg.getScaledInstance(18, 18, 0);
									loopbutton.setIcon(new ImageIcon(loopimg));
									pLoopButton.setIcon(new ImageIcon(loopimg));
									mediaPlayer.setRepeat(true);
								} else if (!pLoop && sLoop) { // no loop
									sLoop = false;
									mediaPlayer.setRepeat(false);
									Image loopimg = ImageIO.read(new File("img\\Loop.png"));
									loopimg = loopimg.getScaledInstance(18, 18, 0);
									loopbutton.setIcon(new ImageIcon(loopimg));
									pLoopButton.setIcon(new ImageIcon(loopimg));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					buttons.add(pLoopButton);

					JButton topAddButton = new JButton();
					Image topAddButtonImg = ImageIO.read(new File("img\\addtop.png"));
					topAddButtonImg = topAddButtonImg.getScaledInstance(18, 18, 0);
					topAddButton.setIcon(new ImageIcon(topAddButtonImg));
					topAddButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							openMultiple(true);
						}
					});
					buttons.add(topAddButton);

					JButton endAddButton = new JButton();
					Image endAddButtonImg = ImageIO.read(new File("img\\addend.png"));
					endAddButtonImg = endAddButtonImg.getScaledInstance(18, 18, 0);
					endAddButton.setIcon(new ImageIcon(endAddButtonImg));
					endAddButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							openMultiple(false);
						}
					});
					buttons.add(endAddButton);

					JButton topRemoveButton = new JButton();
					Image topRemoveButtonImg = ImageIO.read(new File("img\\removetop.png"));
					topRemoveButtonImg = topRemoveButtonImg.getScaledInstance(18, 18, 0);
					topRemoveButton.setIcon(new ImageIcon(topRemoveButtonImg));
					topRemoveButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							list.removeFirst();
						}
					});
					buttons.add(topRemoveButton);

					JButton endRemoveButton = new JButton("Remove Last File");
					Image endRemoveButtonImg = ImageIO.read(new File("img\\removeend.png"));
					endRemoveButtonImg = endRemoveButtonImg.getScaledInstance(18, 18, 0);
					endRemoveButton.setIcon(new ImageIcon(endRemoveButtonImg));
					endRemoveButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							list.removeLast();
						}
					});
					buttons.add(endRemoveButton);

					JButton removeButton = new JButton("Remove a track");
					removeButton.addActionListener(new ActionListener() {
						@SuppressWarnings({ "rawtypes", "unchecked" })
						public void actionPerformed(ActionEvent e) {
							String[] tracks = new String[list.size()];
							for (int i = 0; i < list.size(); i++) {
								tracks[i] = list.get(i).getName();
							}
							JComboBox comboBox = new JComboBox(tracks);
							comboBox.setSelectedIndex(0);
							JOptionPane.showMessageDialog(null, comboBox, "Select a track to remove",
									JOptionPane.QUESTION_MESSAGE);
							String selected = (String) comboBox.getSelectedItem();

							File selectedFile = null;
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).getName().equals(selected)) {
									selectedFile = list.get(i);
								}
							}
							list.removeFirstOccurrence(selectedFile);
						}
					});
					buttons.add(removeButton);
				} catch (IOException r) {
					// yeah
				}

				table.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
				buttons.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

				listF.setLayout(new BorderLayout());
				listF.add(buttons, BorderLayout.PAGE_START);
				listF.add(table, BorderLayout.PAGE_END);

				int height = 135 + (list.size() * 15);
				listF.setSize(400, height);

				listF.setVisible(true);
			}
		});
		lMenu.add(playlistPanel);

		lMenu.add(plLoop);
		lMenu.add(siLoop);
		lMenu.add(topAdd);
		lMenu.add(endAdd);
		lMenu.add(topRemove);
		lMenu.add(endRemove);
		lMenu.add(removeSel);

		main.add(audioSettings);
		main.add(lMenu);

		JMenu subtitle = new JMenu("Subtitle");
		JMenu subtrack = new JMenu("Subtrack");

		Timer subtracks = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (TrackDescription sub : mediaPlayer.getSpuDescriptions()) {
					JMenuItem temp = new JMenuItem(sub.description());
					temp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mediaPlayer.setSpu(sub.id());
						}
					});
					subtrack.add(temp);
				}
				subtitle.add(subtrack);
			}
		});
		subtracks.setRepeats(false);
		subtracks.start();

		main.add(subtitle);

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

		EmbeddedMediaPlayer fullScreenOperation = mediaPlayerFactory
				.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));

		pSize = p.getSize();
		cSize = c.getSize();

		JPanel p0 = new JPanel();

		JLabel posTime = new JLabel("0:00");
		JLabel lenTime = new JLabel("0:00");

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

		Timer timeUpdate = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int getSliderValue = (int) (mediaPlayer.getPosition() * 100);
				slider.setValue(getSliderValue);
				String position = "";
				int seconds = (int) (mediaPlayer.getTime() / 1000);
				int minutes = 0;
				if (seconds >= 60) {
					minutes = seconds / 60;
					seconds %= 60;
				}
				if (seconds < 10) {
					position = "" + minutes + ":0" + seconds;
				} else {
					position = "" + minutes + ":" + seconds;
				}
				posTime.setText("" + position);

				String length = "";
				seconds = (int) (mediaPlayer.getLength() / 1000);
				minutes = 0;
				if (seconds >= 60) {
					minutes = seconds / 60;
					seconds %= 60;
				}
				if (seconds < 10) {
					length = "" + minutes + ":0" + seconds;
				} else {
					length = "" + minutes + ":" + seconds;
				}
				lenTime.setText(length);

			}
		});
		timeUpdate.start();

		p0.add(posTime);
		p0.add(slider);
		p0.add(lenTime);

		p0.setBounds(100, 900, 105, 200);
		frame.add(p0, BorderLayout.CENTER);

		JPanel p1 = new JPanel();

		Timer stop = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.pause();
			}
		});

		p1.setBounds(100, 900, 105, 200);
		frame.add(p1, BorderLayout.SOUTH);
		JButton playbutton = new JButton();
		Image playimg = ImageIO.read(new File("img\\Play.gif"));
		playbutton.setIcon(new ImageIcon(playimg));
		playbutton.setBounds(50, 50, 150, 100);
		playbutton.setBackground(Color.LIGHT_GRAY);
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
		playbutton.setFocusable(false);
		// p1.add(playbutton);

		JButton pausebutton = new JButton();
		Image pauseimg = ImageIO.read(new File("img\\Pause.png"));
		pausebutton.setIcon(new ImageIcon(pauseimg));
		pausebutton.setBounds(80, 50, 150, 100);
		pausebutton.setBackground(Color.LIGHT_GRAY);
		pausebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.pause();
			}
		});
		pausebutton.setFocusable(false);
		// p1.add(pausebutton);

		JButton playpause = new JButton();
		playpauseimg = ImageIO.read(new File("img\\Pause.png"));
		playpause.setIcon(new ImageIcon(playpauseimg));
		playpause.setBounds(80, 50, 150, 100);
		playpause.setBackground(Color.LIGHT_GRAY);
		playpause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Image playpauseimg = null;
					if (mediaPlayer.isPlaying() && !stopped) {
						mediaPlayer.pause();
						playpauseimg = ImageIO.read(new File("img\\Play.gif"));
						playpause.setIcon(new ImageIcon(playpauseimg));
					} else if (!mediaPlayer.isPlaying() && !stopped) {
						mediaPlayer.pause();
						playpauseimg = ImageIO.read(new File("img\\Pause.png"));
						playpause.setIcon(new ImageIcon(playpauseimg));
					} else if (stopped) {
						mediaPlayer.pause();
						playpauseimg = ImageIO.read(new File("img\\Pause.png"));
						playpause.setIcon(new ImageIcon(playpauseimg));
						stop.stop();
						stopped = false;
						mediaPlayer.play();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		playpause.setFocusable(false);
		p1.add(playpause);

		JButton stopbutton = new JButton();
		Image stopimg = ImageIO.read(new File("img\\Stop.gif"));
		stopbutton.setIcon(new ImageIcon(stopimg));
		stopbutton.setBounds(80, 50, 150, 100);
		stopbutton.setBackground(Color.LIGHT_GRAY);
		stopbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					playpauseimg = ImageIO.read(new File("img\\Play.gif"));
					playpause.setIcon(new ImageIcon(playpauseimg));
					mediaPlayer.stop();
					stop.start();
					stopped = true;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		stopbutton.setFocusable(false);
		p1.add(stopbutton);

		loopbutton = new JButton();
		Image loopimg = ImageIO.read(new File("img\\Loop.png"));
		loopimg = loopimg.getScaledInstance(18, 18, 0);
		loopbutton.setIcon(new ImageIcon(loopimg));
		loopbutton.setBounds(80, 50, 150, 100);
		loopbutton.setBackground(Color.LIGHT_GRAY);
		loopbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!pLoop && !sLoop) { // playlist loop
						pLoop = true;
						Image loopimg = ImageIO.read(new File("img\\loopAll.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
					} else if (pLoop && !sLoop) { // single loop
						pLoop = false;
						sLoop = true;
						Image loopimg = ImageIO.read(new File("img\\loopS.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
						mediaPlayer.setRepeat(true);
					} else if (!pLoop && sLoop) { // no loop
						sLoop = false;
						mediaPlayer.setRepeat(false);
						Image loopimg = ImageIO.read(new File("img\\Loop.png"));
						loopimg = loopimg.getScaledInstance(18, 18, 0);
						loopbutton.setIcon(new ImageIcon(loopimg));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		loopbutton.setFocusable(false);
		p1.add(loopbutton);

		JButton mutebutton = new JButton();
		Image muteimg = ImageIO.read(new File("img\\Mute.png"));
		muteimg = muteimg.getScaledInstance(18, 18, 0);
		mutebutton.setIcon(new ImageIcon(muteimg));
		mutebutton.setBounds(80, 50, 150, 100);
		mutebutton.setBackground(Color.LIGHT_GRAY);
		mutebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mediaPlayer.isMute()) {
					mediaPlayer.mute(true);
					mutebutton.setBackground(Color.GRAY);
				} else {
					mediaPlayer.mute(false);
					mutebutton.setBackground(Color.LIGHT_GRAY);
				}
			}
		});
		mutebutton.setFocusable(false);
		p1.add(mutebutton);

		String vLabel = "N/A";
		if (mediaPlayer.getVolume() < 100) {
			if (mediaPlayer.getVolume() < 10) {
				vLabel = "" + mediaPlayer.getVolume() + "%   ";
			} else {
				vLabel = "" + mediaPlayer.getVolume() + "%  ";
			}
		} else {
			vLabel = "" + mediaPlayer.getVolume() + "%";
		}

		JLabel volumePercent = new JLabel(vLabel);
		JSlider volume = new JSlider(0, 200, mediaPlayer.getVolume());
		volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JSlider theSlider = (JSlider) changeEvent.getSource();
				mediaPlayer.setVolume(theSlider.getValue());
				String vLabel = "N/A";
				if (theSlider.getValue() < 100) {
					if (mediaPlayer.getVolume() < 10) {
						vLabel = "" + mediaPlayer.getVolume() + "%   ";
					} else {
						vLabel = "" + mediaPlayer.getVolume() + "%  ";
					}
					;
				} else {
					vLabel = "" + mediaPlayer.getVolume() + "%";
				}
				volumePercent.setText(vLabel);
			}
		});
		volume.setPreferredSize(new Dimension(70, 18));
		p1.add(volume);
		p1.add(volumePercent);

		JPanel fullscreenOverlay = p0;
		fullscreenOverlay.add(p1);

		c.setFocusable(false);
		p0.setFocusable(false);
		p1.setFocusable(false);
		p.setFocusable(false);
		volume.setFocusable(false);
		slider.setFocusable(false);
		fullscreenOverlay.setFocusable(false);

		int csizex = c.getSize().width / 2;
		int csizey = c.getSize().height;
		mediaPlayer.setMarqueeLocation((csizex / 2), (csizey - 60));
		// mediaPlayer.setMarqueeText("" + file.getPath());
		mediaPlayer.enableMarquee(true);
		Timer text = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.enableMarquee(false);
			}
		});
		text.setRepeats(false);
		text.start();
		frame.setFocusable(true);
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent k) {
				int key = k.getKeyCode();
				if (key == (KeyEvent.VK_F)) {
					System.out.println("Toggled fullscreen");

					if (!isAudio()) { // audio files should not be able to toggle fullscreen mode
						String marqueeText = "Toggled fullscreen";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mediaPlayer.enableMarquee(false);

							}
						});
						text.setRepeats(false);
						text.start();

						if (!fullScreenOperation.isFullScreen()) {
							frame.remove(p0);
							frame.remove(p1);
							frame.setJMenuBar(null);
							p.setSize(dim);
							Dimension newCSizeY = new Dimension(dim.width, dim.height - 50);
							c.setSize(newCSizeY);
							p.add(fullscreenOverlay, BorderLayout.SOUTH);
							fullScreenOperation.toggleFullScreen();
						} else {
							fullScreenOperation.toggleFullScreen();
							frame.remove(fullscreenOverlay);
							frame.add(p0, BorderLayout.CENTER);
							frame.add(p1, BorderLayout.SOUTH);
							frame.setJMenuBar(main);
							p.setSize(pSize);
							c.setSize(cSize);
							p.setBounds(100, 50, 1050, 600);
							c.setBounds(100, 50, 1050, 500);
							frame.repaint();
						}
					}

				} else if (key == (KeyEvent.VK_ESCAPE)) {
					System.out.println("Leave fullscreen/close dialogue");

					if (fullScreenOperation.isFullScreen()) {
						fullScreenOperation.toggleFullScreen();
						frame.add(p0, BorderLayout.CENTER);
						frame.add(p1, BorderLayout.SOUTH);
						frame.setJMenuBar(main);
						p.setSize(pSize);
						c.setSize(cSize);
						p.setBounds(100, 50, 1050, 600);
						c.setBounds(100, 50, 1050, 500);
						frame.repaint();
					}
				} else if (key == (KeyEvent.VK_SPACE)) {
					System.out.println("Play/pause");

					String marqueeText;
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
						marqueeText = "Paused";
					} else {
						mediaPlayer.play();
						marqueeText = "Playing";
					}

					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_PLUS)) {
					System.out.println("Faster");

					mediaPlayer.setRate(mediaPlayer.getRate() + 0.1f);
					String marqueeText = "Speed: " + mediaPlayer.getRate() + "%";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_MINUS)) {
					System.out.println("Slower");

					mediaPlayer.setRate(mediaPlayer.getRate() - 0.1f);
					String marqueeText = "Speed: " + mediaPlayer.getRate() + "%";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_EQUALS)) {
					System.out.println("Faster");

					mediaPlayer.setRate(mediaPlayer.getRate() + 0.1f);
					String marqueeText = "Speed: " + mediaPlayer.getRate() + "%";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_N)) {
					System.out.println("Next Track");

					String marqueeText = "Next";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();

					next();

				} else if (key == (KeyEvent.VK_P)) {
					System.out.println("Previus track");

					String marqueeText = "Prev.";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();

					previous();

				} else if (key == (KeyEvent.VK_S)) {
					System.out.println("Stop");

					String marqueeText = "Stopped";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
					mediaPlayer.stop();

				} else if (key == (KeyEvent.VK_UP)) {
					System.out.println("Volume Up");
					int newVol = mediaPlayer.getVolume() + 10;
					if (newVol <= 200) {

						String marqueeText = "Volume Up";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mediaPlayer.enableMarquee(false);

							}
						});
						text.setRepeats(false);
						text.start();

						mediaPlayer.setVolume(newVol);
						String vLabel = "N/A";
						if (mediaPlayer.getVolume() < 100) {
							if (mediaPlayer.getVolume() < 10) {
								vLabel = "" + newVol + "%   ";
							} else {
								vLabel = "" + newVol + "%  ";
							}
						} else {
							vLabel = "" + newVol + "%";
						}
						volumePercent.setText(vLabel);
						volume.setValue(newVol);
					}

				} else if (key == (KeyEvent.VK_DOWN)) {
					System.out.println("Volume Down");

					int newVol = (mediaPlayer.getVolume() - 10);
					if (newVol >= 0) {
						String marqueeText = "Volume Down";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mediaPlayer.enableMarquee(false);

							}
						});
						text.setRepeats(false);
						text.start();
						mediaPlayer.setVolume(newVol);
						String vLabel = "N/A";
						if (mediaPlayer.getVolume() < 100) {
							if (mediaPlayer.getVolume() < 10) {
								vLabel = "" + newVol + "%   ";
							} else {
								vLabel = "" + newVol + "%  ";
							}
						} else {
							vLabel = "" + newVol + "%";
						}
						volumePercent.setText(vLabel);
						volume.setValue(newVol);
					}
				} else if (key == (KeyEvent.VK_RIGHT)) {
					System.out.println("5% Dub ->");

					String marqueeText = "Dubbed >>";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
					mediaPlayer.setPosition(mediaPlayer.getPosition() + 0.05f);
				} else if (key == (KeyEvent.VK_LEFT)) {
					System.out.println("<- 5% Dub");

					String marqueeText = "<< Dubbed";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();

					mediaPlayer.setPosition(mediaPlayer.getPosition() - 0.05f);
				} else if (key == (KeyEvent.VK_M)) {
					System.out.println("Mute");

					String marqueeText = "";
					if (!mediaPlayer.isMute()) {
						mediaPlayer.mute(true);
						marqueeText = "Muted";
					} else {
						mediaPlayer.mute(false);
						marqueeText = "Unmuted";
					}

					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_E)) {
					System.out.println("Next Frame");

					mediaPlayer.nextFrame();
					String marqueeText = "Next Frame";
					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				} else if (key == (KeyEvent.VK_L)) {
					System.out.println("Toggle looping");

					String marqueeText = "";
					if (!mediaPlayer.getRepeat()) {
						mediaPlayer.setRepeat(true);
						marqueeText = "Looping On";
					} else {
						mediaPlayer.setRepeat(false);
						marqueeText = "Looping Off";
					}

					mediaPlayer.setMarqueeLocation((csizex - 15), (15));
					mediaPlayer.setMarqueeText("" + marqueeText);
					mediaPlayer.setMarqueeSize(22);
					mediaPlayer.enableMarquee(true);
					Timer text = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							mediaPlayer.enableMarquee(false);

						}
					});
					text.setRepeats(false);
					text.start();
				}
			}

			@Override
			public void keyReleased(KeyEvent k) {
			}

			@Override
			public void keyTyped(KeyEvent k) {
			}

		});

		frame.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				System.out.println(e.getWheelRotation());

				if (e.getWheelRotation() < 0) {
					int newVol = mediaPlayer.getVolume() + 5;
					if (!(newVol > 200)) {
						mediaPlayer.setVolume(newVol);

						String marqueeText = "" + newVol + "%";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mediaPlayer.enableMarquee(false);

							}
						});
						text.setRepeats(false);
						text.start();

						String vLabel = "N/A";
						if (mediaPlayer.getVolume() < 100) {
							if (mediaPlayer.getVolume() < 10) {
								vLabel = "" + mediaPlayer.getVolume() + "%   ";
							} else {
								vLabel = "" + mediaPlayer.getVolume() + "%  ";
							}
							;
						} else {
							vLabel = "" + mediaPlayer.getVolume() + "%";
						}
						volumePercent.setText(vLabel);
						volume.setValue(newVol);
					}
				} else {
					int newVol = mediaPlayer.getVolume() - 5;

					if (!(newVol < 0)) {
						mediaPlayer.setVolume(newVol);

						String marqueeText = "" + newVol + "%";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mediaPlayer.enableMarquee(false);

							}
						});
						text.setRepeats(false);
						text.start();

						String vLabel = "N/A";
						if (mediaPlayer.getVolume() < 100) {
							if (mediaPlayer.getVolume() < 10) {
								vLabel = "" + mediaPlayer.getVolume() + "%   ";
							} else {
								vLabel = "" + mediaPlayer.getVolume() + "%  ";
							}
							;
						} else {
							vLabel = "" + mediaPlayer.getVolume() + "%";
						}
						volumePercent.setText(vLabel);
						volume.setValue(newVol);
					}
				}
			}
		});

		frame.setSize(904, 645);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);

		mediaPlayer.playMedia(list.get(listP).getPath());
		mediaPlayer.setEqualizer(mediaPlayerFactory.getAllPresetEqualizers().get("Flat"));
		c.setBackground(Color.black);

		// Main logo (shown when stopped, no album art on audio).
		// The album art is displayed over the logo.
		// The timers are to delay the placement of each object.
		// The main logo to ensure it's drawn over the video object.
		// The album art to ensure the album art has loaded in (null pointer errors)

		Timer logoTimer = new Timer(200, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					BufferedImage before = ImageIO.read(new File("img\\logoTapes.png"));
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
		});
		logoTimer.start();
		if (!albumArt) {
			logoTimer.setRepeats(false);
		}

		if (isAudio()) {
			Timer artTimer = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
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
						albumArt = true;
					} catch (NullPointerException e) {
						// this only happens when there is no album art.
						// this isn't a program breaking exception.
					}
				}
			});
			artTimer.start();
		}
		volume.setFocusable(false);
		c.setFocusable(false);

		Timer listAdvance = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayer.getPosition() >= 0.99 && !sLoop) {
					next();
				}
			}
		});
		listAdvance.start();
	}

	private void next() {
		if (listP + 1 != list.size()) {
			listP++;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		} else if (pLoop) {
			listP = 0;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		}
	}

	private void previous() {
		if (listP - 1 != -1) {
			listP--;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		} else if (pLoop) {
			listP = list.size() - 1;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		}
	}

	private void openSingle() {
		JFileChooser j = new JFileChooser();
		j.setFileFilter(new FileNameExtensionFilter("Media Files",
				// audio formats
				"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob", "ape",
				"au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp", "mod",
				"mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra", "rmi",
				"s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm",
				// video formats
				"3g2", "3gp", "3gp2", "3gpp", "amv", "asf", "avi", "bik", "bin", "divx", "drc", "dv", "evo", "f4v",
				"flv", "gvi", "gxf", "iso", "m1v", "m2t", "m2ts", "m2v", "m4v", "mkv", "mov", "mp2", "mp2v", "mp4",
				"mp4v", "mpe", "mpeg", "mpeg1", "mpeg2", "mpeg4", "mpg", "mpv2", "mts", "mtv", "mxf", "mxg", "nsv",
				"nuv", "ogg", "ogm", "ogv", "ogx", "ps", "rec", "rm", "rmvb", "rpl", "thp", "tod", "ts", "tts", "txd",
				"vob", "vro", "webm", "wm", "wmv", "wtv", "xesc"));
		j.showOpenDialog(null);
		if (j.getSelectedFile().exists()) {
			list.add(j.getSelectedFile());
		}
	}

	private void openMultiple(boolean top) {
		JFileChooser j = new JFileChooser();
		j.setMultiSelectionEnabled(true);
		j.setFileFilter(new FileNameExtensionFilter("Media Files",
				// audio formats
				"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob", "ape",
				"au", "awb", "caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp", "mod",
				"mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra", "rmi",
				"s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm",
				// video formats
				"3g2", "3gp", "3gp2", "3gpp", "amv", "asf", "avi", "bik", "bin", "divx", "drc", "dv", "evo", "f4v",
				"flv", "gvi", "gxf", "iso", "m1v", "m2t", "m2ts", "m2v", "m4v", "mkv", "mov", "mp2", "mp2v", "mp4",
				"mp4v", "mpe", "mpeg", "mpeg1", "mpeg2", "mpeg4", "mpg", "mpv2", "mts", "mtv", "mxf", "mxg", "nsv",
				"nuv", "ogg", "ogm", "ogv", "ogx", "ps", "rec", "rm", "rmvb", "rpl", "thp", "tod", "ts", "tts", "txd",
				"vob", "vro", "webm", "wm", "wmv", "wtv", "xesc"));
		j.showOpenDialog(null);
		File[] theList = j.getSelectedFiles();
		if (!top) {
			for (int i = 0; i < theList.length; i++) {
				if (j.getSelectedFile().exists()) {
					list.add(j.getSelectedFile());
				}
			}
		} else {

		}
	}

}
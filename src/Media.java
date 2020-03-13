
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
import java.awt.FlowLayout;
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
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.Equalizer;
import uk.co.caprica.vlcj.player.MediaMetaData;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

public class Media {

	private Dimension dim;

	private EmbeddedMediaPlayer mediaPlayer;
	private boolean stopped;
	private Equalizer eq;
	private MediaPlayerFactory mediaPlayerFactory;

	private Image playpauseimg;

	private String currentSkin;
	private int playPauseState, loopCond, muteState;
	private JPanel p, p0, p1;
	private JSlider posSlider, volume;
	private JButton loopbutton;
	private JLabel posTime, lenTime, volumePercent;
	private JButton playpause, prevButton, stopbutton, nextButton, playlistButton, shuffleButton, mutebutton;

	private LinkedList<File> list = new LinkedList<File>();
	private int listP = 0;
	private boolean pLoop;
	private boolean sLoop;
	private boolean onShuffle;

	private Canvas c;
	private Object[] videoEffectValues = new Object[6];
	private boolean videoEffectToggle;
	private boolean albumArt = false;

	private eqPresetManager presets;

	private Dimension pSize;
	private Dimension cSize;

	private JFrame frame;

	public Media(boolean demo, Dimension d) {
		currentSkin = "default";
		dim = d;
		pLoop = false;
		sLoop = false;
		stopped = false;
		onShuffle = false;
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

	public void getVideo() throws IOException {
		try {
			String title = "" + list.get(listP).getName() + " - Media Player";
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
			JMenuItem openMultiFiles = new JMenuItem("Open multiple files (CTRL+SHIFT+O)");
			openMultiFiles.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openMultiple(false);
					mediaPlayer.stop();
					mediaPlayer.playMedia(list.get(listP).getPath());
				}
			});

			JMenuItem close = new JMenuItem("Quit (CTRL+Q)");
			close.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mediaPlayer.stop();
					frame.dispose();
				}

			});

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
						errorBox(e1, "Error loading icons.");
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
						errorBox(e1, "Error loading icons.");
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
			JMenuItem playlistPanel = new JMenuItem("Manage Playlist");
			playlistPanel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					playlistPanel();
				}
			});

			fileMenu.add(openFile);
			fileMenu.add(openMultiFiles);
			fileMenu.addSeparator();
			fileMenu.add(playlistPanel);
			fileMenu.add(topAdd);
			fileMenu.add(endAdd);
			fileMenu.add(topRemove);
			fileMenu.add(endRemove);
			fileMenu.addSeparator();
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

			JMenu vidTrack = new JMenu("Video Track");

			Timer vidTracks = new Timer(2000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (TrackDescription tra : mediaPlayer.getVideoDescriptions()) {
						JMenuItem temp = new JMenuItem(tra.description());
						temp.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								mediaPlayer.setVideoTrack(tra.id());
							}
						});
						vidTrack.add(temp);
					}
				}
			});

			vidTracks.setRepeats(false);
			vidTracks.start();

			JMenuItem fullscreenMenu = new JMenuItem("Fullscreen");
			JMenuItem screenshot = new JMenuItem("Take Screenshot");

			videoSettings.add(vidTrack);
			videoSettings.addSeparator();
			videoSettings.add(fullscreenMenu);
			videoSettings.addSeparator();
			videoSettings.add(videoEffects);
			videoSettings.addSeparator();
			videoSettings.add(screenshot);
			main.add(videoSettings);

			JMenu audioSettings = new JMenu("Audio");

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
					presetSel.setBackground(skinColorBG());
					presetSel.setForeground(skinColorFG());

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
					
					preamp.setBackground(skinColorBG());
					eq1.setBackground(skinColorBG());
					eq2.setBackground(skinColorBG());
					eq3.setBackground(skinColorBG());
					eq4.setBackground(skinColorBG());
					eq5.setBackground(skinColorBG());
					eq6.setBackground(skinColorBG());
					eq7.setBackground(skinColorBG());
					eq8.setBackground(skinColorBG());
					eq9.setBackground(skinColorBG());
					eq10.setBackground(skinColorBG());

					JButton savePreset = new JButton("Save Preset");
					savePreset.setBackground(skinButtonBG());
					savePreset.setForeground(skinColorFG());
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
					
					eqpanel.setBackground(skinColorBG());
					
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
			JMenuItem volUp = new JMenuItem("Increase Volume");
			JMenuItem volDown = new JMenuItem("Decrease Volume");

			delayAudioTrack.setRepeats(false);
			delayAudioTrack.start();

			audioSettings.add(audioTrack);
			audioSettings.addSeparator();
			audioSettings.add(equalizer);
			audioSettings.addSeparator();
			audioSettings.add(volUp);
			audioSettings.add(volDown);
			audioSettings.add(mute);

			main.add(audioSettings);

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
			p = new JPanel();
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

			p0 = new JPanel();

			posTime = new JLabel("0:00");
			lenTime = new JLabel("0:00");

			posSlider = new JSlider(0, 100, 0);
			posSlider.setPaintLabels(true);

			posSlider.setPreferredSize(new Dimension(600, 30));
			posSlider.addMouseListener(new MouseListener() {

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
					posSlider.setValue(getSliderValue);
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
			p0.add(posSlider);
			p0.add(lenTime);

			p0.setBounds(100, 900, 105, 200);
			frame.add(p0, BorderLayout.CENTER);

			p1 = new JPanel();

			Timer stop = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mediaPlayer.pause();
				}
			});

			p1.setBounds(100, 900, 105, 200);
			frame.add(p1, BorderLayout.SOUTH);

			playpause = new JButton();
			playpause.setBounds(80, 50, 150, 100);
			playPauseState = 0;
			playpause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						Image playpauseimg = null;
						if (mediaPlayer.isPlaying() && !stopped) {
							playPauseState = 1;
							mediaPlayer.pause();
							if (currentSkin.equals("Dark")) {
								playpauseimg = ImageIO.read(new File("img\\dark-play.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							} else if (currentSkin.equals("Default")) {
								playpauseimg = ImageIO.read(new File("img\\play.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							}
						} else if (!mediaPlayer.isPlaying() && !stopped) {
							playPauseState = 2;
							mediaPlayer.pause();
							if (currentSkin.equals("Dark")) {
								playpauseimg = ImageIO.read(new File("img\\dark-pause.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							} else if (currentSkin.equals("Default")) {
								playpauseimg = ImageIO.read(new File("img\\pause.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							}
						} else if (stopped) {
							playPauseState = 0;
							mediaPlayer.pause();
							if (currentSkin.equals("Dark")) {
								playpauseimg = ImageIO.read(new File("img\\dark-pause.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							} else if (currentSkin.equals("Default")) {
								playpauseimg = ImageIO.read(new File("img\\pause.png"));
								playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
								playpause.setIcon(new ImageIcon(playpauseimg));
							}
							stop.stop();
							stopped = false;
							mediaPlayer.play();
						}
					} catch (IOException e) {
						errorBox(e, "Error loading icons.");
					}
				}

			});
			playpause.setFocusable(false);
			p1.add(playpause, BorderLayout.LINE_START);

			JLabel spacer = new JLabel(" ");
			spacer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			p1.add(spacer, BorderLayout.LINE_START);

			prevButton = new JButton();
			prevButton.setBounds(80, 50, 150, 100);
			prevButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					previous();
				}
			});
			prevButton.setFocusable(false);
			p1.add(prevButton, BorderLayout.LINE_START);

			stopbutton = new JButton();
			stopbutton.setBounds(80, 50, 150, 100);
			stopbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						playpauseimg = ImageIO.read(new File("img\\play.png"));
						playpause.setIcon(new ImageIcon(playpauseimg));
						mediaPlayer.stop();
						stop.start();
						stopped = true;
					} catch (IOException e) {
						errorBox(e, "Error loading icons.");
					}

				}
			});
			stopbutton.setFocusable(false);
			p1.add(stopbutton, BorderLayout.LINE_START);

			nextButton = new JButton();
			nextButton.setBounds(80, 50, 150, 100);
			nextButton.setBackground(Color.LIGHT_GRAY);
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					next();
				}
			});
			nextButton.setFocusable(false);
			p1.add(nextButton, BorderLayout.LINE_START);

			p1.add(spacer, BorderLayout.LINE_START);

			playlistButton = new JButton();
			playlistButton.setBounds(80, 50, 150, 100);
			playlistButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					playlistPanel();
				}
			});
			playlistButton.setFocusable(false);
			p1.add(playlistButton, BorderLayout.LINE_START);

			loopbutton = new JButton();
			loopbutton.setBounds(80, 50, 150, 100);
			loopbutton.setBackground(Color.LIGHT_GRAY);
			loopCond = 0;
			loopbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (!pLoop && !sLoop) { // playlist loop
							loopCond = 1;
							pLoop = true;
							Image loopimg = ImageIO.read(new File("img\\loopall.png"));
							loopimg = loopimg.getScaledInstance(18, 18, 0);
							loopbutton.setIcon(new ImageIcon(loopimg));
						} else if (pLoop && !sLoop) { // single loop
							loopCond = 2;
							pLoop = false;
							sLoop = true;
							Image loopimg = ImageIO.read(new File("img\\loopsingle.png"));
							loopimg = loopimg.getScaledInstance(18, 18, 0);
							loopbutton.setIcon(new ImageIcon(loopimg));
							mediaPlayer.setRepeat(true);
						} else if (!pLoop && sLoop) { // no loop
							loopCond = 0;
							sLoop = false;
							mediaPlayer.setRepeat(false);
							Image loopimg = ImageIO.read(new File("img\\loop.png"));
							loopimg = loopimg.getScaledInstance(18, 18, 0);
							loopbutton.setIcon(new ImageIcon(loopimg));
						}
					} catch (IOException e) {
						errorBox(e, "Error loading icons.");
					}
				}
			});
			loopbutton.setFocusable(false);
			p1.add(loopbutton, BorderLayout.LINE_START);

			shuffleButton = new JButton();
			shuffleButton.setBounds(80, 50, 150, 100);
			shuffleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (!onShuffle) {
							onShuffle = true;
							Image shuffleOn = ImageIO.read(new File("img\\shuffleon.png"));
							shuffleOn = shuffleOn.getScaledInstance(18, 18, 0);
							shuffleButton.setIcon(new ImageIcon(shuffleOn));
						} else {
							onShuffle = false;
							Image shuffleOn = ImageIO.read(new File("img\\shuffle.png"));
							shuffleOn = shuffleOn.getScaledInstance(18, 18, 0);
							shuffleButton.setIcon(new ImageIcon(shuffleOn));
						}
					} catch (IOException q) {
						errorBox(q, "Error loading icons.");
					}
				}
			});
			shuffleButton.setFocusable(false);
			p1.add(shuffleButton, BorderLayout.LINE_START);

			mutebutton = new JButton();
			mutebutton.setBounds(80, 50, 150, 100);
			muteState = 0;
			mutebutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (!mediaPlayer.isMute()) {
							muteState = 1;
							mediaPlayer.mute(true);
							Image muteimg = ImageIO.read(new File("img\\muteon.png"));
							muteimg = muteimg.getScaledInstance(18, 18, 0);
							mutebutton.setIcon(new ImageIcon(muteimg));
						} else {
							muteState = 0;
							mediaPlayer.mute(false);
							if (currentSkin.equals("dark")) {
								Image muteimg = ImageIO.read(new File("img\\dark-mute.png"));
								muteimg = muteimg.getScaledInstance(18, 18, 0);
								mutebutton.setIcon(new ImageIcon(muteimg));
							} else if (currentSkin.equals("default")) {
								Image muteimg = ImageIO.read(new File("img\\mute.png"));
								muteimg = muteimg.getScaledInstance(18, 18, 0);
								mutebutton.setIcon(new ImageIcon(muteimg));
							}
						}
					} catch (IOException f) {
						errorBox(f, "Error loading icons.");
					}
				}
			});
			mutebutton.setFocusable(false);
			p1.add(mutebutton, BorderLayout.LINE_END);

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

			volumePercent = new JLabel(vLabel);
			volume = new JSlider(0, 200, mediaPlayer.getVolume());
			volume.addChangeListener(new ChangeListener() {
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
			posSlider.setFocusable(false);
			fullscreenOverlay.setFocusable(false);

			int csizex = (int) cSize.getWidth() - 100;
			int csizey = (int) cSize.getHeight() + 150;

			mediaPlayer.setMarqueeLocation((csizex / 2), csizey);
			mediaPlayer.setMarqueeText("" + list.get(listP).getName());
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
						errorBox(e, "Error loading icons.");
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

			JMenu tools = new JMenu("Tools");
			JMenuItem audioMeta = new JMenuItem("Audio Meta Info");
			audioMeta.addActionListener(new ActionListener() {

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
								errorBox(e1, "Error loading icons.");
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
						errorBox(new Exception(), "Not an audio format.");
					}
				}

			});
			tools.add(audioMeta);

			JMenuItem codec = new JMenuItem("Codec Info");
			codec.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame cF = new JFrame("Codec Information");
					List<TrackInfo> info = mediaPlayer.getTrackInfo();

					JPanel bigOne = new JPanel(new BorderLayout());
					JPanel content = new JPanel(new BorderLayout());

					JPanel codecInfo = new JPanel(new FlowLayout());
					for (TrackInfo in : info) {
						String type = in.toString().substring(0, 14);
						JPanel eachStream = new JPanel();

						JTextField defaultSize = new JTextField("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
						Dimension fSize = defaultSize.getSize();

						JLabel inTitle = new JLabel("Stream " + in.id() + ": ");
						JLabel filler = new JLabel("");
						eachStream.add(inTitle);
						eachStream.add(filler);

						JLabel inDescL = new JLabel("Description:  ");
						String inDescT = in.codecDescription();
						JTextField inDescF = new JTextField(inDescT, 20);
						inDescF.setEditable(false);
						inDescF.setSize(fSize);
						eachStream.add(inDescL);
						eachStream.add(inDescF);

						JLabel inLanL = new JLabel("Language: ");
						String inLanT = in.language();
						JTextField inLanF = new JTextField(inLanT, 20);
						inLanF.setEditable(false);
						inLanF.setSize(fSize);
						eachStream.add(inLanL);
						eachStream.add(inLanF);

						JLabel inTypeL = new JLabel("Type:  ");
						String inTypeT = type.substring(0, 5);
						JTextField inTypeF = new JTextField(inTypeT, 20);
						inTypeF.setEditable(false);
						inTypeF.setSize(fSize);
						eachStream.add(inTypeL);
						eachStream.add(inTypeF);

						if (type.equals("VideoTrackInfo")) {
							VideoTrackInfo vid = (VideoTrackInfo) in;
							JLabel inResL = new JLabel("Video Resolution: ");
							String inResT = "" + vid.width() + "x" + vid.height();
							JTextField inResF = new JTextField(inResT, 20);
							inResF.setEditable(false);
							inResF.setSize(fSize);
							eachStream.add(inResL);
							eachStream.add(inResF);

							JLabel inFPSL = new JLabel("Frame rate: ");
							String inFPST = "" + (vid.frameRate() / 1000) + "." + (vid.frameRate() % 1000);
							JTextField inFPSF = new JTextField(inFPST, 20);
							inFPSF.setEditable(false);
							inFPSF.setSize(fSize);
							eachStream.add(inFPSL);
							eachStream.add(inFPSF);

							eachStream.setLayout(new GridLayout(6, 2));

						} else if (type.equals("AudioTrackInfo")) {
							AudioTrackInfo aud = (AudioTrackInfo) in;
							JLabel inChanL = new JLabel("Channels: ");
							String channels = String.valueOf(aud.channels());
							if (aud.channels() == 1) {
								channels = "Mono";
							} else if (aud.channels() == 2) {
								channels = "Stereo";
							}
							JTextField inChanF = new JTextField(channels, 20);
							inChanF.setEditable(false);
							inChanF.setSize(fSize);
							eachStream.add(inChanL);
							eachStream.add(inChanF);

							JLabel inSamL = new JLabel("Sample Rate: ");
							String inSamT = "" + aud.rate() + " Hz";
							JTextField inSamF = new JTextField(inSamT, 20);
							inSamF.setEditable(false);
							inSamF.setSize(fSize);
							eachStream.add(inSamL);
							eachStream.add(inSamF);

							eachStream.setLayout(new GridLayout(6, 2));

						} else if (type.substring(0, 13).equals("TextTrackInfo")) {
							eachStream.setLayout(new GridLayout(4, 2));
						}

						codecInfo.add(eachStream);
					}

					content.add(codecInfo);

					JPanel lastLine = new JPanel(new BorderLayout());
					lastLine.add(new JLabel("Location: "), BorderLayout.LINE_START);
					JTextField location = new JTextField(list.get(listP).getPath());
					location.setEditable(false);
					lastLine.add(location, BorderLayout.CENTER);
					lastLine.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

					JPanel close = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					JButton closeButton = new JButton("Close");
					closeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							cF.dispose();
						}
					});
					close.add(closeButton);

					JScrollPane scroll = new JScrollPane(content);

					bigOne.add(scroll, BorderLayout.CENTER);
					bigOne.add(lastLine, BorderLayout.NORTH);
					bigOne.add(close, BorderLayout.SOUTH);
					cF.add(bigOne);

					cF.setSize(500, 270);
					cF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					cF.setLocation(dim.width / 2 - cF.getSize().width / 2, dim.height / 2 - cF.getSize().height / 2);
					cF.setResizable(false);
					cF.setVisible(true);
				}
			});
			tools.add(codec);

			JMenuItem appear = new JMenuItem("Appearance");
			appear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame look = new JFrame("Appearance");
					JPanel main = new JPanel(new BorderLayout());
					JPanel subMain = new JPanel(new BorderLayout());

					JPanel presets = new JPanel(new FlowLayout(FlowLayout.CENTER));
					String[] skinList = { "Default", "Dark" };
					JComboBox<String> skins = new JComboBox<String>(skinList);
					JButton applySkin = new JButton("Apply");
					applySkin.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							applySkinChange((String) skins.getSelectedItem());
						}
					});
					presets.add(skins);
					presets.add(applySkin);
					subMain.add(presets, BorderLayout.NORTH);

					JPanel close = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					JButton closeButton = new JButton("Close");
					closeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							look.dispose();
						}
					});
					close.add(closeButton);
					main.add(subMain, BorderLayout.CENTER);
					main.add(close, BorderLayout.SOUTH);

					look.add(main);
					look.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					look.setSize(new Dimension(400, 400));
					look.setVisible(true);
				}
			});
			tools.add(appear);

			main.add(tools);

			main.add(helpMenu);

			volUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int newVol = mediaPlayer.getVolume() + 10;
					if (newVol <= 200) {

						String marqueeText = "Volume Up";
						mediaPlayer.setMarqueeLocation((csizex - 15), (15));
						mediaPlayer.setMarqueeText("" + marqueeText);
						mediaPlayer.setMarqueeSize(22);
						mediaPlayer.enableMarquee(true);
						Timer text = new Timer(1000, new ActionListener() {
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
				}
			});
			volDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
				}
			});
			fullscreenMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
				}
			});

			screenshot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isAudio()) {
						// mediaPlayer.pause();
						int snapNumber = 1 + new File("snapshots\\").list().length;

						JTextField fileName = new JTextField("Snapshot" + snapNumber + ".png");
						JTextField width = new JTextField(String.valueOf(mediaPlayer.getVideoDimension().width));
						JTextField height = new JTextField(String.valueOf(mediaPlayer.getVideoDimension().height));

						JFrame snapshot = new JFrame("Save a snapshot");
						JPanel main = new JPanel(new BorderLayout());
						JPanel subMain = new JPanel(new BorderLayout());

						JPanel header = new JPanel();
						header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
						header.add(new JLabel("Save a snapshot from the video: "));
						JPanel subHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
						subHeader.add(new JLabel("Filename: "));
						subHeader.add(fileName);
						header.add(subHeader);
						header.setBorder(new EmptyBorder(10, 10, 10, 10));
						subMain.add(header, BorderLayout.NORTH);

						JPanel options = new JPanel();
						options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));

						JPanel pWidth = new JPanel(new FlowLayout(FlowLayout.LEFT));
						pWidth.add(new JLabel("Width: "));
						pWidth.add(width);
						options.add(pWidth);
						JPanel pHeight = new JPanel(new FlowLayout(FlowLayout.LEFT));
						pHeight.add(new JLabel("Height: "));
						pHeight.add(height);
						options.add(pHeight);

						try {
							JPanel playback = new JPanel();
							playback.setLayout(new BoxLayout(playback, BoxLayout.X_AXIS));

							JButton splaypause = new JButton();
							BufferedImage splaypauseimg = ImageIO.read(new File("img\\Pause.png"));
							splaypause.setIcon(new ImageIcon(splaypauseimg));
							splaypause.setBounds(80, 50, 150, 100);
							splaypause.setBackground(Color.LIGHT_GRAY);
							splaypause.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									try {
										Image splaypauseimg = null;
										if (mediaPlayer.isPlaying() && !stopped) {
											mediaPlayer.pause();
											splaypauseimg = ImageIO.read(new File("img\\Play.gif"));
											splaypause.setIcon(new ImageIcon(splaypauseimg));
										} else if (!mediaPlayer.isPlaying() && !stopped) {
											mediaPlayer.pause();
											splaypauseimg = ImageIO.read(new File("img\\Pause.png"));
											splaypause.setIcon(new ImageIcon(splaypauseimg));
										} else if (stopped) {
											mediaPlayer.pause();
											splaypauseimg = ImageIO.read(new File("img\\Pause.png"));
											splaypause.setIcon(new ImageIcon(splaypauseimg));
											stop.stop();
											stopped = false;
											mediaPlayer.play();
										}
									} catch (IOException e) {
										errorBox(e, "Error loading play/pause icons.");
									}
								}

							});
							playback.add(splaypause);

							JButton nextFrame = new JButton();
							Image nextFrameIcon = ImageIO.read(new File("img\\nextFrame.png"));
							nextFrameIcon = nextFrameIcon.getScaledInstance(18, 18, 0);
							nextFrame.setIcon(new ImageIcon(nextFrameIcon));
							nextFrame.setBounds(80, 50, 150, 100);
							nextFrame.setBackground(Color.LIGHT_GRAY);
							nextFrame.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									mediaPlayer.nextFrame();
								}
							});
							playback.add(nextFrame);

							options.add(playback);
						} catch (IOException f) {
							errorBox(f, "Error loading nextFrame icon.");
						}

						subMain.add(options, BorderLayout.CENTER);

						main.add(subMain, BorderLayout.CENTER);

						JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
						JButton close = new JButton("Close");
						close.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								snapshot.dispose();
							}
						});
						footer.add(close);
						JButton save = new JButton("Save");
						save.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									File snapFile = new File("snapshots\\" + fileName.getText());
									if (!snapFile.exists()) {
										snapFile.createNewFile();
									}
									mediaPlayer.saveSnapshot(snapFile, Integer.parseInt(width.getText()),
											Integer.parseInt(height.getText()));
								} catch (IOException f) {
									errorBox(f, "Error creating snapshot file");
								}
							}
						});
						footer.add(save);
						main.add(footer, BorderLayout.SOUTH);

						snapshot.add(main);
						snapshot.setSize(new Dimension(600, 250));
						snapshot.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						snapshot.setVisible(true);
					}
				}
			});

			frame.setJMenuBar(main);
			applySkinChange("Default");
		} catch (Exception e) {
			errorBox(e, "General Error");
		}
	}

	private void next() {
		if (listP + 1 != list.size() && !pLoop && !onShuffle) {
			listP++;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		} else if (pLoop && !onShuffle) {
			listP = 0;
			mediaPlayer.stop();
			mediaPlayer.playMedia(list.get(listP).getPath());

			String title = "" + list.get(listP).getName() + " - Media Player";
			frame.setTitle(title);
		} else if (onShuffle) {
			Random rand = new Random();
			listP = rand.nextInt(list.size());
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

	public void playlistPanel() {
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
						errorBox(e, "Error loading icons.");
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
			errorBox(r, "Error loading icons.");
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

	public void errorBox(Exception e, String spec) {
		try {

			if (spec.equals("General Error")) {
				spec = e.getMessage();
			} else {
				spec = spec + " (\"" + e.getMessage() + "\")";
			}

			JFrame error = new JFrame("An error occured");
			JPanel main = new JPanel(new BorderLayout());

			JPanel subMain = new JPanel(new BorderLayout());

			JPanel header = new JPanel(new BorderLayout());
			JLabel errorPic = new JLabel();
			Image errorImage = ImageIO.read(new File("img\\error.png"));
			errorImage = errorImage.getScaledInstance(64, 64, 0);
			errorPic.setIcon(new ImageIcon(errorImage));
			header.add(errorPic, BorderLayout.WEST);
			header.add(new JLabel("<html>An error occured:<br>" + spec + "</html>"), BorderLayout.CENTER);
			header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			subMain.add(header, BorderLayout.NORTH);

			JPanel stackTrace = new JPanel(new BorderLayout());
			stackTrace.add(new JLabel("Java Stacktrace: "), BorderLayout.NORTH);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JTextArea textArea = new JTextArea(sw.toString());
			JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			stackTrace.add(scroll, BorderLayout.CENTER);
			stackTrace.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			subMain.add(stackTrace, BorderLayout.CENTER);

			JPanel close = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton closeButton = new JButton("Close");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					error.dispose();
				}
			});
			close.add(closeButton);

			main.add(subMain, BorderLayout.CENTER);
			main.add(close, BorderLayout.SOUTH);

			error.add(main);
			error.setSize(new Dimension(600, 400));
			error.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			error.setLocation(dim.width / 2 - error.getSize().width / 2, dim.height / 2 - error.getSize().height / 2);
			error.setVisible(true);

		} catch (IOException f) {
			// error in error lmao
		}
	}

	public void applySkinChange(String skin) {
		currentSkin = skin;
		try {
			if (skin.equals("Dark")) {
				p.setBackground(Color.DARK_GRAY);
				p0.setBackground(Color.DARK_GRAY);
				p1.setBackground(Color.DARK_GRAY);
				posSlider.setBackground(Color.DARK_GRAY);
				posSlider.setForeground(Color.BLACK);
				lenTime.setForeground(Color.WHITE);
				posTime.setForeground(Color.WHITE);
				volume.setBackground(Color.DARK_GRAY);
				volume.setForeground(Color.WHITE);
				volumePercent.setForeground(Color.WHITE);

				playpause.setBackground(Color.BLACK);
				if (playPauseState == 0) {
					playpauseimg = ImageIO.read(new File("img\\dark-pause.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				} else if (playPauseState == 1) {
					playpauseimg = ImageIO.read(new File("img\\dark-play.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				} else if (playPauseState == 2) {
					playpauseimg = ImageIO.read(new File("img\\dark-pause.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				}

				prevButton.setBackground(Color.BLACK);
				Image prevIcon = ImageIO.read(new File("img\\dark-previoustrack.png"));
				prevIcon = prevIcon.getScaledInstance(18, 18, 0);
				prevButton.setIcon(new ImageIcon(prevIcon));
				stopbutton.setBackground(Color.BLACK);
				Image stopimg = ImageIO.read(new File("img\\dark-stop.png"));
				stopimg = stopimg.getScaledInstance(18, 18, 0);
				stopbutton.setIcon(new ImageIcon(stopimg));
				nextButton.setBackground(Color.BLACK);
				Image nextIcon = ImageIO.read(new File("img\\dark-nexttrack.png"));
				nextIcon = nextIcon.getScaledInstance(18, 18, 0);
				nextButton.setIcon(new ImageIcon(nextIcon));
				playlistButton.setBackground(Color.BLACK);
				Image playlistImg = ImageIO.read(new File("img\\dark-playlist.png"));
				playlistImg = playlistImg.getScaledInstance(18, 18, 0);
				playlistButton.setIcon(new ImageIcon(playlistImg));
				shuffleButton.setBackground(Color.BLACK);
				Image shuffleImg = ImageIO.read(new File("img\\dark-shuffle.png"));
				shuffleImg = shuffleImg.getScaledInstance(18, 18, 0);
				shuffleButton.setIcon(new ImageIcon(shuffleImg));
				loopbutton.setBackground(Color.BLACK);
				if (loopCond == 0) {
					Image loopimg = ImageIO.read(new File("img\\dark-loop.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				} else if (loopCond == 1) {
					Image loopimg = ImageIO.read(new File("img\\dark-loopall.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				} else if (loopCond == 2) {
					Image loopimg = ImageIO.read(new File("img\\dark-loopsingle.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				}

				mutebutton.setBackground(Color.BLACK);
				if (muteState == 0) {
					Image muteimg = ImageIO.read(new File("img\\dark-mute.png"));
					muteimg = muteimg.getScaledInstance(18, 18, 0);
					mutebutton.setIcon(new ImageIcon(muteimg));
				} else if (muteState == 1) {
					Image muteimg = ImageIO.read(new File("img\\muteon.png"));
					muteimg = muteimg.getScaledInstance(18, 18, 0);
					mutebutton.setIcon(new ImageIcon(muteimg));
				}
			} else if (skin.equals("Default")) {
				p.setBackground(Color.LIGHT_GRAY);
				p0.setBackground(Color.LIGHT_GRAY);
				p1.setBackground(Color.LIGHT_GRAY);
				posSlider.setBackground(Color.LIGHT_GRAY);
				posSlider.setForeground(Color.BLACK);
				lenTime.setForeground(Color.BLACK);
				posTime.setForeground(Color.BLACK);
				volume.setBackground(Color.LIGHT_GRAY);
				volume.setForeground(Color.BLACK);
				volumePercent.setForeground(Color.BLACK);
				playpause.setBackground(Color.GRAY);
				if (playPauseState == 0) {
					playpauseimg = ImageIO.read(new File("img\\pause.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				} else if (playPauseState == 1) {
					playpauseimg = ImageIO.read(new File("img\\play.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				} else if (playPauseState == 2) {
					playpauseimg = ImageIO.read(new File("img\\pause.png"));
					playpauseimg = playpauseimg.getScaledInstance(18, 18, 0);
					playpause.setIcon(new ImageIcon(playpauseimg));
				}
				prevButton.setBackground(Color.GRAY);
				Image prevIcon = ImageIO.read(new File("img\\previoustrack.png"));
				prevIcon = prevIcon.getScaledInstance(18, 18, 0);
				prevButton.setIcon(new ImageIcon(prevIcon));
				stopbutton.setBackground(Color.GRAY);
				Image stopimg = ImageIO.read(new File("img\\stop.png"));
				stopimg = stopimg.getScaledInstance(18, 18, 0);
				stopbutton.setIcon(new ImageIcon(stopimg));
				nextButton.setBackground(Color.GRAY);
				Image nextIcon = ImageIO.read(new File("img\\nexttrack.png"));
				nextIcon = nextIcon.getScaledInstance(18, 18, 0);
				nextButton.setIcon(new ImageIcon(nextIcon));
				playlistButton.setBackground(Color.GRAY);
				Image playlistImg = ImageIO.read(new File("img\\playlist.png"));
				playlistImg = playlistImg.getScaledInstance(18, 18, 0);
				playlistButton.setIcon(new ImageIcon(playlistImg));
				shuffleButton.setBackground(Color.GRAY);
				Image shuffleImg = ImageIO.read(new File("img\\shuffle.png"));
				shuffleImg = shuffleImg.getScaledInstance(18, 18, 0);
				shuffleButton.setIcon(new ImageIcon(shuffleImg));
				loopbutton.setBackground(Color.GRAY);
				if (loopCond == 0) {
					Image loopimg = ImageIO.read(new File("img\\loop.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				} else if (loopCond == 1) {
					Image loopimg = ImageIO.read(new File("img\\loopall.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				} else if (loopCond == 2) {
					Image loopimg = ImageIO.read(new File("img\\loopsingle.png"));
					loopimg = loopimg.getScaledInstance(18, 18, 0);
					loopbutton.setIcon(new ImageIcon(loopimg));
				}
				mutebutton.setBackground(Color.GRAY);
				if (muteState == 0) {
					Image muteimg = ImageIO.read(new File("img\\mute.png"));
					muteimg = muteimg.getScaledInstance(18, 18, 0);
					mutebutton.setIcon(new ImageIcon(muteimg));
				} else if (muteState == 1) {
					Image muteimg = ImageIO.read(new File("img\\muteon.png"));
					muteimg = muteimg.getScaledInstance(18, 18, 0);
					mutebutton.setIcon(new ImageIcon(muteimg));
				}
			}
		} catch (IOException e) {
			errorBox(e, "Error loading icons");
		}
	}
	
	private Color skinColorBG() {
		if (currentSkin.equals("Dark")) {
			return (Color.DARK_GRAY);
		} else if (currentSkin.equals("Default")) {
			return (Color.LIGHT_GRAY);
		} else {
			return Color.WHITE;
		}
	}
	
	private Color skinColorFG() {
		if (currentSkin.equals("Dark")) {
			return (Color.WHITE);
		} else if (currentSkin.equals("Default")) {
			return (Color.BLACK);
		} else {
			return Color.BLACK;
		}
	}
	
	private Color skinButtonBG() {
		if (currentSkin.equals("Dark")) {
			return (Color.BLACK);
		} else if (currentSkin.equals("Default")) {
			return (Color.GRAY);
		} else {
			return Color.WHITE;
		}
	}
}
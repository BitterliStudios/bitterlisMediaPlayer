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
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.Equalizer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Media {

	private boolean loop;
	private EmbeddedMediaPlayer mediaPlayer;
	private File file;
	private boolean stopped;
	private Equalizer eq;
	private MediaPlayerFactory mediaPlayerFactory;
	

	private ActionListener taskPerformed = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			final long time = mediaPlayer.getTime();
			if (time == -1) {
				mediaPlayer.setTime(0);
				mediaPlayer.play();
			}

		}
	};
	@SuppressWarnings("unused")
	private Timer t = new Timer(100, taskPerformed);

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
		String[] audio = {"3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff", "amb", "amr", "aob", "ape", "au", "awb",
						"caf", "dts", "flac", "it", "kar", "m4a", "m4b", "m4p", "m5p", "mid", "mka", "mlp", "mod", "mp1", "mp2", "mp3",
						"mpa", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp", "ra", "rmi", "s3m", "sid", "spx", "tak", "thd",
						"tta", "voc", "vqf", "w64", "wav", "wma", "wv", "xa", "xm"};
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

		});
		fileMenu.add(open);

		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
				//stop.start();
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
		
		JMenuItem brightness = new JMenuItem("Brightness");
		brightness.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.setAdjustVideo(true);
				JFrame input = new JFrame("Input");
				String num = JOptionPane.showInputDialog(input, "Enter the brighness (0 - 200)", "", 1);
				double brightness = (Integer.parseInt(num) / 100);
				mediaPlayer.setBrightness((float) brightness);
				mediaPlayer.setAdjustVideo(false);
			}
		});
		
		JMenuItem contrast = new JMenuItem("Contrast");
		contrast.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.setAdjustVideo(true);
				JFrame input = new JFrame("Input");
				String num = JOptionPane.showInputDialog(input, "Enter the contrast (0 - 200)", "", 1);
				double contrast = (Integer.parseInt(num) / 100);
				mediaPlayer.setBrightness((float) contrast);
				mediaPlayer.setAdjustVideo(false);
			}
		});
		
		JMenuItem hue = new JMenuItem("Hue");
		hue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.setAdjustVideo(true);
				JFrame input = new JFrame("Input");
				String num = JOptionPane.showInputDialog(input, "Enter the brighness (0 - 300)", "", 1);
				double hue = (Integer.parseInt(num));
				mediaPlayer.setBrightness((float) hue);
				mediaPlayer.setAdjustVideo(false);
			}
		});
		
		JMenuItem saturation = new JMenuItem("Brightness");
		saturation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.setAdjustVideo(true);
				JFrame input = new JFrame("Input");
				String num = JOptionPane.showInputDialog(input, "Enter the brighness (0 - 300)", "", 1);
				double saturation = (Integer.parseInt(num) / 100);
				mediaPlayer.setBrightness((float) saturation);
				mediaPlayer.setAdjustVideo(false);
			}
		});
		
		JMenuItem gamma = new JMenuItem("Brightness");
		gamma.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.setAdjustVideo(true);
				JFrame input = new JFrame("Input");
				String num = JOptionPane.showInputDialog(input, "Enter the brighness (0 - 100)", "", 1);
				double gamma = (Integer.parseInt(num) / 10);
				mediaPlayer.setBrightness((float) gamma);
			}
		});
		
		JMenuItem speed = new JMenuItem("Playback Speed");
		speed.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.setAdjustVideo(true);
		        Object[] possibilities = {0.5, 0.75, 1.0, 1.25, 1.50, 2.0};  
		        double speed = (double) JOptionPane.showInputDialog(null,  
		                "Select Speed", "ShowInputDialog",  
		                JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), possibilities, "Numbers");  
		        mediaPlayer.setRate((float) speed);
			}
			
		});
		
		
		videoSettings.add(brightness);
		videoSettings.add(speed);
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

			@Override
			public void actionPerformed(ActionEvent e) {
				//java.util.List<String> presets = (mediaPlayerFactory.getEqualizerPresetNames());
				
				Float[] values = new Float[11];
				values[0] = mediaPlayer.getEqualizer().getPreamp();
				for (int i = 1; i < 11; i++) {
					if (mediaPlayer.getEqualizer().getAmp(i - 1) != 0.0f) {
						values[i] = mediaPlayer.getEqualizer().getAmp(i - 1);
					} else {
						values[i] = 0.0f;
					}
				}
				
				JFrame parent = new JFrame();
			    JOptionPane optionPane = new JOptionPane();
			    JPanel panel = new JPanel(new GridLayout(0, 11));
			    
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
			    panel.add(preampPanel);
			    
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
			    panel.add(eq1Panel);
			    
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
			    panel.add(eq2Panel);
			    
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
			    panel.add(eq3Panel);
			    
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
			    panel.add(eq4Panel);
			    
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
			    panel.add(eq5Panel);
			    
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
			    panel.add(eq6Panel);
			    
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
			    panel.add(eq7Panel);
			    
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
			    panel.add(eq8Panel);
			    
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
			    panel.add(eq9Panel);
			    
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
			    panel.add(eq10Panel);
			    
			    
			    optionPane.setMessage(panel);
			    
			    optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
			    optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
			    JDialog dialog = optionPane.createDialog(parent, "Equalizer");
			    dialog.setVisible(true);
				
			}
			
		});
		
		audioSettings.add(mute);
		audioSettings.add(equalizer);

		main.add(audioSettings);

		helpMenu.add(about);
		main.add(helpMenu);
		
		
		frame.setJMenuBar(main);

		Canvas c = new Canvas();
		JPanel p = new JPanel();
		c.setBounds(100, 500, 1050, 500);
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);
		p.setBounds(100, 50, 1050, 600);
		frame.add(p, BorderLayout.NORTH);
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		if (!isAudio()) {
			mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
		}
		
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
				// sets the slider
				int getSliderValue = (int) (mediaPlayer.getPosition() * 100);
				slider.setValue(getSliderValue);

				// sets the JLabel
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
		
		p0.setBounds(100,900,105,200);
		frame.add(p0, BorderLayout.CENTER);
		
		JPanel p1 = new JPanel();

		p1.setBounds(100, 900, 105, 200);
		frame.add(p1, BorderLayout.SOUTH);
		JButton playbutton = new JButton("Play");
		Image playimg = null;
		try {
			playimg = ImageIO.read(new URL("https://bitterli.us/java/audio/Play.gif").openStream());
		} catch (MalformedURLException e1) {
			errorWindow window = new errorWindow(e1, "005");
			window.show();
			e1.printStackTrace();
		} catch (IOException e1) {
			errorWindow window = new errorWindow(e1, "006");
			window.show();
			e1.printStackTrace();
		}
		playbutton.setIcon(new ImageIcon(playimg));
		playbutton.setBounds(50, 50, 150, 100);
		p1.add(playbutton);

		JButton pausebutton = new JButton("Pause");
		Image pauseimg = null;
		try {
			pauseimg = ImageIO.read(new URL("https://bitterli.us/java/audio/Pause.png").openStream());
		} catch (MalformedURLException e1) {
			errorWindow window = new errorWindow(e1, "005");
			window.show();
			e1.printStackTrace();
		} catch (IOException e1) {
			errorWindow window = new errorWindow(e1, "006");
			window.show();
			e1.printStackTrace();
		}
		pausebutton.setIcon(new ImageIcon(pauseimg));
		pausebutton.setBounds(80, 50, 150, 100);
		p1.add(pausebutton);

		JButton stopbutton = new JButton("Stop");
		Image stopimg = null;
		try {
			stopimg = ImageIO.read(new URL("https://bitterli.us/java/audio/Stop.gif").openStream());
		} catch (MalformedURLException e1) {
			errorWindow window = new errorWindow(e1, "005");
			window.show();
			e1.printStackTrace();
		} catch (IOException e1) {
			errorWindow window = new errorWindow(e1, "006");
			window.show();
			e1.printStackTrace();
		}
		stopbutton.setIcon(new ImageIcon(stopimg));
		stopbutton.setBounds(80, 50, 150, 100);
		p1.add(stopbutton);

		JButton loopbutton = new JButton("Loop");
		Image loopimg = null;
		try {
			loopimg = ImageIO.read(new URL("https://bitterli.us/java/audio/Loop.png").openStream());
		} catch (MalformedURLException e1) {
			errorWindow window = new errorWindow(e1, "005");
			window.show();
			e1.printStackTrace();
		} catch (IOException e1) {
			errorWindow window = new errorWindow(e1, "006");
			window.show();
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
		if (isAudio()) {
			Image img = ImageIO.read(new File("namelogo.png"));
			img = img.getScaledInstance(700, 145, 0);
			c.getGraphics().drawImage(img, 0,0, null);
		} else {
			c.setBackground(Color.black);
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
				// final long time = mediaPlayer.getTime();
				// System.out.println(time);
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
					loopbutton.setBackground(Color.GRAY);
					loop = true;
					t.start();
				} else {
					loopbutton.setBackground(Color.LIGHT_GRAY);
					loop = false;
					t.stop();
				}
			}
		});
	}

	public void stop() {
		mediaPlayer.stop();
		mediaPlayer.pause();
	}
	
}
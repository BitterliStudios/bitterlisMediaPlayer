import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Playlist {

	private static int pos = 0;
	private static LinkedList<audioFile> playlist = new LinkedList<audioFile>();

	private static boolean playing = true;
	private static boolean pLoop = false;
	private static boolean sLoop = false;
	private static boolean shuffle = true;

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		playlist.add(new audioFile("test.wav"));
		playlist.add(new audioFile("test1.wav"));
		playlist.add(new audioFile("test2.wav"));
		playlist.get(0).play();

		JFrame frame = new JFrame("LinkedList Playlist");

		JMenuBar mb = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		file.add(close);
		mb.add(file);

		JMenu opt = new JMenu("Options");

		// add track to end of playlist
		JMenuItem addE = new JMenuItem("Add (End)");
		addE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String filename = JOptionPane.showInputDialog("Enter name: ");
					playlist.addLast(new audioFile(filename));
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException
						| NullPointerException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
		opt.add(addE);

		// add track to top of playlist
		JMenuItem addT = new JMenuItem("Add (Top)");
		addT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String filename = JOptionPane.showInputDialog("Enter name: ");
					playlist.addFirst(new audioFile(filename));
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException
						| NullPointerException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
		opt.add(addT);

		// loop playlist option
		JMenuItem loopP = new JMenuItem("Enable Playlist Loop");
		loopP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loopPlaylist();
				if (pLoop) {
					loopP.setText("Disable Playlist Loop");
				} else {
					loopP.setText("Enable Playlist Loop");
				}
			}
		});
		opt.add(loopP);

		// loop single track option
		JMenuItem loopS = new JMenuItem("Enable Single Loop");
		loopS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loopSingleTrack();
				if (sLoop) {
					loopS.setText("Disable Single Loop");
				} else {
					loopS.setText("Enable Single Loop");
				}
			}
		});
		opt.add(loopS);
		JMenuItem shuffle = new JMenuItem("Enable Shuffle");
		loopS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shuffle();
				if (sLoop) {
					loopS.setText("Disable Shuffle");
				} else {
					loopS.setText("Enable Shuffle");
				}
			}
		});
		opt.add(loopS);
		
		mb.add(opt);

		JMenu view = new JMenu("View");
		JMenuItem print = new JMenuItem("Print");
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame printF = new JFrame("Playlist");
				JPanel printP = new JPanel(new GridLayout(playlist.size(), 1));
				for (int i = 0; i < playlist.size(); i++) {
					JTextField title = new JTextField(playlist.get(i).getName());
					printP.add(title);
				}
				printF.add(printP);
				printF.setSize(new Dimension(200, 200));
				printF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				printF.setVisible(true);
			}
		});
		view.add(print);
		mb.add(view);

		JPanel pT = new JPanel();
		JTextField name = new JTextField(playlist.get(pos).getName());
		name.setEditable(false);
		pT.add(name);
		
		

		JPanel pB = new JPanel(new GridLayout(0, 3));

		// jump to previous track in playlist.
		JButton prev = new JButton("Previous");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!(pos - 1 < 0)) {
						playlist.get(pos).stop();
						pos--;
						name.setText(playlist.get(pos).getName());
						if (playlist.get(pos).opened()) {
							playlist.get(pos).resetAudioStream();
						}
						playlist.get(pos).play();
					}
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		pB.add(prev);

		// play / pause audio playback
		JButton pp = new JButton("Pause");
		pp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (playing) {
					playlist.get(pos).pause();
					playing = false;
					pp.setText("Play");
				} else {
					playlist.get(pos).play();
					playing = true;
					pp.setText("Pause");
				}
			}
		});
		pB.add(pp);

		// advance to the next track
		JButton next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!(pos + 1 == playlist.size())) {
						playlist.get(pos).stop();
						pos++;
						name.setText(playlist.get(pos).getName());
						if (playlist.get(pos).opened()) {
							playlist.get(pos).resetAudioStream();
						}
						playlist.get(pos).play();
					} else if (pLoop) {
						playlist.get(pos).stop();
						pos = 0;
						name.setText(playlist.get(pos).getName());
						if (playlist.get(pos).opened()) {
							playlist.get(pos).resetAudioStream();
						}
						playlist.get(pos).play();
					}
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		pB.add(next);

		// Go to the next track in the playlist
		ActionListener advance = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (playlist.get(pos).isDone()) {
					try {
						if (!(pos + 1 == playlist.size()) && !sLoop) {
							playlist.get(pos).stop();
							pos++;
							name.setText(playlist.get(pos).getName());
							if (playlist.get(pos).opened()) {
								playlist.get(pos).resetAudioStream();
							}
							playlist.get(pos).play();
						} else if (pLoop) {
							playlist.get(pos).stop();
							pos = 0;
							name.setText(playlist.get(pos).getName());
							if (playlist.get(pos).opened()) {
								playlist.get(pos).resetAudioStream();
							}
							playlist.get(pos).play();
						}
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						e.printStackTrace();
					}
				}

			}

		};

		Timer step = new Timer(1000, advance);
		step.start();

		// frame setup stuff
		frame.setLayout(new GridLayout(2, 1));
		frame.add(pT);
		frame.add(pB);
		frame.setJMenuBar(mb);
		frame.setSize(new Dimension(400, 150));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void loopPlaylist() {
		if (!pLoop) {
			pLoop = true;
		} else {
			pLoop = false;
		}
	}

	public static void loopSingleTrack() {
		if (!sLoop) {
			sLoop = true;
			playlist.get(pos).loop(true);
		} else {
			sLoop = false;
			playlist.get(pos).loop(false);
		}
	}
	
	public static void shuffle() {
		if (!shuffle) {
			shuffle = true;
		} else {
			shuffle = false;
		}
	}

}

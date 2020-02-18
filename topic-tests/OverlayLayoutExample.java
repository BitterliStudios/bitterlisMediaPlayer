import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.border.LineBorder;

public class OverlayLayoutExample {
	public static void main(String[] args) {
		JPanel panel = createPanel();
		JFrame frame = createFrame();
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static JPanel createPanel() {
		JPanel mainPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isOptimizedDrawingEnabled() {
				return false;
			}
		};
		mainPanel.setLayout(new OverlayLayout(mainPanel));

		JButton button = new JButton("Show Message");
		button.setAlignmentX(0.5f);
		button.setAlignmentY(0.5f);

		JPanel popupPanel = createPopupPanel(button);
		popupPanel.setAlignmentX(0.1f);
		popupPanel.setAlignmentY(0.1f);

		button.addActionListener(e -> {
			button.setEnabled(false);
			popupPanel.setVisible(true);
		});
		mainPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				button.setEnabled(false);
				popupPanel.setVisible(true);
			}
		});

		mainPanel.add(popupPanel);
		mainPanel.add(button);

		return mainPanel;
	}
	
	/** creates overlay
	 * 
	 * @param overlapComponent
	 * @return
	 */
	private static JPanel createPopupPanel(JComponent overlapComponent) {
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setOpaque(false);
		popupPanel.setMaximumSize(new Dimension(150, 70));
		popupPanel.setBorder(new LineBorder(Color.gray));
		popupPanel.setVisible(false);

		JLabel label = new JLabel("HI there!");
		popupPanel.add(wrapInPanel(label), BorderLayout.CENTER);

		JButton popupCloseButton = new JButton("Close");
		popupPanel.add(wrapInPanel(popupCloseButton), BorderLayout.SOUTH);

		popupCloseButton.addActionListener(e -> {
			overlapComponent.setEnabled(true);
			popupPanel.setVisible(false);
		});

		return popupPanel;
	}

	private static JPanel wrapInPanel(JComponent component) {
		JPanel jPanel = new JPanel();
		jPanel.setBackground(new Color(50, 210, 250, 150));
		jPanel.add(component);
		return jPanel;
	}

	private static JFrame createFrame() {
		JFrame frame = new JFrame("OverlayLayout Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 300));
		return frame;
	}
}
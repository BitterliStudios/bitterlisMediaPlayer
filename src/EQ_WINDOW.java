import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EQ_WINDOW {
	static int[] values = new int[11];

	public static void main(final String[] args) {
    JFrame parent = new JFrame();
    
    JOptionPane optionPane = new JOptionPane();
    JPanel panel = new JPanel(new GridLayout(0, 11));
    
    JPanel preampPanel = new JPanel(new BorderLayout());
    preampPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    JLabel preampLabel = new JLabel("Preamp");
    JLabel preampText = new JLabel("" + values[0] + "dB");
    JSlider preamp = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    preamp.setMajorTickSpacing(2);
    preamp.setPaintTicks(true);
    preamp.setPaintLabels(true);
    ChangeListener preampC = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[0] = (new Integer(theSlider.getValue()));
            preampText.setText("" + values[0] + "dB");
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
    JLabel eq1Label = new JLabel("EQ1");
    JLabel eq1Text = new JLabel("" + values[1] + "dB");
    JSlider eq1 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq1.setMajorTickSpacing(2);
    eq1.setPaintTicks(true);
    eq1.setPaintLabels(true);
    ChangeListener eq1C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[1] = (new Integer(theSlider.getValue()));
            eq1Text.setText("" + values[1] + "dB");
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
    JLabel eq2Label = new JLabel("EQ2");
    JLabel eq2Text = new JLabel("" + values[1] + "dB");
    JSlider eq2 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq2.setMajorTickSpacing(2);
    eq2.setPaintTicks(true);
    eq2.setPaintLabels(true);
    ChangeListener eq2C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[2] = (new Integer(theSlider.getValue()));
            eq2Text.setText("" + values[2] + "dB");
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
    JLabel eq3Label = new JLabel("EQ3");
    JLabel eq3Text = new JLabel("" + values[3] + "dB");
    JSlider eq3 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq3.setMajorTickSpacing(2);
    eq3.setPaintTicks(true);
    eq3.setPaintLabels(true);
    ChangeListener eq3C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[3] = (new Integer(theSlider.getValue()));
            eq3Text.setText("" + values[3] + "dB");
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
    JLabel eq4Label = new JLabel("EQ4");
    JLabel eq4Text = new JLabel("" + values[4] + "dB");
    JSlider eq4 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq4.setMajorTickSpacing(2);
    eq4.setPaintTicks(true);
    eq4.setPaintLabels(true);
    ChangeListener eq4C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[4] = (new Integer(theSlider.getValue()));
            eq4Text.setText("" + values[4] + "dB");
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
    JLabel eq5Label = new JLabel("EQ5");
    JLabel eq5Text = new JLabel("" + values[5] + "dB");
    JSlider eq5 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq5.setMajorTickSpacing(2);
    eq5.setPaintTicks(true);
    eq5.setPaintLabels(true);
    ChangeListener eq5C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[5] = (new Integer(theSlider.getValue()));
            eq5Text.setText("" + values[5] + "dB");
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
    JLabel eq6Label = new JLabel("EQ6");
    JLabel eq6Text = new JLabel("" + values[6] + "dB");
    JSlider eq6 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq6.setMajorTickSpacing(2);
    eq6.setPaintTicks(true);
    eq6.setPaintLabels(true);
    ChangeListener eq6C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[6] = (new Integer(theSlider.getValue()));
            eq6Text.setText("" + values[6] + "dB");
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
    JLabel eq7Label = new JLabel("EQ7");
    JLabel eq7Text = new JLabel("" + values[7] + "dB");
    JSlider eq7 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq7.setMajorTickSpacing(2);
    eq7.setPaintTicks(true);
    eq7.setPaintLabels(true);
    ChangeListener eq7C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[7] = (new Integer(theSlider.getValue()));
            eq7Text.setText("" + values[7] + "dB");
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
    JLabel eq8Label = new JLabel("EQ8");
    JLabel eq8Text = new JLabel("" + values[8] + "dB");
    JSlider eq8 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq8.setMajorTickSpacing(2);
    eq8.setPaintTicks(true);
    eq8.setPaintLabels(true);
    ChangeListener eq8C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[8] = (new Integer(theSlider.getValue()));
            eq8Text.setText("" + values[8] + "dB");
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
    JLabel eq9Label = new JLabel("EQ9");
    JLabel eq9Text = new JLabel("" + values[9] + "dB");
    JSlider eq9 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq9.setMajorTickSpacing(2);
    eq9.setPaintTicks(true);
    eq9.setPaintLabels(true);
    ChangeListener eq9C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[9] = (new Integer(theSlider.getValue()));
            eq9Text.setText("" + values[9] + "dB");
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
    JLabel eq10Label = new JLabel("EQ10");
    JLabel eq10Text = new JLabel("" + values[10] + "dB");
    JSlider eq10 = new JSlider(JSlider.VERTICAL, -12, 12, 0);
    eq10.setMajorTickSpacing(2);
    eq10.setPaintTicks(true);
    eq10.setPaintLabels(true);
    ChangeListener eq10C = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
          JSlider theSlider = (JSlider) changeEvent.getSource();
          if (!theSlider.getValueIsAdjusting()) {
            values[10] = (new Integer(theSlider.getValue()));
            eq10Text.setText("" + values[10] + "dB");
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

}
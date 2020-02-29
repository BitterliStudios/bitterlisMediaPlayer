import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class optionPanetest {

	public static void main(String[] args) {
		Object[] tracks = { "Joe", "Mama" };
		JComboBox comboBox = new JComboBox(tracks);
		comboBox.setSelectedIndex(0);
		JOptionPane.showMessageDialog(null, comboBox, "Select a track to remove", JOptionPane.QUESTION_MESSAGE);
		String selected = (String) comboBox.getSelectedItem();;
	}

}

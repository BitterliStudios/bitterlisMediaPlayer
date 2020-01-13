import javax.swing.JOptionPane;

public class BAbout {
	
	public BAbout() {
	//Constructor	
	}
	
	public void getAbout() {
		String name = "Bitterli's Media Player in Java";
		String version = "Version B_0.7.0 - Efficiency Update";
		String author = "Bitterli";
		String changes = "Changelog:"
				+ "\n(Jan 07 2020) Removed all default library-based players, entirely VLCJ"
				+ "\n(Jan 06 2020) Added base equalization to videos."
				+ "\n(Dec 20 2019) Adding special features to VideoPlayer."
				+ "\n(Dec 20 2019) Added slider functionality (scaling is proper)."
				+ "\n(Dec 18 2019) Fixed menu bar issues."
				+ "\n(Dec 17 2019) Cleaned up video GUI, added slider."
				+ "\n(Dec 14 2019) Finished midi player."
				+ "\n(Dec 10 2019) Added open operation between midi and sample classes."
				+ "\n(Dec 02 2019) Added midi player class GUI."
				+ "\n(Nov 22 2019) Added midi player class base."
				;
		String copyright = "(C) 2020 bitterli.us";
		JOptionPane.showMessageDialog(null, name + "\n" + version + "\n" + "Written by: " + author + "\n\n" + changes + "\n\n" + copyright, "About", 1);
	}

}

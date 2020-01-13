

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class errorWindow {
	private String error;
	private boolean opened;
	private Exception ex;
	private String err;
	
	public errorWindow(Exception e, String s) {
		ex = e;
		err = s;
	}
	
	public void show() {
		error = err;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String stack = sw.toString();
		String errorMessage = "<html><bold>";
		if (error.equals("001")) {
			errorMessage += "An error occured:</bold><br>Unsupported File Selected. Please choose files with the .wav extention.";
		} else if (error.equals("002")) {
			errorMessage += "An error occured:</bold><br>There was trouble reading your file. Please try again or use a different file.";
		} else if (error.equals("003")) {
			errorMessage += "An error occured:</bold><br>There was trouble reading the line information in your file. Please check the integrity of your file and try again.";
		} else if (error.equals("004")) {
			errorMessage += "An error occured:</bold><br>The file specified was not located. Please chekck your pathway and try again.";
		} else if (error.equals("005")) {
			errorMessage += "An error occured:</bold><br>Error while gathering images for the player. See console logs for more information.";
		} else if (error.equals("006")) {
			errorMessage += "An error occured:</bold><br>Error while gathering images for the player. See console logs for more information.";
		}
		
		
		else {
			errorMessage += "An Unexpected Error Occured. Please look at the console logs for more info.";
		}
		errorMessage += ("<br><br>Console Error:<br><font size=\"3\" color=\"red\"> " + stack);
		errorMessage += "</font></html>";
		
		JOptionPane.showMessageDialog(new JFrame(), String.format(errorMessage), "Error", JOptionPane.ERROR_MESSAGE);
		opened = true;
	}

	public int getErrorCode() {
		return (Integer.valueOf(error));
	}
	
	public boolean opened() {
		boolean o = opened;
		if (opened) {
			opened = false;
		}
		return o;
	}

}

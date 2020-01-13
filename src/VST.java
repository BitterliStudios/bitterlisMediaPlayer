import java.io.File;
import java.io.FileNotFoundException;

import com.synthbot.audioplugin.vst.JVstLoadException;
import com.synthbot.audioplugin.vst.vst2.JVstHost2;

public class VST {
	
	public static void main(String[] args) {
		JVstHost2 vst;
		try {
		  vst = JVstHost2.newInstance(new File("D:\\The Flash 2.0\\Advanced Workspace\\Media Player\\jvsthost-master\\effects"));
		} catch (FileNotFoundException fnfe) {
		  fnfe.printStackTrace(System.err);
		} catch (JVstLoadException jvle) {
		  jvle.printStackTrace(System.err);
		}
	}

}


/**
 * Manages the loading and saving of all Equalizer Presets.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import uk.co.caprica.vlcj.player.Equalizer;

public class eqPresetManager {
	private Map<String, Equalizer> allPresetEqualizers;

	public eqPresetManager(Map<String, Equalizer> defaultPresetEqualizers) {
		allPresetEqualizers = defaultPresetEqualizers;
		try {
			getUserPresets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getUserPresets() throws IOException {
		File file = new File("eqPresets.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file));
		int advance = 0;
		String st = "";
		String presetName = "";
		Float[] values = new Float[10];
		while ((st = br.readLine()) != null) {
			if (st.substring(0, 1).equals("[")) {
				presetName = st.replace("[", "");
				presetName = st.replace("]", "");
			} else if (!st.equals("")) {
				if (advance != 11) {
					values[advance] = Float.valueOf(st);
					advance++;
				} else {
					advance = 0;
					Equalizer eq = allPresetEqualizers.get("Flat");
					for (int i = 0; i < 11; i++) {
						eq.setAmp(i, values[i]);
					}
					allPresetEqualizers.put(presetName, eq);
				}

			}
		}
	}

	public void addPreset(String name, Float[] values) {
		try {
			@SuppressWarnings("resource")
			FileWriter writer = new FileWriter("eqpresets.txt", true);
			writer.write("[" + name + "]\n");
			for (int i = 0; i < values.length; i++) {
				writer.write(String.valueOf(values[i]));
			}
		} catch (IOException e) {
			System.out.println("Error saving the file. A series of Ls are below.");
			e.printStackTrace();
		}
	}

	public String[] getAllNames() {
		String[] names = new String[allPresetEqualizers.size()];
		int i = 0;
		for (String key : allPresetEqualizers.keySet()) {
			names[i] = key;
			i++;
		}
		return names;
	}

	public Map<String, Equalizer> getPresets() {
		return allPresetEqualizers;
	}

	public Equalizer getEqualizer(Object object) {
		return allPresetEqualizers.get(object);
	}
}
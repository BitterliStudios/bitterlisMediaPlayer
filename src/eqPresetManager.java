
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
		} else {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int advance = 0;
			String st = "";
			String presetName = "";
			Float[] values = new Float[11];
			while ((st = br.readLine()) != null) {
				if (!st.equals("")) {
					if (st.substring(0, 1).equals("[")) {
						presetName = st.replace("[" , "");
						presetName = presetName.replace("]", "");
					} else {
						if (advance < 10) {
							values[advance] = Float.valueOf(st);
							advance++;
						} else {
							values[advance] = Float.valueOf(st);
							advance = 0;
							Equalizer neweq = allPresetEqualizers.get("Flat");
							for (int i = 0; i < 11; i++) {
								if (i == 0) {
									neweq.setPreamp(values[i]);
								} else {
									neweq.setAmp(i - 1, values[i]);
								}
							}
							allPresetEqualizers.put(presetName, neweq);
							System.out.print("[" + presetName + "]: ");
							for (int i = 0; i < 11; i++) {
								if (i != 10) {
									System.out.print(values[i] + ", ");
								} else {
									System.out.println(values[i]);
								}
							}
							values = new Float[11];
							presetName = "";
						}

					}
				}
			}
			br.close();
		}
	}

	public void addPreset(String name, Float[] values) {
		try {
			FileWriter writer = new FileWriter("eqPresets.txt", true);
			writer.write("[" + name + "]\n");
			for (int i = 0; i < values.length; i++) {
				writer.write(String.valueOf(values[i]) + "\n");
			}
			writer.write("\n");
			writer.close();
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
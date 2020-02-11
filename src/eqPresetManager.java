
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
							Equalizer neweq = new Equalizer(10);
							for (int i = 0; i < 11; i++) {
								if (i == 0) {
									neweq.setPreamp(values[i]);
								} else {
									neweq.setAmp(i - 1, values[i]);
								}
							}
							allPresetEqualizers.put(presetName, neweq);
							testOnTheWay(presetName, values);
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
	
	/**
	 * Ensure each value as being read in works.
	 * @param presetName
	 * @param values
	 */
	private void testOnTheWay(String presetName, Float[] values) {
		System.out.print("[" + presetName + "]: ");
		for (int i = 0; i < 11; i++) {
			if (i != 10) {
				System.out.print(values[i] + ", ");
			} else {
				System.out.println(values[i]);
			}
		}
	}
	/**
	 * Test the equalizer selected from media class.
	 * @param set
	 */
	public void testEqualizer(Equalizer set) {
		Float[] values = new Float[11];
		values[0] = set.getPreamp();
		for (int i = 1; i < 11; i++) {
			if (set.getAmp(i - 1) != 0.0f) {
				values[i] = set.getAmp(i - 1);
			} else {
				values[i] = 0.0f;
			}
		}
		System.out.print("[ ");
		for (int i = 0; i < 11; i++) {
			System.out.print(values[i] + " ");
		}
		System.out.print("]\n");
	}
	/**
	 * Test all equalizers and their values
	 */
	public void testAllEqualizer() {
		System.out.println(allPresetEqualizers);
	}
}
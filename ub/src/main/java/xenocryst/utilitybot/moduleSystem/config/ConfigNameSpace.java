package xenocryst.utilitybot.moduleSystem.config;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ConfigNameSpace {
	public ArrayList<ConfigEntry> entries = new ArrayList<>();
	private String name;

	public ConfigNameSpace(String namespace) {
		this.name = namespace;
	}

	public String getName() {
		return name;
	}

	public ConfigEntry addEntry(String entryName, Object value) {
		System.out.println(">>> Adding entry " + entryName + " to the " + getName() + " nameSpace");
		ConfigEntry c = new ConfigEntry(entryName, value);
		entries.add(c);
		return c;
	}

	public void addEntries(ArrayList<ConfigEntry> entries) {
		this.entries.addAll(entries);
	}

	public ArrayList<ConfigEntry> getEntries() {
		return entries;
	}

	@Nullable
	public ConfigEntry getEntry(String name) throws NoSuchFieldException {
		for (ConfigEntry entry : entries) {
			if (entry.getName().equals(name)) {
				return entry;
			}
		}
		throw new NoSuchFieldException("entry was not found, ,make sure the entry exists and is properly formatted" +
				" \n example of how this entry should look: " + this.name + ":" + name + "=[VALUE]");
	}

	/**
	 * Use this to get an entry safely
	 *
	 * @param name         The name of the entry
	 * @param defaultValue If there is no entry, the value to return (returns as a new configEntry object with the given value)
	 * @return
	 */
	public ConfigEntry getEntry(String name, Object defaultValue) {
		ConfigEntry r = null;
		try {
			r = getEntry(name);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		if (r == null) {
			return new ConfigEntry(name, defaultValue);
		} else {
			return r;
		}
	}

//    public configEntry getEntry(String name, Class object){
//
//    }
}

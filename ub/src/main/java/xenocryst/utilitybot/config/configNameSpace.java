package xenocryst.utilitybot.config;

import java.util.ArrayList;

public class configNameSpace {
    private String name;
    public ArrayList<configEntry> entries = new ArrayList<>();

    public String getName() {
        return name;
    }

    public configEntry addEntry(String entryName, Class type, Object value) {
        System.out.println(">>> Adding entry "+entryName+" = "+value.toString()+" to the "+getName()+" nameSpace");
        configEntry c = new configEntry(entryName, type, value);
        entries.add(c);
        return c;
    }

    public void addEntries(ArrayList<configEntry> entries){
        this.entries.addAll(entries);
    }

    public configNameSpace(String namespace) {
        this.name = namespace;
    }

    public ArrayList<configEntry> getEntries() {
        return entries;
    }

    public configEntry getEntry(String name){
        for (configEntry entry : entries){
            if (entry.getName().equals(name)){
                return entry;
            }
        }
        return null;
    }
}

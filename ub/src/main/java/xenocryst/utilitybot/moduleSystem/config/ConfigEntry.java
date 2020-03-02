package xenocryst.utilitybot.moduleSystem.config;

public class ConfigEntry {
    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
    	return (String)this.getValue();
    }

    public String getStringValue() {
        String value = toString();
        if (value==null)
            value="object is not a string";
        return toString();
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public ConfigEntry(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}

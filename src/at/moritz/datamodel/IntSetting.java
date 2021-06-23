package at.moritz.datamodel;

public class IntSetting extends Setting{
    public int value;

    public IntSetting(String key,int value) {
        super(key);
        setValue(value);
    }

    @Override
    public String getKey() {
        return super.key;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {return this.value;}
}

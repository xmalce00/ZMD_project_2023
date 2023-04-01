package Enums;

public enum WatermarkTypes {

    Space("Space"),
    DTC("DTC");

    String name;

    WatermarkTypes(String s) {
        name = s;
    }

    @Override
    public String toString() { return name; }

}

package sonar.logistics.server.data.types;

import imgui.ImColor;

public enum SubType {
    BOOLEAN,
    NUMBER,
    TEXT,
    OBJECT,
    REFERENCE;

    public int getSubTypeColour() {
        switch (this) {
            case BOOLEAN:
                return ImColor.intToColor(76, 83, 245);
            case NUMBER:
                return ImColor.intToColor(245, 138, 88);
            case TEXT:
                return ImColor.intToColor(53, 165, 245);
            case OBJECT:
                return ImColor.intToColor(245, 204, 39);
            case REFERENCE:
                return ImColor.intToColor(51, 245, 225);
        }
        return -1;
    }

}

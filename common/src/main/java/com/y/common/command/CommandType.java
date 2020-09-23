package com.y.common.command;

public enum CommandType {

    UPLOAD_REQUEST((short) 0),
    UPLOAD_RESPONSE((short) 1),
    DOWNLOAD_REQUEST((short) 2),
    DOWNLOAD_RESPONSE((short) 3),
    QUERYURL_REQUEST((short) 4),
    QUERYURL_RESPONSE((short) 5);

    private short value;

    CommandType(short value) {
        this.value = value;
    }

    public static CommandType valueOf(short value) {
        for (CommandType commandType : values()) {
            if (commandType.value == value) {
                return commandType;
            }
        }
        return null;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}

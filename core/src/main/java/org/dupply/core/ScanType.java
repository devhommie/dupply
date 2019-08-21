package org.dupply.core;

public enum ScanType {

    NAME_AND_SIZE,
    NAME_AND_SIZE_AND_HASH,
    SIZE_AND_HASH;

    public static ScanType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ScanType scanType : ScanType.values()) {
            if (scanType.name().equalsIgnoreCase(value)) {
                return scanType;
            }
        }
        return null;
    }

    public String getTypeName() {
        if (this.equals(NAME_AND_SIZE)) {
            return "Имя файла и размер";
        } else if (this.equals(NAME_AND_SIZE_AND_HASH)) {
            return "Имя файла, его размер и хэш содержимого";
        } else if (this.equals(SIZE_AND_HASH)) {
            return "Размер файла и хэш содержимого";
        }
        return "";
    }
}

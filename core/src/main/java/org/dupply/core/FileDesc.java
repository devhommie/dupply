package org.dupply.core;

import java.io.Serializable;
import java.util.Objects;

public class FileDesc implements Serializable {

    protected long length;
    protected String name;
    protected String digest;

    public FileDesc(){}

    public static FileDesc byLengthAndDigest(long length, String digest) {
        return new FileDesc(null, length, digest);
    }

    public static FileDesc byNameAndLengthAndDigest(String name, long length, String digest) {
        return new FileDesc(name, length, digest);
    }

    public static FileDesc byNameAndLength(String name, long length) {
        return new FileDesc(name, length, null);
    }

    private FileDesc(String name, long length, String digest) {
        this.name = name;
        this.length = length;
        this.digest = digest;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDesc fileDesc = (FileDesc) o;
        return length == fileDesc.length &&
                Objects.equals(name, fileDesc.name) &&
                Objects.equals(digest, fileDesc.digest);
    }

    @Override
    public int hashCode() {

        return Objects.hash(length, name, digest);
    }

    @Override
    public String toString() {
        return "FileDesc{" +
                "length=" + length +
                ", name='" + name + '\'' +
                ", digest='" + digest + '\'' +
                '}';
    }

}

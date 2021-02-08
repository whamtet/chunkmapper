package com.chunkmapper.nbt;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NbtIo {
    public static CompoundTag readCompressed(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
        try {
            return read(dis);
        } finally {
            dis.close();
        }
    }

    public static void writeCompressed(CompoundTag tag, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(out));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
    }

    public static void write(CompoundTag tag, File file) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
    }

    public static CompoundTag read(File file) throws IOException {
        if (!file.exists()) return null;
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        try {
            return read(dis);
        } finally {
            dis.close();
        }
    }

    public static CompoundTag read(DataInput dis) throws IOException {
        Tag tag = Tag.readNamedTag(dis);
        if (tag instanceof CompoundTag) return (CompoundTag) tag;
        throw new IOException("Root tag must be a named compound tag");
    }

    public static void write(CompoundTag tag, DataOutput dos) throws IOException {
        Tag.writeNamedTag(tag, dos);
    }
}

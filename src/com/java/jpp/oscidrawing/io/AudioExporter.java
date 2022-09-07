package com.java.jpp.oscidrawing.io;

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AudioExporter {

    public static boolean writeChannelToFile(String path, Signal signal, int channel) throws IllegalAccessException {
        path += ".raw";

        if (signal.isInfinite() == true) {
            throw new IllegalArgumentException();
        }

        try {
            DataOutputStream data = new DataOutputStream(new FileOutputStream(path));

        } catch (Exception e) {

        }

        return true;
    }

    public static boolean writeStereoToFiles(String path, Signal signal) {
        throw new UnsupportedOperationException();
    }
}

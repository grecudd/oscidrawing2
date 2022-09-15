package com.java.jpp.oscidrawing.io;

import com.java.jpp.oscidrawing.Signal;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class AudioExporter {

    public static boolean writeChannelToFile(String path, Signal signal, int channel) throws IllegalAccessException {
        if (signal.isInfinite()) {
            throw new IllegalArgumentException();
        }
        try {
            path += ".raw";


            DataOutputStream data = new DataOutputStream(new FileOutputStream(path));
            for (int i = 0; i < signal.getSize(); i++) {
                float valueAt = (float) signal.getValueAt(channel, i);
                data.writeFloat(valueAt);
            }

            data.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean writeStereoToFiles(String path, Signal signal) {
        if (signal.isInfinite() || signal.getChannelCount() != 2)
            throw new IllegalArgumentException();
        try {
            String path1 =path + "left.raw";
            DataOutputStream data = new DataOutputStream(new FileOutputStream(path1));
            for (int i = 0; i < signal.getSize(); i++) {
                float valueAt = (float) signal.getValueAt(0, i);
                data.writeFloat(valueAt);
            }
            data.close();
            String path2 =path + "right.raw";
            DataOutputStream data1 = new DataOutputStream(new FileOutputStream(path2));
            for (int i = 0; i < signal.getSize(); i++) {
                float valueAt = (float) signal.getValueAt(1, i);
                data1.writeFloat(valueAt);
            }
            data1.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

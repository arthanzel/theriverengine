package com.arthanzel.theriverengine.reporting;

import com.arthanzel.theriverengine.sim.RiverSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * FileReporter writes JSON messages to a file.
 */
public class FileReporter implements Consumer<String> {
    private FileWriter writer = null;
    private boolean requireComma = false; // Don't need a comma on first write.

    public FileReporter(File file, RiverSystem system) {
        file.getParentFile().mkdirs();
        try {
            writer = new FileWriter(file, false);
            writer.write("{\n\"states\": [\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                writer.write("\n]\n}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void accept(String s) {
        try {
            if (requireComma) {
                writer.write(",\n");
            }
            writer.write(s);
            requireComma = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

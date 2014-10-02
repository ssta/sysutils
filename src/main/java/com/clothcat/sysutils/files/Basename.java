/*
 * The MIT License
 *
 * Copyright 2014 ssta.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.clothcat.sysutils.files;

import com.clothcat.sysutils.Constants;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Strip directory and suffix from filenames.
 *
 * @author ssta
 */
public class Basename {

    static final String VERSION = "Basename version 0.0\n"
            + "Copyright \u00A9 2014 " + Constants.SSTA_NAME + "  " + Constants.SSTA_EMAIL + "\n"
            + "License:" + Constants.MIT_LICENSE;
    private static final String CMDNAME = "Basename";

    static Options options;
    static CommandLine commandLine;
    static List<String> filenames;

    // flags and options.  These are global which is Bad and Wrong, but it's 
    // a short program and I don't care!
    // the suffix to remove if we have one
    static String suffix = null;
    // in fact ignored, we behave as if this is always set since we're going to 
    // act on any non-option arguments as if they're filenames
    static boolean multiple = false;
    // whether to use '\0' instead of newline as a seperator for output
    static boolean zero = false;

    public static void main(String[] args) {
        basename(args);
    }

    public static void basename(String[] args) {
        // temp
        String[] a = new String[]{"-z", "--suffix=txt", "a/b/c.txt", "b/c/d/e.gif", "c.txt"};
        parseOptions(a);
        if (commandLine.hasOption("help")) {
            help();
            // exit early
            return;
        } else if (commandLine.hasOption("version")) {
            version();
            // exit early
            return;
        } else {
            if (commandLine.hasOption("multiple")) {
                multiple = true;
            }
            if (commandLine.hasOption("suffix")) {
                suffix = commandLine.getOptionValue("suffix");
            }
            if (commandLine.hasOption("zero")) {
                zero = true;
            }
            filenames = commandLine.getArgList();
        }
        String pathSep = System.getProperty("file.separator");
        char separator = (zero) ? '\0' : '\n';
        for (String s : filenames) {
            if (suffix != null && s.endsWith(suffix)) {
                s = s.substring(0, s.length() - suffix.length()-1);
            }
            String[] parts = s.split(pathSep);
            System.out.print(parts[parts.length - 1] + separator);
        }
        System.out.println();
    }

    private static void parseOptions(String[] args) {
        try {
            options = new Options();
            options.addOption("help", false, "display help output and exit");
            options.addOption("version", false, "output version information and exit");
            options.addOption("a", "multiple", false, "support multiple arguments and treat each as a NAME");
            options.addOption(OptionBuilder.withLongOpt("suffix")
                    .withDescription("remove a trailing SUFFIX")
                    .hasArg()
                    .withArgName("SUFFIX").create('s'));
            options.addOption("z", "zero", false, "seperate output with NUL rather than newline");

            CommandLineParser parser = new GnuParser();
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            Logger.getLogger(Basename.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(CMDNAME + " NAME [SUFFIX]\n"
                + " OPTION... NAME...", options);
    }

    private static void version() {
        System.out.println(VERSION);
    }
}

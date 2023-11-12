package fr.cercusmc.oneblockmc.utils.files.csv;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

public class CsvWriter {

    private final File file;

    private final LinkedList<String> lines;

    private final String delimiter;

    public CsvWriter(String pathName, String delimiter){
        this.file = new File(pathName);
        this.lines = new LinkedList<>();
        this.delimiter = delimiter;
    }

    public <T> CsvWriter writeLine(LinkedList<T> elements) {

        this.lines.add(getLine(elements));
        return this;
    }

    private <T> String getLine(LinkedList<T> elements) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(T o : elements) {
            if(!Objects.isNull(o)) {
                if(index < elements.size()-1)
                    sb.append(o).append(";");
                else
                    sb.append(o);
            } else {
                if(index < elements.size()-1)
                    sb.append(";");
            }
            index++;
        }
        return sb.toString();
    }

    public CsvWriter setHeader(LinkedList<String> header) {
        this.lines.add(0, getLine(header));

        return this;
    }

    /**
     * Save the current content in file
     */
    public void save() {
        try(BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file)))) {
            this.lines.forEach(k -> {
                try {
                    bf.write(k);
                    bf.newLine();
                } catch(IOException e) {
                    //
                }
            });
        } catch(IOException e) {
            //
        }
    }

    public File getFile() {
        return file;
    }

    public LinkedList<String> getLines() {
        return lines;
    }

    public String getDelimiter() {
        return delimiter;
    }
}

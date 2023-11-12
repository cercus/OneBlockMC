package fr.cercusmc.oneblockmc.utils.files.csv;

import fr.cercusmc.oneblockmc.utils.files.Row;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class CsvReader implements Iterable<Row> {

    private final File file;
    private final LinkedList<Row> rows;
    private final LinkedList<String> header;
    private final String delimiter;
    private final String fileName;
    private final LinkedList<Class<?>> types;

    public CsvReader(String pathName, LinkedList<Class<?>> types, String delimiter) {

        this.file = new File(pathName);
        this.fileName = this.file.getName();
        this.types = types;
        this.delimiter = delimiter;
        this.rows = new LinkedList<>();
        this.header = new LinkedList<>();

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)))){
            int index = 0;
            String line;
            while((line = bf.readLine()) != null) {
                if(line.isBlank() || line.startsWith("#")) continue;

                if(index == 0) {
                    this.header.addAll(Arrays.asList(line.split(delimiter)));
                } else {
                    this.rows.add(new RowImpl(this.header, types, index, line, delimiter));
                }
                index++;
            }
        } catch(IOException e) {
            //
        }

    }

    public Row getRowAt(int index) {
        if(index < 0 || index >= this.header.size()) return null;

        return this.rows.get(index);
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public LinkedList<Class<?>> getTypes() {
        return types;
    }

    public LinkedList<Row> getRows() {
        return rows;
    }

    public LinkedList<String> getHeader() {
        return header;
    }

    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }
}

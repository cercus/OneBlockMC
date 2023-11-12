package fr.cercusmc.oneblockmc.utils.files.csv;

import fr.cercusmc.oneblockmc.utils.files.Column;
import fr.cercusmc.oneblockmc.utils.files.Row;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RowImpl implements Row {

    private final LinkedList<Column<?>> columns;

    private final int rowNum;

    private final String rawRow;

    private final LinkedList<String> header;

    protected RowImpl(LinkedList<String> header, LinkedList<Class<?>> types,  int rowNum, String rawRow, String delimiter) {
        this.header = header;
        this.rawRow = rawRow;
        this.rowNum = rowNum;
        String[] split = rawRow.split(delimiter);
        this.columns = new LinkedList<>();
        int index = 0;
        for(String i : split) {
            if(i.isBlank()) {
                this.columns.add(new ColumnImpl<>(null, null, header.get(index), index));
            } else {
                this.columns.add(new ColumnImpl<>(i, types.get(index), header.get(index), index));
            }
            index++;
        }
    }

    /**
     * Get the column from index given (0-based) <br />
     * Return null if index is < 0 or > max number of col
     *
     * @param index The index
     * @return The column from index given
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Column<T> getColAt(int index) {
        if(index < 0 ||index >= this.columns.size()) return null;
        return (Column<T>) columns.get(index);
    }

    /**
     * Get the column from the column name. <br />
     * Return null if columnName doesn't exist in row header
     *
     * @param columnName Name of column
     * @return The column from the column name
     */
    @Override
    public <T> Column<T> getColAt(String columnName) {
        if(!this.header.contains(columnName)) return null;

        return this.getColAt(this.header.indexOf(columnName));
    }

    /**
     * Get all columns from this row
     *
     * @return All column from this row
     */
    @Override
    public List<Column<?>> getCols() {
        return columns;
    }

    /**
     * Get the number of column
     *
     * @return The number of column
     */
    @Override
    public int getNumberOfCols() {
        return columns.size();
    }

    /**
     * Get the row number this row represented
     *
     * @return The row number
     */
    @Override
    public long getRowNum() {
        return rowNum;
    }

    /**
     * Get the row as map
     *
     * @return The row as map
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> asMap() {

        Map<String, T> res = new HashMap<>();

        for(String h : this.header) {
            res.put(h, (T) getColAt(h));
        }

        return res;
    }

    @Override
    public String getRawRow() {
        return this.rawRow;
    }

    @Override
    public String toString() {
        return "Row{rowNum="+this.rowNum+", columns="+this.columns+"}";
    }
}

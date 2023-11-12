package fr.cercusmc.oneblockmc.utils.files;

import java.util.List;
import java.util.Map;

public interface Row {

    /**
     * Get the column from index given (0-based) <br />
     * Return null if index is < 0 or > max number of col
     * @param index The index
     * @return The column from index given
     */
    public <T> Column<T> getColAt(int index);

    /**
     * Get the column from the column name. <br />
     * Return null if columnName doesn't exist in row header
     *
     * @param columnName Name of column
     * @return The column from the column name
     */
    public <T> Column<T> getColAt(String columnName);

    /**
     * Get all columns from this row
     * @return All column from this row
     */
    public List<Column<?>> getCols();

    /**
     * Get the number of column
     * @return The number of column
     */
    public int getNumberOfCols();

    /**
     * Get the row number this row represented
     * @return The row number
     */
    public long getRowNum();

    /**
     * Get the row as map
     * @return The row as map
     */
    public <T> Map<String, T> asMap();

    public String toString();

    public String getRawRow();
}

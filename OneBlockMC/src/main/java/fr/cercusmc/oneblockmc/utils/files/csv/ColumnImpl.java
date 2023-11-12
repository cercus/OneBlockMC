package fr.cercusmc.oneblockmc.utils.files.csv;

import fr.cercusmc.oneblockmc.utils.files.Column;
import fr.cercusmc.oneblockmc.utils.files.Row;
import org.apache.commons.lang.NotImplementedException;

public class ColumnImpl<T> implements Column<T> {

    private final T value;

    private final Class<?> type;

    private final int colNum;

    private final String columnName;

    protected ColumnImpl(T value, Class<?> type, String columnName, int colNum){
        this.colNum = colNum;
        this.columnName = columnName;
        this.value = value;
        this.type = type;

    }


    /**
     * Get the value of this column
     *
     * @return The value of this column
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * Get the column type. <br />
     * See ...
     *
     * @return The column type
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * Get the index of column
     *
     * @return The index of column
     */
    @Override
    public int getColNum() {
        return colNum;
    }

    /**
     * Get the row associated at this column
     *
     * @return The row
     */
    @Override
    public Row getRow() throws NotImplementedException {
        return null;
    }

    /**
     * Get the name of column
     *
     * @return The name of column
     */
    @Override
    public String getColName() {
        return columnName;
    }
}

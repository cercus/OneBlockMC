package fr.cercusmc.oneblockmc.utils.files;

public interface Column<T> {

    /**
     * Get the value of this column
     * @return The value of this column
     */
    public T getValue();

    /**
     * Get the column type. <br />
     * See ...
     * @return The column type
     */
    public Class<?> getType();

    /**
     * Get the index of column
     * @return The index of column
     */
    public int getColNum();

    /**
     * Get the row associated at this column
     * @return The row
     */
    public Row getRow();

    /**
     * Get the name of column
     * @return The name of column
     */
    public String getColName();

    public String toString();
}

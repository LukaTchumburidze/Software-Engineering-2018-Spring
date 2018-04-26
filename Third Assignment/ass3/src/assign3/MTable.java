package assign3;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * TableModel for Metropolis class
 */

public class MTable extends AbstractTableModel {

    private int nColumns;
    private ArrayList<String>[] data;
    //Column names
    private String[] CNames;

    public MTable (String[] ColumnNames) {
        super();
        nColumns = ColumnNames.length;
        CNames = ColumnNames;

        //Create ArrayList
        data = new ArrayList[nColumns];
        for (int i = 0; i < data.length; i ++) {
            data[i] = new ArrayList<String>();
        }
    }

    @Override
    public String getColumnName(int col) {
        return CNames[col];
    }

    @Override
    public int getRowCount() {
        return data[0].size();
    }

    @Override
    public int getColumnCount() {
        return nColumns;
    }

    private void clearArrayListData () {
        for (int i = 0; i < nColumns; i ++) {
            data[i].clear();
        }
    }

    /**
     * Following function updates data ArrayList's
     * contents according to passed ResultSet
     * @param rs
     */

    public void passNewData (ResultSet rs) {
        clearArrayListData ();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int cnt = 0; rs.next(); cnt ++) {
                for (int i = 1; i <= rsmd.getColumnCount(); i ++) {
                    data[i-1].add(rs.getString(i));
                }
            }
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Just shows contents of last insertion executed
     * by clicking Add
     * @param mVal
     * @param cVal
     * @param pVal
     */

    public void afterAdd (String mVal, String cVal, String pVal) {
        clearArrayListData();

        data[0].add(mVal);
        data[1].add(cVal);
        data[2].add(pVal);
        fireTableDataChanged();
    }

    /**
     * Gets value of cell in O(1)
     * @param rowIndex
     * @param columnIndex
     * @return
     */

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[columnIndex].get(rowIndex);
    }
}

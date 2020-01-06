import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

public class SapJavaConnector {
    
    // Access Point
    public JCoDestination destination;
    
    // Execution Function
    public JCoFunction function;

    /**
     * Receive the constructor property file name and the name of the function to be called and establish a connection. 
     * Perform until the function is obtained from the repository.
     * 
     * @param propKey
     * @param functionName
     * @throws JCoException
     */
    public JcoRfcCall(JCoDestination destination, String functionName) throws JCoException {
        this.destination = destination;
        this.function = this.destination.getRepository().getFunction(functionName);

    }

    /**
     * 
     * @throws JCoException
     */
    public void execute() throws JCoException {
        this.function.execute(this.destination);
    }

    /**
     * Import Parameter Setter
     *
     * @param map
     */
    public void setImportParameter(Map<String, String> map) {
        JCoParameterList listParams = this.function.getImportParameterList();
        Set keySet = map.keySet();
        Iterator keyIte = keySet.iterator();
        while (keyIte.hasNext()) {
            String key = (String) keyIte.next();
            String value = map.get(key);
            listParams.setValue(key, value);
        }
    }

    /**
     * Getter for table parameters
     * 
     * @param tableName
     * @return List<Map>
     */
    public List<Map<String, String>> getTableParameter(String tableName) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        JCoTable tableList = this.function.getTableParameterList().getTable(tableName);
        if (tableList.getNumRows() > 0) {
            do {
                Map<String, String> map = new HashMap<String, String>();
                for (JCoFieldIterator fI = tableList.getFieldIterator(); fI.hasNextField();) {
                    JCoField tabField = fI.nextField();
                    map.put(tabField.getName(), tabField.getString());

                }
                result.add(map);
            } while (tableList.nextRow() == true);
        }
        return result;
    }

    /**
     * Table parameter setter
     *
     * @param tableName
     * @param records
     */
    public void setTableParameter(String tableName, List<Map<String, String>> records) {
        JCoTable tableList = this.function.getTableParameterList().getTable(tableName);
        tableList.appendRows(records.size());
        for (Map<String, String> record : records) {
            Set keySet = record.keySet();
            Iterator keyIte = keySet.iterator();
            while (keyIte.hasNext()) {
                String key = (String) keyIte.next();
                String value = record.get(key);
                tableList.setValue(key, value);
            }
            tableList.nextRow();
        }
    }

    /**
     * Export Parameter Getter
     * 
     * @return Map
     */
    public Map<String, String> getExportParameter() {
        Map<String, String> result = new HashMap<String, String>();
        JCoParameterList listParams = this.function.getExportParameterList();
        for (JCoFieldIterator fI = listParams.getFieldIterator(); fI.hasNextField();) {
            JCoField field = fI.nextField();
            result.put(field.getName(), field.getString());
        }
        return result;
    }
}

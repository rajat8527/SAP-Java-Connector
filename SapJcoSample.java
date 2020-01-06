import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;

public class SapJcoSample {
	public static void main(String[] args) throws JCoException {
		
    // Get Destination
	  JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");

		JcoRfcCall jcoRfcCall = new JcoRfcCall(destination, "RFC_READ_TABLE");
		
		// Set Import Parameters
		jcoRfcCall.setImportParameter(new HashMap<String, String>() {{
			put("QUERY_TABLE", "T001W"); // Table name
			put("DELIMITER", "\t"); // Data Delimiter
			put("NO_DATA", " "); // Handling when there is no data
			put("ROWSKIPS", "0"); // Number of rows to skip
			put("ROWCOUNT", "10"); // Number of rows to fetch
		}});
		
		// Set Table Parameters
		List<Map<String, String>> tableParam = new ArrayList<Map<String, String>>();
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "MANDT"); }});
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "WERKS"); }});
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "NAME1"); }});
		jcoRfcCall.setTableParameter("FIELDS", tableParam);
		
		// Run
		jcoRfcCall.execute();
		
		// Result Processing
		List<Map<String, String>> headers = jcoRfcCall.getTableParameter("FIELDS"); // Select Target Column
		List<Map<String, String>> records = jcoRfcCall.getTableParameter("DATA"); // Select Data

		// Output Header
		for(Map<String, String> header: headers) {
			System.out.print(header.get("FIELDNAME") + "\t");
		}
    
		System.out.print("\n");
    
		// Output Data
		for(Map<String, String> record: records) {
			System.out.println(record.get("WA")); // RFC_READ_TABLE Is stored in the WA with data separated by DELIMITER
		}
		 
		return;
	}
}

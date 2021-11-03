package tk.icudi.analyze;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public interface ApiCaller {

	default String callURL(String urlString) {
		try {
			URL url = new URL(urlString);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			
			StringBuilder builder = new StringBuilder();
			while (null != (strTemp = br.readLine())) {
				builder.append(strTemp).append("\n");
			}
			
			return builder.toString();
		} catch (Exception ex) {
			throw new RuntimeException("could not connect to'"  + urlString + "'", ex);
		}
	}
}

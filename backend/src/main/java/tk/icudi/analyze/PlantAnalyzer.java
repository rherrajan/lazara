package tk.icudi.analyze;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.drew.lang.GeoLocation;

public class PlantAnalyzer {

	private static final int MINIMUM_SCORE = 20;
	
	private ApiCaller apiCaller;

	public PlantAnalyzer(ApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}
	
	public PlantAnalyzer() {
		this(new ApiCaller() {});
	}

	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
	    response.setHeader("Access-Control-Allow-Origin", "*");	    
	}    
	
	public IdentificationResult analyze(String plantURL, String metadata, String uuid) {
		String responseJson = analyzePlant(plantURL);
		IdentificationResult result = extractResult(responseJson);
		return appendMetadata(result, plantURL, metadata, uuid);
	}
	
	String analyzePlant(String plantURL) {
		String plantURLEnc;
		try {
			plantURLEnc = URLEncoder.encode(plantURL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("i can't write UTF-8", e);
		}
		String organ = "flower";
		String analyzeURL = createAnalyseURL(plantURLEnc, organ);
		
		return apiCaller.callURL(analyzeURL);
	}

	private String createAnalyseURL(String plantURLEnc, String organ) {
		return "https://my-api.plantnet.org/v2/identify/all?api-key=2b10hw2I9itlXE4zCJSEZRzvpe&images=" + plantURLEnc + "&organs=" + organ + "&include-related-images=true";
	}

	IdentificationResult extractResult(String responseJson) {
		JSONObject analyzation = new JSONObject(responseJson);
		JSONObject firstResult = analyzation.getJSONArray("results").getJSONObject(0);
		long score = Math.round(firstResult.getDouble("score") * 100);
		if(score < MINIMUM_SCORE){
			System.out.println("score with '"  + score + "' is too low");
			return null;
		}
		
		IdentificationResult result = new IdentificationResult();
		result.setScore(score);
		
		JSONObject species = firstResult.getJSONObject("species");
		result.setFamily(species.getJSONObject("family").getString("scientificNameWithoutAuthor"));
		result.setGenus(species.getJSONObject("genus").getString("scientificNameWithoutAuthor"));
		result.setScientificName(species.getString("scientificNameWithoutAuthor"));
		JSONArray commonNames = species.getJSONArray("commonNames");
		if(commonNames.length() > 0) {
			result.setCommonName(commonNames.getString(0));
		}
		
		JSONObject firstImage = firstResult.getJSONArray("images").getJSONObject(0);
		result.setReferenceImage(firstImage.getJSONObject("url").getString("m"));
		
		return result;
	}
	
	IdentificationResult appendMetadata(IdentificationResult result, String planturl, String metadata,
			String uuid) {
				
		JSONObject exifdata = new JSONObject(metadata).getJSONObject("exifdata");
		GeoLocation coords = ExifConverter.getCoords(exifdata);
		if(coords == null) {
			return null;
		}
		
		result.setLatitude(coords.getLatitude());
		result.setLongitude(coords.getLongitude());
		result.setShotTime(exifdata.optString("DateTime"));
		result.setUploadTime((String.valueOf(System.currentTimeMillis())));

		result.setPlanturl(planturl);
		result.setUuid(uuid);
		
		return result;
	}

}

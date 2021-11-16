package tk.icudi;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tk.icudi.analyze.IdentificationResult;
import tk.icudi.analyze.PlantAnalyzer;

@Controller
public class PlantOrchestrator {

	private PlantAnalyzer plantAnalyzer = new PlantAnalyzer();

	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
	}

	@RequestMapping(value = "/analyze")
	public ResponseEntity<String> analyze(@RequestParam("plantURL") String plantURL,
			@RequestParam("metadata") String metadataRaw, @RequestParam("uuid") String uuid) {

		IdentificationResult result = plantAnalyzer.analyze(plantURL, metadataRaw, uuid);
		
		return convertToJsonResponse(result);
	}

	ResponseEntity<String> convertToJsonResponse(IdentificationResult result) {
		ObjectMapper mapper = new ObjectMapper();
		
		String json;
		try {
			json = mapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("error durning json generation: " + e, e);
		}
		
		JSONObject jsonResult = new JSONObject(json);

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(jsonResult.toString(), httpHeaders, HttpStatus.OK);
	}

}

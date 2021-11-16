package tk.icudi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tk.icudi.analyze.IdentificationResult;

class PlantOrchestratorTest {

	@Test
	void shouldConvertToHTMLResponse() {
		
		PlantOrchestrator plantOrchestrator = new PlantOrchestrator();
		
		IdentificationResult toConvert = new IdentificationResult();
		ResponseEntity<String> result = plantOrchestrator.convertToJsonResponse(toConvert);
		
		assertThat(result.getStatusCode(), is(HttpStatus.OK));
		assertThat(result.getBody(), containsString("scientificName"));
	}

}

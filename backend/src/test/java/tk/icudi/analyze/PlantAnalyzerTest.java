package tk.icudi.analyze;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class PlantAnalyzerTest {

	private final static String plantUrl = "https://firebasestorage.googleapis.com/v0/b/alchemy-dfebf.appspot.com/o/images%2FIMG_20210928_150936.jpg?alt=media&token=ee5b3aac-c396-471a-80e1-b55311acc212";

	private PlantAnalyzer plantAnalyzer;
	private ApiCaller apiCaller;

	@BeforeEach
	private void setup() {
		this.apiCaller = Mockito.mock(ApiCaller.class);
		this.plantAnalyzer = new PlantAnalyzer(apiCaller);
	}

	@Test
	void shouldCallCorrectURL() {

		plantAnalyzer.analyzePlant(plantUrl);

		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(apiCaller).callURL(stringCaptor.capture());
		String expectedUrl = "https://my-api.plantnet.org/v2/identify/all?api-key=2b10hw2I9itlXE4zCJSEZRzvpe&images=https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Falchemy-dfebf.appspot.com%2Fo%2Fimages%252FIMG_20210928_150936.jpg%3Falt%3Dmedia%26token%3Dee5b3aac-c396-471a-80e1-b55311acc212&organs=fruit&include-related-images=true";
		assertThat(stringCaptor.getValue(), is(expectedUrl));
	}

	@Test
	void shouldProcessAnalyzeResults() throws Exception {

		String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/AnalyzeResult.json")));
		IdentificationResult result = plantAnalyzer.extractResult(responseJson);

		assertThat(result.getFamily(), is("Rosaceae"));
		assertThat(result.getGenus(), is("Crataegus"));
		assertThat(result.getScientificName(), is("Crataegus monogyna"));
		assertThat(result.getCommonName(), is("Hawthorn"));
		assertThat(result.getScore(), is(42L));
		assertThat(result.getReferenceImage(),
				is("https://bs.plantnet.org/image/m/ea7f55f2f50b10b09fb3cdf9226cbb8e2cdf7c3d"));
	}

	@Test
	void shouldAppendMetadata() throws Exception {

		String metadata = new String(Files.readAllBytes(Paths.get("src/test/resources/Metadata.json")));
		String uuid = "e2c445b1-8349-43b6-8c4c-a6ff247b6652";
		
		IdentificationResult result = new IdentificationResult();
		
		result = plantAnalyzer.appendMetadata(result, plantUrl, metadata, uuid);
		
		assertThat(result.getLatitude(), is(49.952854156388895));
		assertThat(result.getLongitude(), is(8.335708618055556));
		assertThat(result.getShotTime(), is("2021:09:28 15:09:36"));
		assertThat(result.getUploadTime(), is(notNullValue()));
		assertThat(result.getUuid(), is(uuid));
	}
	
	@Test
	void shouldReturnErrorResult() throws Exception {

		String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/AnalyzeResultBadPicture.json")));
		IdentificationResult result = plantAnalyzer.extractResult(responseJson);
		
		assertThat(result, notNullValue());
	}
	
	
}

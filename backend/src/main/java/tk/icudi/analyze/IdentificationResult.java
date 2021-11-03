package tk.icudi.analyze;

public class IdentificationResult {

	private String family;
	private String genus;
	private String scientificName;
	private String commonName;
	private long score;
	private String referenceImage;
	
	private double latitude;
	private double longitude;
	private String shotTime;
	private String uploadTime;
	
	private String planturl;
	private String uuid;
	
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonNames) {
		this.commonName = commonNames;
	}
	
	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getReferenceImage() {
		return referenceImage;
	}

	public void setReferenceImage(String referenceImage) {
		this.referenceImage = referenceImage;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getShotTime() {
		return shotTime;
	}

	public void setShotTime(String shotTime) {
		this.shotTime = shotTime;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uoploadTime) {
		this.uploadTime = uoploadTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlanturl() {
		return planturl;
	}

	public void setPlanturl(String planturl) {
		this.planturl = planturl;
	}
}


public class Planet {

	private Integer solarSystemId;
	private Integer radius;
	private Integer axis;
	private String name;
	private Integer starRadius = 1;
	private Integer starTeff = 1;
	
	// TODO
	// planet colour?
	
	Planet(Integer id, String planetName, Integer pRadius, Integer semimajorAxis) {
		solarSystemId = id;
		name = planetName;
		radius = pRadius;
		axis = semimajorAxis;
	}
	
	public void setSolarSystemId(Integer id) {
		solarSystemId = id;
	}
	
	public void setPlanetName(String pName) {
		name = pName;
	}
	
	public void setPlanetRadius(Integer pRadius) {
		radius = pRadius;
	}
	
	public void setSemimajorAxis(Integer semimajorAxis) {
		axis = semimajorAxis;
	}
	
	public void setStarRadius(Integer radius) {
		starRadius = radius;
	}
	
	public void setStarTeff(Integer teff) {
		starTeff = teff;
	}

	public Integer getSolarSystemId() {
		return solarSystemId;
	}
	
	public String getPlanetName() {
		return name;
	}
	
	public Integer getPlanetRadius() {
		return radius;
	}
	
	public Integer getSemimajorAxis() {
		return axis;
	}
	
	public Integer getStarRadius() {
		return starRadius;
	}
	
	public Integer getStarTeff() {
		return starTeff;
	}
	
}

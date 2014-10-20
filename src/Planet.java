
public class Planet {

	private Integer solarSystemId;
	private Integer radius;
	private Integer axis;
	private Integer period;
	private String name;
	private String colour;
	private Integer starRadius = 1;
	private Integer starTeff = 1; 
	
	public Planet(Integer id, String planetName, Integer pRadius, Integer semimajorAxis, Integer pPeriod, String pColour) {
		solarSystemId = id;
		name = planetName;
		radius = pRadius;
		axis = semimajorAxis;
		period = pPeriod;
		colour = pColour;
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
	
	public void setPeriod(Integer pPeriod) {
		period = pPeriod;
	}
	
	public void setColour(String pColour){
		colour = pColour;
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
	
	public Integer getPeriod() {
		return period;
	}
	
	public String getColour(){
		return colour;
	}
	
	public Integer getStarRadius() {
		return starRadius;
	}
	
	public Integer getStarTeff() {
		return starTeff;
	}
	
	@Override
	public boolean equals(Object otherPlanet) {
		if (otherPlanet == null) {
			return false;
		} else if (otherPlanet.getClass() != this.getClass()) {
			return false;
		} else {
			Planet other = (Planet) otherPlanet;
			return (this.name.equals(other.name) && 
					this.solarSystemId == other.solarSystemId);
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + solarSystemId.hashCode();
	}
	
}

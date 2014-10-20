
public class Planet {

	private Integer solarSystemId;
	private Integer radius;
	private float axis;
	private Integer period;
	private String name;
	private String hue;
	private Integer starRadius = 1;
	private Integer starTeff = 1; 
	
	public Planet(Integer id, String pName, Integer pRadius, float semimajorAxis, Integer pPeriod, String pHue) {
		solarSystemId = id;
		name = pName;
		radius = pRadius;
		axis = semimajorAxis;
		period = pPeriod;
		hue = pHue;
	}
	
	// id is the id of the solar system project it belongs to
	public void setSolarSystemId(Integer id) {
		solarSystemId = id;
	}
	
	// planet name is name of class it represents
	public void setPlanetName(String pName) {
		name = pName;
	}
	
	// planet radius corresponds to number of methods
	public void setPlanetRadius(Integer pRadius) {
		radius = pRadius;
	}
	
	// correlates to number of revisions in a class (more revisions -> closer to centre)
	public void setSemimajorAxis(float semimajorAxis) {
		axis = semimajorAxis;
	}
	
	// corresponds to "churn" in project (speed set for entire project/solar system)
	public void setPeriod(Integer pPeriod) {
		period = pPeriod;
	}
	
	// colour is based on number of developers; hue is average length of method (#sloc/#methods)
	public void setHue(String pHue){
		hue = pHue;
	}
	
	// this is fixed for all planets and all systems
	public void setStarRadius(Integer radius) {
		starRadius = radius;
	}

	// this is fixed for all planets and all systems
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
	
	public float getSemimajorAxis() {
		return axis;
	}
	
	public Integer getPeriod() {
		return period;
	}
	
	public String getHue(){
		return hue;
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

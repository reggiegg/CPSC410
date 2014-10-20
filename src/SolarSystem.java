import java.util.ArrayList;

public class SolarSystem {

	private Integer id;
	private ArrayList<Planet> planets;
	// TODO figure out how to pass base colour into Planet file for hex math
	private String colour;
	private Integer speed;
	
	public SolarSystem(Integer id) {
		this.id = id;
		planets = new ArrayList<Planet>();
	}
	
	// colour correlates with number of contributors
	public void setColour(Integer devs) {
		colour = String.valueOf(devs.intValue());
	}
	
	// speed of system correlates with "churn"
	public void setSpeed(Integer churn) {
		speed = churn;
	}
	
	// each planet represents one class in the project
	public void addPlanet(Planet p) {
		planets.add(p);
	}
	
	public void setPlanets(ArrayList<Planet> planets) {
		this.planets = planets;
	}
	
	public Integer getId() {
		return id;
	}
	
	public ArrayList<Planet> getPlanets() {
		return planets;
	}
	
	public Integer getSpeed() {
		return speed;
	}
	
	public String getColour() {
		return colour;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other.getClass() != this.getClass()) {
			return false;
		} else {
			SolarSystem otherSystem = (SolarSystem) other;
			return (this.id == otherSystem.id && this.planets.equals(otherSystem.planets));
		}
	}
	
	@Override
	public int hashCode() {
		return (this.id.hashCode() + this.planets.hashCode());
	}
	
}

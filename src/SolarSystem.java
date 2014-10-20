import java.util.ArrayList;

public class SolarSystem {

	private Integer id;
	private ArrayList<Planet> planets;
	private String colour;
	private Integer speed;
	
	public SolarSystem(Integer id) {
		this.id = id;
		planets = new ArrayList<Planet>();
	}
	
	// colour correlates with number of contributors, heatmap style
	public void setColour(Integer devs) {
		if (devs <= 3)
			colour = "800080"; // purple
		else if (3 < devs && devs <= 6) 
			colour = "0000FF"; // blue
		else if (6 < devs && devs <= 9)
			colour = "008000"; // green
		else if (9 < devs && devs <= 12)
			colour = "FFFF00"; // yellow
		else if (12 < devs && devs <= 15)
			colour = "FFA500"; // orange
		else if (15 < devs)
			colour = "FF0000"; // red
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

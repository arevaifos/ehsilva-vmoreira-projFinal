package pt.uc.dei.helpers;

/**
 * @author
 *
 */
public class Gantt {
	
	private String name;
	private String value;
	
	
	public Gantt(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public Gantt() {
		super();
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Gantt [name=" + name + ", value=" + value + "]";
	}
	
	
	

}

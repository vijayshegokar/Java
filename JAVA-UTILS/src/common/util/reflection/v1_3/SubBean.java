package common.util.reflection.v1_3;

public class SubBean {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SubBean [name=" + name + "]";
	}

}

package test.resources;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private String id;

	// private List<SubEntity> list = new ArrayList<SubEntity>();
	// private List<Integer> ages = new ArrayList<Integer>();
	// private SubEntity bean = new SubEntity();

	public Entity() {
	}

	public Entity(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Entity [id=" + id + "]";
	}

	// public List<SubEntity> getList() {
	// return list;
	// }
	//
	// public void setList(List<SubEntity> list) {
	// this.list = list;
	// }
	//
	// public List<Integer> getAges() {
	// return ages;
	// }
	//
	// public void setAges(List<Integer> ages) {
	// this.ages = ages;
	// }
	//
	// public SubEntity getBean() {
	// return bean;
	// }
	//
	// public void setBean(SubEntity bean) {
	// this.bean = bean;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// return 11;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#equals(java.lang.Object)
	// */
	// @Override
	// public boolean equals(Object arg0) {
	// return super.equals(arg0);
	// }
	//
	// public static void main(String[] args) {
	// Entity e1 = new Entity();
	// Entity e2 = new Entity();
	// System.out.println(e1.equals(e2));
	// }
	//
	// @Override
	// public String toString() {
	// return "Entity [id=" + id + ", list=" + list + ", ages=" + ages
	// + ", bean=" + bean + "]";
	// }

}

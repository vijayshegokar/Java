package test.resources;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private String id;
	private List<SubEntity> list = new ArrayList<SubEntity>();
	private List<Integer> ages = new ArrayList<Integer>();
	private SubEntity bean = new SubEntity();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SubEntity> getList() {
		return list;
	}

	public void setList(List<SubEntity> list) {
		this.list = list;
	}

	public List<Integer> getAges() {
		return ages;
	}

	public void setAges(List<Integer> ages) {
		this.ages = ages;
	}

	public SubEntity getBean() {
		return bean;
	}

	public void setBean(SubEntity bean) {
		this.bean = bean;
	}

}

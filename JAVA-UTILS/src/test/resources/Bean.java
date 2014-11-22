package test.resources;

import java.util.ArrayList;
import java.util.List;

public class Bean {

	private String id;
	private List<SubBean> list = new ArrayList<SubBean>();
	private List<Integer> ages = new ArrayList<Integer>();
	private SubBean bean = new SubBean();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SubBean> getList() {
		return list;
	}

	public void setList(List<SubBean> list) {
		this.list = list;
	}

	public List<Integer> getAges() {
		return ages;
	}

	public void setAges(List<Integer> ages) {
		this.ages = ages;
	}

	public SubBean getBean() {
		return bean;
	}

	public void setBean(SubBean bean) {
		this.bean = bean;
	}

	@Override
	public String toString() {
		return "Bean [id=" + id + ", list=" + list + ", ages=" + ages
				+ ", bean=" + bean + "]";
	}

}

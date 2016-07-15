package com.baodian.util.page;

public class ProjectPage extends Page {
	
	private String name;
	/**
	 * 计算页数
	 * @param countNums
	 */
	@Override
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(10);
		super.countPage(countNums);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

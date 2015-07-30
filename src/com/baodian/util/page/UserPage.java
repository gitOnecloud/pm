package com.baodian.util.page;

public class UserPage extends Page {
	
	private int dpmId;
	private String name;
	private String account;
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
//set get
	public int getDpmId() {
		return dpmId;
	}
	public void setDpmId(int dpmId) {
		this.dpmId = dpmId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
}

package com.liu.model;

 /** 
 * @ClassName: Link 
 * @author: lyd
 * @date: 2017��9��18�� ����7:28:11 
 * @describe:����
 */
public class Link {
	private Integer id;
	private String linkname;//��������
	private String linkurl;//���ӵ�ַ
	private Integer orderNum;//���ȼ�
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLinkname() {
		return linkname;
	}
	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
}

package com.liu.model;

 /** 
 * @ClassName: BlogType 
 * @author: lyd
 * @date: 2017��9��18�� ����7:17:39 
 * @describe:��������
 */
public class BlogType {
	private Integer id;
	private String typeName;//��������
	private Integer orderNum;//���ȼ�
	private Integer blogCount; //��������
	public Integer getBlogCount() {
		return blogCount;
	}
	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	
}

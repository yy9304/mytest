package com.liu.model;

import java.util.Date;

 /** 
 * @ClassName: Comment 
 * @author: lyd
 * @date: 2017��9��18�� ����7:25:28 
 * @describe:����
 */
public class Comment {
	private Integer id;
	private String userIp;//�û�Ip��ַ
	private String content;//�����۵�����
	private Date commentDate;//��������
	private Integer state;//�Ƿ�ͨ������
	private Blog blog;//�����۵Ĳ���
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Blog getBlog() {
		return blog;
	}
	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
}

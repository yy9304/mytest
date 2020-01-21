package com.liu.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

 /** 
 * @ClassName: Blog 
 * @author: lyd
 * @date: 2017��9��18�� ����7:25:43 
 * @describe:����ʵ��
 */
public class Blog {
	private Integer id; //����id
	private String title;//���ͱ���
	private String summary;//�������ݼ��
	private Date releaseDate;//����
	private Integer clickHit;//�����
	private Integer replyHit;//�ظ���
	private String content;//��������
	private String contentNoTag;//���Ĳ�����ǩ
	private String keyWord;//�ؼ���
	private BlogType blogType;//��������
	private Integer blogCount;//�����������ǲ���ʵ�����ԣ����ڸ��ݷ������ڹ鵵��ѯ
	private String releaseDateStr;//�������ڵ��ַ�����ֻȡ����
	private List<String>imageList=new LinkedList<String>();//����������������ͼƬ��ַ
	public BlogType getBlogType() {
		return blogType;
	}
	public void setBlogType(BlogType blogType) {
		this.blogType = blogType;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public Integer getBlogCount() {
		return blogCount;
	}
	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
	public String getReleaseDateStr() {
		return releaseDateStr;
	}
	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Integer getClickHit() {
		return clickHit;
	}
	public void setClickHit(Integer clickHit) {
		this.clickHit = clickHit;
	}
	public Integer getReplyHit() {
		return replyHit;
	}
	public void setReplyHit(Integer replyHit) {
		this.replyHit = replyHit;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentNoTag() {
		return contentNoTag;
	}
	public void setContentNoTag(String contentNoTag) {
		this.contentNoTag = contentNoTag;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", summary=" + summary + ", releaseDate=" + releaseDate
				+ ", clickHit=" + clickHit + ", replyHit=" + replyHit + ", content=" + content + ", contentNoTag="
				+ contentNoTag + ", keyWord=" + keyWord + ", blogType=" + blogType + ", blogCount=" + blogCount
				+ ", releaseDateStr=" + releaseDateStr + "]";
	}
	
	
}

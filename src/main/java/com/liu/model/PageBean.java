package com.liu.model;

public class PageBean {
	private int page;//�ڼ�ҳ
	private int pageSize;//ÿҳ��¼��
	private int start;//��ʼҳ
//	private int totalPage;
	public PageBean(int page, int pageSize) {
		super();
		this.page = (page-1)*pageSize;//�������ݿ�limit��ʼ
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}

	
}

package com.liu.service;

import java.util.List;
import java.util.Map;

import com.liu.model.Link;

public interface LinkService {
	public List<Link> getLinkData();

	// ��ҳ��ȡ��������
	public List<Link> listLinkData(Map<String, Object> map);

	// ��ȡ�ܼ�¼��
	public Long getTotal();

	// �����������
	public Integer addLink(Link link);

	// ������������
	public Integer updateLink(Link link);

	// ɾ����������
	public Integer deleteLink(Integer id);
}

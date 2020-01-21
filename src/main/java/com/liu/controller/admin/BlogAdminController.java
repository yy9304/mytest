package com.liu.controller.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.json.Json;
import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.liu.Utils.DateJsonValueProcessor;
import com.liu.Utils.ResponseUtil;
import com.liu.Utils.StringUtil;
import com.liu.lucene.BlogIndex;
import com.liu.model.Blog;
import com.liu.model.BlogType;
import com.liu.model.PageBean;
import com.liu.service.BlogService;
import com.liu.service.BlogTypeService;
import com.liu.service.CommentService;
import com.mysql.fabric.Response;

@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {
	@Resource
	private BlogService blogService;
	@Resource
	private CommentService commentService;
	@Resource
	private  BlogTypeService blogTypeService;
	BlogIndex blogIndex=new BlogIndex();//�������������ڼ�������
	//��Ӹ��²���
	@RequestMapping("/save")
	public String save(Blog blog,HttpServletResponse response,HttpServletRequest request)throws Exception
	{
		int resultTotal=0;
		if(blog.getId()==null){//���ò����Ƿ���id��û������б������
			resultTotal=blogService.addBlog(blog);
			blogIndex.addIndex(blog);//�������
			
		}
		else{
			resultTotal=blogService.update(blog);//�޸Ĳ���
			blogIndex.updateIndex(blog);//�޸�����
			
		}
		JSONObject result=new JSONObject();//����json����
		if(resultTotal>0)
		{
			int allBlogs=blogService.getAllBlog().size();
			request.getSession().getServletContext().setAttribute("countsallblogs", allBlogs);//���º���ж�servletContext�е�����
		result.put("success", true);
		}
		else
		{
		result.put("success", false);
		}
		ResponseUtil.write(response, result);//��ǰ̨����json��Ϣ
		return null;
	}
	//��̨��ҳ��ʾ����
	@RequestMapping("/listBlog")
	public String listBlogType(@RequestParam(value="page",required=false)String page,
							   @RequestParam(value="rows",required=false)String rows,
							   Blog s_blog,
							   HttpServletResponse response)throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("title", StringUtil.formatLike(s_blog.getTitle()));
		map.put("start", pageBean.getPage());
		map.put("pageSize", pageBean.getPageSize());
		List<Blog> blogList = blogService.listBlog(map);
		Long total = blogService.getTotal(map);
		JSONObject result = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class,new DateJsonValueProcessor("yyyy-MM-dd"));//����json���ã����ڽ�sql����ת��Ϊ��Ӧ��ʽ�����ڴ��ݵ�ǰ̨������ʾ
		JSONArray jsonArray = JSONArray.fromObject(blogList,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);//���ݵ�ǰ̨
		return null;
	}
	//����ɾ��
	@RequestMapping("/delete")
	public String deleteBlog(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response,HttpServletRequest request)throws Exception{
		String [] idsStr=ids.split(",");//��ֹ�ѡ�����id
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			commentService.deleteComment(id);//ɾ������
			blogService.deleteBlog(id);//ɾ������
			blogIndex.deleteIndex(idsStr[i]);//ɾ������
		}
		int allBlogs=blogService.getAllBlog().size();
		request.getSession().getServletContext().setAttribute("countsallblogs", allBlogs);
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	@RequestMapping("/findById")
	public String findById(@RequestParam(value="id", required=false)String id,
			HttpServletResponse response)throws Exception
	{
		Blog blog=blogService.findById(Integer.parseInt(id));
		JSONObject result=JSONObject.fromObject(blog);
		ResponseUtil.write(response, result);
		return null;
	}
}

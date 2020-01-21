package com.liu.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.jws.WebParam.Mode;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import javax.ws.rs.Path;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.liu.Utils.DateUtil;
import com.liu.Utils.PageUtil;
import com.liu.Utils.StringUtil;
import com.liu.lucene.BlogIndex;
import com.liu.model.Blog;
import com.liu.model.BlogType;
import com.liu.model.Blogger;
import com.liu.model.Comment;
import com.liu.model.Link;
import com.liu.model.PageBean;
import com.liu.model.Photo;
import com.liu.service.BlogService;
import com.liu.service.BlogTypeService;
import com.liu.service.BloggerService;
import com.liu.service.CommentService;
import com.liu.service.LinkService;
import com.liu.service.PhotoServiceImpl;
import com.mysql.fabric.Server;

 /** 
 * @ClassName: BlogController 
 * @author: lyd
 * @date: 2017��10��10�� ����4:48:16 
 * @describe:ǰ̨������ϢController
 */
@Controller
@RequestMapping("/blog")
public class BlogController {
	@Resource
	private BlogService blogService;
	@Resource
	private CommentService commentService;
	@Resource
	private BloggerService bloggerService;
	@Resource
	private LinkService linkService;
	@Resource
	private BlogTypeService blogTypeService;
	@Resource
	private PhotoServiceImpl photoServiceImpl;
	private BlogIndex blogIndex=new BlogIndex();//��������

	/**  
	* @Title: blogByPage  
	* @Description: ����ҳ����ȡ����
	*/  
	@RequestMapping("/{nowPage}")
	public ModelAndView blogByPage(@PathVariable("nowPage")Integer nowPage) throws Exception
	{
		ModelAndView modelAndView=new ModelAndView();
		int pageSize=5;
		int blogcounts=blogService.getAllBlog().size();//��ȡ��������
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("start", (nowPage-1)*pageSize);
		map.put("pageSize", pageSize);
		List<Blog> blogByPage=blogService.listBlog(map);
		for (Blog blog : blogByPage) {
			blog.setReleaseDateStr(DateUtil.formatString2(blog.getReleaseDate().toString()));
		}
		int totalPage=(int)Math.ceil(blogcounts*1.0/pageSize);//��ȡ�ܵ�ҳ��
		modelAndView.addObject("blogList", blogByPage);
		modelAndView.addObject("firsttotalPage", totalPage);
		modelAndView.addObject("firstnowPage", nowPage);
		modelAndView.setViewName("index");
		return modelAndView;
	}
	/**  
	* @Title: details  
	* @Description: ����Id��ȡ������ϸ����
	*/  
	@RequestMapping("/articles/{id}")
	public ModelAndView details(@PathVariable("id")Integer id,HttpServletRequest request) throws Exception{
		ModelAndView modelAndView=new ModelAndView();
		Blog blog=blogService.findById(id);
		// ��ȡ�ؼ���
		String keyWords=blog.getKeyWord();
		if(StringUtil.isNotEmpty(keyWords))//��ʾ�ؼ���
		{
			String [] strArray=keyWords.split(" ");
			List<String> keyWordList=StringUtil.filterWhite(Arrays.asList(strArray));
			modelAndView.addObject("keyWords", keyWordList);
		}else
		{
			modelAndView.addObject("keyWords", null);
		}
		blog.setReleaseDateStr(DateUtil.formatString(blog.getReleaseDate().toString()));
		Date date=blogService.findById(id).getReleaseDate();
		modelAndView.addObject("preBlog", blogService.getPrevBlog(date));//��ȡǰһƪ����
		modelAndView.addObject("nextBlog", blogService.getNextBlog(date));//��ȡ��һƪ����
		modelAndView.addObject("blog", blog);
		blog.setClickHit(blog.getClickHit()+1);//�����+1
		blogService.update(blog);//�޸Ĳ�����Ϣ
		// ��ѯ������Ϣ
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("blogId", blog.getId());
		map.put("state", 1);
		List<Comment> commentList=commentService.getCommentData(map);
		Long totalcomments=commentService.getTotal(map);
		modelAndView.addObject("commentList", commentList);//������Ϣ
		modelAndView.addObject("totalComments",totalcomments);
//		modelAndView.addObject("commonPage", "foreground/blog/blogDetail.jsp");//��ϸ��ҳ��
//		modelAndView.addObject("title", blog.getTitle()+"-����Ĳ���");//����
//		modelAndView.addObject("pageCode", PageUtil.getPrevAndNextPageCode(blogService.getPrevBlog(id), blogService.getNextBlog(id),request.getServletContext().getContextPath()));//��һƪ����һƪ
		modelAndView.setViewName("detail");//��ת��ҳ��
		return modelAndView;
	}

	/**  
	* @Title: blogcategory  
	* @Description: ��ȡ�������
	*/  
	@RequestMapping("/category")
	public ModelAndView blogcategory(){
		ModelAndView modelAndView=new ModelAndView();
		int categoryNum=blogTypeService.getBlogTypeData().size();
		List<BlogType> blogTypeList=blogTypeService.getBlogTypeData();
		for(int i=0;i<blogTypeList.size();i++)
		{
			blogTypeList.get(i).setBlogCount(blogService.getBlogByTypeId(blogTypeList.get(i).getId()));
		}
		modelAndView.addObject("blogTypeList", blogTypeList);
		modelAndView.addObject("blogTypeNum", categoryNum);
		modelAndView.setViewName("category");
		return modelAndView;
	}
	/**  
	* @Title: categories  
	* @Description:��������ȡ��Ӧ���в���
	*/  
	@RequestMapping("/categories/{categoryid}")
	public ModelAndView categories(@PathVariable("categoryid")Integer cid) throws Exception
	{
		ModelAndView modelAndView=new ModelAndView();
		BlogType blogType=blogTypeService.getById(cid);
		modelAndView.addObject("categoryName", blogType.getTypeName());
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("typeId", cid);
		List<Blog> listBlogByType=blogService.listBlogByTypeId(map);
		for(int i=0;i<listBlogByType.size();i++)
		{
			listBlogByType.get(i).setReleaseDateStr(DateUtil.formatString1(listBlogByType.get(i).getReleaseDate().toString()));
		}
		modelAndView.addObject("ListBycategory", listBlogByType);
		modelAndView.setViewName("categories");
		return modelAndView;
	}
	/**  
	* @Title: archives  
	* @Description: ����ҳ�����й鵵
	*/  
	@RequestMapping("/archive/{nowpage}")
	public ModelAndView archives(@PathVariable("nowpage")Integer nowPage) throws Exception{
		ModelAndView modelAndView=new ModelAndView();
		int blogcounts=blogService.getAllBlog().size();
		int pageSize=5;
		Map<String, Object> map=new HashMap<String, Object>();
		int totalPage=(int)Math.ceil(blogcounts*1.0/pageSize);
		map.put("start", (nowPage-1)*pageSize);
		map.put("pageSize", pageSize);
		List<Blog> archivelist=blogService.listBlog(map);
		for(int i=0;i<archivelist.size();i++)
		{
			archivelist.get(i).setReleaseDateStr(DateUtil.formatString1(archivelist.get(i).getReleaseDate().toString()));
		}
		modelAndView.addObject("firstdate", DateUtil.formatString3(archivelist.get(0).getReleaseDate().toString()));
		modelAndView.addObject("blogcount",blogcounts);
		modelAndView.addObject("archivelist",archivelist);
		modelAndView.addObject("nowPage", nowPage);
		modelAndView.addObject("totalPage", totalPage);
		modelAndView.setViewName("archive");
		return	modelAndView;
	}
	/**  
	* @Title: tags  
	* @Description: ��ȡ���б�ǩ
	*/  
	@RequestMapping("/tags")
	public ModelAndView tags(){
		ModelAndView modelAndView=new ModelAndView();
		List<BlogType> tagsList=blogTypeService.getBlogTypeData();
		List<Link> linksList=linkService.getLinkData();
		int tagsTotal=blogTypeService.getBlogTypeData().size();
		int linkTotal=linkService.getLinkData().size();
		int alltotals=tagsTotal+linkTotal;
		modelAndView.addObject("tagsTotal",alltotals);
		modelAndView.addObject("tagsList", tagsList);
		modelAndView.addObject("linksList", linksList);
		modelAndView.setViewName("tags");
		return modelAndView;
	}
	/**  
	* @Title: tagsById  
	* @Description: ���ݱ�ǩ��ȡ��ϸ����
	*/  
	@RequestMapping("/tag/{tagid}")
	public ModelAndView tagsById(@PathVariable("tagid")Integer tid) throws Exception
	{
		ModelAndView modelAndView=new ModelAndView();
		BlogType blogType=blogTypeService.getById(tid);
		modelAndView.addObject("tagName", blogType.getTypeName());
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("typeId", tid);
		List<Blog> listBlogtags=blogService.listBlogByTypeId(map);
		for(int i=0;i<listBlogtags.size();i++)
		{
			listBlogtags.get(i).setReleaseDateStr(DateUtil.formatString1(listBlogtags.get(i).getReleaseDate().toString()));
		}
		modelAndView.addObject("ListBytag", listBlogtags);
		modelAndView.setViewName("tagsdetail");
		return modelAndView;
	}
	//�ϴ�ͼƬ
@RequestMapping("/upload")
public String upload(@RequestParam(value="file",required=true) MultipartFile file,HttpServletRequest request ,ModelMap model)
{
	String path=request.getSession().getServletContext().getRealPath("upload");
	String fileName=file.getOriginalFilename();//��ȡͼƬ����
	File targetFile=new File(path,fileName);
	if(!targetFile.exists())
	{
		targetFile.mkdirs();
	}
	try
	{
		file.transferTo(targetFile);
	}catch (Exception e) {
		e.printStackTrace();
	}
	Photo photo=new Photo();
	if(!fileName.equals(""))
	{
	photo.setPhotoName("upload/"+fileName);
	photoServiceImpl.insertphoto(photo);
	model.addAttribute("fileUrl", request.getContextPath()+"/upload"+fileName);
	}
	return "redirect:/blog/searchphoto";
	
}
//����ȫ��
@RequestMapping("/search")
public ModelAndView search(@RequestParam(value="q",required=false)String q,@RequestParam(value="page",required=false)String page,HttpServletRequest request)throws Exception
{
	SimpleDateFormat format=new SimpleDateFormat("MM-dd");
	int pageSize=10;
	ModelAndView modelAndView=new ModelAndView();
	List<Blog> blogIndexList=blogIndex.searchBlog(q);//����������ݽ��м���
	if(page==null){
		page="1";
	}
//	int fromIndex=(Integer.parseInt(page)-1)*pageSize;//��ʼλ��
//	Integer toIndex=blogIndexList.size()>=Integer.parseInt(page)*pageSize?Integer.parseInt(page)*pageSize:blogIndexList.size();//��ʾ���10������ܸ���
	modelAndView.addObject("blogIndexList", blogIndexList);//��ȡ�ַ��� 
	modelAndView.addObject("pageCode", PageUtil.getUpAndDownPageCode(Integer.parseInt(page), blogIndexList.size(), q, pageSize, request.getServletContext().getContextPath()));
	modelAndView.addObject("q", q);
	modelAndView.addObject("resultTotal", blogIndexList.size());
	modelAndView.setViewName("searchResult");
	return modelAndView;
}
@RequestMapping("/index/{id}")
public ModelAndView refreshBlog(@PathVariable("id")Integer id,HttpServletRequest request)//��ҳ��ʾ��ҳ
{
	ModelAndView modelAndView=new ModelAndView();
	ServletContext application=request.getSession().getServletContext();
//	List<Blog> blogList=blogService.getAllBlog();
	PageBean pageBean=new PageBean(id, 10);
	Map map=new HashMap<String, Object>();
	map.put("start", pageBean.getPage());
	map.put("pageSize", pageBean.getPageSize());
	List<Blog> blogList=blogService.listBlog(map);
	modelAndView.addObject("nowPage", id);
	Map map1=new HashMap<String, Object>();
	Long total=blogService.getTotal(map1);
	Double num=Math.ceil((double)total/10);
	modelAndView.addObject("allPage",num.intValue());
	int[] array=new int[num.intValue()];
	for(int i=0;i<num.intValue();i++)
	{
		array[i]=i+1;
	}
	modelAndView.addObject("allPage1",array);
	try {
		for(int i=0;i<blogList.size();i++)
		{
			List<String> list=new ArrayList<String>();
			blogList.get(i).setReleaseDateStr(DateUtil.formatString(blogList.get(i).getReleaseDate().toString()));
			String imageString=blogList.get(i).getContent();
			Document document=Jsoup.parse(imageString);
			Elements element=document.select("img[src$=.jpg]");
			for(int j=0;j<element.size();j++){
				if(j==3)
					break;
				list.add(element.get(j).attr("src"));
			}
			blogList.get(i).setImageList(list);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	application.setAttribute("blogList", blogList);
	Blogger blogger=bloggerService.getBloggerData();
	blogger.setPassword(null);
	application.setAttribute("blogger", blogger);
	List<Link> linkList=linkService.getLinkData();
	application.setAttribute("linkList", linkList);
	List<BlogType> blogTypeList=blogTypeService.getBlogTypeData();
	application.setAttribute("blogTypeList", blogTypeList);
	System.out.println("blogtypesize:"+blogTypeList.size());
//	BlogService blogService=(BlogService) applicationContext.getBean("blogService");
	List<Blog>blogTimeList=blogService.getBlogData();
	application.setAttribute("blogTimeList", blogTimeList);
	modelAndView.setViewName("index");
	return modelAndView;
	}
}

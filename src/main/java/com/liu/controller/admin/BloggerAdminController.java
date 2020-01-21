package com.liu.controller.admin;

import java.io.File;

import javax.annotation.Resource;
import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.liu.Utils.DateUtil;
import com.liu.Utils.ResponseUtil;
import com.liu.model.Blogger;
import com.liu.service.BloggerService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {
	@Resource
	private BloggerService bloggerService;
	//��ѯ�û���ͨ��json���͵�ǰ̨��ʾ
	@RequestMapping("/findBlooger")
	public String findBlogger(HttpServletResponse response)throws Exception
	{
		Blogger blogger=bloggerService.getBloggerData();
		JSONObject jsonObject=JSONObject.fromObject(blogger);
		ResponseUtil.write(response, jsonObject);
		return null;
	}
	//�޸��û���Ϣ
	@RequestMapping("/save")
	public String save(@RequestParam("imageFile")MultipartFile imageFile,Blogger blogger,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		if(!imageFile.isEmpty())
		{
			String filePath = request.getServletContext().getRealPath("/"); //��ȡ��������·��
			String imageName = DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];//��ȡͷ����
			imageFile.transferTo(new File(filePath + "static\\userImages\\" + imageName));//���Ƶ��µ�ַ
			blogger.setImagename(imageName);
		}
		int resultTotal=bloggerService.updateBlogger(blogger);//�޸Ĳ���
		JSONObject jsonObject=new JSONObject();
		if(resultTotal>0)
		{
			request.getSession().getServletContext().setAttribute("blogger", bloggerService.getBloggerData());
			jsonObject.put("success", true);
		}
		else
		{
			jsonObject.put("success", false);
		}
		ResponseUtil.write(response, jsonObject);
		return null;
	}
	//�޸�����
		@RequestMapping("/modifyPassword")
		public String modifyPassword(@RequestParam("password")String password,HttpServletResponse response)throws Exception{
			Blogger blogger=new Blogger();
			blogger.setPassword(password);
			int resultTotal=bloggerService.updateBlogger(blogger);
			JSONObject jsonObject=new JSONObject();
			if(resultTotal>0)
			{
				jsonObject.put("success", true);
			}
			else
			{
				jsonObject.put("success", false);
			}
			ResponseUtil.write(response, jsonObject);
			return null;			
		}
		//�˳�
		@RequestMapping("/logout")
		public String logout()throws Exception
		{
			SecurityUtils.getSubject().logout();
			return "redirect:/login.jsp";
		}
}

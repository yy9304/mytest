package com.liu.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liu.model.Blogger;
import com.liu.service.BloggerService;

 /** 
 * @ClassName: BloggerController 
 * @author: lyd
 * @date: 2017��10��10�� ����5:21:48 
 * @describe:ͨ��shiro���е�½��֤
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {
	@Resource
	private BloggerService bloggerService;
	@RequestMapping("/login")
	public String login(Blogger blogger,HttpServletRequest request){
	Subject subject=SecurityUtils.getSubject();//��ȡ����
	UsernamePasswordToken token=new UsernamePasswordToken(blogger.getUsername(), blogger.getPassword());//���˺��������token
	try
	{
		subject.login(token);//���Ե�½
		return "redirect:/admin/main.jsp";
	}catch (Exception e) 
		{
		e.printStackTrace();
		request.setAttribute("blogger", blogger);
		request.setAttribute("errorInfo", "�û����������");
		return "login";
		}
    }
	
 }

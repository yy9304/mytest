package com.liu.Utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


 /** 
 * @ClassName: ResponseUtil 
 * @author: lyd
 * @date: 2017��10��10�� ����10:31:06 
 * @describe:���д������ݵ�ǰ̨ ��Ҫ��������Json��ʽ
 */
public class ResponseUtil {
	public static void write(HttpServletResponse response,Object obj)throws Exception
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(obj.toString());
		out.flush();
		out.close();
	}
}

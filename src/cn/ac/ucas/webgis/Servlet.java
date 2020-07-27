package cn.ac.ucas.webgis;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 定义返回的字符串变量
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        ServletContext ctx =  session.getServletContext();
        String realPath = ctx.getRealPath(servletPath);

        PrintWriter writer = response.getWriter(); // 将strText写回客户端
        writer.println(realPath);

        String strText = "Hello Servlet"; // 从response对象中获取PrintWriter对象

        response.setContentType("html");
        String name = null;
        String value =null;
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()){
            name = names.nextElement();
            value = request.getParameterValues(name)[0];
            writer.println(name+":"+value);
            writer.println("<br>");
        }

        writer.write(strText);
    }
}

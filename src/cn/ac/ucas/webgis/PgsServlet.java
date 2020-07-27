package cn.ac.ucas.webgis;

//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;

import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PgsServlet")
public class PgsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1. 定义JDBC Connection对象
        Connection conn = null;
        Statement stmt = null;
        ResultSet rst = null;

        //2. 定义JDBC连接字符串、登录用户名和密码 --暂时不解耦
        String user = "postgres";
        String pswd = "123456";
        String connString ="jdbc:postgresql://127.0.0.1:5432/gisdb";

        try{
            //3. 加载PostgreSQL的JDBC驱动
            Class.forName("org.postgresql.Driver").newInstance();

            //4. 连接数据库
            conn = DriverManager.getConnection(connString, user, pswd);
            //writeConnectSucceedReponse(response);
            //5. 定义查询SQL(查询gisdb数据库中的houseprice表)
            //   返回所有字段，返回前30条记录（limit）
            String price = request.getParameter("Price");

            String sql = "select * from HOUSEPRICE limit 30 ";

            //6.创建Statement对象，调用executeQuery方法执行查询
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);

            response.setContentType("text/json");
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonData = JSONArray.fromObject(convertList(rst)); 
            System.out.println(jsonData.toString());
            PrintWriter out = response.getWriter();    //把json数据传递到前端，记着前端用ajax接收
            out.print(jsonData);

//            //7. 构造返回结果
//            String xmlString = writeResultAsXML(rst);
//            //8. 输出返回结果

//            response.getWriter().write(xmlString);
//            // writeConnectSucceedReponse(response); 返回状态
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (InstantiationException e){
            e.printStackTrace();
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }
        // 5. 增加新的异常处理SQLException
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{
            //6. 关闭ResultSet
            try{
                rst.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            //7. 关闭Statement
            try{
                stmt.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            try{
                conn.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    
//  ResultSet rs 转json
	private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}

//    private void writeConnectSucceedReponse(HttpServletResponse response) throws IOException {
//        //1. 创建xml document对象
//        Document doc = DocumentHelper.createDocument();
//        //2. 创建xml的根目录，并设置到doc对象上
//        Element root = DocumentHelper.createElement("Status");
//        doc.setRootElement(root);
//        root.setText("Success");
//
//        response.setContentType("text/xml");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(doc.asXML());
//    }
//
//    private String writeResultAsXML(ResultSet rst) throws  SQLException{
//
//        //1. 创建xml document对象
//        Document doc = DocumentHelper.createDocument();
//        //2. 创建根节点，名称为：FeatureCollection
//        Element root = DocumentHelper.createElement("FeatureCollection");
//        doc.setRootElement(root);
//
//        //3. 遍历ResultSet中的每一条记录
//        while(rst.next()){
//
//            //4. 每一条记录，创建一个名称为Feature的节点
//            Element featureElement = root.addElement("Feature");
//
//            //5. 读取记录的字段值（注意：字段序号从1开始，而不是0）
//            String HouseName = rst.getString(1);
//            Element nameElement = featureElement.addElement("HouseName");
//            nameElement.setText(HouseName);
//
//            Integer Price = rst.getInt(2);
//            Element idElement = featureElement.addElement("Price");
//            idElement.setText(Price.toString());
//
//            double Lat = rst.getDouble(3);
//            Element LatElement = featureElement.addElement("Lat");
//            LatElement.setText(String.valueOf(Lat));
//
//            double Lon = rst.getDouble(4);
//            Element LonElement = featureElement.addElement("Lon");
//            LonElement.setText(String.valueOf(Lon));
//
//            String Adress = rst.getString(5);
//            Element AdressElement = featureElement.addElement("Adress");
//            AdressElement.setText(Adress);
//
//        }
//        //6. 将xml doc对象序列化为字符串
//        return doc.asXML();
//    }
//    }



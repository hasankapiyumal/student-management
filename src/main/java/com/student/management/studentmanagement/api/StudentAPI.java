package com.student.management.studentmanagement.api;

import com.google.gson.Gson;
import com.student.management.studentmanagement.dto.StudentDTO;
import com.student.management.studentmanagement.util.ContextSignleTon;
import com.student.management.studentmanagement.util.InMemory;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/student")
public class StudentAPI extends HttpServlet {
    @Resource(name = "connectionPool")
    DataSource pool;
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     try{
         Connection connection = pool.getConnection();
         String sql = "SELECT * FROM student WHERE  id =?";
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1,req.getParameter("id"));
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()){
             StudentDTO studentDTO =new StudentDTO();
             studentDTO.setId(resultSet.getString("id"));
             studentDTO.setName(resultSet.getString("name"));
             studentDTO.setEmail(resultSet.getString("email"));
             studentDTO.setPassword(resultSet.getString("password"));
             studentDTO.setAddress(resultSet.getString("address"));
             resp.setContentType("application/json");
             resp.getWriter().write(new Gson().toJson(studentDTO));
             resp.setStatus(HttpServletResponse.SC_OK);
             return;
         }
         resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
         resp.getWriter().write("Student not found");
     } catch (SQLException e) {
         e.printStackTrace();
     }
//        String id = req.getParameter("id");
//        if (id!=null){
//            resp.setContentType("application/json");
//            StudentDTO studentById = InMemory.getStudentById(id);
//            resp.getWriter().write(new Gson().toJson(studentById));
//        }else {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("Student not found");
//        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        String status ="Student Added Successfully";
        if (header.equals("application/json")){
            StudentDTO dto = new Gson().fromJson(req.getReader(), StudentDTO.class);
            status = InMemory.saveStudent(dto)?status:"Student Cannot be Added";
        }
        resp.getWriter().write(status);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

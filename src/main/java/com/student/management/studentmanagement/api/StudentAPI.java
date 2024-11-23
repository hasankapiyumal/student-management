package com.student.management.studentmanagement.api;

import com.google.gson.Gson;
import com.student.management.studentmanagement.dto.StudentDTO;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;


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
        try {
            Connection connection = pool.getConnection();
            String sql = "SELECT * FROM student WHERE  id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, req.getParameter("id"));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                StudentDTO studentDTO = new StudentDTO();
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


    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        String status = "Student Added Successfully";
        if (header.equals("application/json")) {
            StudentDTO dto = new Gson().fromJson(req.getReader(), StudentDTO.class);
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = pool.getConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO student (id,name,email,password,address)VALUES (?,?,?,?,?)");
                preparedStatement.setString(1, dto.getId());
                preparedStatement.setString(2, dto.getName());
                preparedStatement.setString(3, dto.getEmail());
                preparedStatement.setString(4, dto.getPassword());
                preparedStatement.setString(5, dto.getAddress());
                int affectedRows = preparedStatement.executeUpdate();
                status = affectedRows > 0 ? status : "Student Cannot be Added";
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(status);
                return;


            } catch (SQLException e) {
                resp.getWriter().write("Student Cannot be Added");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();

            } finally {
                if (preparedStatement != null && !preparedStatement.isClosed()) preparedStatement.close();
                if (connection != null && !connection.isClosed()) connection.close();

            }

        }
        resp.getWriter().write(status);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        String status = "Student Updated Successfully";
        if (header.equals("application/json")) {
            StudentDTO studentDTO = new Gson().fromJson(req.getReader(), StudentDTO.class);
            PreparedStatement preparedStatement = null;
            Connection connection = null;
            try  {
                connection = pool.getConnection();
                preparedStatement = connection.prepareStatement("UPDATE student SET name=?,email=?,password=?,address=? WHERE id=?");
                preparedStatement.setString(1, studentDTO.getName());
                preparedStatement.setString(2, studentDTO.getEmail());
                preparedStatement.setString(3, studentDTO.getPassword());
                preparedStatement.setString(4, studentDTO.getAddress());
                preparedStatement.setString(5, studentDTO.getId());
                int affectedRows = preparedStatement.executeUpdate();
                status = affectedRows > 0 ? status : "Student Cannot be Updated";
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(status);
                return;
            } catch (SQLException e) {
                status = "Student Cannot be Updated";
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            } finally {

                    try {
                        if (preparedStatement != null && !preparedStatement.isClosed()) preparedStatement.close();
                        if (connection != null && !connection.isClosed())connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


            }

            resp.getWriter().write(status);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        String status ="Student Delete Failed";
        String id = req.getParameter("id");


            Connection  connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection=pool.getConnection();
                preparedStatement=connection.prepareStatement("DELETE FROM  student WHERE id=?");
                preparedStatement.setString(1,id);
                int affectedRows = preparedStatement.executeUpdate();
                status = affectedRows > 0 ? "Student Deleted Successfully" : status;
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(status);
                return;
            } catch (SQLException e) {
                resp.getWriter().write(status);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();

            }finally {
                try {
                    if (preparedStatement!=null&& !preparedStatement.isClosed()) preparedStatement.close();
                    if (connection!=null&& !connection.isClosed()) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        resp.getWriter().write(status);


    }
}

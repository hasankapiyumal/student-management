package com.student.management.studentmanagement.util;

import com.student.management.studentmanagement.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class InMemory {
    static List<StudentDTO> studentDTOList = new ArrayList<>();
    static {
        studentDTOList.add(new StudentDTO("1", "Alice", "alice@student.com", "pass123", "123 Elm St"));
        studentDTOList.add(new StudentDTO("2", "Bob", "bob@student.com", "securePass", "456 Maple St"));
        studentDTOList.add(new StudentDTO("3", "Charlie", "charlie@student.com", "pass789", "789 Oak St"));
        studentDTOList.add(new StudentDTO("4", "Diana", "diana@student.com", "myPass456", "321 Birch St"));
        studentDTOList.add(new StudentDTO("5", "Eve", "eve@student.com", "evePass987", "987 Cedar St"));

    }
    public static boolean saveStudent(StudentDTO studentDTO) {
        if (isExist(studentDTO)) {
            return false;
        }
        return studentDTOList.add(studentDTO);
    }

    public static boolean isExist(StudentDTO dto) {
        return studentDTOList.contains(dto);
    }

    public static boolean updateStudent(StudentDTO studentDTO) {
        if (isExist(studentDTO)) {
            return false;
        }
        for (StudentDTO dto : studentDTOList) {
            dto.setId(studentDTO.getId());
            dto.setName(studentDTO.getName());
            dto.setEmail(studentDTO.getEmail());
            dto.setPassword(studentDTO.getPassword());
            dto.setAddress(studentDTO.getAddress());
            return true;
        }
        return false;
    }

    public static boolean deleteStudent(StudentDTO studentDTO) {
        return studentDTOList.remove(studentDTO);

    }

    public static List<StudentDTO> getAllStudents() {
        return studentDTOList;
    }
    public static StudentDTO getStudentById(String id) {
        for(StudentDTO studentDTO:studentDTOList){
            if (studentDTO.getId().equals(id)) {
                return studentDTO;
            }
        }
        return null;
    }
}

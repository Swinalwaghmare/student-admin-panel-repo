package com.datastore.person.controller;

import com.datastore.person.pojo.Student;
import com.datastore.person.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentRepository studentRepository;

    @RequestMapping(method = RequestMethod.POST, path = "/student/post")
    private ResponseEntity<String> postStudent(@RequestBody Student student, HttpServletRequest request) {
        studentRepository.save(student);
        logger.info("Posted student to DB : {}", student.getName());
        return ResponseEntity.status(HttpStatus.OK).body("Student successfully posted.");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/student/get/{name}")
    private ResponseEntity<Student> getStudent(@PathVariable("name") String name) {
        logger.info("Getting student by name : {}", name);
        return studentRepository.findByName(name)
                .map(student -> ResponseEntity.ok().body(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/student/all")
    private ResponseEntity<List<Student>> getAllStudents() {
        logger.info("Getting all students");
        List<Student> stuList = studentRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(stuList);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/student/update/{name}")
    private ResponseEntity<String> updateStudent(@PathVariable("name") String name, @RequestBody Student updatedStudent) {
        logger.info("Updating student with name : {}", name);
        return studentRepository.findByName(name)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setAge(updatedStudent.getAge());
                    studentRepository.save(student);
                    logger.info("Updated student : {}", student.getName());
                    return ResponseEntity.ok().body("Student successfully updated.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found."));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/student/delete/{name}")
    private ResponseEntity<String> deleteStudent(@PathVariable("name") String name) {
        logger.info("Deleting student with name : {}", name);
        return studentRepository.findByName(name)
                .map(student -> {
                    studentRepository.delete(student);
                    logger.info("Deleted student : {}", name);
                    return ResponseEntity.ok().body("Student successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found."));
    }
}
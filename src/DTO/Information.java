/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import DAO.FileDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class Information {
    private HashMap<Campus,List<Subject>> info;

    public static String FILE_NAME = "nah.csv";
    public static List<Campus> campuses = FileDAO.loadCampusFromFile(FILE_NAME);
    public static List<Subject> subjects = FileDAO.LoadSubjectFromFile(FILE_NAME);

    public Information() {
        info =new HashMap<>();
    }

    public HashMap<Campus, List<Subject>> getInfo() {
        return info;
    }

    public void addCampus(Campus campus, List<Subject> list)
    {
        List<Subject> subjects;
        if(info.containsKey(campus))
            subjects = info.get(campus);
        else
            subjects = new ArrayList<>();
        subjects.addAll(list);
        info.put(campus, subjects);
    }
    //you can add any method to finish this assignment.
    public void getData(){
        List<Student> students = FileDAO.LoadStudentFromFile(FILE_NAME);
        for (Campus campus : campuses) {
            List<Subject> list = new ArrayList<>();
            for (Subject subject : subjects) {
                Subject subject1 = new Subject(subject.getName());
                for (Student student : students)
                    if (student.getExternalId().contains(subject.getName()) &&
                            student.getExternalId().contains(campus.getName()))
                        subject1.getListOfStudent().add(student);
                if(!subject1.getListOfStudent().isEmpty())
                    list.add(subject1);
            }
            this.addCampus(campus,list);
        }
    }
    public void printInfo(){
        for (Campus campus : info.keySet()) {
            System.out.println(campus.getName() + ":");
            for (Subject subject : info.get(campus)) {
                StringBuilder campusTabSpace = new StringBuilder();
                campusTabSpace.append(" ".repeat(campus.getName().length()));
                System.out.println(campusTabSpace.toString() + subject.getName() + ":");
                for (Student student : subject.getListOfStudent()) {
                    String studentTabSpace = campusTabSpace + " ".repeat(subject.getName().length());
                    System.out.println(studentTabSpace + student);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Information information = new Information();
        information.getData();
        information.printInfo();
        FileDAO.writeFile("nah.txt",information.info);
    }
}

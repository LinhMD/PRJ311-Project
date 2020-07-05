/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import DAO.FileDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class Information {
    private final HashMap<Campus,List<Subject>> info;

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

    //load all student from FILE_NAME to appropriate place for them(their campus and subject)
    public void getData(){
        List<Student> students = FileDAO.LoadStudentFromFile(FILE_NAME);
        for (Campus campus : campuses) {
            List<Subject> list = new ArrayList<>();
            for (Subject subjectTemp : subjects) {
                Subject subject1 = new Subject(subjectTemp.getName()); // a campus may not have all the subject
                for (Student student : students)
                    if (student.getExternalId().contains(subjectTemp.getName()) &&
                            student.getExternalId().contains(campus.getName()))
                        subject1.getListOfStudent().add(student);
                if(!subject1.getListOfStudent().isEmpty())
                    list.add(subject1);
            }
            this.addCampus(campus,list);
        }
    }

    //print info to some output stream
    public void printInfo(OutputStream stream){
        System.out.println("1");
        PrintWriter writer = new PrintWriter(stream);
        for (Campus campus : info.keySet()) {
            writer.println(campus.getName() + ":");
            for (Subject subject : info.get(campus)) {
                StringBuilder campusTabSpace = new StringBuilder();
                campusTabSpace.append(" ".repeat(campus.getName().length()));
                writer.println(campusTabSpace.toString() + subject.getName() + ":");
                for (Student student : subject.getListOfStudent()) {
                    String studentTabSpace = campusTabSpace + " ".repeat(subject.getName().length());
                    writer.println(studentTabSpace + student);
                    System.out.println("nah");
                }
            }
        }
        writer.close();
        System.out.println("here");
    }
    
    public static void main(String[] args) {
        Information information = new Information();
        information.getData();
        information.printInfo(System.out);
//        FileDAO.writeFile("nah.txt",information.info);
//        try {
//            information.printInfo(new FileOutputStream(new File("nah.txt")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        System.out.println("nah");
    }
}

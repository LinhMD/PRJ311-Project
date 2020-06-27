/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Campus;
import DTO.Student;
import DTO.Subject;

import java.io.*;
import java.util.*;

/**
 *
 * @author user
 */
public class FileDAO {
    public static List<Campus> loadCampusFromFile(String filename)
    {
        //only load campus name from file
        //return List of Campus'names
        Set<Campus> campus = new TreeSet<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            if (bufferedReader.ready()) bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String[] arr = bufferedReader.readLine().split(",");
                campus.add( new Campus(arr[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(campus);
    }
    public static List<Subject> LoadSubjectFromFile(String filename)
    {
        Set<Subject> subjects = new TreeSet<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            if (reader.ready()) reader.readLine();
            while (reader.ready()) {
                String[] arr = reader.readLine().split(",");
                subjects.add( new Subject(arr[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(subjects);
    }
    public static List<Student> LoadStudentFromFile(String filename)
    {
        List<Student> students = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            if (reader.ready()) reader.readLine();
            while (reader.ready()) {
                String[] arr = reader.readLine().split(",");
                try{

                    students.add( new Student(arr[1], arr[2], arr[3], arr[4], Double.parseDouble(arr[6]), arr[7]));
                } catch (Exception e){
                    System.out.println(arr[7]);
//                    e.printStackTrace();
//                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return students;
    }
    //Write list of students to given filename
    //fomat like as: 
    /* Campus:
              Subject:
                    FullName - Email - TotalLearning - Status
    
    example:
    HN:
        HRM201c: 
               ThuTTSB02022-ThuTTSB02022@fpt.edu.vn-20.95-Ok
               AnhNTQSB02073-AnhNTQSB02073@fpt.edu.vn-18.61-Not OK
        WED201c:
               DatLHHE141658-DatLHHE141658@fpt.edu.vn-21.81-Ok 
    SG:
       HRM201c: 
               ThuTTSB02022-ThuTTSB02022@fpt.edu.vn-20.95-Ok
               AnhNTQSB02073-AnhNTQSB02073@fpt.edu.vn-18.61-Not OK
       WED201c:
               DatLHHE141658-DatLHHE141658@fpt.edu.vn-21.81-Ok 
    */

    public static boolean writeCSVFile(String fileName, HashMap<Campus, List<Subject>> info){
        PrintWriter writer = null;
        try{
            int i = 1;
            writer = new PrintWriter(new File(fileName));
            writer.println("No,Full Name,Email,External Id,Campus,Sub,Total Learning Hours,Status (after 4 weeks)");
            for (Campus campus : info.keySet()) {
                for (Subject subject : info.get(campus)) {
                    for (Student student : subject.getListOfStudent()) {
                        writer.println(i++ +","
                                        + student.getFullName() + ","
                                        + student.getEmail() + ","
                                        + student.getExternalId() +","
                                        + campus +","
                                        + subject +","
                                        + student.getTotalLearning() +","
                                        + student.getStatus());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                if(writer != null) writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
    public static boolean writeFile(String filename, HashMap<Campus, List<Subject>> info)
    {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(filename));
            for (Campus campus : info.keySet()) {
                writer.println(campus.getName() + ":");
                for (Subject subject : info.get(campus)) {
                    StringBuilder campusTabSpace = new StringBuilder();
                    campusTabSpace.append(" ".repeat(campus.getName().length()));
                    writer.println(campusTabSpace.toString() + subject.getName() + ":");
                    for (Student student : subject.getListOfStudent()) {
                        String studentTabSpace = campusTabSpace + " ".repeat(subject.getName().length());
                        writer.println(studentTabSpace + student);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                if(writer != null) writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
}

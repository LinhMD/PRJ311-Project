/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import javax.swing.*;

/**
 *
 * @author user
 */
public class Student {
    public static final String EXTERNAL_ID_FORMAT = "\\w+-\\w+-\\w+";
    public static final String NOT_OK = "Not OK|Ok";

    private String FullName;
    private String Email;
    private String ExternalId;
    private String Campus;
    private double TotalLearning;
    private String Status;

    public Student(String FullName, String Email, String ExternalId, String Campus, double TotalLearning, String Status) throws IllegalArgumentException {
        if(FullName.isBlank())
            throw new IllegalArgumentException("Full name can not be empty");
        else
            this.FullName = FullName;

        if(Email.isBlank())
            throw new IllegalArgumentException("Email cant not be empty");
        else if(!Email.contains("@fpt.edu.vn"))
            throw new IllegalArgumentException("Email invalid, must have @fpt.edu.vn extensions");
        else
            this.Email = Email;

        if(!ExternalId.matches(EXTERNAL_ID_FORMAT))
            throw new IllegalArgumentException("External id invalid");
        else
            this.ExternalId = ExternalId;

        if(Campus.isBlank())
            throw new  IllegalArgumentException("Campus can not be empty");
        else
            this.Campus = Campus;

        if(TotalLearning < 0)
            throw new IllegalArgumentException("Learning hour invalid");
        else
            this.TotalLearning = TotalLearning;

        if(!Status.matches(NOT_OK))
            throw new IllegalArgumentException("Status invalid");
        else
            this.Status = Status;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getExternalId() {
        return ExternalId;
    }

    public void setExternalId(String ExternalId) {
        this.ExternalId = ExternalId;
    }

    public String getCampus() {
        return Campus;
    }

    public void setCampus(String Campus) {
        this.Campus = Campus;
    }

    public double getTotalLearning() {
        return TotalLearning;
    }

    public void setTotalLearning(double TotalLearning) {
        this.TotalLearning = TotalLearning;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public static String getStudentID(String name){
        char[] chars = name.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if(Character.isDigit(chars[i])){
                pos = i - 2;
                break;
            }
        }
        return name.substring(pos).toUpperCase();
    }

    @Override
    public String toString() {
        return this.getFullName();
    }

    public static void main(String[] args) {

        System.out.println("Not OK".matches("(Not )?OK"));
        JOptionPane.showConfirmDialog(null, "nah0");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DAO.FileDAO;
import DTO.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 *
 * @author user
 */
public class MainFrame extends javax.swing.JFrame {
    Information information = new Information(); //data model?
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root"); //tree model, use for jTree(show to user)
    Vector<Campus> campuses = new Vector<>(); //key set of info but have index .-.

    boolean isForNew = false;
    boolean isForEdit = false;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        information.getData();
        loadTreeModel();
        loadCampus();
        loadSubject(Information.subjects);
    }
    /**load data model to tree model*/
    private void loadTreeModel(){
        HashMap<Campus, List<Subject>> info = information.getInfo();
        campuses.addAll(info.keySet());
        for (Campus campus : campuses) {
            DefaultMutableTreeNode campusNode = new DefaultMutableTreeNode(campus);
            root.add(campusNode);
            for (Subject subject : info.get(campus)) {
                DefaultMutableTreeNode subjectNode = new DefaultMutableTreeNode(subject);
                campusNode.add(subjectNode);
                for (Student student : subject.getListOfStudent()) {
                    DefaultMutableTreeNode studentNode = new DefaultMutableTreeNode(student);
                    subjectNode.add(studentNode);
                }
            }
        }
        this.jTree1.setModel(new DefaultTreeModel(root));
    }

    /**load all campus to cbxCampus*/
    private void loadCampus(){
        this.cbxCampus.setModel(new DefaultComboBoxModel<>(campuses));
    }
    /**load input subject to cbxSubject*/
    private void loadSubject(Vector<Subject> subjects){
        this.cbxSubject.setModel(new DefaultComboBoxModel<>(subjects));
    }

    /**make what user can touch or not */
    private void enableStuff(Boolean identify, Boolean allElse, Boolean treeModel){
        this.txtName.setEnabled(identify);
        this.txtEmail.setEnabled(identify);

        this.cbxSubject.setEnabled(allElse);
        this.cbxCampus.setEnabled(allElse);
        this.txtLearningHours.setEnabled(allElse);
        this.isOK.setEnabled(allElse);

        this.jTree1.setEnabled(treeModel);
    }

    /** check if something is not save */
    private boolean saveCheck(ActionEvent actionEvent){
        if(isForEdit || isForNew){
            int option = JOptionPane.showConfirmDialog(null, "Action not save, do you want to save");
            if (option == JOptionPane.YES_OPTION){
                save(actionEvent);
            } else if(option == JOptionPane.NO_OPTION){
                isForNew = isForEdit = false;
                enableStuff(false, false, true);
            } else return option != JOptionPane.CANCEL_OPTION && option != JOptionPane.CLOSED_OPTION;
        }
        return true;
    }

    private void btnNewAction(ActionEvent actionEvent){
        if(!saveCheck(null)) return;
        this.isForEdit = false;
        this.isForNew = true;

        this.txtLearningHours.setText("");
        this.txtEmail.setText("");
        this.txtName.setText("");
        enableStuff(true, true, false);
    }

    private void btnEditAction(ActionEvent actionEvent){
        if(!saveCheck(null)) return;
        this.isForNew = false;
        this.isForEdit = true;
        enableStuff(false, true, false);
    }

    /**check if there is something need to save and write data model to nah.csv file*/
    private void save(ActionEvent actionEvent) {
        if(isForNew){
            addNewStudent();
        }else if(isForEdit){
            updateStudent();
        }
        FileDAO.writeCSVFile("nah.csv", information.getInfo());
    }

    /**delete student from data model and tree model*/
    private void deleteStudent(ActionEvent actionEvent) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof Student) {
            Student student = (Student) node.getUserObject();
            int option = JOptionPane.showConfirmDialog(null, "Do you want to delete " + student.getFullName() + " student?");
            if (option == JOptionPane.YES_OPTION) {
                DefaultMutableTreeNode subjectNode = (DefaultMutableTreeNode) node.getParent();
                Subject subject = (Subject) subjectNode.getUserObject();
                subject.getListOfStudent().remove(student); //delete from data model

                DefaultTreeModel model = (DefaultTreeModel) this.jTree1.getModel();
                model.removeNodeFromParent(node); //delete from tree model
                this.jScrollPane1.repaint();
            }
            JOptionPane.showMessageDialog(null, "Delete student successfully");
        }else{
            JOptionPane.showMessageDialog(null, "Please select a student");
        }
    }

    /**get selected student and update them*/
    private void updateStudent() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof Student){
            Student student = (Student) node.getUserObject();
//            try {
//                double hours = Double.parseDouble(this.txtLearningHours.getText());
//                String status;
//                if(this.isOK.isSelected())
//                    status = "Ok";
//                else
//                    status = "Not OK";
//                student.setTotalLearning(hours);
//                student.setStatus(status);
//                enableStuff(false, false, true);
//                isForEdit = isForNew = false;
//                JOptionPane.showMessageDialog(null,"Update successfully");
//            }catch (NumberFormatException e){
//                JOptionPane.showMessageDialog(null, "Learning hours invalid");
//            }
            Student newStudent = getStudent();
            if(newStudent == null) return;
            //this part is quite hacky but last min teacher requirement, so....
            if(newStudent.getExternalId().equalsIgnoreCase(student.getExternalId())){
                //if student didn't move to new campus or subject
                student.setStatus(newStudent.getStatus());
                student.setTotalLearning(newStudent.getTotalLearning());
                enableStuff(false, false, true);
                isForEdit = isForNew = false;
                JOptionPane.showMessageDialog(null,"Update successfully");
            }else{
                //if student move to new campus and subject
                // (this could be replace with delete old student and add new but i don't want to deal with JOptionPane message)

                //add newStudent to data model and tree model
                addNewStudent();
                //delete student from data model:
                DefaultMutableTreeNode subjectNode = (DefaultMutableTreeNode) node.getParent();
                Subject subject = (Subject) subjectNode.getUserObject();
                subject.getListOfStudent().remove(student);
                //delete student from tree model:
                DefaultTreeModel treeModel = (DefaultTreeModel) jTree1.getModel();
                treeModel.removeNodeFromParent(node);
            }
        }else
            JOptionPane.showMessageDialog(null, "Please choose a student");
    }

    /**get new student then add to tree model(root) and add to data model */
    private void addNewStudent() {
        Student student = getStudent();
        if (student == null) return;

        //add student to data model
        Campus campus = campuses.get(cbxCampus.getSelectedIndex());
        List<Subject> subjects = information.getInfo().get(campus);
        Subject subject = subjects.get(cbxSubject.getSelectedIndex());
        subject.getListOfStudent().add(student);

        //add new student node to tree model
        DefaultMutableTreeNode subjectNode = (DefaultMutableTreeNode) root.getChildAt(campuses.indexOf(campus)).getChildAt(subjects.indexOf(subject));
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(student);
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.insertNodeInto(node, subjectNode, subjectNode.getChildCount());

        if(isForNew)
            JOptionPane.showMessageDialog(null, "Add Student successfully");
        else if(isForEdit)
            JOptionPane.showMessageDialog(null, "Update Student successfully");
        isForEdit = isForNew = false;
        //set selected path to newly add node

        TreeNode[] pathToRoot = model.getPathToRoot(node);
        TreePath path = new TreePath(pathToRoot);
        jTree1.setSelectionPath(path);
        this.jScrollPane1.repaint();

        enableStuff(false, false, true);
    }

    /**Make a new student from text field data */
    private Student getStudent() {
        Student student = null;
        try{
            String name = this.txtName.getText();
            String email = this.txtEmail.getText();
            double hours = Double.parseDouble(this.txtLearningHours.getText());
            String campus = Objects.requireNonNull(this.cbxCampus.getSelectedItem()).toString();
            String subject = Objects.requireNonNull(this.cbxSubject.getSelectedItem()).toString();
            String status;
            if(this.isOK.isSelected())
                status = "Ok";
            else
                status = "Not OK";
            String externalId =  subject + "-" + campus + "-" + Student.getStudentID(name);

            student = new Student(name, email, externalId, campus, hours, status);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return student;
    }

    /**when ever a new student is selected load that student info to frame*/
    private void treeSelected(TreeSelectionEvent event) {
        if(!saveCheck(null)) return;
        TreePath selectionPath = jTree1.getSelectionPath();
        if (selectionPath == null) return;

        Object[] path = selectionPath.getPath();
        if (path.length < 4) return;
        ArrayList<DefaultMutableTreeNode> treeNodes = new ArrayList<>();
        //cast raw object to DMTModel
        for (Object o : path) treeNodes.add((DefaultMutableTreeNode) o);

        //this is selected object path
        Campus instantCampus = null;
        Subject instantSubject = null;
        Student instantStudent = null;
        //cast to true form of object
        if (treeNodes.get(1).getUserObject() instanceof Campus)  instantCampus = (Campus) treeNodes.get(1).getUserObject();
        if (treeNodes.get(2).getUserObject() instanceof Subject) instantSubject = (Subject) treeNodes.get(2).getUserObject();
        if (treeNodes.get(3).getUserObject() instanceof Student) instantStudent = (Student) treeNodes.get(3).getUserObject();
        //UX
        this.cbxCampus.setSelectedIndex(campuses.indexOf(instantCampus));
        Vector<Subject> subjects = (Vector<Subject>) information.getInfo().get(instantCampus);
        this.loadSubject(subjects);
        this.cbxSubject.setSelectedIndex(subjects.indexOf(instantSubject));
        this.loadStudentInfo(instantStudent);
    }

    /**
     * everytime a new campus is selected load it subject list to cbxSubject
     **/
    private void campusSelected(ActionEvent actionEvent) {
        Campus campus = campuses.get(cbxCampus.getSelectedIndex());
        Vector<Subject> subjects = (Vector<Subject>) information.getInfo().get(campus);
        this.loadSubject(subjects);
    }

    private void loadStudentInfo(Student student){
        if(student != null){
            this.txtName.setText(student.getFullName());
            this.txtEmail.setText(student.getEmail());
            this.txtLearningHours.setText(student.getTotalLearning() + "");
            this.isOK.setSelected(!student.getStatus().contains("Not"));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxCampus = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbxSubject = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtLearningHours = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        isOK = new javax.swing.JRadioButton();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

//        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
//        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("HN");
//        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("HRM201c");
//        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("xxx");
//        treeNode3.add(treeNode4);
//        treeNode2.add(treeNode3);
//        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("WED201c");
//        treeNode2.add(treeNode3);
//        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("PMG201c");
//        treeNode2.add(treeNode3);
//        treeNode1.add(treeNode2);
//        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("SG");
//        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("HRM201c");
//        treeNode2.add(treeNode3);
//        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("WED201c");
//        treeNode2.add(treeNode3);
//        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("PMG201c");
//        treeNode2.add(treeNode3);
//        treeNode1.add(treeNode2);
//        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addTreeSelectionListener(this::treeSelected);
        jScrollPane1.setViewportView(jTree1);

        jLabel1.setText("Full Name");

        txtName.setText("jTextField1");
        txtName.setEnabled(false);

        jLabel2.setText("Email");

        txtEmail.setText("jTextField2");
        txtEmail.setEnabled(false);

        jLabel3.setText("Campus");

//        cbxCampus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HN", "SG", "DN", "CT" }));
        cbxCampus.setEnabled(false);
        cbxCampus.addActionListener(this::campusSelected);

        jLabel4.setText("Subject");

//        cbxSubject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HRM201c", "PMG201", "WED201c" }));
        cbxSubject.setEnabled(false);

        jLabel5.setText("Total learning");

        txtLearningHours.setText("jTextField3");
        txtLearningHours.setEnabled(false);

        jLabel6.setText("Status");

        isOK.setText("OK");

        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditAction);

        btnSave.setText("Save");
        btnSave.addActionListener(this::save);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(this::deleteStudent);

        btnNew.setText("New");
        btnNew.addActionListener(this::btnNewAction);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this::saveCheck);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isOK)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtName)
                                .addComponent(txtEmail)
                                .addComponent(cbxCampus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbxSubject, 0, 158, Short.MAX_VALUE)
                                .addComponent(txtLearningHours))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel)))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(101, 101, 101))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxCampus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLearningHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(isOK))
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnSave)
                    .addComponent(btnDelete)
                    .addComponent(btnNew)
                    .addComponent(btnCancel))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Campus> cbxCampus;
    private javax.swing.JComboBox<Subject> cbxSubject;
    private javax.swing.JRadioButton isOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLearningHours;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}

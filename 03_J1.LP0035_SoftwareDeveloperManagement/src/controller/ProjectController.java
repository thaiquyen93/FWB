/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.ProjectDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Developer;
import model.Project;
import service.DeveloperService;
import service.ProjectService;
import util.Acceptable;
import util.Inputter;
import view.ViewFormatter;

/**
 *
 * @author Admin
 */
public class ProjectController {

    private ProjectService projectService;
    private ViewFormatter viewFormatter;
    private DeveloperService developerService;

    Inputter ip = new Inputter();
    public boolean dirty = false;

    public ProjectController(ProjectService ProjectService, DeveloperService developerService) {
        this.projectService = ProjectService;
        this.developerService = developerService;
        this.viewFormatter = new ViewFormatter();
    }

    private void markDirty() {
        dirty = true;
    }

    private void clearDirty() {
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void listAllProject() {
        viewFormatter.displayProjects(projectService.listAll());
    }

    public void addNewProject() {
        String proID;
        do {
            proID = ip.getStringAndCheck("Enter new project code: ", Acceptable.PROJECT_ID_VALID);
            if (projectService.findById(proID) != null) {
                System.out.println("Project code already exists!");
                proID = null;
            }
        } while (proID == null);

        String devId;
        do {
            devId = ip.getStringAndCheck("Enter new developer code: ", Acceptable.DEV_ID_VALID);
            if (developerService.findById(devId) == null) {
                System.out.println("Developer code must be existed in developers list");
            }
        } while (devId == null);

        String projectName = ip.getStringAndCheck("Enter project name: ", Acceptable.PROJECT_ID_VALID);
        int duration = ip.getInt("Enter duration: ", 1);

        Date startDate = ip.getDateFromUser("Enter start date: ", false);

        Project newProject = new Project(proID, devId, projectName, duration, startDate);

        // Tự động tính và in ra ngày kết thúc dự án
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        System.out.println(">> Start date:  " + sdf.format(startDate));
        System.out.println(">> End date:    " + sdf.format(newProject.getEndDate()));

        projectService.addProject(newProject);
        markDirty();
    }

    public void listProjectsGroupedByDeveloper() {

        List<Developer> devs = developerService.listAll();

        if (devs == null || devs.isEmpty()) {
            System.out.println("No developers found.");
            return;
        }

        for (Developer dev : devs) {
            List<Project> projectList = projectService.findProjectByDevId(dev.getDevID());
            viewFormatter.displayProjectsByDeveloper(dev, projectList);
        }
    }

    public void calculateTotalExperience() {
        String devId;

        do {
            devId = ip.getStringAndCheck("Enter developer ID to calculate total experience: ", Acceptable.DEV_ID_VALID);
            if (developerService.findById(devId) == null) {
                System.out.println("Developer \n"
                        + "ID does not exist!");
                devId = null;
            }
        } while (devId == null);

        List<Project> projectList = projectService.findProjectByDevId(devId);
        int totalExp = 0;
        for (Project project : projectList) {
            totalExp += project.getDurationMonth();
        }
        System.out.println("--------------TOTAL DEVELOPER EXPERIENCE--------------");
        System.out.println(developerService.findById(devId));
        System.out.println("Total experience: " + totalExp);
    }

    public void calculateProjectEndDate() {
        String proId;
        Project project;
        do {
            proId = ip.getStringAndCheck("Enter project ID to calculate end date: ", Acceptable.PROJECT_ID_VALID);
            project = projectService.findById(proId);
            if (project == null) {
                System.out.println("Project ID does not exist!");
                proId = null;
            }
        } while (proId == null);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        System.out.println("--------------PROJECT DATE INFO--------------");
        System.out.println("Project:    " + project.getProjectName());
        System.out.println("Duration:   " + project.getDurationMonth() + " months");
        System.out.println("Start date: " + sdf.format(project.getStartDate()));
        System.out.println("End date:   " + sdf.format(project.getEndDate()));
    }

    public void chuiChoNguUyen() {
        System.out.println("Ta Ngu Uyen an cuc");
    }

    public void choNgguyenNguvc() {
        System.out.println("Himalia");
    }
}

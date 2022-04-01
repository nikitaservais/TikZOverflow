package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all the infos of the user displayed in the "ManageProjectView"
 */
public class ManageProjectModel {
    private ProjectDB projectDB;
    private List<Project> userProjects;
    private final User user;

    public ManageProjectModel(User user) throws DatabaseConnectionException {
        this.projectDB = new ProjectDB();
        this.user = user;
    }

    /**
     * Updates the user file infos that will be displayed in the view table.
     * @throws DataAccessException
     * @throws DatabaseException
     */
    public void updateUserFileInfo() throws DataAccessException, DatabaseException {
        List<Integer> userProjectId;
        try {
            userProjectId = projectDB.getProjectsOfUser(user.getId());
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new DataAccessException(e);
        }
        List<Project> userFiles = new ArrayList<Project>();
        for (int i = 0; i < userProjectId.size(); i++) {
                Integer projectID = userProjectId.get(i);
                String[] projectData;
                try {
                    projectData = projectDB.getProjectData(projectID);
                } catch (DatabaseException e) {
                throw new DataAccessException(e);
            }
            File file = new File(projectData[1]);
            if (file.exists()) {
                Project project = new Project();
                project.setName(projectData[0]);
                project.setId(projectID);
                project.setFilePath(projectData[1]);
                project.setLastModification(projectData[3]);
                project.setUser(user);
                userFiles.add(project);
            } else {
                projectDB.deleteProject(projectID);
            }
        }
        userProjects = userFiles;
    }

    /**
     * Copies a file.
     * @param project the instance of the project to be copied.
     * @throws DataAccessException
     * @throws IOException
     * @throws VersioningException
     * @throws DatabaseException
     * @throws InvalidDriverException
     */
    public void copyProject(Project project) throws DataAccessException, IOException, VersioningException, DatabaseException, InvalidDriverException {
        String oldName = project.getFilePath();
        BranchManager branchManager = new BranchManager();
        List<Branch> oldProjectBranches = branchManager.getBranchesFromProject(project.getId());

        File file = new File(oldName);
        File newFile = new File(copyNameFile(oldName)); // this provides the name for the copied file and creates it
        String projectContent = FileUtils.fileToString(oldName);

        if (file.renameTo(newFile)) {
            IntegrityModel integrityModel = new IntegrityModel();
            int projectID = projectDB.insertProject(user.getId(), newFile.getName(), newFile.getAbsolutePath(), "tmp_checksum");
            BranchDB branchDB = new BranchDB();
            for (Branch oldProjectBranch : oldProjectBranches) {
                String oldMetadataPath = projectDB.getMetadataPath(project.getId(), oldProjectBranch.getBranchID());
                int branchID = branchDB.insertBranch(projectID, oldProjectBranch.getBranchName());
                String metadataCopiedContent = FileUtils.fileToString(oldMetadataPath);
                branchManager.createMetadataFile(projectID, branchID, metadataCopiedContent);
            }
            integrityModel.updateHashProject(projectID, user);
        } else {
            throw new FileAlreadyExistsException("File already exist");
        }
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file, false);
            writer.write(projectContent);
            writer.close();
            //todo erase the System and eventually tell the user that Project was copied via Pop up
        } else
            throw new FileAlreadyExistsException("File already exist");
        updateUserFileInfo();
    }

    /**
     * Deletes a file in both data access and user's computer
     * @param project The instance of the project to be deleted.
     * @throws DataAccessException
     */
    public void deleteProject(Project project) throws DataAccessException {
        File file = new File(project.getFilePath());
        file.delete();
        List<String> metadataFilesNames = getMetadataFilesPaths(project);

        for(String metadataName : metadataFilesNames) {
            File metadataFile = new File(metadataName);
            metadataFile.delete();
        }
        try {
            projectDB.deleteProject(project.getId());
            updateUserFileInfo();
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Relocates a file in both data access and user's computer.
     * @param project instance of the file Project information
     * @param path the path to relocate the project.
     * @throws FileNotFoundException
     * @throws DataAccessException
     */
        public void changeDirectory(Project project, String path) throws FileNotFoundException, DataAccessException {
        String os = System.getProperty("os.name");
        File file = new File(project.getFilePath());
        //Move the metadata Files
        List<List<String>> metadataFilesNames = getMetadataFilesPathsAndNames(project);
        for(List<String> metadataInfo : metadataFilesNames) {
            File metadataFile = new File(metadataInfo.get(1)); // WHAT IS THE ONE FOR !?
            if(metadataFile.renameTo(new File(path + File.separator + metadataInfo.get(0)))) {
                project.setFilePath(path + File.separator + project.getName());
                if (metadataInfo.get(0).charAt(metadataInfo.get(0).length()-1) == '0') {
                    project.setPathToMetaData(path + File.separator + metadataInfo.get(0));
                }
                metadataFile.delete();
            }
            else { throw new FileNotFoundException(); }
        }
        // Move the file
        if (file.renameTo(new File(path + File.separator + project.getName()))) {
            project.setFilePath(path + File.separator + project.getName());
            file.delete();
            try {
                projectDB.updatePath(project.getId(), project.getFilePath());
            } catch (DatabaseException e) {
                throw new DataAccessException(e);
            }
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Renames a file in both data access and user's computer
     * @param name new name for the project
     * @param project project to be renamed
     * @return true if the project has been renamed, false otherwise.
     */
    public boolean renameProject(String name, Project project) {
        boolean res = false;
        String oldName = project.getFilePath();
        Path path = Paths.get(oldName);
        String directory = path.getParent().toString();
        File oldFile = new File(oldName);
        File newFile = new File(directory + File.separator + name);
        if (oldFile.exists()) {
            oldFile.renameTo(newFile);
            try {
                projectDB.updatePath(project.getId(), newFile.getAbsolutePath());
                projectDB.renameProject(project.getId(), newFile.getName());
                IntegrityModel integrityModel = new IntegrityModel();
                String checksum = integrityModel.hashProject(project.getId(), user);
                projectDB.updateChecksum(project.getId(), checksum);
                updateUserFileInfo();
                res = true;
            } catch (DataAccessException | DatabaseException | VersioningException | IOException e) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Connects to the edition window
     * @param project the instance of the current project used.
     * @return String - The content of the project.
     * @throws IOException
     */
    public String editProject(Project project) throws IOException {
        return FileUtils.fileToString(project.getFilePath());
    }


    /**
     * Returns a copy of the list of the files information of the user.
     * @return List of Project copy of the List userProject.
     */
    public List<Project> getUserProjects() {
        List<Project> listCopy = new ArrayList<Project>(userProjects);
        return listCopy;
    }

    /**
     * Constructs a correct file name with Constants.copyFileString paying attention to extension of files.
     * @param fileName the file name.
     * @return file name with Constants.copyFileString at the right spot.
     */
    public String copyNameFile(String fileName) {
        // TODO : pass this method in protected and change TestManageProjectModel; no time left for it.
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return fileName + ConstantsUtils.copyFileString;
        }
        else {
            String beforeDot = fileName.substring(0,dotIndex);
            String afterDot = fileName.substring(dotIndex);
            return beforeDot + ConstantsUtils.copyFileString + afterDot;
        }
    }

    private List<String> getMetadataFilesPaths(Project project){
        List<String> metadataFilesNames = new ArrayList<>();
        for(Branch branch : project.getBranches()){
            String path = project.getFilePath();
            String name = project.getName();
            String metaDataPath = path.split(name)[0];
            String metadataFilePath = metaDataPath + ConstantsUtils.projectExtensionString + project.getId() + ConstantsUtils.branchFileString + branch.getBranchID();
            metadataFilesNames.add(metadataFilePath);
        }
        return metadataFilesNames;
    }


    private List<List<String>> getMetadataFilesPathsAndNames(Project project) {
        int projectId = project.getId();
        List<List<String>> metadataFilesNames = new ArrayList<>();

        for(Branch branch : project.getBranches()){
            String path = project.getFilePath();
            String name = project.getName();
            int branchId = branch.getBranchID();

            String metaDataPath = path.split(name)[0];
            String metadataFilePath = metaDataPath + ConstantsUtils.projectExtensionString + projectId + ConstantsUtils.branchFileString + branchId;
            ArrayList<String> metadataFileInfo = new ArrayList<>();
            metadataFileInfo.add(ConstantsUtils.projectExtensionString + projectId + ConstantsUtils.branchFileString + branchId);
            metadataFileInfo.add(metadataFilePath);
            metadataFilesNames.add(metadataFileInfo);
        }
        return metadataFilesNames;
    }
}

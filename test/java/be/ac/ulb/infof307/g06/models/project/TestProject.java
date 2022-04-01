package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestProject {
    private Project fi;
    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("admin");
        fi = new Project("TestP",user,"12.03.2020","C",1);
    }

    @Test
    void getNameReturnsCorrectName(){
        assertEquals("TestP",fi.getName());
    }

    @Test
    void nameIsCorrectlyUpdated() {
        fi.setName("P");
        assertEquals("P",fi.getName());
    }

    @Test
    void getUserReturnsCorrectUser() {
        assertEquals("admin",fi.getUser().getUsername());
    }

    @Test
    void usernameIsCorrectlyUpdated() {
        User user = new User();
        user.setUsername("JoJo");
        fi.setUser(user);
        assertEquals("JoJo", fi.getUser().getUsername());
    }

    @Test
    void getLastModificationReturnsCorrectDate() {
        assertEquals("12.03.2020",fi.getLastModification());
    }

    @Test
    void lastModificationIsCorrectlyUpdated() {
        fi.setLastModification("23.03.2020");
        assertEquals("23.03.2020",fi.getLastModification());
    }

    @Test
    void getFilePathReturnsCorrectPath(){
        assertEquals("C",fi.getFilePath());
    }

    @Test
    void filePathIsCorrectlyUpdated() {
        fi.setFilePath("D");
        assertEquals("D",fi.getFilePath());
    }

    @Test
    void getIdReturnsCorrectID() {
        assertEquals(1,fi.getId());
    }

    @Test
    void projectIDIsCorrectlyUpdated() {
        fi.setId(2);
        assertEquals(2,fi.getId());
    }

    @Test
    void equalsOnSameProjectsReturnTrue() {
        Project testProject = new Project("TestP",user,"12.03.2020","C",1);
        assertTrue(fi.equals(testProject));
    }

    @Test
    void equalsOnDifferentProjectsReturnFalse() {
        Project testProject = new Project("TestP",user,"12.03.2020","C",2);
        assertFalse(fi.equals(testProject));
    }


}
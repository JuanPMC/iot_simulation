package ece448.iot_hub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;


import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GroupsResourceTest {

    private GroupsModel groupsModel;
    private GroupsResource groupsResource;


    @Before
    public void setUp() {
        Topic topic = new Topic("pepe");
        groupsModel = new GroupsModel();
        groupsResource = new GroupsResource(groupsModel,topic);
    }

    @Test
    public void testAddNewGroup() {
        // Arrange
        String groupName = "TestGroup";
        List<String> members = Arrays.asList("Plug1", "Plug2", "Plug3");

        // Act
        groupsResource.createGroup(groupName, members);

        // Assert
        assertTrue(groupsModel.getGroups().contains(groupName));
        assertEquals(members, groupsModel.getGroupMembers(groupName));
    }

    @Test
    public void testRetrieveAllGroups() {
        // Arrange
        List<String> members1 = Arrays.asList("Plug1", "Plug2");
        List<String> members2 = Arrays.asList("Plug3", "Plug4");
        groupsResource.createGroup("Group1", members1);
        groupsResource.createGroup("Group2", members2);
    
        // Act
        Collection<Object> groups = groupsResource.getGroups();
    
        // Assert
        assertEquals(2, groups.size()); // Assuming two groups were added
    }
    

    @Test
    public void testRetrieveSpecificGroup() {
        // Arrange
        String groupName = "Group1";
        List<String> members = Arrays.asList("Plug1", "Plug2");
        groupsModel.setGroupMembers(groupName, members);

        // Act
        Object group = groupsResource.getGroup(groupName, null); // No action parameter

        // Assert
        assertNotNull(group);
    }

    @Test
    public void testPublishAction() {
        // Arrange
        String groupName = "Group3";
        List<String> members = Arrays.asList("Plug1", "Plug2");
        groupsModel.setGroupMembers(groupName, members);

        // Act
        Object result = groupsResource.getGroup(groupName, "off"); // action off

        // Assert
        assertFalse("ERROR".equals(result));
    }

    @Test
    public void testRemoveGroup() {
        // Arrange
        String groupName = "GroupToRemove";
        List<String> members = Arrays.asList("Plug1", "Plug2");
        groupsModel.setGroupMembers(groupName, members);

        // Act
        groupsResource.removeGroup(groupName);

        // Assert
        assertFalse(groupsModel.getGroups().contains(groupName));
    }

}

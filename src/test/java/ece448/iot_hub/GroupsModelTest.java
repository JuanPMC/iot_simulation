package ece448.iot_hub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GroupsModelTest {

    private GroupsModel groupsModel;

    @Before
    public void setUp() {
        groupsModel = new GroupsModel();
    }

    @Test
    public void testGetGroups() {
        List<String> groups = groupsModel.getGroups();
        assertTrue(groups.isEmpty());

        groupsModel.setGroupMembers("Group1", Arrays.asList("Member1", "Member2"));
        groupsModel.setGroupMembers("Group2", Arrays.asList("Member3"));

        groups = groupsModel.getGroups();
        assertEquals(2, groups.size());
        assertTrue(groups.contains("Group1"));
        assertTrue(groups.contains("Group2"));
    }

    @Test
    public void testGetGroupMembers() {
        groupsModel.setGroupMembers("Group1", Arrays.asList("Member1", "Member2"));
        groupsModel.setGroupMembers("Group2", Arrays.asList("Member3"));

        List<String> members = groupsModel.getGroupMembers("Group1");
        assertEquals(2, members.size());
        assertTrue(members.contains("Member1"));
        assertTrue(members.contains("Member2"));

        members = groupsModel.getGroupMembers("Group2");
        assertEquals(1, members.size());
        assertTrue(members.contains("Member3"));

        members = groupsModel.getGroupMembers("NonExistingGroup");
        assertTrue(members.isEmpty());
    }

    @Test
    public void testSetGroupMembers() {
        groupsModel.setGroupMembers("Group1", Arrays.asList("Member1", "Member2"));
        List<String> members = groupsModel.getGroupMembers("Group1");
        assertEquals(2, members.size());
        assertTrue(members.contains("Member1"));
        assertTrue(members.contains("Member2"));

        // Set new members for Group1
        groupsModel.setGroupMembers("Group1", Arrays.asList("NewMember1", "NewMember2"));
        members = groupsModel.getGroupMembers("Group1");
        assertEquals(2, members.size());
        assertTrue(members.contains("NewMember1"));
        assertTrue(members.contains("NewMember2"));
    }

    @Test
    public void testRemoveGroup() {
        groupsModel.setGroupMembers("Group1", Arrays.asList("Member1", "Member2"));
        assertTrue(groupsModel.getGroups().contains("Group1"));

        groupsModel.removeGroup("Group1");
        assertFalse(groupsModel.getGroups().contains("Group1"));
        assertTrue(groupsModel.getGroupMembers("Group1").isEmpty());
    }
}

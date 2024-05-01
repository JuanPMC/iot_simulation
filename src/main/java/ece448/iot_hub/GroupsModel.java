package ece448.iot_hub;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GroupsModel {
    private HashMap<String, HashSet<String>> groups;

    public GroupsModel() {
        this.groups = loadGroupsFromFile();
    }

    synchronized public List<String> getGroups() {
        return new ArrayList<>(groups.keySet());
    }

    synchronized public List<String> getGroupMembers(String group) {
        HashSet<String> members = groups.get(group);
        return (members == null) ? new ArrayList<>() : new ArrayList<>(members);
    }

    synchronized public void setGroupMembers(String group, List<String> members) {
        groups.put(group, new HashSet<>(members));
        saveGroupsToFile(groups);
    }

    synchronized public void removeGroup(String group) {
        groups.remove(group);
        saveGroupsToFile(groups);
    }

    private HashMap<String, HashSet<String>> loadGroupsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("groups.ser"))) {
            return (HashMap<String, HashSet<String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            // If file not found, return an empty HashMap
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveGroupsToFile(HashMap<String, HashSet<String>> groups) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("groups.ser"))) {
            oos.writeObject(groups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

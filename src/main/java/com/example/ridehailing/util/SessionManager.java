package com.example.ridehailing.util;

import com.example.ridehailing.model.User;

import java.io.*;

public class SessionManager {

    private static final String SESSION_FILE = "session.dat";

    /**
     * Serializes the current logged-in user object to disk.
     */
    public static void createSession(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            oos.writeObject(user);
            System.out.println("[Session] Created binary session file: " + SESSION_FILE);
        } catch (IOException e) {
            System.err.println("[Session Error] Failed to create session file: " + e.getMessage());
        }
    }

    /**
     * Reads and deserializes the logged-in user from disk.
     */
    public static User loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            User user = (User) ois.readObject();
            System.out.println("[Session] Restored session for user: " + user.getName());
            return user;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Session Error] Failed to read session file: " + e.getMessage());
            cleanSession(); // Clean up corrupted session file
            return null;
        }
    }

    /**
     * Checks if an active session file exists.
     */
    public static boolean hasActiveSession() {
        File file = new File(SESSION_FILE);
        return file.exists() && file.length() > 0;
    }

    /**
     * Deletes the session.dat file on logout.
     */
    public static void cleanSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("[Session] Session file successfully purged.");
            } else {
                System.err.println("[Session Error] Could not delete session file.");
            }
        }
    }
}
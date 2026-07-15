package com.example.ridehailing.model;

public class Admin extends User {
    private int adminId;

    public Admin(int adminId, String name, String phone, String email) {
        super(name, phone, email);
        this.adminId = adminId;
    }

    public void managePassengers() {
        System.out.println("Admin " + name + " is managing records.");
    }

    public void manageDrivers() {
        System.out.println("Admin " + name + " is reviewing driver statuses.");
    }

    public void generateReports() {
        System.out.println("Admin " + name + " generated daily platform reports.");
    }
}
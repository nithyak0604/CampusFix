package com.campusfix.entity;

import jakarta.persistence.*;

@Entity @Table(name = "locations")
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String building;
    private String block; private String floor; private String room; private String details;
    public Long getId() { return id; }
    public String getBuilding() { return building; } public void setBuilding(String v) { building = v; }
    public String getBlock() { return block; } public void setBlock(String v) { block = v; }
    public String getFloor() { return floor; } public void setFloor(String v) { floor = v; }
    public String getRoom() { return room; } public void setRoom(String v) { room = v; }
    public String getDetails() { return details; } public void setDetails(String v) { details = v; }
}

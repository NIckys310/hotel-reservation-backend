package com.hotel.reservation.model;

public class Room {

    private final String number;
    private final RoomType type;
    private final int floor;
    private final String description;
    private RoomStatus status = RoomStatus.AVAILABLE;

    public Room(String number, RoomType type, int floor, String description) {
        this.number = number;
        this.type = type;
        this.floor = floor;
        this.description = description;
    }

    public String getNumber() { return number; }
    public RoomType getType() { return type; }
    public int getFloor() { return floor; }
    public String getDescription() { return description; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}

package com.hotel.reservation.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private String number;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private int floor;
    private String name;

    @Column(length = 600)
    private String description;

    private int sizeM2;
    private String bedType;
    private String view;

    @Column(length = 400)
    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_number"))
    @OrderColumn(name = "position")
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;

    protected Room() {
    }

    public Room(String number, RoomType type, int floor, String name, String description, int sizeM2,
                String bedType, String view, String imageUrl, List<String> amenities) {
        this.number = number;
        this.type = type;
        this.floor = floor;
        this.name = name;
        this.description = description;
        this.sizeM2 = sizeM2;
        this.bedType = bedType;
        this.view = view;
        this.imageUrl = imageUrl;
        this.amenities = new ArrayList<>(amenities);
    }

    public String getNumber() { return number; }
    public RoomType getType() { return type; }
    public int getFloor() { return floor; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getSizeM2() { return sizeM2; }
    public String getBedType() { return bedType; }
    public String getView() { return view; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getAmenities() { return amenities; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}

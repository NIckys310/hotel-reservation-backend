package com.hotel.reservation.repository;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findAllByOrderByNumberAsc();

    List<Room> findByTypeOrderByNumberAsc(RoomType type);
}

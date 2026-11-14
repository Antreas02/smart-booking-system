package com.andreas.bookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andreas.bookingsystem.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}

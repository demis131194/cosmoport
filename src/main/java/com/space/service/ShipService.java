package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    Ship add (Ship ship);
    Ship get(long id);
    void deleteById(long id);
    Ship update(Ship ship);
    long getCount();
    long getCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                  Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
    List<Ship> getFilteredShips(Integer pageNumber, Integer pageSize, ShipOrder order, String name, String planet, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipType shipType, Long before, Long after);
}

package com.space.controller;

import com.space.exception.BadRequestException;
import com.space.exception.ResourceNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class MainController {

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/ships",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public  @ResponseBody List<Ship> getFilteredShips(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                          @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                          @RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "planet", required = false) String planet,
                                          @RequestParam(value = "shipType", required = false) ShipType shipType,
                                          @RequestParam(value = "after", required = false) Long after,
                                          @RequestParam(value = "before", required = false) Long before,
                                          @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                          @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                          @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                          @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                          @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                          @RequestParam(value = "minRating", required = false) Double minRating,
                                          @RequestParam(value = "maxRating", required = false) Double maxRating) {
        List<Ship> ships = shipService.getFilteredShips(pageNumber, pageSize, order, name, planet, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, shipType, before, after);
        return ships;
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody long getCountOfFilteredShips(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "planet", required = false) String planet,
                                           @RequestParam(value = "shipType", required = false) ShipType shipType,
                                           @RequestParam(value = "after", required = false) Long after,
                                           @RequestParam(value = "before", required = false) Long before,
                                           @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                           @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                           @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                           @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                           @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                           @RequestParam(value = "minRating", required = false) Double minRating,
                                           @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return shipService.getCount(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Ship createShip(@RequestBody Ship ship) {

        Ship createdShip = shipService.add(ship);
        return createdShip;
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Ship getShipById(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException();
        }
        Ship ship = shipService.get(id);
        if (ship == null) {
            throw new ResourceNotFoundException();
        }
        return ship;
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Ship updateShip(@PathVariable("id") Long id, @RequestBody Ship updatedShip) {

        if (id == null || id <= 0) {
            throw new BadRequestException();
        }
        updatedShip.setId(id);

        return shipService.update(updatedShip);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteShip(@PathVariable("id") Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException();
        }

        Ship ship = shipService.get(id);
        if (ship == null) {
            throw new ResourceNotFoundException();
        }

        shipService.deleteById(id);
    }

}

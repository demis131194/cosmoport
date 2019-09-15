package com.space.service.validator;

import com.space.exception.BadRequestException;
import com.space.model.Ship;

import java.util.Calendar;

public class ShipValidator {
    private static Integer prodDateYear;

    private ShipValidator() {
    }

    public static void validateForUpdate(Ship ship) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (ship.getProdDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());
            prodDateYear = calendar.get(Calendar.YEAR);
    }

        String name = ship.getName();
        String planet = ship.getPlanet();
        Integer crewSize = ship.getCrewSize();

        if ((name != null && (name.length() > 50 || name.isEmpty()))
                || planet != null && planet.length() > 50
                || crewSize != null && (crewSize <1 || crewSize >9999)
                || prodDateYear != null && (prodDateYear <2800 || prodDateYear > (currentYear + 1000))) {

            throw new BadRequestException();
        }
    }

    public static void validateForCreate(Ship ship) {
        validateForUpdate(ship);
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getProdDate() == null
                || ship.getCrewSize() == null
                || ship.getShipType() == null
                || ship.getSpeed() == null || (ship.getSpeed() > 1 || ship.getSpeed() < 0)) {
            throw new BadRequestException();
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
    }
}

package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;
    private EntityManager entityManager;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                         Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                         Double minRating, Double maxRating) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Ship> root = query.from(Ship.class);

        Predicate[] predicates = getFilters(cb, root, name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        query.where(predicates);

        query.select(cb.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Ship> getFilteredShips(Integer pageNumber, Integer pageSize, ShipOrder order, String name, String planet, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                       Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipType shipType, Long before, Long after) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> query = cb.createQuery(Ship.class);
        Root<Ship> root = query.from(Ship.class);

        Predicate[] predicates = getFilters(cb, root, name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        query.where(predicates);

        if (order != null)
            query.orderBy(cb.asc(root.get(order.getFieldName())));

        CriteriaQuery<Ship> select = query.select(root);
        TypedQuery<Ship> resultQuery = entityManager.createQuery(select);
        resultQuery.setFirstResult(pageNumber*pageSize);
        resultQuery.setMaxResults(pageSize);

        return resultQuery.getResultList();
    }

    @Override
    public Ship get(long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public Ship add(Ship ship) {
        double rating = calculateRating(ship.getUsed(), ship.getProdDate(), ship.getSpeed());
        ship.setRating(rating);

        Ship createdShip = shipRepository.saveAndFlush(ship);
        return createdShip;
    }

    @Override
    public void deleteById(long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public Ship update(Ship ship) {
        double rating = calculateRating(ship.getUsed(), ship.getProdDate(), ship.getSpeed());
        ship.setRating(rating);
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public long getCount() {
        return shipRepository.count();
    }

    private double calculateRating(Boolean isUsed, Date prodDate, Double speed) {
        double k = isUsed ? 0.5 : 1;
        Calendar prodDateCal = Calendar.getInstance();
        prodDateCal.setTime(prodDate);
        double rating = (80*speed*k) / (Calendar.getInstance().get(Calendar.YEAR) - prodDateCal.get(Calendar.YEAR) + 1);
        return rating;
    }

    private Predicate[] getFilters(CriteriaBuilder cb, Root<Ship> root,
                                  String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                  Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                  Double minRating, Double maxRating) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null)
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        if (planet != null)
            predicates.add(cb.like(root.get("planet"), "%" + planet + "%"));
        if (shipType != null)
            predicates.add(cb.equal(root.get("shipType"), shipType));
        if (isUsed != null)
            predicates.add(cb.equal(root.get("isUsed"), isUsed));
        if (after != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("prodDate").as(Date.class), new Date(after)));
        if (before != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("prodDate").as(Date.class), new Date(before)));
        if (minSpeed != null)
            predicates.add(cb.ge(root.get("speed"), minSpeed));
        if (maxSpeed != null)
            predicates.add(cb.le(root.get("speed"), maxSpeed));
        if (minCrewSize != null)
            predicates.add(cb.ge(root.get("crewSize"), minCrewSize));
        if (maxCrewSize != null)
            predicates.add(cb.le(root.get("crewSize"), maxCrewSize));
        if (minRating != null)
            predicates.add(cb.ge(root.get("rating"), minRating));
        if (maxRating != null)
            predicates.add(cb.le(root.get("rating"), maxRating));

        return predicates.toArray(new Predicate[0]);
    }

}

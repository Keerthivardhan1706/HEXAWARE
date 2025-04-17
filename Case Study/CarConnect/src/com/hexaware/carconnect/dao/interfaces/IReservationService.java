package com.hexaware.carconnect.dao.interfaces;

import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.ReservationException;
import com.hexaware.carconnect.exception.InvalidInputException;
import java.util.List;

public interface IReservationService {

    Reservation getReservationById(int reservationId) throws ReservationException;

    List<Reservation> getReservationsByCustomerId(int customerId) throws ReservationException;

    void createReservation(Reservation reservation) throws InvalidInputException;

    void updateReservation(Reservation reservation) throws ReservationException, InvalidInputException;

    void cancelReservation(int reservationId) throws ReservationException;

    List<Reservation> viewAllReservations() throws ReservationException;
}

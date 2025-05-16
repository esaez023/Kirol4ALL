package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.User;
import domain.Activity;
import domain.Bill;
import domain.Member;
import domain.Reservation;
import domain.Room;
import domain.Session;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");

		dbManager = new DataAccess();

		// dbManager.close();

	}

	public BLFacadeImplementation(DataAccess da) {

		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c = ConfigXML.getInstance();

		dbManager = da;
	}

	@Override
	public User getUserById(String id) {
		dbManager.open();
		User user = dbManager.getUserById(id);
		dbManager.close();
		return user;
	}

	@Override
	public List getAllSessions() {
		dbManager.open();
		List sessions = dbManager.getAllSessions();
		dbManager.close();
		return sessions;
	}

	public void close() {
		DataAccess dB4oManager = new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void initializeBD() {
		dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

	@Override
	public List<Reservation> getReservations(Member member) {
		dbManager.open();
		List sessions = dbManager.getReservations(member);
		dbManager.close();
		return sessions;
	}

	@Override
	public Reservation makeReservation(Reservation reservation) {
		dbManager.open();
		Reservation res = dbManager.makeReservation(reservation);
		dbManager.close();
		return res;
	}

	@Override
	public List<String> getActivities() {
		dbManager.open();
		List<String> activities = dbManager.getActivities();
		dbManager.close();
		return activities;
	}

	@Override
	public List<Room> getRooms() {
		dbManager.open();
		List<Room> rooms = dbManager.getRooms();
		dbManager.close();
		return rooms;
	}

	@Override
	public void assignSessionToRoom(Session selectedSession, Room selectedRoom) {
		dbManager.open();
		dbManager.assignSessionToRoom(selectedSession, selectedRoom);
		dbManager.close();
	}

	@Override
	public List<User> getAllUsers() {
		dbManager.open();
		List<User> users = dbManager.getAllUsers();
		dbManager.close();
		return users;
	}
	
	@Override
	public List<Reservation> getReservationsByMember(Member member) {
		dbManager.open();
		List<Reservation> userReservations = dbManager.getReservationsByMember(member);
		dbManager.close();
		return userReservations;
	}

	@Override
	public void createMember(Member member) {
		dbManager.open();
		dbManager.createMember(member);
		dbManager.close();
	}

	@Override
	public void createSession(Session session) {
		dbManager.open();
		dbManager.createSession(session);
		dbManager.close();
	}

	@Override
	public Activity getActivityByName(String selectedActivity) {
		dbManager.open();
		Activity activity = dbManager.getActivityByName(selectedActivity);
		dbManager.close();
		return activity;
	}

	@Override
	public List<Bill> getBillsForMember(Member member) {
		dbManager.open();
		List<Bill> bills = dbManager.getBillsForMember(member);
		dbManager.close();
		return bills;
	}

	@Override
	public void createBill(Bill bill) {
		dbManager.open();
		dbManager.createBill(bill);
		dbManager.close();
	}

	@Override
	public void updateBill(Bill selectedBill) {
		dbManager.open();
		dbManager.updateBill(selectedBill);
		dbManager.close();
	}

}

package businessLogic;

import java.util.Date;
import java.util.List;

import domain.User;
import domain.Activity;
import domain.Bill;
import domain.Member;
import domain.Reservation;
import domain.Room;
import domain.Session;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	
	@WebMethod public void initializeBD();
	  
	@WebMethod public User getUserById(String id);
	
	@WebMethod public List<Session> getAllSessions();
	
	@WebMethod public List<Reservation> getReservationsByMember(Member member);
	
	@WebMethod public List<Reservation> getReservations(Member member);

	@WebMethod public Reservation makeReservation(Reservation reservation);

	@WebMethod public List<String> getActivities();

	@WebMethod public List<Room> getRooms();

	@WebMethod public void assignSessionToRoom(Session selectedSession, Room selectedRoom);

	@WebMethod public List<User> getAllUsers();

	@WebMethod public Member createMember(Member member);

	@WebMethod public void createSession(Session session);

	@WebMethod public Activity getActivityByName(String selectedActivity);

	@WebMethod public List<Bill> getBillsForMember(Member member);

	@WebMethod public void createBill(Bill bill);

	@WebMethod public void updateBill(Bill selectedBill);
	
}

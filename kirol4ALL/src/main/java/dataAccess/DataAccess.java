package dataAccess;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;

public class DataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;
	private ConfigXML c = ConfigXML.getInstance();

	public DataAccess() {
		if (c.isDatabaseInitialized()) {
			String fileName = c.getDbFilename();
			File fileToDelete = new File(fileName);
			if (fileToDelete.delete()) {
				new File(fileName + "$").delete();
				System.out.println("File deleted");
			} else {
				System.out.println("Operation failed");
			}
		}
		open();
		if (c.isDatabaseInitialized())
			initializeDB();
		close();
	}

	public DataAccess(EntityManager db) {
		this.db = db;
	}

	// --- Database Initialization ---
	public void initializeDB() {
		db.getTransaction().begin();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 7); // Move to next week

			Room room1 = new Room("A1", 0);
			Room room2 = new Room("A2", 20);
			Room room3 = new Room("B3", 10);
			Room room4 = new Room("B4", 10);
			db.persist(room1);
			db.persist(room2);
			db.persist(room3);
			db.persist(room4);

			Member member1 = new Member("member1@gmail.com", "password123", "John Doe", "12345678A");
			Member member2 = new Member("member2@gmail.com", "password456", "Jane Smith", "87654321B");
			Member member3 = new Member("member3@gmail.com", "password789", "Alice Johnson", "11223344C");
			Manager manager1 = new Manager("manager1@gmail.com", "adminpass", "Admin Manager", "79188452G");
			db.persist(member1);
			db.persist(member2);
			db.persist(member3);
			db.persist(manager1);

			Activity yoga = new Activity("Yoga", 3, 10, 15.0f);
			Activity zumba = new Activity("Zumba", 2, 8, 18.0f);
			Activity boxing = new Activity("Boxing", 5, 10, 35.0f);
			Activity crossfit = new Activity("CrossFit", 5, 12, 40.0f);
			Activity meditation = new Activity("Meditation", 1, 5, 12.0f);
			Activity pilates = new Activity("Pilates", 2, 8, 18.0f);
			db.persist(yoga);
			db.persist(zumba);
			db.persist(boxing);
			db.persist(crossfit);
			db.persist(meditation);
			db.persist(pilates);

			// Sessions
			List<Session> sessions = new ArrayList<>();
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			String[] times = { "08:00", "10:00", "12:00", "14:00", "16:00", "18:00" };

			// Yoga and Zumba
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[0]), yoga, new ArrayList<>(), room1));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[1]), yoga, new ArrayList<>(), room2));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[2]), zumba, new ArrayList<>(), room1));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[3]), zumba, new ArrayList<>(), room1));

			// Boxing and CrossFit
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[4]), boxing, new ArrayList<>(), room1));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(new Session(calendar.getTime(), timeFormat.parse(times[5]), boxing, new ArrayList<>(), room2));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(
					new Session(calendar.getTime(), timeFormat.parse(times[0]), crossfit, new ArrayList<>(), room4));

			// Meditation and Pilates
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(
					new Session(calendar.getTime(), timeFormat.parse(times[4]), meditation, new ArrayList<>(), room2));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sessions.add(
					new Session(calendar.getTime(), timeFormat.parse(times[5]), pilates, new ArrayList<>(), room3));
			for (Session session : sessions) {
				db.persist(session);
			}

			db.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
		}
	}

	// --- Session Methods ---
	public List<Session> getAllSessions() {
		try {
			return db.createQuery("SELECT s FROM Session s", Session.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void createSession(Session session) {
		try {
			db.getTransaction().begin();
			Activity managedActivity = db.find(Activity.class, session.getActivity().getName());
			if (managedActivity == null)
				throw new IllegalArgumentException("Activity not found in the database.");
			session.setActivity(managedActivity);
			db.persist(session);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
		}
	}

	public void assignSessionToRoom(Session selectedSession, Room selectedRoom) {
		try {
			db.getTransaction().begin();
			Session session = db.find(Session.class, selectedSession.getDate());
			Room room = db.find(Room.class, selectedRoom.getRoomNumber());
			if (session != null && room != null) {
				session.setRoom(room);
				db.merge(session);
			}
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
		}
	}

	public List<Reservation> getReservationsByMember(Member member) {
		TypedQuery<Reservation> query = db.createQuery("SELECT r FROM Reservation r WHERE r.client = :client",
				Reservation.class);
		query.setParameter("client", member);
		return query.getResultList();
	}

	// --- Activity Methods ---
	public List<String> getActivities() {
		try {
			TypedQuery<Activity> query = db.createQuery("SELECT a FROM Activity a", Activity.class);
			List<Activity> activities = query.getResultList();
			List<String> activityNames = new ArrayList<>();
			for (Activity activity : activities) {
				activityNames.add(activity.getName());
			}
			return activityNames;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Activity getActivityByName(String selectedActivity) {
		try {
			TypedQuery<Activity> query = db.createQuery("SELECT a FROM Activity a WHERE a.name = :name",
					Activity.class);
			query.setParameter("name", selectedActivity);
			List<Activity> activities = query.getResultList();
			return activities.isEmpty() ? null : activities.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// --- User Methods ---
	public List<User> getAllUsers() {
		try {
			return db.createQuery("SELECT u FROM User u", User.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public User getUserById(String id) {
		try {
			TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.documentID = :id", User.class);
			query.setParameter("id", id);
			List<User> users = query.getResultList();
			return users.isEmpty() ? null : users.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Member createMember(Member member) {
		try {
			db.getTransaction().begin();
			db.persist(member);
			db.getTransaction().commit();
			return member;
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
			return null;
		}
	}

	// --- Reservation Methods ---
	public List getReservations(Member member) {
		try {
			TypedQuery query = db.createQuery("SELECT r FROM Reservation r WHERE r.client = :client",
					Reservation.class);
			query.setParameter("client", member);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Reservation makeReservation(Reservation reservation) {
		try {
			Member member = (Member) reservation.getClient();

			// Check if the member can make a reservation
			if (!member.canMakeReservation()) {
				System.out.println("Member has reached the maximum number of reservations.");
				return null;
			}

			db.getTransaction().begin();

			db.persist(reservation);

			// Decrement the number of reservations left
			member.decrementReservations();
			db.merge(member); // Update the member in the database

			db.getTransaction().commit();

			return reservation; // Return the created reservation
		} catch (Exception e) {
			e.printStackTrace();

			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}

			return null; // Return null if the operation fails
		}
	}

	// --- Room Methods ---
	public List<Room> getRooms() {
		try {
			return db.createQuery("SELECT r FROM Room r", Room.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	// --- Bill Methods ---

	public List getBillsForMember(Member member) {
		TypedQuery query = db.createQuery("SELECT b FROM Bill b WHERE b.client = :member", Bill.class);
		query.setParameter("member", member);
		return query.getResultList();
	}

	public void createBill(Bill bill) {
		try {
			db.getTransaction().begin();
			db.persist(bill);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
		}
	}

	public void updateBill(Bill bill) {
		try {
			db.getTransaction().begin();
			db.merge(bill);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
		}
	}

	// --- Utility Methods ---
	public void open() {
		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());
			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());
	}

	public void close() {
		db.close();
		System.out.println("DataAccess closed");
	}

}

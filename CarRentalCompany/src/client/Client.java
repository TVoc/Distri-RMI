package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Set;

import agency.IRentalAgency;
import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.RentalServer;
import rental.Reservation;
import rental.ReservationException;
import sessions.IManagerSession;
import sessions.IReservationSession;

public class Client extends
		AbstractScriptedTripTest<IReservationSession, IManagerSession> {

	public Client(String scriptFile) {
		super(scriptFile);
	}

	public static void main(String[] args) {
		try {
			ICarRentalCompany dockx = new CarRentalCompany("Dockx", RentalServer.loadData("dockx.csv"));
			ICarRentalCompany hertz = new CarRentalCompany("Hertz", RentalServer.loadData("hertz.csv"));
			Registry registry = LocateRegistry.getRegistry();
			IRentalAgency agency = (IRentalAgency) registry.lookup("agency");
			IManagerSession session = agency.getManagerSession();
			session.register((ICarRentalCompany) UnicastRemoteObject.exportObject(dockx, 0));
			session.register((ICarRentalCompany) UnicastRemoteObject.exportObject(hertz, 0));
			Client client = new Client("trips");
			try {
				client.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ReservationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected IReservationSession getNewReservationSession(String name)
			throws Exception {
		Registry registry = LocateRegistry.getRegistry();
		IRentalAgency agency = (IRentalAgency) registry.lookup("agency");
		return agency.spawnReservationSession(name);
	}

	@Override
	protected IManagerSession getNewManagerSession(String name)
			throws Exception {
		Registry registry = LocateRegistry.getRegistry();
		IRentalAgency agency = (IRentalAgency) registry.lookup("agency");
		return agency.getManagerSession();
	}

	@Override
	protected void checkForAvailableCarTypes(IReservationSession session,
			Date start, Date end) throws Exception {
		session.checkAvailableCarTypes(start, end);
	}

	@Override
	protected String getCheapestCarType(IReservationSession session,
			Date start, Date end) throws Exception {
		return session.getCheapestCarType(start, end);
	}

	@Override
	protected void addQuoteToSession(IReservationSession session, Date start,
			Date end, String carType, String carRentalName) throws Exception {
		session.createQuote(start, end, carType, carRentalName);
	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSession session)
			throws Exception {
		return session.confirmQuotes();
	}

	@Override
	protected int getNumberOfReservationsBy(IManagerSession ms,
			String clientName) throws Exception {
		return ms.getNumberOfReservationsBy(clientName);
	}

	@Override
	protected Set<String> getBestClients(IManagerSession ms) throws Exception {
		return ms.getBestClient();
	}

	@Override
	protected int getNumberOfReservationsForCarType(IManagerSession ms,
			String carRentalCompanyName, String carType) throws Exception {
		return ms.getNumberOfReservationsForCarType(carRentalCompanyName, carType);
	}

	@Override
	protected CarType getMostPopularCarTypeIn(IManagerSession ms,
			String carRentalCompanyName) throws Exception {
		return ms.getMostPopularCarTypeIn(carRentalCompanyName);
	}

}

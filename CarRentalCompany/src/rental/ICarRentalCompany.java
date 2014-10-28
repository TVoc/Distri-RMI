package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICarRentalCompany extends Remote {

	/********
	 * NAME *
	 ********/

	public abstract String getName() throws RemoteException;

	/****************
	 * RESERVATIONS *
	 ****************/

	public abstract Quote createQuote(ReservationConstraints constraints,
			String client) throws ReservationException, RemoteException;

	public abstract Reservation confirmQuote(Quote quote)
			throws ReservationException, RemoteException;

	public abstract void cancelReservation(Reservation res) throws RemoteException;
	
	public abstract Collection<CarType> getAllCarTypes() throws RemoteException;
	
	public abstract Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException, IllegalArgumentException;
	
	/**
	 * Get the reservations by the specified client
	 * 
	 * @param clientName
	 * 		The name of the client in question
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for synchronisation
	 * 		purposes
	 * @return
	 * 		A list of the reservations by the specified client
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract List<Reservation> getReservationsBy(String clientName, long timeStamp) throws RemoteException;
	
	/**
	 * Get the number of reservations for the specified car type at the time specified by timeStamp
	 * 
	 * @param carType
	 * 		The car type in question
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for
	 * 		synchronisation purposes
	 * @return
	 * 		The number of reservations
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract int getNumberOfReservationsFor(String carType, long timeStamp) throws RemoteException;
	
	/**
	 * Returns the cheapest car type available in the period specified by start and end.
	 * 
	 * @param start
	 * 		Start of the period
	 * @param end
	 * 		End of the period
	 * @return
	 * 		The cheapest car type available in the specified period. If no cars are available at all
	 * 		in the specified period, null is returned
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 * @throws IllegalArgumentException
	 * 		Specified period was illegal
	 */
	public abstract CarType getCheapestCarType(Date start, Date end) throws RemoteException, IllegalArgumentException;
	
	/**
	 * Each client is associated with the number of reservations made by that client.
	 * 
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for
	 * 		synchronisation purposes.
	 * @return
	 * 		A map associating each client with their number of reservations made
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract Map<String, Integer> getNumReservationsPerCustomer(long timeStamp) throws RemoteException;
	
	/**
	 * Get the best client in this company.
	 * 
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for synchronisation
	 * 		purposes.
	 * @return
	 * 		The client with the most reservations in this company. If there are no reservations at all,
	 * 		null is returned.
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract String getBestCustomer(long timeStamp) throws RemoteException;
	
	/**
	 * Each car type is associated with the number of reservations made for that car type.
	 * 
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for
	 * 		synchronisation purposes.
	 * @return
	 * 		A map associating each car type with the number of reservations made for that car type
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract Map<CarType, Integer> getNumReservationsPerCarType(long timeStamp) throws RemoteException;
	
	/**
	 * Get the most popular car type in this company.
	 * 
	 * @param timeStamp
	 * 		Only reservations submitted no later than timeStamp are considered. This is for
	 * 		synchronisation purposes.
	 * @return
	 * 		The car type with the most reservations in this company. If there are no reservations at all,
	 * 		null is returned.
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation
	 */
	public abstract CarType getMostPopularCarType(long timeStamp) throws RemoteException;

}
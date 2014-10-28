package sessions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rental.Quote;
import rental.Reservation;
import rental.ReservationException;

public interface IReservationSession extends Remote, Session {

	public void createQuote(Date start, Date end, String carType, String companyName) throws RemoteException, ReservationException, InvalidSessionException;
	
	public Collection<Quote> getCurrentQuotes() throws RemoteException, InvalidSessionException;
	
	public List<Reservation> confirmQuotes() throws RemoteException, ReservationException, InvalidSessionException;
	
	public void checkAvailableCarTypes(Date start, Date end) throws RemoteException, InvalidSessionException;
	
	public String getCheapestCarType(Date start, Date end) throws RemoteException, InvalidSessionException;
}

package sessions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import rental.Quote;
import rental.ReservationException;

public interface IReservationSession extends Remote, Session {

	public void createQuote(Date start, Date end, String carType, String companyName) throws RemoteException, ReservationException;
	
	public Collection<Quote> getCurrentQuotes() throws RemoteException;
	
	public void confirmQuotes() throws RemoteException, ReservationException;
	
	public void checkAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	
}

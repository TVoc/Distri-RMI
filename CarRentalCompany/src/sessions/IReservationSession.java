package sessions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import rental.Quote;
import rental.ReservationException;

public interface IReservationSession extends Remote, Session {

	public void createQuote(Date start, Date end, String carType, String companyName) throws RemoteException, ReservationException, InvalidSessionException;
	
	public Collection<Quote> getCurrentQuotes() throws RemoteException, InvalidSessionException;
	
	public void confirmQuotes() throws RemoteException, ReservationException, InvalidSessionException;
	
	public void checkAvailableCarTypes(Date start, Date end) throws RemoteException, InvalidSessionException;
	
	public String getCheapestCarType(Date start, Date end) throws RemoteException, InvalidSessionException;
}

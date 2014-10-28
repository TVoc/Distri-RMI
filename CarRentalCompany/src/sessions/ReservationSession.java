package sessions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import naming.ICompanyIterator;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class ReservationSession implements IReservationSession {

	public ReservationSession(String clientName, SessionManager sessionManager) {
		this.clientName = clientName;
		this.sessionManager = sessionManager;
		this.quoteList = new ArrayList<Quote>();
	}
	
	private boolean isValid = true;
	
	@Override
	public synchronized boolean isValid() {
		return isValid;
	}

	private long timeStamp;
	
	@Override
	public long getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public synchronized void destroy() {
		this.sessionManager = null;
		this.getQuoteList().clear();
		this.quoteList = null;
		this.isValid = false;
	}

	private String clientName;
	
	private String getClientName() {
		return this.clientName;
	}
	
	private SessionManager sessionManager;
	
	private SessionManager getSessionManager() {
		return this.sessionManager;
	}
	
	private List<Quote> quoteList;
	
	private List<Quote> getQuoteList() {
		return this.quoteList;
	}
	
	@Override
	public synchronized void createQuote(Date start, Date end, String carType,
			String companyName) throws RemoteException, ReservationException, InvalidSessionException {
		if (! this.isValid()) {
			throw new InvalidSessionException("Session no longer valid");
		}
		try {
			ICarRentalCompany company = this.getSessionManager().lookup(companyName);
			ReservationConstraints constraints = new ReservationConstraints(start, end, carType);
			this.getCurrentQuotes().add(company.createQuote(constraints, this.getClientName()));
		} catch (RemoteException e) {
			System.err.println("Error doing remote invocation");
			e.printStackTrace();
		} catch (ReservationException e) {
			System.err.println("Cannot create quote");
			e.printStackTrace();
		} finally {
			this.updateTimeStamp();
		}

	}

	@Override
	public synchronized Collection<Quote> getCurrentQuotes() throws RemoteException, InvalidSessionException {
		if (! this.isValid()) {
			throw new InvalidSessionException("Session no longer valid");
		}
		this.updateTimeStamp();
		return this.getQuoteList();
	}

	@Override
	public synchronized void confirmQuotes() throws RemoteException, ReservationException, InvalidSessionException {
		if (! this.isValid()) {
			throw new InvalidSessionException("Session no longer valid");
		}
		List<Reservation> reservationList = new ArrayList<Reservation>();
		try {
			for (Quote quote : this.getQuoteList()) {
				ICarRentalCompany company = this.getSessionManager().lookup(quote.getRentalCompany());
				Reservation res = company.confirmQuote(quote);
				reservationList.add(res);
			}
			this.getCurrentQuotes().clear();
		} catch (RemoteException e) {
			System.err.println("Error doing remote invocation");
			e.printStackTrace();
		} catch (ReservationException e) {
			System.err.println("Error confirming quote");
			e.printStackTrace();
			for (Reservation reservation : reservationList) {
				ICarRentalCompany company = this.getSessionManager().lookup(reservation.getRentalCompany());
				company.cancelReservation(reservation);
			}
			this.getCurrentQuotes().clear();
		} finally {
			this.updateTimeStamp();
		}
	}

	@Override
	public synchronized void checkAvailableCarTypes(Date start, Date end)
			throws RemoteException, InvalidSessionException {
		if (! this.isValid()) {
			throw new InvalidSessionException("Session no longer valid");
		}
		List<CarType> types = new ArrayList<CarType>();
		ICompanyIterator it = this.getSessionManager().getCompanyIterator();
		ICarRentalCompany company = null;
		while (it.hasNext()) {
			company = it.next();
			Set<CarType> companyTypes = company.getAvailableCarTypes(start, end);
			types.addAll(companyTypes);
		}
		for (CarType carType : types) {
			System.out.println(carType);
		}
		this.updateTimeStamp();
	}

	@Override
	public synchronized String getCheapestCarType(Date start, Date end)
			throws RemoteException, InvalidSessionException {
		if (! this.isValid()) {
			throw new InvalidSessionException("Session no longer valid");
		}
		CarType cheapest = null;
		ICompanyIterator it = this.getSessionManager().getCompanyIterator();
		ICarRentalCompany company = null;
		while (it.hasNext()) {
			company = it.next();
			CarType temp = company.getCheapestCarType(start, end);
			if (cheapest == null || temp.getRentalPricePerDay() < cheapest.getRentalPricePerDay()) {
				cheapest = temp;
			}
		}
		this.updateTimeStamp();
		return cheapest.getName();
	}
	
	private void updateTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}

}

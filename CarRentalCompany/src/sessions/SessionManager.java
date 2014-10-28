package sessions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import naming.CompanyNotFoundException;
import naming.ICompanyIterator;
import rental.ICarRentalCompany;
import agency.RentalAgency;

/**
 * Is responsible for determining when sessions should be destroyed
 * 
 * @author Thomas Vochten, Maarten Tegelaers
 *
 */
public class SessionManager implements Runnable {

	/**
	 * Initialises a new session manager with the given timeout.
	 * 
	 * @param timeOut
	 * 		In milliseconds. If sessions receive no calls for a period of timeOut, they are destroyed.
	 */
	public SessionManager(RentalAgency agency, long timeOut) {
		this.sessionList = new ArrayList<Session>();
		this.timeOut = timeOut;
		this.agency = agency;
	}
	
	/**
	 * Initialises a new session manager with the default timeout of five minutes.
	 */
	public SessionManager(RentalAgency agency) {
		this(agency, 5 * 60 * 1000);
	}
	
	private RentalAgency agency;
	
	private RentalAgency getAgency() {
		return this.agency;
	}
	
	public ICarRentalCompany lookupCompany(String companyName) throws CompanyNotFoundException, RemoteException {
		return this.getAgency().lookupCompany(companyName);
	}
	
	public ICompanyIterator getIterator() throws RemoteException {
		return this.getAgency().getIterator();
	}
	
	/**
	 * List of sessions managed by this session manager
	 */
	private List<Session> sessionList;
	
	/**
	 * @return The list of sessions managed by this session manager
	 */
	private List<Session> getSessionList() {
		return this.sessionList;
	}
	
	/**
	 * Let the given session be managed by this session manager
	 * 
	 * @param session
	 */
	public void addSession(Session session) {
		this.getSessionList().add(session);
	}
	
	/**
	 * In milliseconds. If sessions receive no calls for a period of timeOut, they are destroyed.
	 */
	private long timeOut;
	
	/**
	 * @return The timeout
	 */
	private long getTimeOut() {
		return this.timeOut;
	}
	
	/**
	 * Determines whether the given session must be destroyed.
	 * 
	 * @param session
	 * 		The session is question
	 * @return
	 * 		True iff the difference between system time and last call on sessions is timeOut
	 * @throws RemoteException 
	 */
	private boolean mustDestroySession(Session session) throws RemoteException {
		return (System.currentTimeMillis() - session.getTimeStamp()) >= this.getTimeOut();
	}
	
	
	
	@Override
	public void run() {
		while (true) {
			Iterator<Session> it = this.getSessionList().iterator();
			Session session = null;
			while (it.hasNext()) {
				session = it.next();
				synchronized(session) {
					try {
						if (mustDestroySession(session)) {
							try {
								session.destroy();
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							it.remove();
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

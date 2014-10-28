package agency;

import naming.CompanyNotFoundException;
import naming.ICompanyIterator;
import naming.INamingService;
import rental.ICarRentalCompany;
import sessions.IManagerSession;
import sessions.IReservationSession;
import sessions.ManagerSession;
import sessions.ReservationSession;
import sessions.SessionManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Mediator Server that serves as access point for both the INamingService of
 * the system, as well as the SessionManager.
 */
public class RentalAgency implements IRentalAgency {
    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------
    public RentalAgency() {
    	this.sessionManager = new SessionManager(this);
    	this.managerSession = new ManagerSession(this.sessionManager, this);
    	try {
			this.sessionStub = (IManagerSession) UnicastRemoteObject.exportObject(this.managerSession, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	Thread thread = new Thread(this.sessionManager);
    	thread.start();
    }
    
	@Override
	public void register(ICarRentalCompany newCompany) throws RemoteException {
        this.getNamingService().register(newCompany);
    }

	@Override
	public void unregister(String name) throws RemoteException {
        this.getNamingService().unregister(name);
    }
    
	@Override
	public ICarRentalCompany lookupCompany(String companyName) throws RemoteException, CompanyNotFoundException {
    	return this.getNamingService().lookupCompany(companyName);
    }
    
	@Override
	public ICompanyIterator getIterator() throws RemoteException {
    	return this.getNamingService().iterator();
    }

    /**
     * Get the INamingService of this RentalAgency.
     *
     * @return The INamingService of this RentalAgency.
     */
    private INamingService getNamingService() {
        return this.namingService;
    }
    
	@Override
	public void registerNamingService(INamingService service) {
    	this.namingService = service;
    }

    /** The INamingService of this RentalAgency. */
    private INamingService namingService;

    //------------------------------------------------------------------------
    // SessionManager
    //---------------------------------------------    ---------------------------
    /** The SessionManager of this RentalAgency */
    private final SessionManager sessionManager;
    
    private SessionManager getSessionManager() {
    	return this.sessionManager;
    }

	@Override
	public IReservationSession spawnReservationSession(String clientName)
			throws RemoteException {
		IReservationSession session = new ReservationSession(clientName, this.getSessionManager());
		this.getSessionManager().addSession(session);
		IReservationSession stub = (IReservationSession) UnicastRemoteObject.exportObject(session, 0);
		return stub;
	}
	
	 //------------------------------------------------------------------------
    // ManagerSession
    //---------------------------------------------    ---------------------------
	private ManagerSession managerSession;
	
	private IManagerSession sessionStub;
	
	public IManagerSession getManagerSession() throws RemoteException {
		return this.sessionStub;
	}
}

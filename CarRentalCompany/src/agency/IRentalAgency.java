package agency;

import java.rmi.Remote;
import java.rmi.RemoteException;

import naming.CompanyNotFoundException;
import naming.ICompanyIterator;
import naming.INamingService;
import rental.ICarRentalCompany;
import sessions.IManagerSession;
import sessions.IReservationSession;

public interface IRentalAgency extends Remote {

	//------------------------------------------------------------------------
	// Naming service
	//------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see agency.INamingService#register(rental.ICarRentalCompany)
	 */
	public abstract void register(ICarRentalCompany newCompany)
			throws RemoteException;

	/* (non-Javadoc)
	 * @see agency.INamingService#unregister(java.lang.String)
	 */
	public abstract void unregister(String name) throws RemoteException;

	/* (non-Javadoc)
	 * @see agency.INamingService#lookupCompany(java.lang.String)
	 */
	public abstract ICarRentalCompany lookupCompany(String companyName)
			throws RemoteException, CompanyNotFoundException;

	/* (non-Javadoc)
	 * @see agency.INamingService#getIterator()
	 */
	public abstract ICompanyIterator getIterator() throws RemoteException;

	/* (non-Javadoc)
	 * @see agency.INamingService#registerNamingService(naming.INamingService)
	 */
	public abstract void registerNamingService(INamingService service) throws RemoteException;
	
	public IReservationSession spawnReservationSession(String clientName) throws RemoteException;
	
	public IManagerSession getManagerSession() throws RemoteException;

}
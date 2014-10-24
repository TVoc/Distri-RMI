package naming;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rental.ICarRentalCompany;

/**
 * Implements the naming service used by clients to look up companies by name. Additionally, it is possible to register
 * and unregister companies from the naming service.
 * 
 * @author Thomas Vochten
 *
 */
public interface INamingService extends Remote {
	
	/**
	 * Looks up the company specified by the given name. If such a company has not been registered, an exception is raised.
	 * 
	 * @param name
	 * 		Name to look up a company by.
	 * @return
	 * 		The company specified by name.
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation.
	 * @throws CompanyNotFoundException
	 * 		The company specified by name has not been registered.
	 * @throws IllegalArgumentException
	 * 		name is null.
	 */
	public ICarRentalCompany lookupCompany(String name) throws RemoteException, CompanyNotFoundException, IllegalArgumentException;
	
	/**
	 * Registers the given company under the name of that company.
	 * 
	 * @param company
	 * 		Company to be registered
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation.
	 * @throws IllegalArgumentException
	 * 		company is null.
	 */
	public void register(ICarRentalCompany company) throws RemoteException, IllegalArgumentException;
	
	/**
	 * Unregisters the company specified by name. Has no effect if no such company has been registered.
	 * 
	 * @param name
	 * 		Name of the company to be unregistered
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation.
	 * @throws IllegalArgumentException
	 * 		name is null.
	 */
	public void unregister(String name) throws RemoteException, IllegalArgumentException;
	
	/**
	 * @return An object that allows a client to perform an operation on every company currently registered.
	 * @throws RemoteException
	 * 		Something goes wrong during remote invocation.
	 */
	public ICompanyIterator iterator() throws RemoteException;

}

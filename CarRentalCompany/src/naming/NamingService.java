package naming;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import rental.ICarRentalCompany;

public class NamingService implements INamingService {

	public NamingService() {
		companies = new HashMap<String, ICarRentalCompany>();
	}
	
	private Map<String, ICarRentalCompany> companies;
	
	/**
	 * @return This NamingService's companies.
	 */
	private Map<String, ICarRentalCompany> getCompanies() {
		return this.companies;
	}
	
	@Override
	public ICarRentalCompany lookupCompany(String name) throws RemoteException,
			CompanyNotFoundException, IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name was null");
		}
		ICarRentalCompany toReturn = null;
		toReturn = this.getCompanies().get(name);
		if (toReturn == null) {
			throw new CompanyNotFoundException("Company by name of " + name + " was not found.");
		}
		return toReturn;
	}

	@Override
	public void register(ICarRentalCompany company) throws RemoteException,
			IllegalArgumentException {
		if (company == null) {
			throw new IllegalArgumentException("company was null");
		}
		this.getCompanies().put(company.getName(), company);
	}

	@Override
	public void unregister(String name) throws RemoteException,
			IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name was null");
		}
		this.getCompanies().remove(name);
	}

	@Override
	public ICompanyIterator iterator() throws RemoteException {
		ICompanyIterator toReturn = new CompanyIterator();
		ICompanyIterator stub = (ICompanyIterator) UnicastRemoteObject.exportObject(toReturn, 0);
		return stub;
	}
	
	private class CompanyIterator implements ICompanyIterator {

		public CompanyIterator() {
			this.it = NamingService.this.getCompanies().values().iterator();
		}
		
		Iterator<ICarRentalCompany> it;
		
		private Iterator<ICarRentalCompany> getIterator() {
			return this.it;
		}
		
		@Override
		public boolean hasNext() throws RemoteException {
			return this.getIterator().hasNext();
		}

		@Override
		public ICarRentalCompany next() throws RemoteException,
				NoSuchElementException {
			return this.getIterator().next();
		}
		
	}

}

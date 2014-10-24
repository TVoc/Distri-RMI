package naming;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

import rental.ICarRentalCompany;

/**
 * 
 * Meant to be used in conjunction with a naming service. Allows a client to perform operations on all companies contained herein.
 * 
 * @author Thomas Vochten
 *
 */
public interface ICompanyIterator extends Remote {
	
	public boolean hasNext() throws RemoteException;
	
	public ICarRentalCompany next() throws RemoteException, NoSuchElementException;

}

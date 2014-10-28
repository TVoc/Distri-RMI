package sessions;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Sessions live on the agency server.
 * 
 * @author Thomas Vochten
 *
 */
public interface Session extends Remote {
	
	/**
	 * @return Indicates whether this sessions can process method invocations
	 */
	public boolean isValid() throws RemoteException;
	
	/**
	 * @return The time stamp of the last call to this session
	 */
	long getTimeStamp() throws RemoteException;
	
	/**
	 * Destroys the session such that it cannot process calls anymore.
	 */
	void destroy() throws RemoteException;
	
    SessionManager getSessionManager() throws RemoteException;

}

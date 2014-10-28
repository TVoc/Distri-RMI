package sessions;

import java.util.ArrayList;
import java.util.List;

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
	public SessionManager(long timeOut) {
		this.sessionList = new ArrayList<Session>();
		this.timeOut = timeOut;
	}
	
	/**
	 * Initialises a new session manager with the default timeout of five minutes.
	 */
	public SessionManager() {
		this(5 * 60 * 1000);
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
	 */
	private boolean mustDestroySession(Session session) {
		return (System.currentTimeMillis() - session.getTimeStamp()) >= this.getTimeOut();
	}
	
	@Override
	public void run() {
		while (true) {
			for (Session session : this.getSessionList()) {
				synchronized(session) {
					if (mustDestroySession(session)) {
						session.destroy();
						this.getSessionList().remove(session);
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

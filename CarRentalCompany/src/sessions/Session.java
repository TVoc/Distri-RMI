package sessions;

/**
 * Sessions live on the agency server.
 * 
 * @author Thomas Vochten
 *
 */
public interface Session {
	
	/**
	 * @return Indicates whether this sessions can process method invocations
	 */
	public boolean isValid();
	
	/**
	 * @return The time stamp of the last call to this session
	 */
	long getTimeStamp();
	
	/**
	 * Destroys the session such that it cannot process calls anymore.
	 */
	void destroy();
	
    SessionManager getSessionManager();

}

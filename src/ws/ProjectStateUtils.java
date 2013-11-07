package ws;


public interface ProjectStateUtils {

	/**
	 * Read the project state
	 * @return
	 */
	public Object read();
	
	/**
	 * Update the project state
	 */
	public void update();
	
	/**
	 * Delete the project state
	 */
	public void delete();
}

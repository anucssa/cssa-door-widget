package au.edu.anu.club.cs.cssadoorwidget;

public class DoorAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public DoorAccessException(Throwable source) {
		super("Could not access door state", source);
	}
}

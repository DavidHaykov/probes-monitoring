package telran.exceptions;

@SuppressWarnings("serial")
public class EntityAlreadyExistsException extends RuntimeException
{
	public EntityAlreadyExistsException(String message)
	{
		super(message);
	}
}

package room13.message.messages;

import room13.message.RawMessage;

/**
 * Event message notifying that user has changed names
 * @author Habbes
 *
 */
public class UserNameChangedEventMessage extends UserEventMessage {

	private String name;
	
	public UserNameChangedEventMessage(String user, String name) {
		super(EventMessage.USER_NAME_CHANGED, user);
		raw.addValue(name);
		this.name = name;
	}

	public UserNameChangedEventMessage(String user) {
		this(user, "");
	}

	public UserNameChangedEventMessage() {
		this("");
	}

	public UserNameChangedEventMessage(RawMessage rm) {
		super(rm);
		name = rm.getValue(2);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		name = n;
		raw.setValue(2, n);
	}

}

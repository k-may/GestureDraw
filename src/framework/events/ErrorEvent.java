package framework.events;

import framework.ErrorType;

public class ErrorEvent extends LogEvent {

	private ErrorType _errorType;

	public ErrorEvent(ErrorType errorType, String msg) {
		super(EventType.Error, msg);

		_errorType = errorType;
	}
	
	public ErrorType get_errorType() {
		return _errorType;
	}

}

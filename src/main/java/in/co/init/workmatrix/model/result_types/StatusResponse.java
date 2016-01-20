package in.co.init.workmatrix.model.result_types;

import java.io.Serializable;

/**
 * Provide response for customized status messages in case of exception or
 * invalid service parameter data
 */
public class StatusResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String message;

	public StatusResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}

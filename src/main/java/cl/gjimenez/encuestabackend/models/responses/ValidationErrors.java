package cl.gjimenez.encuestabackend.models.responses;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class ValidationErrors { 
	
	
	private Map<String,String> errors;
	
	private Date timestamp;
	
	public ValidationErrors(Map<String, String> errors, Date timestamp) {
		super();
		this.errors = errors;
		this.timestamp = timestamp;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
	
}

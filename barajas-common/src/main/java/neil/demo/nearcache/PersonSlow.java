package neil.demo.nearcache;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * {@link java.io.Serializable}, the slowest easiest way.
 * </p>
 */
@Data
@SuppressWarnings("serial")
public class PersonSlow implements Serializable {

	private String firstName;
	private String lastName;
	private int count;
}

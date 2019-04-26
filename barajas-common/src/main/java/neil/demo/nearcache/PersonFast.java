package neil.demo.nearcache;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * {@link com.hazelcast.nio.serialization.IdentifiedDataSerializable}, the fastest way.
 * </p>
 */
@Data
@Slf4j
public class PersonFast implements IdentifiedDataSerializable {

	private String firstName;
	private String lastName;
	private int count;
	
	// Send this object
	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeInt(this.count);
		out.writeUTF(this.firstName);
		out.writeUTF(this.lastName);
		log.info("Serialize {}", this);
	}
	
	// Receive this object
	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.count = in.readInt();
		this.firstName = in.readUTF();
		this.lastName = in.readUTF();
		log.info("De-serialize {}", this);
	}
	
	// What creates this object
	@Override
	public int getFactoryId() {
		return 1;
	}

	// The code representing this class
	@Override
	public int getId() {
		return 2;
	}
}

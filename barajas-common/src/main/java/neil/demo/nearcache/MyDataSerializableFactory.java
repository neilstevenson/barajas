package neil.demo.nearcache;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyDataSerializableFactory implements DataSerializableFactory {

	@Override
	public IdentifiedDataSerializable create(int arg0) {

		if (arg0 == 2) {
			return new PersonFast();
		}

		log.error("create() -> {}", arg0);
		return null;
	}

}

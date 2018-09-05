package tuanpv.thbs.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONUtils {
	public static Map<String, Object> parse(String path) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(new File(path), HashMap.class);
	}
}

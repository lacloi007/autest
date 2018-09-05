package tuanpv.thbs.flow.action;

import java.util.Map;

public interface WFAction {
	public void apply(Map<String, Object> data) throws Exception;

	public void draft(Map<String, Object> data) throws Exception;

	public void completed(Map<String, Object> data) throws Exception;

	public void returnBack(Map<String, Object> data) throws Exception;

	public void reject(Map<String, Object> data) throws Exception;

	public void cancel(Map<String, Object> data) throws Exception;
}

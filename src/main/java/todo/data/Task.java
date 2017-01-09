/*
 * Each of the tasks
 * 
 * @author jnana
 */
package todo.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Task {
	
	public STATE getStatus() {
		return status;
	}

	public void setStatus(STATE status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public LocalDateTime getDeadline() {
		return dueDate;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.dueDate = deadline;
	}

	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getAsignee() {
		return assignee;
	}

	public void setAsignee(String asignee) {
		this.assignee = asignee;
	}
	
	private String title;
	private String desc;
	private LocalDateTime dueDate;
	private List<String> tags;
	private STATE status;
	private String assignee;
	
	public Task(String title){
		this(title,"Please update description");
	}
	
	public Task(String title, String desc){
		this.desc = desc;
		this.title = title;
		this.status = STATE.TOBEDONE;
		this.tags = new LinkedList<String>();
		//By default set a deadline of one week
		this.dueDate = LocalDateTime.now();
		this.dueDate = dueDate.plusWeeks(1);
		this.assignee = "myself";
	}
	
	public Task(String title, String desc, String tags){
		this(title, desc);
		for(String tmp : tags.split(" ")){
			this.tags.add(tmp);
		}
	}
	
	public Task(String title, String desc, String tags, LocalDateTime deadline){
		this(title, desc, tags);
		this.dueDate = deadline;
	}
	
	public Task(String title, String desc, String tags, LocalDateTime deadline, String asignee){
		this(title, desc);
		for(String tmp : tags.split(" ")){
			this.tags.add(tmp);
		}
		this.dueDate = deadline;
		this.assignee = asignee;
	}
	
	public void addTags(String tags){
		for(String tmp : tags.split(" ")){
			if(!this.tags.contains(tmp)){
				this.tags.add(tmp);
			}
		}
	}
	
	public void removeTags(String tags){
		for(String tmp : tags.split(" ")){
			if(this.tags.contains(tmp)){
				this.tags.remove(tmp);
			}
		}
	}
	
	public void removeAllTags(){
		this.tags.clear();
	}
	
	public String toJson(){
		//TODO return json representation of this object, it should include details of all the object parameters (except tags)
		JSONObject json = new JSONObject();
		json.put("title", this.title);
		if(this.desc != null){
			json.put("desc", this.desc);
		}
		if(this.status != null){
			json.put("status", this.status.toString());
		}
		if(this.dueDate != null){
			json.put("dueDate", this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}
		if(!this.tags.isEmpty()){
			JSONArray tags = new JSONArray();
			for(String tag : this.tags){
				tags.add(tag);
			}
			json.put("tags", tags);
		}
		if(this.assignee != null){
			json.put("assignee", this.assignee.toString());
		}
		return json.toString();
		//return json.toJSONString();
	}
	
	public enum STATE{
		
		TOBEDONE(0),
		DOING(1),
		DONE(2);
		
		private static final String[] states = { "TOBEDONE", "DOING", "DONE"};
		
		private int index;
		
		STATE(int index){
			this.index = index;
		}
		
		public String toString(){
			return states[this.index];
		}
	}
}

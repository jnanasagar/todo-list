/* 
 * Handles all the crud requests for todo-list
 * @author jnana
 */

package todo.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import todo.data.TodoList;
import todo.data.Task;

@Path("/")
public class TodoListService {

	private static TodoList list = new TodoList();
	
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		list.addTask("first");
		list.addTask("second", "what am I supposed to do", "I dont know");
		list.addTask("Third", "this is third task", "I swear to go I dont know what to do", LocalDateTime.now().plusDays(2));
		
		//TODO print the whole task list
		
		System.out.println("Task list : " + list.toJson());
		
		Task item = list.getTask("first");
		item.setStatus(Task.STATE.DOING);
		
		System.out.println("tasks with DOING status : "+ categorize("DOING"));
		
		list.deleteTask("Third");
		
		System.out.println("Tasklist after delete : " + list.toJson());
		
		String tmp = "{\"due_date\":\"2017-01-14T21:14:59.43\",\"title\":\"fourth\",\"desc\":\"Please update description\",\"status\":\"DOING\"}";
		addTask(tmp);
	}*/
	
	public static void initiate(){
		addTask("{\"tile\":\"first\"}");
		addTask("{\"title\":\"second\", \"desc\":\"what am I supposed to do\",\"tags\": \"I dont know\"}");
		list.addTask("{\"title\":\"Third\", \"desc\":\"this is third task\", \"tags\": \"I swear to go I dont know what to do\", \"dueDate\": \""+LocalDateTime.now().plusDays(2) + "\", \"assignee\": \"jnana\" }");
		
		System.out.println("Task list : " + list.toJson());
		
		Task item = list.getTask("first");
		item.setStatus(Task.STATE.DOING);
		String tmp = "{\"due_date\":\"2017-01-14T21:14:59.43\",\"title\":\"fourth\",\"desc\":\"Please update description\",\"status\":\"DOING\"}";
		addTask(tmp);
		System.out.println("Task list : " + list.toJson());
	}

	//Read methods, will be get requests
	//Read only tasks of particular state
	@GET
	@Path("categorize/{state}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String categorize(@PathParam("state")String state){
		//initiate();//TODO should take out this line at the end
		JSONObject json = new JSONObject();
		int count = 1;
		for(Task task : list.taskList){
			if(task.getStatus().toString().equalsIgnoreCase(state)){
				
				json.put(count, task.toJson());
				count++;
			}
		}
		return json.toJSONString();
	}
	
	//Read all tasks
	@GET
	@Path("tasks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String tasks(){
		return list.toJson();
	}
	
	//TODO Delete a task, will be a get request
	@DELETE
	@Path("/{title}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String deleteTask(@PathParam("title")String title){
		for(Task tmp : list.taskList){
			if(tmp.getTitle().equalsIgnoreCase(title)){
				try{
					list.taskList.remove(tmp);
					return "task : "+title+" is deleted";
				}catch(Exception e){
					System.out.println("There was an error while deleting the task : "+title);
					e.printStackTrace();
					return "could not delete : "+e.getMessage();
				}
			}
		}
		return "There was no task : "+title;
	}
	
	//TODO Create and Update features will be post requests
	@POST
	@Path("addTask")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String addTask(String data){
		System.out.println("post data : "+ data);
		try {
			JSONObject taskData = (JSONObject) new JSONParser().parse(data);
			String title = "";
			String desc = "";
			String task = "";
			String assignee = "myself";
			LocalDateTime dueDate = LocalDateTime.now();
			if(!taskData.containsKey("title") || taskData.get("title").toString().equals("")){
				return "Cannot add new task with empty title";
			}else{
				title = taskData.get("title").toString();
			}
			
			if(taskData.containsKey("desc")){
				desc = taskData.get("desc").toString();
			}
			
			if(taskData.containsKey("dueDate")){
				dueDate = LocalDateTime.parse(taskData.get("dueDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
			
			if(taskData.containsKey("assignee")){
				assignee = taskData.get("assignee").toString();
			}
			
			String tags = "";
			//check if tags is a string or JSONArray and do the needful
			if(taskData.containsKey("tags") && taskData.get("tags").getClass() == (JSONArray.class)){
				JSONArray arr = (JSONArray) taskData.get("tags");
				for(Object tag : arr){
					if(tags.equals("")){
						tags = tag.toString();
					}else{
						tags = tags + " " + tag.toString();
					}
				}
			}else if(taskData.containsKey("tags") && taskData.get("tags").getClass() == (String.class)){
				tags = taskData.get("tags").toString();
				tags.replace('[', ' ');
				tags.replace(']',' ');
				tags.trim();
			}
			//list.addTask(taskData.get("title").toString(), taskData.get("desc").toString(), taskData.get("tags").toString(), LocalDateTime.parse(taskData.get("dueDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			list.addTask(title, desc, tags, dueDate, assignee);		
		} catch (ParseException e) {
			System.out.println("There was an exception on adding new Task" + e.getMessage());
			e.printStackTrace();
			return "Could not add new task : "+ e.getMessage();
		}
		return "Task addition successful";
	}
	
	//TODO UPDATE, will be a post request
	@POST
	@Path("updateTask")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String updateTask(String data){
		try{
			JSONObject taskUpd = (JSONObject) new JSONParser().parse(data);
			if(!taskUpd.containsKey("title") || taskUpd.get("title").toString().equals("")){
				return "Update failure : Please provide title";
			}
			Task task = list.getTask(taskUpd.get("title").toString());
			if(task == null){
				return "Update failure, there is no task with given title";
			}
			if(taskUpd.containsKey("status") && !(task.getStatus().toString().equals(taskUpd.get("status").toString()))){
				if(taskUpd.get("status").toString().equals(Task.STATE.TOBEDONE.toString())){
					task.setStatus(Task.STATE.TOBEDONE);
				}else if(taskUpd.get("status").toString().equals(Task.STATE.DOING.toString())){
					task.setStatus(Task.STATE.DOING);
				}else if(taskUpd.get("status").toString().equals(Task.STATE.DONE.toString())){
					task.setStatus(Task.STATE.DONE);
				}else{
					return "Update failure : Status should one of (TOBEDONE, DOING, DONE)";
				}
			}
			//shouldn't update title, thats my primary key
			if(taskUpd.containsKey("desc") && !task.getDesc().equals(taskUpd.get("desc").toString())){
				task.setDesc(taskUpd.get("desc").toString());
			}
			if(taskUpd.containsKey("assignee") && !task.getAsignee().equals(taskUpd.get("assignee").toString())){
				task.setAsignee(taskUpd.get("assignee").toString());
			}
			if(taskUpd.containsKey("dueDate") && !(task.getDeadline().equals(LocalDateTime.parse(taskUpd.get("dueDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)))){
				
				task.setDeadline(LocalDateTime.parse(taskUpd.get("dueDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			}
			if(taskUpd.containsKey("tags") && taskUpd.get("tags").getClass() == (JSONArray.class)){
				task.removeAllTags();
				JSONArray arr = (JSONArray) taskUpd.get("tags");
				for(Object tag : arr){
					task.addTags(tag.toString());
				}
			}else if(taskUpd.containsKey("tags") && taskUpd.get("tags").getClass() == (String.class)){
				task.removeAllTags();
				task.addTags(taskUpd.get("tags").toString().replace('[', ' ').replace(']', ' ').replace(',', ' ').replace('"', ' ').trim());
			}
			
		}catch(Exception e){
			System.out.println("There was an error on updating task : "+e.getMessage());
			e.printStackTrace();
			return "Update failure : "+ e.getMessage();
		}
		return "Task update successfull";
	}
}

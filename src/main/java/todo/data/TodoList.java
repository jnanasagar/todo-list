/*
 * provides todo-list behaviour
 * 
 * @author jnana
 */

package todo.data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

public class TodoList {
	
	public List<Task> taskList;
	
	public TodoList(){
		this.taskList = new LinkedList<Task>();
	}
	
	
	public Boolean addTask(String title, String desc){
		if(!this.checkTask(title)){
			try{
				this.taskList.add(new Task(title, desc));
			}catch(Exception e){
				System.out.println("There was an exception on task creation : "+e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Boolean addTask(String title){
		if(!this.checkTask(title)){
			try{
				this.taskList.add(new Task(title));
			}catch(Exception e){
				System.out.println("There was an exception on task creation : "+e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Boolean addTask(String title, String desc, String tags){
		if(!this.checkTask(title)){
			try{
				this.taskList.add(new Task(title, desc, tags));
			}catch(Exception e){
				System.out.println("There was an exception on task creation : "+e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Boolean addTask(String title, String desc, String tags, LocalDateTime dueDate){
		if(!this.checkTask(title)){
			try{
				this.taskList.add(new Task(title, desc, tags, dueDate));
			}catch(Exception e){
				System.out.println("There was an exception on task creation : "+e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Boolean addTask(String title, String desc, String tags, LocalDateTime dueDate, String assignee){
		if(!this.checkTask(title)){
			try{
				this.taskList.add(new Task(title, desc, tags, dueDate, assignee));
			}catch(Exception e){
				System.out.println("There was an exception on task creation : "+e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Boolean deleteTask(String title){
		for(Task tmp : this.taskList){
			if(tmp.getTitle() == title){
				this.taskList.remove(tmp);
				return true;
			}
		}
		
		return false;
	}
	
	public Task getTask(String title){
		
		for(Task tmp : this.taskList){
			if(tmp.getTitle().equalsIgnoreCase(title)){
				return tmp;
			}
		}
		return null;
	}
	//check if a task exists with same title or not
	public Boolean checkTask(String title){
		for(Task tmp : this.taskList){
			if(tmp.getTitle().equalsIgnoreCase(title)){
				return true;
			}
		}
		
		return false;
	}
	
	public String toJson(){
		JSONObject json = new JSONObject();
		int count = 1;
		for(Task item : this.taskList){
			json.put(count, item.toJson());
			count++;
		}
		return json.toString();
		//return json.toJSONString();
	}
}

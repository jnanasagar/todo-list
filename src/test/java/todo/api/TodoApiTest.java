/*
 * Api testing
 * @author jnana
 */
package todo.api;


import java.net.URI;
import junit.framework.Assert;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/*import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)*/
public class TodoApiTest {
	
	public static WebTarget target;
	public static final String BASE_URI = "http://localhost:8080/todo-list/";
	private HttpServer server;

	@Before
	public void setUp() throws Exception {
		final ResourceConfig resConfig = new ResourceConfig(TodoListService.class);
		server = GrizzlyHttpServerFactory.createHttpServer(
				URI.create(BASE_URI), resConfig);
		//create a target to be used in all the test cases .
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		TodoApiTest.target = client.target(UriBuilder.fromUri(
				"http://localhost:8080/todo-list").build());
	}

	@After
	public void tearDown() throws Exception {
		server.shutdownNow();
	}

	@Test
	public void addTaskTest(){
		final String task = "{\"title\":\"Third\", \"desc\":\"this is third task\", \"tags\": \"I swear to god I dont know what to do\", \"dueDate\": \"2017-01-14T21:14:59.43\", \"assignee\": \"myself\" }";
		String response = TodoApiTest.target.path("addTask").request(MediaType.APPLICATION_JSON).post(Entity.entity(task, MediaType.APPLICATION_JSON), String.class);
		Assert.assertEquals("Task addition successful", response);
	}
	
	
	@Test
	public void updateTasktest(){
		final String task = "{\"title\":\"Third\", \"desc\":\"this is third task\", \"tags\": \"I swear to go I dont know what to do\", \"dueDate\": \"2017-01-14T21:14:59.43\", \"assignee\": \"jnana\" }";
		String response = TodoApiTest.target.path("updateTask").request(MediaType.APPLICATION_JSON).post(Entity.entity(task, MediaType.APPLICATION_JSON), String.class);
		Assert.assertEquals("Task update successfull",response);
	}
	
	@Test
	public void deleteTasktest(){
		String response = TodoApiTest.target.path("Third").request().delete(String.class);
		Assert.assertEquals("task : Third is deleted", response);
	}
	
	@Test
    public void readTaskstest() {
		String response = TodoApiTest.target.path("tasks").request().get(String.class);
		Assert.assertEquals("{}", response);        
    }
}

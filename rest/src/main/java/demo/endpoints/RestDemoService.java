package demo.endpoints;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.model.Demo;
import demo.services.DemoService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/demo")
@Api(value = "demo", description = "Demo")
public class RestDemoService {

	private static final Logger log = LoggerFactory.getLogger(RestDemoService.class);

	@Inject
	private DemoService service;

	@GET
	@Path("/fetch")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Fetch the counter value.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
	})
	public Demo get() {
		log("fetch");
		return new Demo(
				service.fetchCounter()
		);
	}

	@GET
	@Path("/increase")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Fetch and increase the counter value.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
	})
	public Demo post() {
		log("increase");
		return new Demo(
				service.fetchAndIncreaseCounter()
		);
	}

	@GET
	@Path("/reset")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation("Reset the counter.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Reset"),
	})
	public String reset() {
		log("reset");
		try {
			service.reset();
			return "Done";
		} catch (final Exception e) {

			return "Fail";
		}
	}

	private void log(final String endpoint) {
		log.info("Call rest endpoint: {}", endpoint);
	}
}

package demo.endpoints;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.services.Version;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Reset all data saved in memory.
 */
@Api(value = "system", description = "System")
@Path("/system")
public class RestSystemService {

    private static final Logger log = LoggerFactory.getLogger(RestSystemService.class);

    @GET
    @Path("/version")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Query for version and git informations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    public RestVersion version(@Context final ServletContext servletContext) {
        log.info("Restcall GET 'system/version'");
        final Version version = Version.ofWebapp(servletContext);
        return new RestVersion(version);
    }

}

package demo;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.reflections.Reflections;

import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@ApplicationPath("/" + RestApplication.API_VERSION)
public class RestApplication extends Application {

    public static final String API_VERSION = "v1";
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US", "US");

    static {
        Locale.setDefault(DEFAULT_LOCALE);
    }

    @Override
    public Set<Object> getSingletons() {
        final Set<Object> singletons = new HashSet<>(super.getSingletons());
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<>(super.getClasses());
        resources.add(ApiListingResourceJSON.class);
        resources.add(ApiDeclarationProvider.class);
        resources.add(ResourceListingProvider.class);
        resources.add(ApiListingResource.class);
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(final Set<Class<?>> resources) {
        final Reflections reflections = new Reflections(getClass().getPackage().getName());

        final Set<Class<?>> restClasses = reflections.getTypesAnnotatedWith(Path.class);
        resources.addAll(restClasses.stream().collect(Collectors.toList()));

        final Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(Provider.class);
        resources.addAll(providerClasses.stream().collect(Collectors.toList()));

    }

}

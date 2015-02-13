package demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

/**
 * Loads version information from the JAR or WAR manifest, and also infers a
 * build timestamp based on the modification time of the manifest. Useful for
 * printing version information in an application for QA purposes.
 * <p>
 * Usage:
 * <pre class="example">
 * Version v = Version.ofJar(MyApplication.class);
 * v.getVersion() // "1.0"
 * v.getModifiedDate() // Wed Feb 25 13:51:35 PST 2009
 * v.getBuildNumber() // 34
 * v.getGitHash() // ff345ee2
 * v.getBuildTag() // jenkins-${JOBNAME}-${BUILD_NUMBER}
 * v.getBuildId() // 2005-08-22_23-59-59
 * </pre>
 * <p>
 * For this to work, the version number needs to be included in the JAR
 * metadata. Maven will do this for you, but you must add the following
 * snippet to your POM.
 *
 * @author Daniel Zwicker
 */
public class Version {

	private static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
	private static final Pattern COMPILE = Pattern.compile("\\.");
	private Manifest _manifest = new Manifest();
	private Date _modified;

	/**
	 * Private constructor.
	 */
	private Version() {
	}

	/**
	 * Creates a Version object by loading the manifest for this webapp.
	 *
	 * @throws RuntimeException if there is an error parsing the manifest
	 */
	public static Version ofWebapp(final ServletContext context) {
		try {
			return fromUrl(context.getResource(MANIFEST_PATH));
		} catch (final MalformedURLException murle) {
			throw new RuntimeException(murle);
		}
	}

	/**
	 * Creates a Version object by loading the manifest from the JAR
	 * that contains the specified class.
	 *
	 * @throws RuntimeException if there is an error parsing the manfest
	 */
	public static Version ofJar(final Class<?> cls) {
		String url = null;

		// Construct the path to the class file and get its URL.
		// E.g. /org/apache/wicket/Component.class
		final String path = "/" + COMPILE.matcher(cls.getName()).replaceAll("/") + ".class";
		final URL classRsrc = cls.getResource(path);

		if (classRsrc != null) {
			// If if the class was in a JAR, its URL should look like this:
			// jar:file:/path/to/the/jar!/org/apache/wicket/Component.class

			// Replace the part after the ! with the manifest path.
			// That way we open the manifest stored in that particular JAR.

			if (classRsrc.toString().startsWith("jar:") &&
					classRsrc.toString().indexOf(path) > 0) {
				url = classRsrc.toString().replace(path, MANIFEST_PATH);
			}
		}
		try {
			return fromUrl(url != null ? new URL(url) : null);
		} catch (final MalformedURLException ignore) {
			return fromUrl(null);
		}
	}

	private static Version fromUrl(final URL url) {
		final Version v = new Version();
		if (url != null) {
			InputStream stream = null;
			try {
				final URLConnection conn = url.openConnection();
				v._modified = new Date(conn.getLastModified());

				stream = conn.getInputStream();
				v._manifest = new Manifest(stream);
			} catch (final IOException ioe) {
				throw new RuntimeException(
						"Failed to load manifest from " + url, ioe
				);
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (final IOException ignored) {
				}
			}
		}
		return v;
	}

	/**
	 * Returns the date when the application was built. This is determined
	 * by looking at the modification time of the JAR/WAR.
	 */
	public Date getModifiedDate() {
		return _modified;
	}

	/**
	 * Returns the version number of the application, taken from the
	 * Implementation-Version entry of the manifest. If the version is
	 * not present or the manifest cannot be located, returns "unknown".
	 */
	public String getVersion() {
		return getManifestMainAttribute(
				Name.IMPLEMENTATION_VERSION, "unknown"
		);
	}

	/**
	 * Returns full descriptive version of current build. Includes closest tag, tag delta,
	 * and abbreviated commit hash or "unknown".
	 */
	public String getFullversion() {
		return getManifestMainAttribute(
				new Name("fullversion"), "unknown"
		);
	}

	/**
	 * Returns the title of the application, taken from the
	 * Implementation-Title entry of the manifest. If the name is
	 * not present or the manifest cannot be located, return "unknown".
	 */
	public String getTitle() {
		return getManifestMainAttribute(
				Name.IMPLEMENTATION_TITLE, "unknown"
		);
	}

	/**
	 * Returns the build number. If there is no build number present, return "unknown".
	 */
	public String getBuildNumber() {
		return getManifestMainAttribute(
				new Name("build-number"), "unknown"
		);
	}

	/**
	 * Returns the git hash. If there is no git hash present, return "unknown".
	 */
	public String getGitHash() {
		return getManifestMainAttribute(
				new Name("git-hash"), "unknown"
		);
	}

	/**
	 * Returns the timestamp of this git commit or "unknown".
	 */
	public String getGitTimeStamp() {
		return getManifestMainAttribute(
				new Name("git-tstamp"), "unknown"
		);
	}

	/**
	 * Returns the build tag. If there is no build tag present, return "unknown".
	 */
	public String getBuildTag() {
		return getManifestMainAttribute(
				new Name("build-tag"), "unknown"
		);
	}

	/**
	 * Returns the build id. If there is no build id present, return "unknown".
	 */
	public String getBuildId() {
		return getManifestMainAttribute(
				new Name("build-id"), "unknown"
		);
	}

	/**
	 * Returns the manifest where the version information is held. May be
	 * empty.
	 */
	public Manifest getManifest() {
		return _manifest;
	}

	/**
	 * Returns the string value of the main attribute that has the specified
	 * name. If the attribute is not present in the manifest, return the
	 * defaultValue instead.
	 */
	public String getManifestMainAttribute(final Name name,
			final String defaultValue) {
		final Map atts = _manifest.getMainAttributes();
		final String value = (String) atts.get(name);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else if (value.startsWith("${")) {
			return defaultValue;
		} else if (value.equals("null")) {
			return defaultValue;
		}
		return value;
	}

	public String getModifiedDateAsString() {
		if (_modified != null) {
			return new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(_modified);
		} else {
			return "unknown";
		}
	}

}

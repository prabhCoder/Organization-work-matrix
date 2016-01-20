package in.co.init.workmatrix.model.result_types;

import java.io.Serializable;
import java.net.URI;

/**
 * Provides JSON output for employee/ project resource link
 */
public class ResourceLink implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rel;
	private String name;
	private URI href;

	public ResourceLink(String rel, String name, URI href) {
		super();
		this.rel = rel;
		this.name = name;
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public String getRel() {
		return rel;
	}

	public URI getHref() {
		return href;
	}

}

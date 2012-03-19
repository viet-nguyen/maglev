package com.altaworks.magnolia;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.importexport.BootstrapUtil;
import info.magnolia.importexport.DataTransporter;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractTask;
import info.magnolia.module.delta.TaskExecutionException;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.jcr.ImportUUIDBehavior;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Åke Argéus
 */
public class BootstrapGrailsResources extends AbstractTask {


	public BootstrapGrailsResources() {
		super("Grails bootstrap", "Bootstraps using a real classpathscanner(springs)");
	}

	@Override
	public void execute(InstallContext installContext) throws TaskExecutionException {

		try {

			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath*:/grails-bootstrap/*.xml");

			List<Resource> list = Arrays.asList(resources);

			Collections.sort(list, new Comparator<Resource>() {
				@Override
				public int compare(Resource resource, Resource resource1) {
					if (resource.getFilename().length() > resource1.getFilename().length())
						return 1;
					if (resource.getFilename().length() < resource1.getFilename().length())
						return -1;
					return 0;
				}
			});

			for (Resource resource : list) {
				String resourceName = resource.getFilename();

				String name = BootstrapUtil.getFilenameFromResource(resourceName, ".xml");
				String repository = BootstrapUtil.getWorkspaceNameFromResource(resourceName);
				String pathName = BootstrapUtil.getPathnameFromResource(resourceName);
				String fullPath = BootstrapUtil.getFullpathFromResource(resourceName);
				String nodeName = StringUtils.substringAfterLast(fullPath, "/");

				log.debug("Will bootstrap {}", resourceName);

				final InputStream stream = resource.getInputStream();
				if (stream == null) {
					throw new IOException("Can't find resource to bootstrap at " + resourceName);
				}

				// if the node already exists we will keep the order
				String nameOfNodeAfterTheImportedNode = null;

				final HierarchyManager hm = MgnlContext.getHierarchyManager(repository);

				DataTransporter.importXmlStream(stream, repository, pathName, name, false, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING, false, true);

				if (nameOfNodeAfterTheImportedNode != null) {
					Content newNode = hm.getContent(fullPath);
					newNode.getParent().orderBefore(nodeName, nameOfNodeAfterTheImportedNode);
				}
			}
		} catch (Exception e) {
			throw new TaskExecutionException("Failed to bootstrap grails resources", e);
		}
	}


}

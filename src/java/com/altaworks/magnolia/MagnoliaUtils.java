package com.altaworks.magnolia;

import info.magnolia.cms.core.Content;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.blossom.support.RepositoryUtils;

import javax.jcr.RepositoryException;
import java.util.*;

public class MagnoliaUtils {

    public static String getUrlFromUuidNodeData(String nodeName) {
        String redirectUUid = RepositoryUtils.getLocalNodeData(nodeName);
        Content redirectContent = RepositoryUtils.getWebsiteContentByUuid(redirectUUid);
        String redirectUrl = LinkUtil.createAbsoluteLink(redirectContent);
        return redirectUrl;
    }


	public static List<Content> getPageLeafs(Content mainContent) {

		List<Content> result = new ArrayList<Content>();

		for(Content childContent : mainContent.getChildren()) {

			if(childContent.hasChildren()) {
				result.addAll(getPageLeafs(childContent));
			} else {
				result.add(childContent);
			}

		}

		return result;

	}

	public static void sortContentAfterCreationDate(List<Content> list, final boolean direction) {

		Collections.sort(list, new Comparator<Content>() {
			public int compare(Content content1, Content content2) {
				int i = content1.getMetaData().getCreationDate().compareTo(content2.getMetaData().getCreationDate());
				if(direction) {
					return i;
				} else {
					return -i;
				}

			}
		});
	}


	public static Collection<Content> getSiblings(Content mainContent) throws RepositoryException {
		Content parent = mainContent.getParent();

		Collection<Content> siblings = parent.getChildren();
		List<Content> result = new ArrayList<Content>();
		for(Content c : siblings) {
			if(!c.getUUID().equals(mainContent.getUUID())) {
				result.add(c);
			}
		}
		return result;
	}


	public static Integer getLocalInteger(String name) throws RepositoryException {

		Integer result = null;

		if (RepositoryUtils.hasNodeData(RepositoryUtils.getLocalContentNode(), name)) {
			String num = RepositoryUtils.getLocalNodeData(name);
			try {
				result = Integer.parseInt(num);
			} catch (NumberFormatException nfe) {
			}
		}
		return result;

	}

	public static String getLocalNodeData(String name, String defaultValue) throws RepositoryException {
		if (RepositoryUtils.hasNodeData(RepositoryUtils.getLocalContentNode(), name)) {
			String localNodeData = RepositoryUtils.getLocalNodeData(name);
			if(localNodeData != null && localNodeData.length() > 0) {
				return localNodeData;
			}
		}
		return defaultValue;
	}
}

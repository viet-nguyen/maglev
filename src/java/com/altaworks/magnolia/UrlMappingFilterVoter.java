package com.altaworks.magnolia;

import info.magnolia.voting.voters.AbstractBoolVoter;
import org.apache.catalina.connector.RequestFacade;

/**
 * @author Åke Argéus
 */
public class UrlMappingFilterVoter extends AbstractBoolVoter {

	private final static ThreadLocal<Boolean> bypass = new ThreadLocal<Boolean>();

	protected boolean boolVote(Object o) {
		if (bypass.get() != null)
			return bypass.get();
		return false;
	}

	public static void bypass() {
		bypass.set(true);
	}
	
	public static void remove(){
		bypass.remove();
	}
}

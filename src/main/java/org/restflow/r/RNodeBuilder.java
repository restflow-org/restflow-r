package org.restflow.r;

import org.restflow.nodes.ActorNodeBuilder;
import org.restflow.nodes.ActorWorkflowNode;

public class RNodeBuilder extends ActorNodeBuilder {
	
	private RActorBuilder _rActorBuilder = new RActorBuilder();

	public RNodeBuilder step(String script) {
		_rActorBuilder.step(script);
		return this;
	}
	
	public RNodeBuilder state(String name) {
		_rActorBuilder.state(name);
		return this;
	}
	
	public ActorWorkflowNode build() throws Exception {
		_rActorBuilder.context(_context);
		_rActorBuilder.types(_types);
		_actor = _rActorBuilder.build();
		return super.build();
	}
}

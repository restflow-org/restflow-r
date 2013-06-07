package org.restflow.r;

import java.util.HashSet;
import java.util.Set;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.actors.Workflow;
import org.restflow.actors.WorkflowBuilder;
import org.restflow.data.ConsumableObjectStore;
import org.restflow.directors.MTDataDrivenDirector;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.StdoutRecorder;


public class TestRActorBuilder extends RestFlowTestCase {

	private WorkflowContext _context;
	private ConsumableObjectStore _store;
	
	public void setUp() throws Exception {
		super.setUp();
		_store = new ConsumableObjectStore();
		_context = new WorkflowContextBuilder()
			.store(_store)
			.build();
	}
	
	public void test_WorkflowBuilder_HelloWorld_OneNode_R() throws Exception {

		Workflow workflow = new WorkflowBuilder()
			.name("Hello")
			.context(_context)
			.node(new RNodeBuilder()
				.step("cat('Hello world!')"))
			.build();
		
		workflow.configure();
		workflow.initialize();
		
		workflow.run();

		assertEquals(0, _store.size());
	}
	
	public void test_WorkflowBuilder_HelloWorld_TwoNodes_R() throws Exception {

		final Workflow workflow = new WorkflowBuilder()
			
			.context(_context)
			.name("HelloWorld")
			
			.node(new RNodeBuilder()
				.name("CreateGreeting")
				.step("greeting <- 'Hello!'")
				.outflow("greeting", "/greeting"))
			
			.node(new RNodeBuilder()
				.name("PrintGreeting")
				.inflow("/greeting", "text")
				.step("cat(text)"))
			
			.build();

		workflow.configure();
		workflow.initialize();
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {workflow.run();}});
			
		// confirm expected stdout showing three values printed
		assertEquals(
			"Hello!",
			recorder.getStdoutRecording());

		assertEquals("Hello!", _store.take("/greeting"));
		assertEquals(0, _store.size());
	}
	
	public void test_WorkflowWithConcurrentActor_R() throws Exception {
		
		Workflow workflow = new WorkflowBuilder()
		
			.name("DoublerWorkflow")
			.context(_context)
			.director(new MTDataDrivenDirector())
			
			.node(new RNodeBuilder()
				.name("source")
				.type("c", "Integer")
				.type("o", "Integer")
				.sequence("c", new Object [] {
						2,
						4,
						6,
						8,
						10,
						12})
				.step("o <- c")
				.outflow("o", "/original"))
				
			.node(new RNodeBuilder()
				.name("doubler")
				.type("x", "Integer")
				.type("y", "Integer")
				.inflow("/original", "x")
				.step(	"#import time					"	+ EOL +
						"#import random					"	+ EOL +
						"#delay = random.random() 		"	+ EOL +
						"Sys.sleep((12 - x)/10.0)		"	+ EOL +			
						"cat('Finishing 3 *', x, '\\n')	"	+ EOL +
						"y <- 3 * x						")
				.outflow("y", "/tripled")
				.maxConcurrency(6)
				.ordered(false))
				
			.node(new RNodeBuilder()
				.name("printer")
				.inflow("/tripled", "value")
				.step("cat(value, '\\n')"))
		
			.build();
		
		workflow.configure();
		workflow.initialize();
		workflow.run();
		workflow.wrapup();
		workflow.dispose();
		
		assertEquals(2, _store.take("/original/1"));
		assertEquals(4, _store.take("/original/2"));
		assertEquals(6, _store.take("/original/3"));
		assertEquals(8, _store.take("/original/4"));
		assertEquals(10, _store.take("/original/5"));
		assertEquals(12, _store.take("/original/6"));
		
		Set<Integer> triples = new HashSet<Integer>();
		triples.add((Integer)_store.take("/tripled/1"));
		triples.add((Integer)_store.take("/tripled/2"));
		triples.add((Integer)_store.take("/tripled/3"));
		triples.add((Integer)_store.take("/tripled/4"));
		triples.add((Integer)_store.take("/tripled/5"));
		triples.add((Integer)_store.take("/tripled/6"));
		
//		assertEquals(0, _store.size());

		assertTrue(triples.remove(6));
		assertTrue(triples.remove(12));
		assertTrue(triples.remove(18));
		assertTrue(triples.remove(24));
		assertTrue(triples.remove(30));
		assertTrue(triples.remove(36));
		
		assertEquals(0, triples.size());
	}
}

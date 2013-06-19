package org.restflow.r;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.WorkflowRunner;
import org.restflow.actors.Workflow;
import org.restflow.actors.WorkflowBuilder;
import org.restflow.data.ConsumableObjectStore;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.StdoutRecorder;
import org.restflow.util.TestUtilities;


public class TestTableReader extends RestFlowTestCase {

	private WorkflowContext _context;
	private ConsumableObjectStore _store;
	private String _testRunsDirectoryPath;
	
	public void setUp() throws Exception {
		super.setUp();
		_store = new ConsumableObjectStore();
		_testRunsDirectoryPath = TestUtilities.getTestRunsDirectoryPath();
		_context = new WorkflowContextBuilder()
			.store(_store)
			.build();
	}
	
	public void test_TableReader_PrintFileName() throws Exception {


		String yamlString =
			"imports:                                                   " + EOL +
			"                                                           " + EOL +
			"  - classpath:/org/restflow/java/actors.yaml               " + EOL +
			"  - classpath:/org/restflow/directors.yaml                 " + EOL +
			"  - classpath:/org/restflow/r/actors/TableReader.yaml      " + EOL +
			"                                                           " + EOL +
			"components:                                                " + EOL +
			"                                                           " + EOL +
			"  - id: HelloWorld                                         " + EOL +
			"    type: Workflow                                         " + EOL +
			"    properties:                                            " + EOL +
			"      director: !ref PublishSubscribeDirector              " + EOL +
			"      nodes:                                               " + EOL +
			"        - !ref ReadGffFile             	             	" + EOL +
			"        - !ref RenderFileName                              " + EOL +
			"                                                           " + EOL +
			"  - id: ReadGffFile                                        " + EOL +
			"    type: Node                                             " + EOL +
			"    properties:                                            " + EOL +
			"      actor: !ref TableReader                              " + EOL +
			"      constants:                                           " + EOL +
			"        filePath: /home/tmcphillips/GitHub/org-restflow/restflow-r/src/test/resources/org/restflow/r/data/31311_E2F1_A.gff" + EOL +
			"      outflows:                                            " + EOL +
			"        fileName: file:/name		                        " + EOL +
			"                                                           " + EOL +
			"  - id: RenderFileName                                     " + EOL +
			"    type: Node                                             " + EOL +
			"    properties:                                            " + EOL +
			"      actor: !ref PrintStreamWriter                        " + EOL +
			"      inflows:                                             " + EOL +
			"        message: /name                                     " + EOL +
			"                                                           " + EOL 
		;
		
//		final WorkflowRunner runner = new WorkflowRunner.Builder()
//			.workflowDefinitionString(yamlString)
//			.runsDirectory(_testRunsDirectoryPath)
//			.build();
//		
//		
//		// run the workflow while capturing stdout and stderr 
//		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
//			public void execute() throws Exception {runner.run();}});
//		
//		assertEquals("", runner.getStderrRecording());
//
//		System.out.println(recorder.getStdoutRecording());
//		
//		assertEquals(
//				"31311_E2F1_A.gff" + EOL, 
//			runner.getStdoutRecording());
	}
}
package org.restflow.r;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.r.RActor;
import org.restflow.r.RActorBuilder;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.StdoutRecorder;


public class TestRActor extends RestFlowTestCase {

	private WorkflowContext _context;
	
	public void setUp() throws Exception {
		super.setUp();
		_context = new WorkflowContextBuilder()
			.build();
	}
	
	public void testGetAugmentedStepScript_NoInputsOutputsOrState() throws Exception {

		final RActor actor = new RActorBuilder()
			.context(_context)
			.name("Hello")
			.step("cat('Hello world!')")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
				
		assertEquals(
			"# AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"# BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			"" 																							+ EOL +
			"cat('Hello world!')" 																		+ EOL +
			"" 																							+ EOL +
			"# END OF ORIGINAL SCRIPT" 																	+ EOL +
			"" 																							+ EOL +
			"# signal end of output from original script" 												+ EOL +
			"cat('__END_OF_SCRIPT_OUTPUT__\\n')" 														+ EOL +
			"" 																							+ EOL +
			"# Serialization of actor outputs"															+ EOL +
			"" 																							+ EOL 
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm nothing was written to stderr
		assertEquals("", recorder.getStderrRecording());
		
		// confirm expected stdout showing three values printed
		assertEquals(
			"Hello world!",
			recorder.getStdoutRecording());
	}
		
	public void testGetAugmentedStepScript_WithInputs_NoOutputsOrState() throws Exception {

		final RActor actor = new RActorBuilder()
			.context(_context)
			.name("Hello")
			.input("greeting")
			.step("cat(greeting, 'world!')")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		actor.setInputValue("greeting", "Goodbye");
		
		assertEquals(
			"# AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"# define functions for enabling and disabling actor inputs" 								+ EOL +
			"enableInput <- function(input) { enabledInputs <<- paste(enabledInputs, input) }" 			+ EOL +
			"disableInput <- function(input) { disabledInputs <<- paste(disabledInputs, input) }" 		+ EOL +
			"" 																							+ EOL +
			"# initialize input control variables"		 												+ EOL +
			"enabledInputs   <- ''" 																	+ EOL +
			"disabledInputs  <- ''" 																	+ EOL +
			"" 																							+ EOL +
			"# initialize actor input variables"														+ EOL +
			"greeting <- 'Goodbye'"																		+ EOL +
			""																							+ EOL +
			"# BEGINNING OF ORIGINAL SCRIPT"															+ EOL +
			""																							+ EOL +
			"cat(greeting, 'world!')"																	+ EOL +
			"" 																							+ EOL +
			"# END OF ORIGINAL SCRIPT" 																	+ EOL +
			"" 																							+ EOL +
			"# signal end of output from original script" 												+ EOL +
			"cat('__END_OF_SCRIPT_OUTPUT__\\n')"														+ EOL +
			"" 																							+ EOL +
			"# Serialization of actor outputs" 															+ EOL +
			"cat('enabledInputs', ':', enabledInputs, '\\n')"											+ EOL +
			"cat('disabledInputs', ':', disabledInputs, '\\n')"		 									+ EOL +
			""																							+ EOL +
			"" 																							+ EOL 
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm nothing was written to stderr
		assertEquals("", recorder.getStderrRecording());

		// confirm expected stdout showing three values printed
		assertEquals(
			"Goodbye world!",
			recorder.getStdoutRecording());
	}

	public void testGetAugmentedStepScript_WithOutputs_NoInputsOrState() throws Exception {

		final RActor actor = new RActorBuilder()
			.context(_context)
			.name("Hello")
			.step("greeting<-'Nice to meet you.'")
			.output("greeting")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		assertEquals(
			"# AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"# define functions for enabling and disabling actor outputs" 								+ EOL +
			"enableOutput <- function(output) { enabledOutputs <<- paste(enabledOutputs, output) }"		+ EOL +
			"disableOutput <- function(output) { disabledOutputs <<- paste(disabledOutputs, output) }" 	+ EOL +
			"" 																							+ EOL +
			"# initialize output control variables" 													+ EOL +
			"enabledOutputs  <- ''" 																	+ EOL +
			"disabledOutputs <- ''" 																	+ EOL +
			"" 																							+ EOL +
			"# BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			"" 																							+ EOL +
			"greeting<-'Nice to meet you.'" 															+ EOL +
			"" 																							+ EOL +
			"# END OF ORIGINAL SCRIPT" 																	+ EOL +
			"" 																							+ EOL +
			"# signal end of output from original script" 												+ EOL +
			"cat('__END_OF_SCRIPT_OUTPUT__\\n')" 														+ EOL +
			"" 																							+ EOL +
			"# Serialization of actor outputs" 															+ EOL +
			"cat('greeting', ' : \"', greeting, '\"', '\\n', sep='')"										+ EOL +
			"" 																							+ EOL +
			"cat('enabledOutputs', ':', enabledOutputs, '\\n')" 										+ EOL +
			"cat('disabledOutputs', ':', disabledOutputs, '\\n')" 										+ EOL +
			""																							+ EOL +
			"" 																							+ EOL 
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm expected stdout showing three values printed
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals("Nice to meet you.", actor.getOutputValue("greeting"));
	}

	public void testGetAugmentedStepScript_WithState_NoInputsOrOutput() throws Exception {

		final RActor actor = new RActorBuilder()
			.context(_context)
			.name("Hello")
			.state("greeting")
			.step("greeting <- 'Nice to meet you.'")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		assertEquals(
			"# AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"# initialize actor state variables" 														+ EOL +
			"greeting <- NULL" 																			+ EOL +
			""							 																+ EOL +
			"# BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			""							 																+ EOL +
			"greeting <- 'Nice to meet you.'" 															+ EOL +
			""							 																+ EOL +
			"# END OF ORIGINAL SCRIPT" 																	+ EOL +
			""							 																+ EOL +
			"# signal end of output from original script" 												+ EOL +
			"cat('__END_OF_SCRIPT_OUTPUT__\\n')" 														+ EOL +
			""							 																+ EOL +
			"# Serialization of actor outputs" 															+ EOL +
			"cat('greeting', ' : \"', greeting, '\"', '\\n', sep='')"									+ EOL +
			"" 																							+ EOL +
			"" 																							+ EOL 
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm nothing written to stderr
		assertEquals("", recorder.getStderrRecording());

		// confirm nothing written to stdout
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals("Nice to meet you.", actor.getStateValue("greeting"));
	}
	
	public void testGetAugmentedStepScript_WithInputsAndOutput_NoState() throws Exception {

		final RActor actor = new RActorBuilder()
			.context(_context)
			.name("Multiplier")
			.input("x")
			.input("y")
			.step("z <- x * y")
			.output("z")
			.type("x", "Integer")
			.type("y", "Integer")
			.type("z", "Integer")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		actor.setInputValue("x", 3);
		actor.setInputValue("y", 12);
		
		assertEquals(
			"# AUGMENTED STEP SCRIPT FOR ACTOR Multiplier"												+ EOL +
			""																							+ EOL +
			"# define functions for enabling and disabling actor inputs" 								+ EOL +
			"enableInput <- function(input) { enabledInputs <<- paste(enabledInputs, input) }" 			+ EOL +
			"disableInput <- function(input) { disabledInputs <<- paste(disabledInputs, input) }" 		+ EOL +
			""																							+ EOL +
			"# initialize input control variables"														+ EOL +
			"enabledInputs   <- ''"																		+ EOL +
			"disabledInputs  <- ''"																		+ EOL +
			""																							+ EOL +
			"# define functions for enabling and disabling actor outputs" 								+ EOL +
			"enableOutput <- function(output) { enabledOutputs <<- paste(enabledOutputs, output) }"		+ EOL +
			"disableOutput <- function(output) { disabledOutputs <<- paste(disabledOutputs, output) }" 	+ EOL +
			""																							+ EOL +
			"# initialize output control variables"														+ EOL +
			"enabledOutputs  <- ''"																		+ EOL +
			"disabledOutputs <- ''"																		+ EOL +
			""																							+ EOL +
			"# initialize actor input variables"														+ EOL +
			"y <- 12"																					+ EOL +
			"x <- 3"																					+ EOL +
			""																							+ EOL +
			"# BEGINNING OF ORIGINAL SCRIPT"															+ EOL +
			""																							+ EOL +
			"z <- x * y"																				+ EOL +
			""																							+ EOL +
			"# END OF ORIGINAL SCRIPT"																	+ EOL +
			""																							+ EOL +
			"# signal end of output from original script"												+ EOL +
			"cat('__END_OF_SCRIPT_OUTPUT__\\n')"														+ EOL +
			""																							+ EOL +
			"# Serialization of actor outputs"															+ EOL +
			"cat('z', ':', z, '\\n')"																	+ EOL +
			""																							+ EOL +
			"cat('enabledInputs', ':', enabledInputs, '\\n')"											+ EOL +
			"cat('disabledInputs', ':', disabledInputs, '\\n')"											+ EOL +
			""																							+ EOL +
			"cat('enabledOutputs', ':', enabledOutputs, '\\n')"											+ EOL +
			"cat('disabledOutputs', ':', disabledOutputs, '\\n')" 										+ EOL +
			"" 																							+ EOL +
			"" 																							+ EOL
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm nothing written to stderr
		assertEquals("", recorder.getStderrRecording());

		// confirm nothing written to stdout
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals(36, actor.getOutputValue("z"));
	}
}

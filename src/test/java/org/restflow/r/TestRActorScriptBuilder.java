package org.restflow.r;

import org.restflow.actors.ActorScriptBuilder;
import org.restflow.test.RestFlowTestCase;


public class TestRActorScriptBuilder extends RestFlowTestCase {

	public void testAppendCode() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		assertEquals(
			"", 
			builder.toString()
			);

		builder.appendCode("Some code");
		
		assertEquals(
			"Some code" 			+ EOL, 
			builder.toString()
		);
		
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}
	
	
	public void testAppendSeparator() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendSeparator();
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"######################################################################################" + EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendBlankLine() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendBlankLine();
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			""						+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}

	public void testAppendComment() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendComment("A comment");
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"# A comment"			+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_NullType_NullValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_no_type", null, null, false, false);
		assertEquals(
			"var_with_no_type <- NULL" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_NullType_StringValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_no_type", "A string", null, false, false);
		assertEquals(
			"var_with_no_type <- 'A string'" 		+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_NullType_IntegerValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_no_type", 42, null, false, false);
		assertEquals(
			"var_with_no_type <- '42'" 				+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_NullType_BooleanValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_no_type", true, null, false, false);
		assertEquals(
			"var_with_no_type <- 'true'" 				+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_StringType_NullValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_string_type", null, "String", false, false);
		assertEquals(
			"var_with_string_type <- NULL" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_StringType_StringValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_string_type", "A string", "String", false, false);
		assertEquals(
			"var_with_string_type <- 'A string'" 	+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendLiteralAssignment_StringType_IntegerValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_string_type", 42, "String", false, false);
		assertEquals(
			"var_with_string_type <- '42'" 			+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendLiteralAssignment_StringType_BooleanValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_string_type", true, "String", false, false);
		assertEquals(
			"var_with_string_type <- 'true'" 			+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendLiteralAssignment_IntegerType_NullValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_integer_type", null, "Integer", false, false);
		assertEquals(
			"var_with_integer_type <- NULL" 			+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendLiteralAssignment_IntegerType_StringValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		Exception exception = null;
		try {
			builder.appendLiteralAssignment("var_with_integer_type", "A string", "Integer", false, false);
		} catch (Exception e) {
			exception = e;
		}
		
		assertNotNull(exception);
		assertEquals(
			"Error assigning value to R Integer variable 'var_with_integer_type': A string", 
			exception.getMessage()
		);
	}
	
	public void testAppendLiteralAssignment_IntegerType_IntegerValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_integer_type", 42, "Integer", false, false);
		assertEquals(
			"var_with_integer_type <- 42" 				+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_IntegerType_BooleanValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		Exception exception = null;
		try {
			builder.appendLiteralAssignment("var_with_integer_type", true, "Integer", false, false);
		} catch (Exception e) {
			exception = e;
		}
		
		assertNotNull(exception);
		assertEquals(
			"Error assigning value to R Integer variable 'var_with_integer_type': true", 
			exception.getMessage()
		);
	}
	
	public void testAppendLiteralAssignment_BooleanType_NullValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_boolean_type", null, "Boolean", false, false);
		assertEquals(
			"var_with_boolean_type <- NULL" 			+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendLiteralAssignment_BooleanType_StringValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		Exception exception = null;
		try {
			builder.appendLiteralAssignment("var_with_boolean_type", "A string", "Boolean", false, false);
		} catch (Exception e) {
			exception = e;
		}
		
		assertNotNull(exception);
		assertEquals(
			"Error assigning value to R Boolean variable 'var_with_boolean_type': A string", 
			exception.getMessage()
		);
	}

	public void testAppendLiteralAssignment_BooleanType_NonZeroIntegerValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_boolean_type", 42, "Boolean", false, false);
		assertEquals(
			"var_with_boolean_type <- TRUE" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_BooleanType_ZeroIntegerValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_boolean_type", 0, "Boolean", false, false);
		assertEquals(
			"var_with_boolean_type <- FALSE" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_BooleanType_TrueBooleanValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_boolean_type", true, "Boolean", false, false);
		assertEquals(
			"var_with_boolean_type <- TRUE" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendLiteralAssignment_BooleanType_FalseBooleanValue() throws Exception {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendLiteralAssignment("var_with_boolean_type", false, "Boolean", false, false);
		assertEquals(
			"var_with_boolean_type <- FALSE" 			+ EOL,
			builder.toString()
		);
	}

	public void testAppendChangeDirectory() {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendChangeDirectory("test/temp");
		assertEquals(
			"setwd('test/temp')" 				+ EOL,
			builder.toString()
		);
	}

	public void testAppendPrintStringStatement() {

		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendPrintStringStatement("string to print");
		
		assertEquals(
			"cat('string to print\\n')" 			+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendVariablePrintStatement_StringType() {
		
		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendVariableSerializationStatement("var_with_string_type", "String");
		
		assertEquals(
				"outputList <- c(outputList, list(var_with_string_type=var_with_string_type));" +EOL, 	
				builder.toString()
		);		
	}

	public void testAppendVariablePrintStatement_IntegerType() {
		
		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendVariableSerializationStatement("var_with_integer_type", "Integer");
		
		assertEquals(
			"outputList <- c(outputList, list(var_with_integer_type=var_with_integer_type));"		+ EOL,
			builder.toString()
		);		
	}

	public void testAppendVariablePrintStatement_BooleanType() {
		
		ActorScriptBuilder builder = new RActor.ScriptBuilder();
		
		builder.appendVariableSerializationStatement("var_with_boolean_type", "Boolean");
		
		assertEquals(
			"outputList <- c(outputList, list(var_with_boolean_type=var_with_boolean_type));"		+ EOL,
			builder.toString()
		);		
	}
}

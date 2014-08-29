package de.kumpelblase2.remoteentities;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JRubyScriptEngineManager
{
	private static final ScriptEngineManager s_scriptEngineManager = new ScriptEngineManager();

	public static ScriptEngine getEngine()
	{
		return s_scriptEngineManager.getEngineByName("jruby");
	}
}
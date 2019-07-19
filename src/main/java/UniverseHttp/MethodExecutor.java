package UniverseHttp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

public class MethodExecutor {

    public Map<String,ScriptExecutorInternalMethod> InternalMethods;

    public void InitInternalMethod(Map<String,ScriptExecutorInternalMethod> methods)
    {
        InternalMethods = methods;
    }

    public static ScriptEngine GetScriptExecutor(String scriptCode) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("JavaScript");
        se.eval(scriptCode);
        return se;
    }

    public interface ScriptExecutorInternalMethod<TParam>
    {
        String Invoke(TParam ... params);
    }
}

package UniverseHttp;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Pattern;

public class ParamEvaluatorMarkPattern {

    private Pattern MatchInternalMethod;
    private Pattern MatchJsFunction;
    private Pattern MatchFunctionReturn;
    private Pattern MatchParameterMark;

    public Pattern getMatchParameterMark() {
        return MatchParameterMark;
    }

    public Pattern getMatchInternalMethod() {
        return MatchInternalMethod;
    }

    public Pattern getMatchJsFunction() {
        return MatchJsFunction;
    }

    public Pattern getMatchFunctionReturn() {
        return MatchFunctionReturn;
    }

    public ParamEvaluatorMarkPattern(JSONObject regexObj)
    {
        JSONObject androidRegex = regexObj.getJSONObject("RecognitionRegex").getJSONObject("Android");
        MatchInternalMethod = Pattern.compile(androidRegex.getString("InternalFunction"));
        MatchJsFunction = Pattern.compile(androidRegex.getString("JSFunction"));
        MatchFunctionReturn = Pattern.compile(androidRegex.getString("FunctionReturn"));
        MatchParameterMark = Pattern.compile(androidRegex.getString("ParamMark"));
    }
}

package UniverseHttp;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.Map;
import java.util.regex.Matcher;

public class UniverseHttpRequestMaker {

    private String Url;
    private String Method;
    private Headers RequestHeader;
    private Map<String,String> QueryString;
    private RequestBody ReqBody;

    private ParamEvaluatorMarkPattern markPattern;
    private MethodExecutor executor;

    public ParamEvaluatorMarkPattern getMarkPattern() {
        return markPattern;
    }

    public void setMarkPattern(ParamEvaluatorMarkPattern markPattern) {
        this.markPattern = markPattern;
    }

    public MethodExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(MethodExecutor executor) {
        this.executor = executor;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public Headers getRequestHeader() {
        return RequestHeader;
    }

    public void setRequestHeader(Headers requestHeader) {
        RequestHeader = requestHeader;
    }

    public Map<String, String> getQueryString() {
        return QueryString;
    }

    public void setQueryString(Map<String, String> queryString) {
        QueryString = queryString;
    }

    public RequestBody getReqBody() {
        return ReqBody;
    }

    public void setReqBody(RequestBody reqBody) {
        ReqBody = reqBody;
    }


    public Request GetRequest(String[] QueryStringParam,String[] BodyParam)
    {
        Request.Builder req = new Request.Builder().headers(RequestHeader);

        if(!QueryString.isEmpty())
        {
            StringBuilder urlQs = new StringBuilder(Url + "?");
            for (Map.Entry<String, String> qs : QueryString.entrySet()) {
                String key = qs.getKey();
                String val = EvaluateParams(qs.getValue(),QueryStringParam);
                urlQs.append(key).append("=").append(val).append("&");
            }
            urlQs.deleteCharAt(urlQs.length()-1);
            req = req.url(urlQs.toString());
        }
        else
        {
            req = req.url(Url);
        }

        switch (Method){
            case "GET":{
                 req = req.get();
                break;
            }
            case "POST":{
                req = req.post(ReqBody);
                break;
            }
        }
        return req.build();
    }
    private String EvaluateParams(String CodeSnippet,String... params)
    {
        Matcher matchInternal = markPattern.getMatchInternalMethod().matcher(CodeSnippet);

        Matcher matchJsFunc = markPattern.getMatchJsFunction().matcher(CodeSnippet);

        if(matchInternal.find())
        {
            String evaluateResult = EvaluateParams(matchInternal.group(0));
            String MethodName = matchInternal.group(1);

            MethodExecutor.ScriptExecutorInternalMethod<String> me = executor.InternalMethods.get(MethodName);
            return me.Invoke(evaluateResult);
        }
        else if(matchJsFunc.find())
        {
            String innerJsCode = matchJsFunc.group(0);
            String evaluateResult = EvaluateParams(innerJsCode,params);

            String evaluatedCode = matchJsFunc.replaceFirst(evaluateResult);
            String CodeWithParam = PlaceParams(evaluatedCode,params);
            try {
                Invocable invo = (Invocable) MethodExecutor.GetScriptExecutor(CodeWithParam);
                String returned = (String) invo.invokeFunction("executor");
                return returned;
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return PlaceParams(CodeSnippet,params);
    }

    private String PlaceParams(String code,String[] params)
    {
        Matcher match = markPattern.getMatchParameterMark().matcher(code);
        String replaced = code;
        while (match.find()) {
            replaced = replaced.replace(match.group(),params[Integer.parseInt(match.group(1))]);
        }
        return replaced;
    }
}

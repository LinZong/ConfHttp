package UniverseHttp;

import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Map;

public class UniverseRequestRule {


    private HashMap<String,UniverseHttpRequestMaker> rules = new HashMap<>();

    public void Init(JSONObject RolesObject,JSONObject MarkPattern)
    {

        ParamEvaluatorMarkPattern pattern = new ParamEvaluatorMarkPattern(MarkPattern);


        for (Map.Entry<String, Object> api : RolesObject.entrySet()) {
            String apiName = api.getKey();
            JSONObject apiFunc = (JSONObject) api.getValue();

            UniverseHttpRequestMaker maker = new UniverseHttpRequestMaker();

            maker.setMarkPattern(pattern);

            maker.setUrl(apiFunc.getString("Url"));
            maker.setMethod(apiFunc.getString("Method"));

            JSONObject headerObj = apiFunc.getJSONObject("Header");
            HashMap<String,String> headerMap = new HashMap<>();
            for (Map.Entry<String, Object> header : headerObj.entrySet()) {
                headerMap.putIfAbsent(header.getKey(), (String) header.getValue());
            }

            maker.setRequestHeader(Headers.of(headerMap));


            JSONObject qsObj = apiFunc.getJSONObject("QueryString");
            HashMap<String,String> qsMap = new HashMap<>();
            for (Map.Entry<String, Object> qs : qsObj.entrySet()) {
                qsMap.putIfAbsent(qs.getKey(), qs.getValue().toString());
            }
            maker.setQueryString(qsMap);

            JSONObject bodyObj = apiFunc.getJSONObject("Body");
            MediaType mediaType = MediaType.parse(bodyObj.getString("Type"));

            RequestBody reqBody = null;

            switch (mediaType.toString()){
                case "application/json":{
                    reqBody = FormBody.create(mediaType,bodyObj.getJSONObject("Data").toJSONString());
                    break;
                }
                case "form-data":{
                    FormBody.Builder builder = new FormBody.Builder();
                    JSONObject dataObj = bodyObj.getJSONObject("Data");
                    for (Map.Entry<String, Object> data : dataObj.entrySet()) {
                        builder.add(data.getKey(),(String) data.getValue());
                    }
                    reqBody = builder.build();
                    break;
                }
            }

            maker.setReqBody(reqBody);

            rules.put(apiName,maker);
        }
    }

    public UniverseHttpRequestMaker GetRequestMaker(String RuleName)
    {
        return rules.get(RuleName);
    }
}

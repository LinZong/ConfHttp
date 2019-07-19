package UniverseHttp;

import UniverseHttp.Methods.MD5;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UniverseHttpRequest {

    public static void main(String[] args) throws IOException {
        // 创建请求规则文件Handle
        File rules = new File("roles.json");
        File patts = new File("pattern.json");

        Rules ruleInstance = new Rules();

        // 解析请求规则
        ruleInstance.ParseRules(rules);
        ruleInstance.ParsePatterns(patts);

        // 内置函数执行器注入
        MethodExecutor executor = new MethodExecutor();
        HashMap<String, MethodExecutor.ScriptExecutorInternalMethod> methodHashMap = new HashMap<>();
        methodHashMap.put("MD5",new MD5());
        executor.InitInternalMethod(methodHashMap);


        // 获取通用请求规则Instance。

        UniverseRequestRule KugouRule = ruleInstance.GetRequestRule("Kugou");
        UniverseHttpRequestMaker maker = KugouRule.GetRequestMaker("Search");

        // 设置内置函数执行器
        maker.setExecutor(executor);

        // 从规则文件生成Okhttp3的Request对象。只要代码入参一定即可。
        Request req = maker.GetRequest(new String[]{"勇气"},null);

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(req).execute();
        if(response.isSuccessful()){
            System.out.println(response.body().string());
        }

        // 同理。

        UniverseHttpRequestMaker urlMaker = KugouRule.GetRequestMaker("GetSongUrl");
        urlMaker.setExecutor(executor);
        Request UrlReq = urlMaker.GetRequest(new String[]{"134683878C7945D01D44E9B5CF0FDF1F"},null);

        Response urlResp = client.newCall(UrlReq).execute();
        if(urlResp.isSuccessful())
        {
            System.out.println(urlResp.body().string());
        }
    }
}

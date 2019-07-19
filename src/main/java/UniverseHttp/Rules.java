package UniverseHttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class Rules {

    private JSONObject RulesCollection;
    private JSONObject PatternCollection;
    public void ParseRules(String Json)
    {
        RulesCollection = JSON.parseObject(Json);
    }

    public void ParsePatterns(String Json)
    {
        PatternCollection = JSON.parseObject(Json);
    }

    public void ParseRules(InputStream is) throws IOException {

        RulesCollection = JSON.parseObject(ReadJsonString(is));
    }

    public void ParsePatterns(InputStream is) throws IOException {

        PatternCollection = JSON.parseObject(ReadJsonString(is));
    }

    public void ParseRules(File is) throws IOException {

        RulesCollection = JSON.parseObject(ReadJsonString(new FileInputStream(is)));
    }

    public void ParsePatterns(File is) throws IOException {

        PatternCollection = JSON.parseObject(ReadJsonString(new FileInputStream(is)));
    }

    public String ReadJsonString(InputStream is) throws IOException {
        StringBuilder roles = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                roles.append(line).append("\n");
            }
        }
        return roles.toString();
    }


    public JSONObject GetRolesCollection() {
        return RulesCollection;
    }

    public UniverseRequestRule GetRequestRule(String platformName)
    {
        UniverseRequestRule rule = new UniverseRequestRule();
        rule.Init(RulesCollection.getJSONObject(platformName),PatternCollection);
        return rule;
    }
}

package jp.kyuuki.rensou.android.net;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.kyuuki.rensou.android.model.Rensou;

/**
 * 連想 API。
 * 
 * - API 仕様にかかわる部分をここに集約。
 * - 通信ライブラリには依存したくない。
 */
public class RensouApi {
    // https://www.facebook.com/groups/428772910554185/permalink/430164457081697/
    public static String BASE_URL = "http://api.u1fukui.com/rensou";
    
    public static String getGetUrlLast() {
        return BASE_URL + "/rensou.json";
    }

    public static String getPostUrl() {
        return BASE_URL + "/rensou.json";
    }
    
    // http://www.adakoda.com/adakoda/2010/02/android-iso-8601-parse.html
    static FastDateFormat fastDateFormat1 = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;  // yyyy-MM-dd'T'HH:mm:ssZZ
    
    // 2010-02-27T13:00:00Z がパースできない。 2010-02-27T13:00:00+00:00 と同義っぽいんだけど。
    // http://stackoverflow.com/questions/424522/how-can-i-recognize-the-zulu-time-zone-in-java-dateutils-parsedate
    static FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'");
    //static FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
    static String patterns[] = { fastDateFormat1.getPattern(),  fastDateFormat2.getPattern() };

    //
    // API JSON 仕様
    //
    public static JSONObject makeRensouJson(long themeId, String keyword) {
        JSONObject json = new JSONObject();
        try {
            json.put("theme_id", themeId);
            json.put("keyword", keyword);
        } catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
        
        return json;
    }
    
    public static ArrayList<Rensou> json2Rensous(JSONArray a) {
        ArrayList<Rensou> list = new ArrayList<Rensou>();
        for (int i = 0, len = a.length(); i < len; i++) {
            try {
                JSONObject o = a.getJSONObject(i);
                
                Rensou r = RensouApi.json2Rensou(o);
                list.add(r);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        
        return list;
    }

    public static Rensou json2Rensou(JSONObject o) {
        Rensou rensou = new Rensou();
        try {
            rensou.setId(o.getLong("id"));
            rensou.setKeyword(o.getString("keyword"));
            Date d = parseDate(o.getString("created_at"));
            System.err.println(d);
            rensou.setCreatedAt(d);
        } catch (JSONException e) {
            // TODO: JSON 構文解析エラー処理
            e.printStackTrace();
            return null;
        }
        
        return rensou;
    }
    
    // API 仕様変更されてもいいように、それなりの値を返してしまう。ただ、エラーはどこかで検知したい。
    public static Date parseDate(String s) {
        Date d;
        
        try {
            d = DateUtils.parseDate(s, patterns);
        } catch (DateParseException e) {
            e.printStackTrace();
            d = null;
        }
        return d;
    }
}

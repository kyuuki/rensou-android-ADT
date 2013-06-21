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
    // https://www.facebook.com/groups/428772910554185/permalink/428791710552305/
    public static String BASE_URL = "http://u1fukui.com:4567";
    
    public static String getGetUrlLast() {
        return BASE_URL + "/rensous.json";
    }

    public static String getPostUrl() {
        return BASE_URL + "/rensou.json";
    }
    
    // http://www.adakoda.com/adakoda/2010/02/android-iso-8601-parse.html
    static FastDateFormat fastDateFormat = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
    //static FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
    static String patterns[] = { fastDateFormat.getPattern() };

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
            Date d = DateUtils.parseDate(o.getString("created_at"), patterns);
            System.err.println(d);
            rensou.setCreatedAt(d);
        } catch (JSONException e) {
            // TODO: JSON 構文解析エラー処理
            e.printStackTrace();
            return null;
        } catch (DateParseException e) {
            e.printStackTrace();
            return null;
        }

        return rensou;
    }
}

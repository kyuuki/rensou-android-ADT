package jp.kyuuki.rensou.android.net;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import jp.kyuuki.rensou.android.model.Rensou;

/**
 * 連想 API。
 * 
 * - API 仕様にかかわる部分をここに集約。
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

    // API の JSON 仕様に依存。
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

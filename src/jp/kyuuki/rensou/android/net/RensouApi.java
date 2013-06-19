package jp.kyuuki.rensou.android.net;

import org.json.JSONException;
import org.json.JSONObject;

import jp.kyuuki.rensou.android.model.Rensou;

/**
 * �A�z API�B
 * 
 * - API �d�l�ɂ�����镔���������ɏW��B
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
    
    // API �� JSON �d�l�Ɉˑ��B
    public static Rensou json2Rensou(JSONObject o) {
        Rensou rensou = new Rensou();
        try {
            rensou.setId(o.getLong("id"));
            rensou.setKeyword(o.getString("keyword"));
        } catch (JSONException e) {
            // TODO: JSON �\����̓G���[����
            e.printStackTrace();
            return null;
        }

        return rensou;
    }
}

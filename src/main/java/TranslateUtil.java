import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

public class TranslateUtil {
    private static final String from = "zh"; // 原始语言
    private static final String to = "en";   // 转换后的语言

    static String fetchTranslateResult(String query) {
        String appId = PropertiesComponent.getInstance().getValue("baidu_app_id", "");
        String appKey = PropertiesComponent.getInstance().getValue("baidu_key", "");
        if (appId.isEmpty() || appKey.isEmpty()) {
            Messages.showInfoMessage("百度翻译API的appId和key不能为空，请先到Settings页完善", "警告");
            return null;
        }
        // 转换后的结果
        String translation = null;
        try {
            // 构造salt参数
            String salt = String.valueOf(System.currentTimeMillis());
            // 构造Sign参数
            String sign = generateMD5(appId + query + salt + appKey);
            // 拼接请求URL
            String url = "http://api.fanyi.baidu.com/api/trans/vip/translate" +
                    "?q=" + URLEncoder.encode(query, "UTF-8") +
                    "&from=" + from +
                    "&to=" + to +
                    "&appid=" + appId +
                    "&salt=" + salt +
                    "&sign=" + sign;
            // 响应数据
            String responseText = "";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8");
                responseText = scanner.useDelimiter("\\A").next();
                scanner.close();
            }
            connection.disconnect();

            if (responseText.contains("\"error_code\":")) {
                translation = responseText;
            } else {
                // 解析翻译结果
                int startIndex = responseText.indexOf("\"dst\":\"") + 7;
                int endIndex = responseText.indexOf("\"", startIndex);
                translation = responseText.substring(startIndex, endIndex);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translation;
    }

    // 生成MD5
    public static String generateMD5(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            Formatter formatter = new Formatter();
            for (byte b : hashBytes) {
                formatter.format("%02x", b);
            }
            String hash = formatter.toString();
            formatter.close();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

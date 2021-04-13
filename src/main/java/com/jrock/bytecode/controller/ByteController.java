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
import java.util.zip.InflaterInputStream;

@Slf4j
@RestController
public class ByteController {

    @Autowired
    TestMapper testMapper;

    @PostMapping("/byte")
    public void byteTest(@RequestBody String data) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        OutputStream out = new DeflaterOutputStream(baos);

        out.write(data.getBytes(StandardCharsets.UTF_8));
        out.close();

        byte[] bytes = baos.toByteArray();
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        String tarSb = sb.toString();
        String toStringByte = bytes.toString();
        testMapper.insertData(tarSb);
    }

    @GetMapping("/byte")
    public String getByte() throws IOException {

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

    @PostMapping("/byte2")
    public void byteTest2(@RequestBody String data) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        OutputStream out = new DeflaterOutputStream(baos);

        out.write(data.getBytes(StandardCharsets.UTF_8));
        out.close();

        byte[] bytes = baos.toByteArray();
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        String tarSb = sb.toString();
        String toStringByte = bytes.toString();
        testMapper.insertData(tarSb);
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

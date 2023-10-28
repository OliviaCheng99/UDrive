package com.capstone.udrive.utils;

import com.capstone.udrive.exception.BusinessException;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Log4j2
public class ProcessUtils {

    public static String executeCommand(String cmd, Boolean outprintLog) throws BusinessException {
        if (StringTools.isEmpty(cmd)) {
            log.error("executeCom error");
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            errorStream.start();
            inputStream.start();
            process.waitFor();
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer + "\n").toString();

            if (outprintLog) {
                log.info("cmd: {}，result:{}", cmd, result);
            } else {
                log.info("cmd :{} done", cmd);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("conversion fail");
        } finally {
            if (null != process) {
                ProcessKiller ffmpegKiller = new ProcessKiller(process);
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }

    private static class ProcessKiller extends Thread {
        private final Process process;

        public ProcessKiller(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            this.process.destroy();
        }
    }


    static class PrintStream extends Thread {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        public PrintStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                if (null == inputStream) {
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (null != bufferedReader) {
                        bufferedReader.close();
                    }
                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}

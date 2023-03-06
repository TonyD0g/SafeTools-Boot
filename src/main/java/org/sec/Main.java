package org.sec;

import org.apache.log4j.Logger;
import org.sec.input.Logo;
import org.sec.start.Application;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        // 输出logo
        Logo.PrintLogo();
        logger.info("start xxx application");
        logger.info("please wait 30 second...");

        // 运行主逻辑
        Application.start(args);
    }
}
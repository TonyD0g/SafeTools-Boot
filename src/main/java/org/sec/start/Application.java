package org.sec.start;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import org.sec.input.Command;
import java.io.IOException;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class);

    public static void start(String[] args) {
        try {
            // 处理用户输入的参数，如 -h 等
            Command command = new Command();
            JCommander jc = JCommander.newBuilder().addObject(command).build();
            jc.parse(args);

            if (command.help) {
                jc.usage();
                return;
            }
            if (command.output == null || command.output.equals("")) {
                command.output = "result.jsp";
            }

            logger.info("use reflection module");
            doSimple(command);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void doSimple(Command command) throws IOException {

    }

}

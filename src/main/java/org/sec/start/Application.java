package org.sec.start;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import org.sec.input.Command;
import java.io.IOException;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class);

    public static void start(String[] args) {
        try {
            Command command = new Command();
            JCommander jc = JCommander.newBuilder().addObject(command).build();
            jc.parse(args);

            if(CommandChoice.CommandChoice(command,jc)){
                return;
            }

            doSimple(command);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void doSimple(Command command) throws IOException {

    }

}
/** [+] 根据命令自定义选项*/
class CommandChoice{
    public static boolean CommandChoice(Command command, JCommander jc) {
        if (command.help) {
            jc.usage();
            return false;
        }
        if (command.output == null || command.output.equals("")) {
            command.output = "result.jsp";
            return true;
        }
        return false;
    }
}
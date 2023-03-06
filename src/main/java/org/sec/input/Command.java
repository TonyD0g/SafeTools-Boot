package org.sec.input;

import com.beust.jcommander.Parameter;
/** 用户可输入的各种参数 */
public class Command {
    @Parameter(names = {"-h", "--help"}, description = "Help Info", help = true)
    public boolean help;

    @Parameter(names = {"-o", "--output"}, description = "Output File")
    public String output;
}

package ru.skypro.homework.utils;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;


public class LogShifter {
    static LogShifter shifter;
    private final String RIGHT_SHIFT = "==>";
    private final String LEFT_SHIFT = "<==";
    private final Character SPACE_RIGHT = ' ';
    private final Integer COUNT_SHIFTS = 3;

    private Integer depthLog = 0;

    private LogShifter() {

    }

    private synchronized void increasedDepthLog() {
        depthLog++;
    }
    private synchronized void decreaseDepthLog() {
        depthLog--;
    }


    public static LogShifter getLogShifter() {
        if (shifter == null) {
            shifter = new LogShifter();
        }
        return shifter;
    }

    private StringBuilder getShiftLogText() {
        increasedDepthLog();

        StringBuilder builder = new StringBuilder();
        appendSpaces(builder, depthLog);
        builder.append(RIGHT_SHIFT);
        return builder;
    }
    private StringBuilder getShiftBackLogText() {
        StringBuilder builder = new StringBuilder();
        appendSpaces(builder,depthLog);
        builder.append(LEFT_SHIFT);

        decreaseDepthLog();
        return builder;
    }

    private StringBuilder getLogText() {
        StringBuilder builder = new StringBuilder();
        return appendSpaces(builder, depthLog + 1);
    }

    private StringBuilder appendSpaces(StringBuilder builder, Integer depth) {
        Integer length = COUNT_SHIFTS * depth;
        for (Integer i = 0; i < length; i++) {
            builder.append(SPACE_RIGHT);
        }
        return builder;
    }

    public void shiftLog(Logger logger, String var1) {
        logger.info(getShiftLogText() + var1);
    }

    public void shiftLog(Logger logger, String var1, Object var2) {
        logger.info(getShiftLogText() + var1, var2);
    }

    public void shiftLog(Logger logger, String var1, Object var2, Object var3) {
        logger.info(getShiftLogText() + var1, var2, var3);
    }
    public void shiftLog(Logger logger, String var1, Object... var2) {
        logger.info(getShiftLogText() + var1, var2);
    }



    public void shiftBackLog(Logger logger, String var1) {
        logger.info(getShiftBackLogText() + var1);
    }

    public void shiftBackLog(Logger logger, String var1, Object var2) {
        logger.info(getShiftBackLogText() + var1, var2);
    }

    public void shiftBackLog(Logger logger, String var1, Object var2, Object var3) {
        logger.info(getShiftBackLogText() + var1, var2, var3);
    }
    public void shiftBackLog(Logger logger, String var1, Object... var2) {
        logger.info(getShiftBackLogText() + var1, var2);
    }


    public void log(Logger logger, String var1) {
        logger.info(getLogText() + var1);
    }

    public void log(Logger logger, String var1, Object var2) {
        logger.info(getLogText() + var1, var2);
    }

    public void log(Logger logger, String var1, Object var2, Object var3) {
        logger.info(getLogText() + var1, var2, var3);
    }
    public void log(Logger logger, String var1, Object... var2) {
        logger.info(getLogText() + var1, var2);
    }
}

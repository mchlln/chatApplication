package cuni.mff.chollonm.utils;

/**
 * Enum representing ANSI escape codes for text colors.
 */
public enum Colors {
    /**
     * The ANSI escape code to reset the text color.
     * */
    RESET("\033[0m"),

    /**
     * The ANSI escape code for black text color.
     * */
    BLACK("\033[0;30m"),

    /**
     * The ANSI escape code for red text color.
     * */
    RED("\033[0;31m"),

    /**
     * The ANSI escape code for green text color.
     * */
    GREEN("\033[0;32m"),

    /**
     * The ANSI escape code for yellow text color.
     * */
    YELLOW("\033[0;33m"),

    /**
     * The ANSI escape code for blue text color.
     * */
    BLUE("\033[0;34m"),

    /**
     * The ANSI escape code for purple text color.
     * */
    PURPLE("\033[0;35m"),

    /**
     * The ANSI escape code for cyan text color.
     * */
    CYAN("\033[0;36m"),

    /**
     * The ANSI escape code for white text color.
     * */
    WHITE("\033[0;37m");

    private final String code;


    /**
     * Constructs a Colors enum with the specified ANSI escape code.
     *
     * @param code the ANSI escape code representing the color
     */
    Colors(String code) {
        this.code=code;
    }

    /**
     * Returns the ANSI escape code associated with the color.
     *
     * @return the ANSI escape code
     */
    public String getCode() {
        return code;
    }
}

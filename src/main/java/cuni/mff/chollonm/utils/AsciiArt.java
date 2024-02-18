package cuni.mff.chollonm.utils;

/**
 * Enum representing ASCII Art to send reactions in the chat.
 */
public enum AsciiArt {

    /**
     * Represents a thumbs-up ASCII art.
     */
    THUMBS_UP("""
                _
               /(|
              (  :
             __\\  \\  _____
            (____)  `|
            (____)|   |
             (____).__|
              (___)__.|_____
            """
    ),
    /**
     * Represents a Snoopy ASCII art.
     */
    SNOOPY("""
              \n
              ,-~~-.___.
             / |  '     \\
            (  )         0             \s
             \\_/-, ,----'           \s
                ====           //                    \s
               /  \\-'~;    /~~~(O)
              /  __/~|   /       |    \s
            =(  _____| (_________|
            """),

    /**
     * Represents a hearts ASCII art.
     */
    HEARTS("""
                \n
                ,-"-,-"-.
               (         )
                ".     ."
                  "._.      _  _
                           ( `' )
                            `.,'
            ,-.-.
            `. ,'
              `
            """),

    /**
     * Represents a smile smiley in ASCII art.
     */
    SMILE("""
                 ..:=*#*=-..      \s
              ..=@#-.   .:#@+..   \s
             .=%=.         .=%=.  \s
            .=#. .:=:. .:=:. .#+. \s
            :#-. .=@=. .=@+. .:#- \s
            -#:               :#= \s
            :#-. :.       .:..:#- \s
            .+#. -#*:...:+#-..#+. \s
             .=%=..:=+++=:..=%=.  \s
              ..+@#-.   .:*@+..   \s
                 ..-+###+-..  \n"""),


    /**
     * Represents a sad smiley in ASCII art.
     */
    SAD("""
                    ..:---:..       \s
                 .=%@%*+=+*%@%=.    \s
              ..#@#.         .#@#.. \s
             .-%#. ...     ... .#%-.\s
            .:%*.  :@%-   :%@-. .*%:.
            .+%-   .::.   ..:.   -%+.
            .*#:                 .#*.
            .+%-   .=%@%%%@%=..  -%+.
            .:%*.  #*:.   .:*#. .+%:.
             .-%#.             .#%-.\s
              ..#@#.         .#@#.. \s
                 .=@@%*===*%@@=.    \s
                    ..:---:..       \n""")
    ;


    private final String image;

    /**
     * Constructs a AsciiArt enum with the specified ASCII art image.
     *
     * @param image the ASCII art corresponding to the name
     */
    AsciiArt(String image) {
        this.image=image;
    }

    /**
     * Returns the ASCII art associated with the name.
     *
     * @return the ASCII art
     */
    public String getCode() {
        return image;
    }
}

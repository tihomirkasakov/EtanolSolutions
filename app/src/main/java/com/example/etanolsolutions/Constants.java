package com.example.etanolsolutions;

public final class Constants {
    public static final int REQUEST_CODE_CORRECTION_ACTIVITY_FOR_SOURCE_DEGREE = 1;
    public static final int REQUEST_CODE_CORRECTION_ACTIVITY_FOR_DILUENT_DEGREE = 2;
    public static final String EXTRA_RESULT = "result";
    public static final String EXTRA_INPUT_VALUE = "inputValue";
    public static final String EXTRA_RETURN_TO_CALLER = "returnToCaller";
    //
    // PRIVATE //

    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}

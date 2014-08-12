package com.derbysoft.bookit.common.translator;

public interface Translator<FROM, PARAM, TO> {

    TO translate(FROM from, PARAM param);

}

package com.y.common.codec;

public interface CommandEncoder<P> {


    byte[] encoder(P t);

}

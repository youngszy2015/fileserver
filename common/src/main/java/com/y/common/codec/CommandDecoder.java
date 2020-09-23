package com.y.common.codec;

public interface CommandDecoder<T> {


    T decoder(byte[] content);
}

package ink.codflow.sync.core;

import java.io.InputStream;
import java.io.OutputStream;

import ink.codflow.sync.exception.FileException;

public interface StreamObject {

    InputStream fileInputStream() throws FileException;
   // OutputStream fileOutputStream() throws FileException;

    void copyContentFrom(InputStream in) throws FileException;
}
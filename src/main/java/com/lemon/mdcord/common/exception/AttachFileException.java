package com.lemon.mdcord.common.exception;

import java.nio.file.Path;

public class AttachFileException extends RuntimeException {

    private static final long serialVersionUID = -4365526348381709616L;

    public AttachFileException(String msg, Path targetDir) {
        super(targetDir + " " + msg);
    }

}

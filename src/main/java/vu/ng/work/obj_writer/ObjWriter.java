package vu.ng.work.obj_writer;

import vu.ng.work.model.Model;

import java.io.IOException;

public interface ObjWriter {
    void write(Model model, String fileName) throws IOException;
}

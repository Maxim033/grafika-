package vu.ng.work;

import vu.ng.work.model.Model;
import vu.ng.work.model.polygon_remover.PolygonRemover;
import vu.ng.work.objReader.ObjReader;
import vu.ng.work.obj_writer.ObjWriterClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        Path fileName = Path.of("C:\\Repositories\\Polygon_remover\\src\\caracal_cube.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);
        //before
        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Polygons: " + model.polygons.size());

        PolygonRemover.removePolygons(model, List.of(0,1,2,3,4,5));
        ObjWriterClass objwriter = new ObjWriterClass();
        objwriter.write(model,"caracal_2.obj");
        //after
        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Polygons: " + model.polygons.size());
    }
}

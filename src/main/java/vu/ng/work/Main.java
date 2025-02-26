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
        Path fileName = Path.of("C:\\Users\\Максим\\Downloads\\task3\\task3\\src\\caracal_cube.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Загрузка модели ...");
        Model model = ObjReader.read(fileContent);
        //before
        System.out.println("Вершины: " + model.vertices.size());
        System.out.println("Полигоны: " + model.polygons.size());

        PolygonRemover.removePolygons(model, List.of(2,3,4));
        ObjWriterClass objwriter = new ObjWriterClass();
        objwriter.write(model,"C:\\Users\\Максим\\Downloads\\task3\\task3\\caracal_2.obj");
        System.out.println("Вершины: " + model.vertices.size());
        System.out.println("Полигоны: " + model.polygons.size());
    }
}

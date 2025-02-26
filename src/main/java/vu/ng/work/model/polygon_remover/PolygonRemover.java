package vu.ng.work.model.polygon_remover;

import vu.ng.work.model.Model;
import vu.ng.work.model.Polygon;

import java.util.*;

public class PolygonRemover {
    public static void removePolygons(Model model, List<Integer> polygonIndices) {
        if (model == null || polygonIndices == null || polygonIndices.isEmpty()) {
            return;
        }

        // Проверка на корректность индексов полигонов
        for (Integer index : polygonIndices) {
            if (index < 0 || index >= model.polygons.size()) {
                throw new IllegalArgumentException("Polygon index " + index + " does not exist");
            }
        }

        // Сбор вершин, которые могут стать неиспользуемыми
        Set<Integer> potentialUnusedVertices = new HashSet<>();

        // Удаление полигонов и сбор связанных вершин
        for (int i = polygonIndices.size() - 1; i >= 0; i--) {
            int index = polygonIndices.get(i);
            Polygon polygon = model.polygons.get(index);

            potentialUnusedVertices.addAll(polygon.getVertexIndices());
            model.polygons.remove(index);
        }

        // Удаление вершин, которые больше не используются
        Iterator<Integer> iterator = potentialUnusedVertices.iterator();
        while (iterator.hasNext()) {
            Integer vertexIndex = iterator.next();

            boolean isUsed = model.polygons.stream().anyMatch(poly -> poly.getVertexIndices().contains(vertexIndex));
            if (isUsed) {
                iterator.remove();
            }
        }

        // Сортировка и удаление неиспользуемых вершин
        List<Integer> unusedVertices = new ArrayList<>(potentialUnusedVertices);
        unusedVertices.sort(Collections.reverseOrder());

        for (Integer index : unusedVertices) {
            model.vertices.remove((int) index);
        }

        // Корректировка индексов вершин в оставшихся полигонах
        adjustVertexIndicesInPolygons(model, unusedVertices);
    }

    private static void adjustVertexIndicesInPolygons(Model model, List<Integer> removedVertices) {
        if (removedVertices.isEmpty()) return;

        for (Polygon polygon : model.polygons) {
            List<Integer> indices = polygon.getVertexIndices();

            for (int i = 0; i < indices.size(); i++) {
                int currentIndex = indices.get(i);
                int adjustment = (int) removedVertices.stream().filter(removed -> removed < currentIndex).count();

                indices.set(i, currentIndex - adjustment);
            }
        }
    }
}
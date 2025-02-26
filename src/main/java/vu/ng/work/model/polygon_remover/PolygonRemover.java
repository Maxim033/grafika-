package vu.ng.work.model.polygon_remover;

import vu.ng.work.model.Model;
import vu.ng.work.model.Polygon;

import java.util.*;

public class PolygonRemover {
    public static void removePolygons(Model model, List<Integer> polygonIndices) {
        if (model == null || polygonIndices == null || polygonIndices.isEmpty()) {
            return;
        }
        // valid polygon index or not
        for (Integer index : polygonIndices) {
            if (index < 0 || index >= model.polygons.size()) {
                throw new PolygonException("Polygon index " + index + " does not exist");
            }
        }
        // vertex liên quan tới polygon cbi được delete
        Set<Integer> potentialUnusedVertices = new HashSet<>();

        // Xóa polygon ngược và thu thập vertex
        for (int i = polygonIndices.size() - 1; i >= 0; i--) {
            int index = polygonIndices.get(i);
            Polygon polygon = model.polygons.get(index);

            // collect vertex của cả polygon sắp bị xóa
            potentialUnusedVertices.addAll(polygon.getVertexIndices());

            // Xóa polygon
            model.polygons.remove(index);
        }

         //Lọc vertex không sử dụng
        Iterator<Integer> iterator = potentialUnusedVertices.iterator();
        while (iterator.hasNext()) {
            Integer vertexIndex = iterator.next();

            // Kiểm tra vertex có tồn tại trong bất kỳ polygon nào không
            boolean isUsed = model.polygons.stream().anyMatch(poly -> poly.getVertexIndices().contains(vertexIndex));

            if (isUsed) {iterator.remove();}
        }

        // delete unsused vertex
        List<Integer> unusedVertices = new ArrayList<>(potentialUnusedVertices);
        Collections.sort(unusedVertices, Collections.reverseOrder());

        for (Integer index : unusedVertices) {
            model.vertices.remove((int)index);
        }

        // adjust index
        adjustVertexIndicesInPolygons(model, unusedVertices);
    }

    private static void adjustVertexIndicesInPolygons(Model model, List<Integer> removedVertices) {
        if (removedVertices.isEmpty()) return;

        for (Polygon polygon : model.polygons) {
            List<Integer> indices = polygon.getVertexIndices();

            // Điều chỉnh index
            for (int i = 0; i < indices.size(); i++) {
                int currentIndex = indices.get(i);
                int adjustment = (int) removedVertices.stream().filter(removed -> removed < currentIndex).count();

                indices.set(i, currentIndex - adjustment);
            }
        }
    }
}

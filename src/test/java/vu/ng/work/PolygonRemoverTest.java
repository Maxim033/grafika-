package vu.ng.work;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vu.ng.work.math.Vector3f;
import vu.ng.work.model.Model;
import vu.ng.work.model.Polygon;
import vu.ng.work.model.polygon_remover.PolygonException;
import vu.ng.work.model.polygon_remover.PolygonRemover;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolygonRemoverTest {
    private static Model model;

    @BeforeEach
    void setUp() {
        model = new Model();

        model.vertices.add(new Vector3f(0, 0, 0));
        model.vertices.add(new Vector3f(1, 0, 0));
        model.vertices.add(new Vector3f(0, 1, 0));
        model.vertices.add(new Vector3f(1, 1, 0));

        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(new ArrayList<>(List.of(0, 1, 2)));

        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(new ArrayList<>(List.of(1, 2, 3)));

        model.polygons.add(polygon1);
        model.polygons.add(polygon2);
    }

    @Test
    void testRemoveSinglePolygon() {
        int startPolygonCount = model.polygons.size();

        PolygonRemover.removePolygons(model, List.of(0));

        assertEquals(startPolygonCount - 1, model.polygons.size());
    }

    @Test
    void testRemoveMultiplePolygons() {
        int startPolygonCount = model.polygons.size();

        PolygonRemover.removePolygons(model, List.of(0, 1));

        assertEquals(startPolygonCount - 2, model.polygons.size());
    }

    @Test
    void testRemoveNonExistentPolygons() {
        int startPolygonCount = model.polygons.size();

        PolygonException exception = assertThrows(PolygonException.class,
                () -> {PolygonRemover.removePolygons(model, List.of(5, 6));}
        );

        assertTrue(exception.getMessage().contains("Polygon index 5 does not exist") ||
                exception.getMessage().contains("Polygon index 6 does not exist"));
        // same amount?
        assertEquals(startPolygonCount, model.polygons.size());
    }
}
